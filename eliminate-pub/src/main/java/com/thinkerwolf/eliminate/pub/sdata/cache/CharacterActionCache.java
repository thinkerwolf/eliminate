package com.thinkerwolf.eliminate.pub.sdata.cache;

import com.google.common.collect.ArrayListMultimap;
import com.thinkerwolf.eliminate.pub.sdata.AbstractCache;
import com.thinkerwolf.eliminate.pub.sdata.entity.CharacterAction;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import com.thinkerwolf.eliminate.pub.sdata.service.CharacterActionService;

import java.util.Comparator;
import java.util.List;

/**
 * @author wukai
 * @since 2020-08-31
 */
@Component
public class CharacterActionCache extends AbstractCache<Integer, CharacterAction> {

    @Autowired
    CharacterActionService baseService;

    private final ArrayListMultimap<Integer, CharacterAction> multimap = ArrayListMultimap.create();

    @Override
    public void afterPropertiesSet() throws Exception {
        List<CharacterAction> models = baseService.list();
        models.sort(Comparator.comparingInt(CharacterAction::getId));
        multimap.clear();
        for (CharacterAction model : models) {
            put(model.getId(), model);
            multimap.put(model.getGroupId(), model);
        }
    }

    public List<CharacterAction> getGroup(Integer groupId) {
        if (groupId == null) {
            return null;
        }
        return multimap.get(groupId);
    }

}