package com.thinkerwolf.eliminate.pub.sdata.cache;

import com.google.common.collect.Maps;
import com.thinkerwolf.eliminate.pub.sdata.entity.Stage;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import com.thinkerwolf.eliminate.pub.sdata.AbstractCache;
import com.thinkerwolf.eliminate.pub.sdata.service.StageService;

import java.util.List;
import java.util.Map;
import java.util.NavigableMap;

/**
 * @author wukai
 * @since 2020-05-23
 */
@Component
public class StageCache extends AbstractCache<Integer, Stage> {

    @Autowired
    StageService baseService;

    private final NavigableMap<Integer, Stage> sortedStageMap = Maps.newTreeMap();

    @Override
    public void afterPropertiesSet() throws Exception {
        sortedStageMap.clear();
        List<Stage> models = baseService.list();
        for (Stage model : models) {
            put(model.getId(), model);
            sortedStageMap.put(model.getId(), model);
        }
    }

    public Stage nextStage(int stageId) {
        Map.Entry<Integer, Stage> en = sortedStageMap.higherEntry(stageId);
        if (en == null) {
            return null;
        }
        return en.getValue();
    }
}