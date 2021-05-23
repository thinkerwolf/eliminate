package com.thinkerwolf.eliminate.gateway.net.http;

import com.thinkerwolf.eliminate.gateway.net.GatewayMessage;
import com.thinkerwolf.gamer.common.URL;
import com.thinkerwolf.gamer.common.concurrent.DefaultPromise;
import com.thinkerwolf.gamer.common.concurrent.Promise;
import com.thinkerwolf.gamer.common.log.InternalLoggerFactory;
import com.thinkerwolf.gamer.common.log.Logger;
import com.thinkerwolf.gamer.core.servlet.Request;
import com.thinkerwolf.gamer.remoting.ExchangeClient;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;

import java.util.Map;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static com.thinkerwolf.gamer.common.URL.RPC_CLIENT_NUM;

/**
 * Http gateway exchange client
 *
 * @author wukai
 */
public class HttpGatewayExchangeClient implements ExchangeClient<Object> {

    private static final Logger LOG = InternalLoggerFactory.getLogger(HttpGatewayExchangeClient.class);
    private final URL url;
    private static final int DEFAULT_MAX_TOTAL_CONNECTIONS;
    private static final int DEFAULT_READ_TIMEOUT;
    private static final long DEFAULT_CONN_CHECK_INTERVAL;

    private static final Map<String, CloseableHttpClient> httpClientCache = new ConcurrentHashMap<>();
    private static final ScheduledExecutorService scheduled = Executors.newScheduledThreadPool(1);

    static {
        DEFAULT_MAX_TOTAL_CONNECTIONS = 10;
        DEFAULT_CONN_CHECK_INTERVAL = 3000;
        DEFAULT_READ_TIMEOUT = 5 * 1000;
    }

    private CloseableHttpClient httpClient;

    public HttpGatewayExchangeClient(URL url) {
        this.url = url;

        initHttpClient();
    }

    private void initHttpClient() {
        this.httpClient = httpClientCache.computeIfAbsent(url.toHostPort(), k -> {
            Registry<ConnectionSocketFactory> schemeRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                    .register("http", PlainConnectionSocketFactory.getSocketFactory())
                    .register("https", SSLConnectionSocketFactory.getSocketFactory())
                    .build();
            PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(schemeRegistry);
            connManager.setDefaultConnectionConfig(ConnectionConfig.DEFAULT);
            connManager.setDefaultSocketConfig(SocketConfig.custom().setTcpNoDelay(true).build());
            Integer maxConn = url.getInteger(RPC_CLIENT_NUM);
            if (maxConn == null) {
                maxConn = url.getAttach(RPC_CLIENT_NUM, DEFAULT_MAX_TOTAL_CONNECTIONS);
            }
            connManager.setMaxTotal(maxConn);
            connManager.setDefaultMaxPerRoute(maxConn);

            CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(connManager).build();
            scheduled.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    connManager.closeExpiredConnections();
                    connManager.closeIdleConnections(30000, TimeUnit.MILLISECONDS);
                }
            }, DEFAULT_CONN_CHECK_INTERVAL, DEFAULT_CONN_CHECK_INTERVAL, TimeUnit.MILLISECONDS);
            return httpClient;
        });
    }

    @Override
    public Promise<Object> request(Object message) {
        return request(message, DEFAULT_READ_TIMEOUT, TimeUnit.MILLISECONDS);

    }

    @Override
    public Promise<Object> request(Object message, long timeout, TimeUnit unit) {
        DefaultPromise<Object> promise = new DefaultPromise<>();
        GatewayMessage gatewayMessage = (GatewayMessage) message;
        Request request = gatewayMessage.getRequest();
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(DEFAULT_READ_TIMEOUT)
                .build();
        HttpPost httpPost = new HttpPost(url.toProtocolHostPort() + "/" + request.getCommand());
        httpPost.setEntity(new ByteArrayEntity(request.getContent()));
        httpPost.setHeader(HttpHeaders.CONNECTION, "keep-alive");
        httpPost.setConfig(requestConfig);

        try {
            CloseableHttpResponse httpResponse = httpClient.execute(httpPost);
            promise.setSuccess(decorate(httpResponse));
        } catch (Exception e) {
            promise.setFailure(e);
        }
        return promise;
    }

    private Object decorate(CloseableHttpResponse httpResponse) throws Exception {
        DefaultFullHttpResponse fullHttpResponse = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.valueOf(httpResponse.getStatusLine().getStatusCode()));
        for (Header header : httpResponse.getAllHeaders()) {
            fullHttpResponse.headers().add(header.getName(), header.getValue());
        }
        HttpEntity content = httpResponse.getEntity();
        byte[] body = EntityUtils.toByteArray(content);
        fullHttpResponse.headers().add(HttpHeaderNames.CONTENT_LENGTH, body.length);
        fullHttpResponse.content().writeBytes(body);
        EntityUtils.consume(content);
        IOUtils.closeQuietly(httpResponse);
        return fullHttpResponse;
    }
}
