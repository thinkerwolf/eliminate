package com.thinkerwolf.eliminate.common;

public enum MiscKey {
    /**
     * 最大的体力值
     */
    VIT_MAX(2, "10"),
    /**
     * 每点体力恢复时间
     */
    VIT_TIME(3, "10"),
    /**
     * 星星上限
     */
    STAR_MAX(4, "30"),

    ;
    private int id;
    /**
     * 默认值
     */
    private String defaultValue;

    MiscKey(int id) {
        this(id, "");
    }

    MiscKey(int id, String defaultValue) {
        this.id = id;
        this.defaultValue = defaultValue;
    }

    public int getId() {
        return id;
    }

    public String getDefaultValue() {
        return defaultValue;
    }
}
