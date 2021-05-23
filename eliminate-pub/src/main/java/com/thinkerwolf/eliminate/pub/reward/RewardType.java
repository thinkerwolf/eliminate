package com.thinkerwolf.eliminate.pub.reward;

/**
 * 奖励类型
 *
 * @author wukai
 * @since 2020-05-24
 */
public enum RewardType {
    /**
     * 建筑
     */
    BUILDING(1),
    /**
     * 体力
     */
    VIT(2),
    /**
     * 道具
     */
    PROP(3),
    /**
     * 建筑等级
     */
    BUILDING_LV(4),
    /**
     * 建造加速
     */
    BUILDING_CD(5),
    /**
     * 星星
     */
    STAR(6),
    ;


    private int resId;

    RewardType(int resId) {
        this.resId = resId;
    }

    public int getResId() {
        return resId;
    }

    public String getName() {
        return name().toLowerCase();
    }

    public static RewardType nameOf(String name) {
        for (RewardType t : RewardType.values()) {
            if (t.getName().equalsIgnoreCase(name)) {
                return t;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return String.valueOf(resId);
    }
}
