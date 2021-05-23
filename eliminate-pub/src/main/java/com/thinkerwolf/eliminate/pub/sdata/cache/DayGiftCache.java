package com.thinkerwolf.eliminate.pub.sdata.cache;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.thinkerwolf.eliminate.pub.sdata.AbstractCache;
import com.thinkerwolf.eliminate.pub.sdata.entity.DayGift;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import com.thinkerwolf.eliminate.pub.sdata.service.DayGiftService;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author wukai
 * @since 2020-08-05
 */
@Component
public class DayGiftCache extends AbstractCache<Integer, DayGift> {

    @Autowired
    DayGiftService baseService;

    private static Comparator<DayGift> comparator = Comparator.comparingInt(DayGift::getProgress);

    private ArrayListMultimap<Integer, DayGift> day2Gifts = ArrayListMultimap.create();

    @Override
    public void afterPropertiesSet() throws Exception {
        List<DayGift> models = baseService.list();
        models.sort(comparator);
        for (DayGift model : models) {
            put(model.getId(), model);
            day2Gifts.put(model.getDay(), model);
        }
    }

    public List<DayGift> getDayGifts(int day) {
        return day2Gifts.get(day);
    }

    public List<DayGift> getRangeDayGifts(int day, int startProcess, int endProcess) {
        List<DayGift> list = getDayGifts(day);
        if (list == null) {
            return Collections.emptyList();
        }
        List<DayGift> newList = Lists.newLinkedList();
        for (DayGift dayGift : list) {
            if (dayGift.getProgress() > startProcess && dayGift.getProgress() <= endProcess) {
                newList.add(dayGift);
            }
        }
        return newList;
    }

}