package com.thinkerwolf.eliminate.game.reward;

import com.thinkerwolf.eliminate.game.databus.DataBusManager;
import com.thinkerwolf.eliminate.game.player.entity.Player;
import com.thinkerwolf.eliminate.common.MiscKey;
import com.thinkerwolf.eliminate.pub.player.PlayerRoleInfo;
import com.thinkerwolf.eliminate.pub.player.PlayerRoleInfoManager;
import com.thinkerwolf.eliminate.pub.reward.IRewardProcessor;
import com.thinkerwolf.eliminate.pub.reward.Processor;
import com.thinkerwolf.eliminate.pub.reward.RewardType;
import com.thinkerwolf.eliminate.pub.reward.detail.RewardVit;
import com.thinkerwolf.eliminate.pub.sdata.SDataBusManager;

@Processor(value = RewardType.VIT)
public class RewardProcessorVit implements IRewardProcessor<Integer, RewardVit> {

    @Override
    public void add(Integer target, RewardVit reward, String addKey) {
        PlayerRoleInfo roleInfo = PlayerRoleInfoManager.getInstance().getRole(target);
        final int maxVit = SDataBusManager.getDataGetter().getMiscCache().getInt(MiscKey.VIT_MAX);
        if (roleInfo.getVit() >= maxVit) {
            return;
        }
        int newVit = Math.min(maxVit, reward.getValue() + roleInfo.getVit());
        roleInfo.setVit(newVit);

        Player player = DataBusManager.getDataBus().getPlayerService().getById(target);
        player.setVit(newVit);
        DataBusManager.getDataBus().getPlayerService().saveOrUpdate(player);

        DataBusManager.getDataBus().getPlayerService().vitRecover(target);
    }

    @Override
    public void consume(Integer target, RewardVit reward, String consumeKey) {
        PlayerRoleInfo roleInfo = PlayerRoleInfoManager.getInstance().getRole(target);
        int newVit = roleInfo.getVit() - reward.getValue();
        if (newVit < 0) {
            return;
        }
        roleInfo.setVit(newVit);

        Player player = DataBusManager.getDataBus().getPlayerService().getById(target);
        player.setVit(newVit);
        DataBusManager.getDataBus().getPlayerService().saveOrUpdate(player);
        DataBusManager.getDataBus().getPlayerService().vitRecover(target);
    }

    @Override
    public boolean checkAdd(Integer target, RewardVit reward, String addKey) {
        return true;
    }

    @Override
    public boolean checkConsume(Integer target, RewardVit reward, String consumeKey) {
        PlayerRoleInfo roleInfo = PlayerRoleInfoManager.getInstance().getRole(target);
        return roleInfo.getVit() >= reward.getValue();
    }
}
