package com.thinkerwolf.eliminate.rpc.common;

import com.thinkerwolf.gamer.common.URL;
import com.thinkerwolf.gamer.registry.Registry;
import com.thinkerwolf.gamer.remoting.Protocol;
import com.thinkerwolf.gamer.rpc.ReferenceConfig;
import com.thinkerwolf.gamer.rpc.RpcReferenceManager;
import org.apache.commons.lang.StringUtils;

import java.util.Collections;
import java.util.List;

/**
 * 跨服工具类
 *
 * @author wukai
 * @since 2020-06-29
 */
public final class KfUtils {

    public static List<URL> lookup(Registry registry, ServerType serverType, Protocol protocol) {
        return lookup(registry, serverType, protocol, null);
    }

    public static List<URL> lookup(Registry registry, ServerType serverType, Protocol protocol, String serverId) {
        final URL lookupUrl = new URL();
        final boolean idExists = StringUtils.isNotEmpty(serverId);
        StringBuilder sb = new StringBuilder(registry.url().getPath()).append('/')
                .append(serverType.getName()).append('/')
                .append(protocol.getName());
        if (idExists) {
            sb.append('/').append(serverId);
        }
        lookupUrl.setPath(sb.toString());
        lookupUrl.setParameters(Collections.emptyMap());
        List<URL> urls = registry.lookup(lookupUrl);

        // Http server support webSocket
        if (urls.isEmpty() && Protocol.WEBSOCKET.equals(protocol)) {
            sb = new StringBuilder(registry.url().getPath()).append('/')
                    .append(serverType.getName()).append('/').append(Protocol.HTTP.getName());
            if (idExists) {
                sb.append('/').append(serverId);
            }
            lookupUrl.setPath(sb.toString());
            urls = registry.lookup(lookupUrl);
        }
        return urls;
    }

    public static <T> T getRpcService(Class<T> service, URL url) {
        ReferenceConfig<T> ref = RpcReferenceManager.getInstance().getReferenceConfig(service, url);
        return ref.get();
    }


}
