package com.thinkerwolf.eliminate.pub.sdata;

import java.util.List;

/**
 * 对应一张静态库库表的cache
 *
 * @author wukai
 */
public interface Cache<K, V> {

    void put(K k, V v);

    V remove(K k);

    V get(K k);

    List<V> getAll();

}
