package com.thinkerwolf.eliminate.pub.sdata.cache;

import com.thinkerwolf.eliminate.pub.sdata.entity.Building;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import com.thinkerwolf.eliminate.pub.sdata.AbstractCache;
import com.thinkerwolf.eliminate.pub.sdata.service.BuildingService;

import java.util.List;

/**
 * @author wukai
 * @since 2020-05-24
 */
@Component
public class BuildingCache extends AbstractCache<Integer, Building> {

    @Autowired
    BuildingService baseService;

    @Override
    public void afterPropertiesSet() throws Exception {
        List<Building> models = baseService.list();
        for (Building model : models) {
            put(model.getId(), model);
        }
    }
}