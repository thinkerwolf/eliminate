package com.thinkerwolf.eliminate.gateway.net;

import com.thinkerwolf.eliminate.common.OpResult;
import com.thinkerwolf.eliminate.gateway.databus.DataBusManager;
import com.thinkerwolf.eliminate.gateway.net.http.HttpGatewayExchangeClient;
import com.thinkerwolf.eliminate.gateway.net.tcp.TcpGatewayExchangeClient;
import com.thinkerwolf.eliminate.gateway.net.websocket.WebsocketGatewayExchangeClient;
import com.thinkerwolf.eliminate.rpc.common.KfUtils;
import com.thinkerwolf.eliminate.rpc.common.ServerType;
import com.thinkerwolf.gamer.common.ServiceLoader;
import com.thinkerwolf.gamer.common.URL;
import com.thinkerwolf.gamer.common.concurrent.Promise;
import com.thinkerwolf.gamer.common.log.InternalLoggerFactory;
import com.thinkerwolf.gamer.common.log.Logger;
import com.thinkerwolf.gamer.core.mvc.decorator.Decorator;
import com.thinkerwolf.gamer.core.mvc.model.JsonModel;
import com.thinkerwolf.gamer.core.servlet.Request;
import com.thinkerwolf.gamer.core.servlet.Response;
import com.thinkerwolf.gamer.core.servlet.Session;
import com.thinkerwolf.gamer.core.util.ResponseUtil;
import com.thinkerwolf.gamer.netty.NettyServletHandler;
import com.thinkerwolf.gamer.registry.Registry;
import com.thinkerwolf.gamer.remoting.Channel;
import com.thinkerwolf.gamer.remoting.ExchangeClient;
import com.thinkerwolf.gamer.remoting.Protocol;
import org.apache.commons.lang.math.RandomUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author wukai
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public class GatewayHandler extends NettyServletHandler {

    private static final int DEFAULT_CLIENT_NUM = 2;

    private static final Logger LOG = InternalLoggerFactory.getLogger(GatewayHandler.class);

    private final Map<String, List<ExchangeClient>> exchangeClients = new ConcurrentHashMap<>();

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
        gatewayMessage.setSession(session);

        ExchangeClient client = exchangeClient(urls.get(0), request, response);
        Promise<Object> promise = client.request(gatewayMessage);
        promise.addListener(future -> {
            if (future.isSuccess()) {
                response.write(future.getNow());
            } else {
                StringBuilder error = new StringBuilder(request.getProtocol() + " gateway ");
                if (future.cause() != null) {
                    error.append(" with exception.").append(future.cause().getMessage());
                    LOG.error(request.getProtocol() + " gateway error. ", future.cause());
                } else {
                    error.append(" with failure.");
                }
                handleError(error.toString(), request, response);
            }
        });

    }

    private ExchangeClient exchangeClient(URL url, Request request, Response response) {
        if (Protocol.WEBSOCKET.equals(request.getProtocol())) {
            url = new URL(url.getHost(), url.getPort());
            url = URL.parse(url.toString());
            url.setProtocol(Protocol.WEBSOCKET.getName());
            url.setPath("websocket");
            url.setParameters(url.getParameters());
        }
        List<ExchangeClient> clients = exchangeClients.get(url.toProtocolHostPort());
        if (clients == null) {
            int num = Math.max(1, url.getInteger(URL.RPC_CLIENT_NUM, DEFAULT_CLIENT_NUM));
            clients = new ArrayList<>();
            for (int i = 0; i < num; i++) {
                clients.add(newExchangeClient(request.getProtocol(), url));
            }
            exchangeClients.putIfAbsent(url.toProtocolHostPort(), clients);
        }
        return clients.get(RandomUtils.nextInt(clients.size()));
    }

    protected ExchangeClient newExchangeClient(Protocol protocol, URL url) {
        if (Protocol.TCP.equals(protocol)) {
            return new TcpGatewayExchangeClient(url);
        } else if (Protocol.HTTP.equals(protocol)) {
            return new HttpGatewayExchangeClient(url);
        } else if (Protocol.WEBSOCKET.equals(protocol)) {
            return new WebsocketGatewayExchangeClient(url);
        }
        return newOtherExchangeClient(protocol, url);
    }

    /**
     * Create exchangeClient with protocol defined by your own
     *
     * @param protocol 协议
     * @param url      url
     * @return ExchangeClient
     * @see GatewayHandler#newExchangeClient(Protocol, URL)
     */
    protected ExchangeClient newOtherExchangeClient(Protocol protocol, URL url) {
        throw new UnsupportedOperationException(protocol.toString());
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
            ResponseUtil.render(ResponseUtil.JSON_VIEW, new JsonModel(op), request, response);
        } catch (Exception ignored) {
        }
    }

}
