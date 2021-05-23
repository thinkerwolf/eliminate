package com.thinkerwolf.eliminate.game.task.diary;

public enum DiaryTaskState {
    NONE(0),
    START(1),
    FINISH(2),
    REWARD(3),
    ;
    private int id;


    DiaryTaskState(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static DiaryTaskState idOf(int id) {
        for (DiaryTaskState state : DiaryTaskState.values()) {
            if (state.id == id) {
                return state;
            }
        }
        return DiaryTaskState.NONE;
    }
}
