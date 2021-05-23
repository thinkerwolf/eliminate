package com.thinkerwolf.eliminate.common;

public class SeprationLock {

    private Object[] locks;

    public SeprationLock(int lockNum) {
        this.locks = new Object[lockNum];
        for (int i = 0; i < lockNum; i++) {
            locks[i] = new Object();
        }
    }

    public Object getLock(Object target) {
        return locks[Math.abs(target.hashCode()) % locks.length];
    }

}
