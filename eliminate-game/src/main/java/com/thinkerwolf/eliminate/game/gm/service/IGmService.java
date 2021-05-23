package com.thinkerwolf.eliminate.game.gm.service;

import com.thinkerwolf.eliminate.common.OpResult;

public interface IGmService {

    OpResult help();

    OpResult sendReward(int playerId, String reward);

    OpResult consumeReward(int playerId, String reward);

    OpResult stopBattle(int playerId);

    OpResult refreshSdata(int playerId, String[] ps);

    OpResult action(int playerId, String[] ps);
}
