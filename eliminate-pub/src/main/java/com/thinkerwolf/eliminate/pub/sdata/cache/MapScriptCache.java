package com.thinkerwolf.eliminate.pub.sdata.cache;

import com.thinkerwolf.eliminate.pub.sdata.entity.MapScript;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import com.thinkerwolf.eliminate.pub.sdata.AbstractCache;
import com.thinkerwolf.eliminate.pub.sdata.service.MapScriptService;

import java.util.List;

/**
 * @author wukai
 * @since 2020-05-23
 */
@Component
public class MapScriptCache extends AbstractCache<Integer, MapScript> {

    @Autowired
    MapScriptService baseService;

    @Override
    public void afterPropertiesSet() throws Exception {
        List<MapScript> models = baseService.list();
        for (MapScript model : models) {
            put(model.getScriptId(), model);
        }
    }
}