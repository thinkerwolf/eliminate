package com.thinkerwolf.eliminate.rpc.common;

/**
 * 服务器类型
 *
 * @author wukai
 */
public enum ServerType {
    LOGIN(1),
    GATEWAY(2),
    GAME(3),
    CHAT(4),
    LOBBY(5),
    MATCH(6),

    ;
    private final int id;

    ServerType(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name().toLowerCase();
    }

    public static ServerType nameOf(String name) {
        return ServerType.valueOf(name.toUpperCase());
    }

    public static ServerType idOf(int id) {
        for (ServerType t : ServerType.values()) {
            if (t.id == id) {
                return t;
            }
        }
        throw new IllegalArgumentException(
                "No enum constant ServerType" + "." + id);
    }
}
