package com.thinkerwolf.eliminate.common.util;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public final class HttpUtils {

    private static volatile CloseableHttpClient defaultHttpClient;

    public static byte[] sendGetRequest(String uri) throws Exception {
        HttpGet httpGet = new HttpGet(uri);
        HttpEntity entity = null;
        CloseableHttpResponse resp = null;
        try {
            resp = getDefaultHttpClient().execute(httpGet);
            entity = resp.getEntity();
            return EntityUtils.toByteArray(entity);
        } finally {
            EntityUtils.consumeQuietly(entity);
            IOUtils.closeQuietly(resp);
        }
    }


    private static CloseableHttpClient getDefaultHttpClient() {
        if (defaultHttpClient == null) {
            synchronized (HttpUtils.class) {
                if (defaultHttpClient == null) {
                    defaultHttpClient = HttpClients.createDefault();
                }
            }
        }
        return defaultHttpClient;
    }

}
