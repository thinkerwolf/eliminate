package com.thinkerwolf.eliminate.gateway.net;

import com.thinkerwolf.gamer.core.servlet.Request;
import com.thinkerwolf.gamer.core.servlet.Response;
import com.thinkerwolf.gamer.core.servlet.Session;
import com.thinkerwolf.gamer.remoting.Channel;
import lombok.Data;

/**
 * 网关信息
 *
 * @author wukai
 */
@Data
public class GatewayMessage {

    /**
     * 服务器类型
     */
    private String serverType;
    /**
     * 服务器id
     */
    private String serverId;
    /**
     * 原始channel
     */
    private Channel channel;
    /**
     * 原始请求 request
     */
    private Request request;
    /**
     * 原始请求 response
     */
    private Response response;
    /**
     * session
     */
    private Session session;
}
