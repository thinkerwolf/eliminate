package com.thinkerwolf.eliminate.game.reward;

import com.thinkerwolf.eliminate.game.building.comm.BuildingState;
import com.thinkerwolf.eliminate.game.building.entity.PlayerBuilding;
import com.thinkerwolf.eliminate.game.building.service.IPlayerBuildingService;
import com.thinkerwolf.eliminate.pub.reward.IRewardProcessor;
import com.thinkerwolf.eliminate.pub.reward.Processor;
import com.thinkerwolf.eliminate.pub.reward.RewardType;
import com.thinkerwolf.eliminate.pub.reward.detail.RewardBuilding;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

@Processor(RewardType.BUILDING)
public class RewardProcessorBuilding implements IRewardProcessor<Integer, RewardBuilding> {

    @Autowired
    IPlayerBuildingService playerBuildingService;

    @Override
    public void add(Integer target, RewardBuilding reward, String addKey) {
        PlayerBuilding pb = playerBuildingService.getBuilding(target, reward.getValue());
        if (pb == null) {
            pb = new PlayerBuilding();
            pb.setPlayerId(target);
            pb.setBuildingId(reward.getValue());
            pb.setState(BuildingState.IDLE.getId());
            pb.setUpdateTime(new Date());
            pb.setLv(0);
            playerBuildingService.saveOrUpdate(pb);
            playerBuildingService.getManager(reward.getValue()).upgrade(target);
            // 通知前端可建造
//            playerBuildingService.getManager(reward.getValue()).pushInfo(pb);
        }
    }

    @Override
    public void consume(Integer target, RewardBuilding reward, String consumeKey) {

    }

    @Override
    public boolean checkAdd(Integer target, RewardBuilding reward, String addKey) {
        return true;
    }

    @Override
    public boolean checkConsume(Integer target, RewardBuilding reward, String consumeKey) {
        return false;
    }
}
