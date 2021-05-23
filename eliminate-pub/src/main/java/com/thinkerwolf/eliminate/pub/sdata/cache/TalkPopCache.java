package com.thinkerwolf.eliminate.pub.sdata.cache;

import com.google.common.collect.ArrayListMultimap;
import com.thinkerwolf.eliminate.pub.sdata.AbstractCache;
import com.thinkerwolf.eliminate.pub.sdata.entity.TalkPop;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import com.thinkerwolf.eliminate.pub.sdata.service.TalkPopService;

import java.util.Comparator;
import java.util.List;

/**
 * @author wukai
 * @since 2020-08-31
 */
@Component
public class TalkPopCache extends AbstractCache<Integer, TalkPop> {

    @Autowired
    TalkPopService baseService;

    private final ArrayListMultimap<Integer, TalkPop> multimap = ArrayListMultimap.create();

    @Override
    public void afterPropertiesSet() throws Exception {
        List<TalkPop> models = baseService.list();
        models.sort(Comparator.comparingInt(TalkPop::getSeq));
        multimap.clear();
        for (TalkPop model : models) {
            put(model.getId(), model);
            multimap.put(model.getGroupId(), model);
        }
    }

    public List<TalkPop> getGroup(Integer groupId) {
        if (groupId == null) {
            return null;
        }
        return multimap.get(groupId);
    }
}