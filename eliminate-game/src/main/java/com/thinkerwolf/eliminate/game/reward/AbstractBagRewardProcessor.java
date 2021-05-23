package com.thinkerwolf.eliminate.game.reward;

import com.thinkerwolf.eliminate.game.bag.entity.PlayerBag;
import com.thinkerwolf.eliminate.game.databus.DataBusManager;
import com.thinkerwolf.eliminate.pub.reward.AbstractRewardIdNum;

public abstract class AbstractBagRewardProcessor extends AbstractPlayerRewardProcessor<AbstractRewardIdNum> {

    @Override
    public void add(Integer target, AbstractRewardIdNum reward, String addKey) {
        PlayerBag bag = DataBusManager.getDataBus().getPlayerBagService().getBagSafty(target, reward.getRewardType().getResId(), reward.getId());
        bag.setNum(bag.getNum() + reward.getNum());
        DataBusManager.getDataBus().getPlayerBagService().saveOrUpdate(bag);
    }

    @Override
    public void consume(Integer target, AbstractRewardIdNum reward, String consumeKey) {
        PlayerBag bag = DataBusManager.getDataBus().getPlayerBagService().getBagSafty(target, reward.getRewardType().getResId(), reward.getId());
        bag.setNum(bag.getNum() - reward.getNum());
        DataBusManager.getDataBus().getPlayerBagService().saveOrUpdate(bag);
    }

    @Override
    public boolean checkAdd(Integer target, AbstractRewardIdNum reward, String addKey) {
        return true;
    }

    @Override
    public boolean checkConsume(Integer target, AbstractRewardIdNum reward, String consumeKey) {
        PlayerBag bag = DataBusManager.getDataBus().getPlayerBagService().getBag(target, reward.getRewardType().getResId(), reward.getId());
        return bag != null && bag.getNum() >= reward.getNum();
    }

}
