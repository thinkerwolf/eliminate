package com.thinkerwolf.eliminate.pub.battle.event;

import com.thinkerwolf.eliminate.pub.battle.Battle;
import com.thinkerwolf.eliminate.pub.battle.BattleEvent;

public class EventBattleEnd extends BattleEvent {

    private Battle battle;

    public EventBattleEnd(long delay, Battle battle) {
        super(delay);
        this.battle = battle;
    }

    @Override
    protected void doRun() {
        battle.setStop();
        battle.destroy();
    }
}
