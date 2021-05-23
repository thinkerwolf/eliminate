package com.thinkerwolf.eliminate.pub.sdata.cache;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.thinkerwolf.eliminate.pub.sdata.entity.BuildingLv;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import com.thinkerwolf.eliminate.pub.sdata.AbstractCache;
import com.thinkerwolf.eliminate.pub.sdata.service.BuildingLvService;

import java.util.List;

/**
 * @author wukai
 * @since 2020-05-24
 */
@Component
public class BuildingLvCache extends AbstractCache<Integer, BuildingLv> {

    @Autowired
    BuildingLvService baseService;

    private Table<Integer, Integer, BuildingLv> table = HashBasedTable.create();

    @Override
    public void afterPropertiesSet() throws Exception {
        List<BuildingLv> models = baseService.list();
        for (BuildingLv model : models) {
            put(model.getId(), model);
            table.put(model.getBuildingId(), model.getLv(), model);
        }
    }

    public BuildingLv getBuildingLv(int buildingId, int lv) {
        return table.get(buildingId, lv);
    }
}