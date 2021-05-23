package com.thinkerwolf.eliminate.game.reward;

import com.thinkerwolf.eliminate.game.building.comm.PlayerBuildingManager;
import com.thinkerwolf.eliminate.game.databus.DataBusManager;
import com.thinkerwolf.eliminate.pub.reward.Processor;
import com.thinkerwolf.eliminate.pub.reward.RewardType;
import com.thinkerwolf.eliminate.pub.reward.detail.RewardBuildingLv;

@Processor(RewardType.BUILDING_LV)
public class RewardProcessorBuildingLv extends AbstractPlayerRewardProcessor<RewardBuildingLv> {

    @Override
    public void add(Integer target, RewardBuildingLv reward, String addKey) {
        PlayerBuildingManager manager = DataBusManager.getDataBus().getPlayerBuildingService().getManager(reward.getId());
        manager.directUpgrade(target, reward.getNum());
    }

    @Override
    public void consume(Integer target, RewardBuildingLv reward, String consumeKey) {

    }

    @Override
    public boolean checkAdd(Integer target, RewardBuildingLv reward, String addKey) {
        return true;
    }

    @Override
    public boolean checkConsume(Integer target, RewardBuildingLv reward, String consumeKey) {
        return false;
    }
}
