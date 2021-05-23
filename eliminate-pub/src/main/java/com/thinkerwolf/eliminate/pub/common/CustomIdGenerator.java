package com.thinkerwolf.eliminate.pub.common;

import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.Sequence;

public class CustomIdGenerator implements IdentifierGenerator {

    private Sequence sequence;

    public CustomIdGenerator() {
        this.sequence = new Sequence();
    }

    @Override
    public Number nextId(Object entity) {
        Class<?> cls = entity.getClass();
        TableInfo tableInfo = TableInfoHelper.getTableInfo(cls);
        if (tableInfo == null) {
            return defaultNextId();
        }
        final Class<?> keyType = tableInfo.getKeyType();
        if (keyType == Long.class ||
                keyType == long.class) {
            return defaultNextId();
        }
        if (keyType == Integer.class ||
                keyType == int.class) {
               
        }

        return defaultNextId();
    }

    private Long defaultNextId() {
        return sequence.nextId();
    }

}
