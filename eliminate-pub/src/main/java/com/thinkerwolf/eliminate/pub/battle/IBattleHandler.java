package com.thinkerwolf.eliminate.pub.battle;

import java.util.List;
import java.util.Map;

public interface IBattleHandler {

    List<Prop> getProps(int playerId, PropType[] propTypes);

    void updateStage(int playerId, Battle battle);

    Integer getNextStage(int playerId, int curStageId);

    Map<String, Object> nextStageDoc(int playerId, int curStageId);

}