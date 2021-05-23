package com.thinkerwolf.eliminate.game.diary.comm;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.thinkerwolf.eliminate.pub.sdata.SDataBusManager;
import com.thinkerwolf.eliminate.pub.sdata.entity.DayGift;

import java.util.List;
import java.util.Map;

public final class DiaryTaskUtil {


    public static List<Map<String, Object>> createDayGiftDoc(int day) {
        List<Map<String, Object>> list = Lists.newLinkedList();
        List<DayGift> dgs = SDataBusManager.getDataGetter().getDayGiftCache().getDayGifts(day);
        for (DayGift dg : dgs) {
            Map<String, Object> m = Maps.newHashMapWithExpectedSize(2);
            m.put("p", dg.getProgress());
            m.put("pic", dg.getPic());
            list.add(m);
        }
        return list;
    }

    public static boolean isAutoTask(int start, int end, DayGift dayGift) {
        return start < end && dayGift.getProgress() > start && dayGift.getProgress() <= end;
    }

}
