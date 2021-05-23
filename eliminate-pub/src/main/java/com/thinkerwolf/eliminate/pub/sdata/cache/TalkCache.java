package com.thinkerwolf.eliminate.pub.sdata.cache;

import com.google.common.collect.ArrayListMultimap;
import com.thinkerwolf.eliminate.pub.sdata.AbstractCache;
import com.thinkerwolf.eliminate.pub.sdata.entity.Talk;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import com.thinkerwolf.eliminate.pub.sdata.service.TalkService;

import java.util.Comparator;
import java.util.List;

/**
 * @author wukai
 * @since 2020-08-31
 */
@Component
public class TalkCache extends AbstractCache<Integer, Talk> {

    @Autowired
    TalkService baseService;

    private final ArrayListMultimap<Integer, Talk> multimap = ArrayListMultimap.create();

    @Override
    public void afterPropertiesSet() throws Exception {
        List<Talk> models = baseService.list();
        multimap.clear();
        models.sort(Comparator.comparingInt(Talk::getSeq));
        for (Talk model : models) {
            put(model.getId(), model);
            multimap.put(model.getGroupId(), model);
        }
    }

    public List<Talk> getGroup(Integer groupId) {
        if (groupId == null) {
            return null;
        }
        return multimap.get(groupId);
    }
}