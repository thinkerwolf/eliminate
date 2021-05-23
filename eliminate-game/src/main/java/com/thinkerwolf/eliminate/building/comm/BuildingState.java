package com.thinkerwolf.eliminate.building.comm;

public enum BuildingState {

    NONE(0),

    IDLE(1),

    BUILDING(2),

    CONFIRM(3),

    ;

    private int id;

    BuildingState(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static BuildingState idOf(Integer id) {
        if (id == null) {
            return NONE;
        }
        for (BuildingState state : BuildingState.values()) {
            if (state.id == id) {
                return state;
            }
        }
        return NONE;
    }
}
