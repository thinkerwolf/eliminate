package com.thinkerwolf.eliminate.reward;

import com.thinkerwolf.eliminate.building.comm.PlayerBuildingManager;
import com.thinkerwolf.eliminate.databus.DataBusManager;
import com.thinkerwolf.eliminate.pub.reward.Processor;
import com.thinkerwolf.eliminate.pub.reward.RewardType;
import com.thinkerwolf.eliminate.pub.reward.detail.RewardBuildingCd;

@Processor(RewardType.BUILDING_CD)
public class RewardProcessorBuildingCd extends AbstractPlayerRewardProcessor<RewardBuildingCd> {
    @Override
    public void add(Integer target, RewardBuildingCd reward, String addKey) {
        PlayerBuildingManager manager = DataBusManager.getDataBus().getPlayerBuildingService().getManager(reward.getId());
        manager.reduceCd(target, reward.getNum());
    }

    @Override
    public void consume(Integer target, RewardBuildingCd reward, String consumeKey) {

    }

    @Override
    public boolean checkAdd(Integer target, RewardBuildingCd reward, String addKey) {
        return true;
    }

    @Override
    public boolean checkConsume(Integer target, RewardBuildingCd reward, String consumeKey) {
        return false;
    }
}
