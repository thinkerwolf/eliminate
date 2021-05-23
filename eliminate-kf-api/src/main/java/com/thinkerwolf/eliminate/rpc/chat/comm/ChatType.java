package com.thinkerwolf.eliminate.rpc.chat.comm;

/**
 * 聊天记录类型
 *
 * @author wukai
 */
public enum ChatType {
    /**
     * 文字
     */
    TEXT(1),
    /**
     * 红包
     */
    RED(2),
    ;
    private final int id;

    ChatType(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static ChatType idOf(int id) {
        for (ChatType ct : ChatType.values()) {
            if (ct.id == id) {
                return ct;
            }
        }
        throw new IllegalArgumentException("" + id);
    }
}
