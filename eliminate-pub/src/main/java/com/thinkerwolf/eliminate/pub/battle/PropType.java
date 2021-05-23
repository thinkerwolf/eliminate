package com.thinkerwolf.eliminate.pub.battle;

import com.thinkerwolf.eliminate.common.LocalMessages;

/**
 * 道具类型
 *
 * @author wukai
 */
public enum PropType {
    NONE(0, ""),
    /**
     * 撤回
     */
    WITHDRAW(1, LocalMessages.N_PROP_1),
    /**
     * 自动消除
     */
    ELIMINATE(2, LocalMessages.N_PROP_2),
    /**
     * 重新打乱
     */
    SHUFFLE(3, LocalMessages.N_PROP_3),

    ;
    private final int id;

    private String name = "";

    PropType(int id) {
        this.id = id;
    }

    PropType(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public static PropType idOf(int id) {
        for (PropType p : PropType.values()) {
            if (p.id == id) {
                return p;
            }
        }
        return NONE;
    }

}
