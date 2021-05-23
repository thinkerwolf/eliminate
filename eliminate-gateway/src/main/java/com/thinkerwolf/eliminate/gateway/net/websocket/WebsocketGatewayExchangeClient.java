package com.thinkerwolf.eliminate.gateway.net.websocket;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.IntNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.thinkerwolf.eliminate.gateway.net.GatewayMessage;
import com.thinkerwolf.eliminate.gateway.net.GatewaySessions;
import com.thinkerwolf.gamer.common.URL;
import com.thinkerwolf.gamer.common.concurrent.DefaultPromise;
import com.thinkerwolf.gamer.common.concurrent.Promise;
import com.thinkerwolf.gamer.common.log.InternalLoggerFactory;
import com.thinkerwolf.gamer.common.log.Logger;
import com.thinkerwolf.gamer.core.mvc.model.JacksonModel;
import com.thinkerwolf.gamer.core.mvc.model.JsonModel;
import com.thinkerwolf.gamer.core.servlet.Request;
import com.thinkerwolf.gamer.core.util.RequestUtil;
import com.thinkerwolf.gamer.netty.NettyClient;
import com.thinkerwolf.gamer.remoting.AbstractExchangeClient;
import com.thinkerwolf.gamer.remoting.Channel;
import com.thinkerwolf.gamer.remoting.Content;
import com.thinkerwolf.gamer.remoting.RemotingException;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class WebsocketGatewayExchangeClient extends AbstractExchangeClient<Object> {

    private static final Logger LOG = InternalLoggerFactory.getLogger(WebsocketGatewayExchangeClient.class);

    private static final String HANDSHAKE_COMPLETE = "HANDSHAKE_COMPLETE";
    private static final String HANDSHAKE_TIMEOUT = "HANDSHAKE_TIMEOUT";

    private final DefaultPromise<Object> handshakePromise;

    public WebsocketGatewayExchangeClient(URL url) {
        super(url);
        this.handshakePromise = new DefaultPromise<>();
        setClient(new NettyClient(url, this));
    }


    @Override
    public Promise<Object> request(Object message, long timeout, TimeUnit unit) {
        try {
            handshakePromise.await();
        } catch (InterruptedException ignored) {
        }
        return super.request(message, timeout, unit);
    }


    @Override
    protected Object encodeRequest(Object message, int requestId) throws Exception {
        GatewayMessage gatewayMessage = (GatewayMessage) message;
        Request request = gatewayMessage.getRequest();
        StringBuilder text = new StringBuilder();
        text.append("command=").append(request.getCommand());
        text.append("&").append(RequestUtil.REQUEST_ID_KEY).append("=").append(requestId);
        text.append("&").append("gateway").append("=").append(gatewayMessage.getServerId());
        text.append("&").append(RequestUtil.REQUEST_SESSION_KEY).append("=").append(gatewayMessage.getSession().getId());
        for (Map.Entry<String, Object> attr : request.getAttributes().entrySet()) {
            if (shouldAddAttr(attr.getKey(), attr.getValue())) {
                text.append("&").append(attr.getKey()).append("=").append(attr.getValue());
            }
        }
        return new TextWebSocketFrame(text.toString());
    }

    private boolean shouldAddAttr(String key, Object value) {
        if (!(value instanceof String)) {
            return false;
        }
        if (RequestUtil.REQUEST_ID_KEY.equalsIgnoreCase(key)) {
            return false;
        }
        if (RequestUtil.REQUEST_SESSION_KEY.equalsIgnoreCase(key)) {
            return false;
        }
        return !Request.DECORATOR_ATTRIBUTE.equalsIgnoreCase(key);
    }

    @Override
    protected Integer decodeResponseId(Object message) {
        TextWebSocketFrame frame = (TextWebSocketFrame) message;
        try {
            JsonNode jn = JsonModel.objectMapper.readTree(frame.text());
            return jn.get("requestId").asInt();
        } catch (IOException e) {
            LOG.error("decode id error", e);
            return null;
        }
    }

    @Override
    protected Object decodeResponse(Object message, DefaultPromise<Object> promise) throws Exception {
        GatewayMessage gatewayMessage = (GatewayMessage) promise.getAttachment();
        TextWebSocketFrame frame = (TextWebSocketFrame) message;
        ObjectNode on = (ObjectNode) JacksonModel.objectMapper.readTree(frame.text());
        on.set("requestId", new IntNode(gatewayMessage.getRequest().getRequestId()));
        return new TextWebSocketFrame(JacksonModel.objectMapper.writeValueAsString(on));
    }

    @Override
    protected void handleNullIdResponse(Channel channel, Object message) {
        // handle push
        try {
            TextWebSocketFrame frame = (TextWebSocketFrame) message;
            ObjectNode on = (ObjectNode) JacksonModel.objectMapper.readTree(frame.text());
            JsonNode sessionNode = on.get(RequestUtil.REQUEST_SESSION_KEY);
            if (sessionNode != null) {
                String sessionId = sessionNode.asText();
                String cmd = on.get("cmd").asText();
                on.remove(RequestUtil.REQUEST_SESSION_KEY);
                GatewaySessions.getInstance().push(sessionId, Content.CONTENT_JSON, cmd, on.toString().getBytes(StandardCharsets.UTF_8));
            }
        } catch (Exception e) {
            LOG.error("gateway push error", e);
        }
    }

    @Override
    public void event(Channel channel, Object evt) throws RemotingException {
        super.event(channel, evt);
        System.err.println(evt);
        if (handshakePromise.isDone()) {
            return;
        }
        if (HANDSHAKE_COMPLETE.equalsIgnoreCase(evt.toString())) {
            handshakePromise.setFailure(new TimeoutException());
        } else if (HANDSHAKE_TIMEOUT.equalsIgnoreCase(evt.toString())) {
            handshakePromise.setSuccess(new Object());
        }
    }

}
