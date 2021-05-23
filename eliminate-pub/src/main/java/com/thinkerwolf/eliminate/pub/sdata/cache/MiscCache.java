package com.thinkerwolf.eliminate.pub.sdata.cache;

import com.thinkerwolf.eliminate.common.MiscKey;
import com.thinkerwolf.eliminate.pub.sdata.entity.Misc;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import com.thinkerwolf.eliminate.pub.sdata.AbstractCache;
import com.thinkerwolf.eliminate.pub.sdata.service.MiscService;

import java.util.List;

/**
 * @author wukai
 * @since 2020-06-09
 */
@Component
public class MiscCache extends AbstractCache<Integer, Misc> {

    @Autowired
    MiscService baseService;

    @Override
    public void afterPropertiesSet() throws Exception {
        List<Misc> models = baseService.list();
        for (Misc model : models) {
            put(model.getId(), model);
        }
    }

    public int getInt(MiscKey key) {
        Misc misc = get(key.getId());
        if (misc == null) {
            return NumberUtils.toInt(key.getDefaultValue());
        }
        return NumberUtils.toInt(misc.getValue());
    }

    public float getFloat(MiscKey key) {
        return (float) getDouble(key);
    }

    public double getDouble(MiscKey key) {
        Misc misc = get(key.getId());
        if (misc == null) {
            return NumberUtils.toDouble(key.getDefaultValue());
        }
        return NumberUtils.toDouble(misc.getValue());
    }

    public String getString(MiscKey key) {
        Misc misc = get(key.getId());
        if (misc != null) {
            return misc.getValue();
        }
        return key.getDefaultValue();
    }


}