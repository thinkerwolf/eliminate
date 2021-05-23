package com.thinkerwolf.eliminate.pub.sdata;

import org.springframework.beans.factory.InitializingBean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractCache<K, T> implements Cache<K, T>, InitializingBean {
    private Map<K, T> cacheMap = new ConcurrentHashMap<>();

    private List<T> cacheList = new ArrayList<>();

    @Override
    public T get(K k) {
        return cacheMap.get(k);
    }

    @Override
    public void put(K k, T v) {
        cacheMap.put(k, v);
        cacheList.add(v);
    }

    @Override
    public T remove(K k) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<T> getAll() {
        return Collections.unmodifiableList(cacheList);
    }
}
