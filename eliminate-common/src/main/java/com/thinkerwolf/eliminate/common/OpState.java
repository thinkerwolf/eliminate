package com.thinkerwolf.eliminate.common;

public enum OpState {

    /**
     * 异常
     */
    EXCEPTION(0),
    /**
     * 成功
     */
    SUCCESS(1),
    /**
     * 失败
     */
    FAIL(2),
    /**
     * 推送
     */
    PUSH(3),
    /**
     * 未登录或者session过期
     */
    NO_LOGIN(4),

    ;
    int id;

    OpState(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }


}
