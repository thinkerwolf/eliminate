package com.thinkerwolf.eliminate.gateway.net.tcp;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.thinkerwolf.eliminate.gateway.net.GatewayMessage;
import com.thinkerwolf.eliminate.gateway.net.GatewaySessions;
import com.thinkerwolf.gamer.common.URL;
import com.thinkerwolf.gamer.common.concurrent.DefaultPromise;
import com.thinkerwolf.gamer.common.log.InternalLoggerFactory;
import com.thinkerwolf.gamer.common.log.Logger;
import com.thinkerwolf.gamer.core.mvc.model.JacksonModel;
import com.thinkerwolf.gamer.core.servlet.Request;
import com.thinkerwolf.gamer.core.util.RequestUtil;
import com.thinkerwolf.gamer.netty.NettyClient;
import com.thinkerwolf.gamer.remoting.AbstractExchangeClient;
import com.thinkerwolf.gamer.remoting.Channel;
import com.thinkerwolf.gamer.remoting.Content;
import com.thinkerwolf.gamer.remoting.tcp.Packet;

import java.nio.charset.StandardCharsets;

public class TcpGatewayExchangeClient extends AbstractExchangeClient<Object> {
    private static final Logger LOG = InternalLoggerFactory.getLogger(TcpGatewayExchangeClient.class);

    public TcpGatewayExchangeClient(URL url) {
        super(url);
        setClient(new NettyClient(url, this));
    }

    @Override
    protected Object encodeRequest(Object message, int requestId) throws Exception {
        GatewayMessage gatewayMessage = (GatewayMessage) message;
        Request request = gatewayMessage.getRequest();
        Packet packet = new Packet();
        packet.setRequestId(requestId);
        packet.setOpcode(Content.CONTENT_TEXT);
        packet.setCommand(request.getCommand());
        packet.setContent(request.getContent());
        return packet;
    }

    @Override
    protected Integer decodeResponseId(Object message) {
        Packet packet = (Packet) message;
        return packet.getRequestId();
    }

    @Override
    protected Object decodeResponse(Object message, DefaultPromise<Object> promise) throws Exception {
        Packet packet = (Packet) message;
        GatewayMessage gatewayMessage = (GatewayMessage) promise.getAttachment();
        packet.setRequestId(gatewayMessage.getRequest().getRequestId());
        return packet;
    }

    @Override
    protected void handleNullIdResponse(Channel channel, Object message) {
        try {
            Packet packet = (Packet) message;
            ObjectNode on = (ObjectNode) JacksonModel.objectMapper.readTree(packet.getContent());
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
}
