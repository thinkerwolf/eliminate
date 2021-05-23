package com.thinkerwolf.eliminate.game.stage.service;

import com.thinkerwolf.eliminate.common.OpResult;

public interface IBattleService {

    OpResult begin(int playerId);

    OpResult start(int playerId, int stageId);

    OpResult view(int playerId, String battleId);

    OpResult takeBlock(int playerId, int layer, int y, int x);

    OpResult useProp(int playerId, int propId);

    OpResult restart(int playerId);
}
