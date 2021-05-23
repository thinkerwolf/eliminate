package com.thinkerwolf.eliminate.gateway.net.websocket;

import com.thinkerwolf.eliminate.common.OpResult;
import com.thinkerwolf.eliminate.gateway.databus.DataBusManager;
import com.thinkerwolf.eliminate.gateway.net.GatewayMessage;
import com.thinkerwolf.eliminate.gateway.net.GatewaySessions;
import com.thinkerwolf.eliminate.rpc.common.KfUtils;
import com.thinkerwolf.eliminate.rpc.common.ServerType;
import com.thinkerwolf.gamer.common.URL;
import com.thinkerwolf.gamer.common.concurrent.Promise;
import com.thinkerwolf.gamer.common.log.InternalLoggerFactory;
import com.thinkerwolf.gamer.common.log.Logger;
import com.thinkerwolf.gamer.core.mvc.model.JsonModel;
import com.thinkerwolf.gamer.core.servlet.Request;
import com.thinkerwolf.gamer.core.servlet.Response;
import com.thinkerwolf.gamer.core.servlet.Session;
import com.thinkerwolf.gamer.netty.websocket.WebsocketServletHandler;
import com.thinkerwolf.gamer.registry.Registry;
import com.thinkerwolf.gamer.remoting.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.apache.commons.lang.math.RandomUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class WebsocketGatewayHandler extends WebsocketServletHandler {

    private static final Logger LOG = InternalLoggerFactory.getLogger(WebsocketGatewayHandler.class);

    private final Map<URL, List<WebsocketGatewayExchangeClient>> exchangeClients = new ConcurrentHashMap<>();

    @Override
    protected void service(Request request, Response response, Channel channel, Object message) {
        String type = (String) request.getAttribute("serverType");
        String serverId = (String) request.getAttribute("serverId");
        if (type == null) {
            super.service(request, response, channel, message);
            return;
        }
        writeRequestLog(request);
        Registry registry = DataBusManager.getDataBus().getRegistry();
        List<URL> urls = KfUtils.lookup(registry, ServerType.nameOf(type), request.getProtocol(), serverId);
        if (urls.isEmpty()) {
            handleError("Server doesn't exists", request, response);
            return;
        }

        Session session = GatewaySessions.getInstance().requestSession(request);
        if (session == null) {
            handleError("Gateway session is null", request, response);
            return;
        }
        if (session.getPush() == null) {
            session.setPush(request.newPush());
        }
        session.touch();

        GatewayMessage gatewayMessage = new GatewayMessage();
        gatewayMessage.setServerType(type);
        gatewayMessage.setServerId(serverId);
        gatewayMessage.setChannel(channel);
        gatewayMessage.setRequest(request);
        gatewayMessage.setResponse(response);


        WebsocketGatewayExchangeClient client = exchangeClient(urls.get(0));
        Promise<Object> promise = client.request(gatewayMessage);
        promise.addListener(future -> {
            if (future.isSuccess()) {
                response.write(future.getNow());
            } else {
                StringBuilder error = new StringBuilder("websocket gateway ");
                if (future.cause() != null) {
                    error.append(" with exception.").append(future.cause().getMessage());
                    LOG.error("websocket gateway error. ", future.cause());
                } else {
                    error.append(" with failure.");
                }
                handleError(error.toString(), request, response);
            }
        });

    }

    private void writeRequestLog(Request request) {
        StringBuilder sb = new StringBuilder();
        sb.append("Command [").append(request.getCommand())
                .append("] Params[");
        for (Map.Entry<String, Object> en : request.getAttributes().entrySet()) {
            sb.append(en.getKey()).append("=").append(en.getValue()).append(",");
        }
        sb.append("]");
        LOG.info("GwInvoke {}", sb.toString());
    }

    protected void handleError(String text, Request request, Response response) {
        try {
            OpResult op = OpResult.fail(text);
            op.setRequestId(request.getRequestId());
            TextWebSocketFrame frame = new TextWebSocketFrame(JsonModel.objectMapper.writeValueAsString(op));
            response.write(frame);
        } catch (IOException e) {
        }
    }

    private WebsocketGatewayExchangeClient exchangeClient(URL url) {
        if (!url.getProtocol().startsWith("ws")) {
            url = new URL(url.getHost(), url.getPort());
            url.setProtocol("ws");
            url.setPath("websocket");
            url.setParameters(url.getParameters());
        }
        List<WebsocketGatewayExchangeClient> clients = exchangeClients.get(url);
        if (clients == null) {
            clients = new ArrayList<>();
            for (int i = 0; i < 3; i++) {
                clients.add(new WebsocketGatewayExchangeClient(url));
            }
            exchangeClients.putIfAbsent(url, clients);
        }
        return clients.get(RandomUtils.nextInt(clients.size()));
    }

}
