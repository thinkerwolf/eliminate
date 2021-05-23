package com.thinkerwolf.eliminate.pub.reward.detail;

import com.thinkerwolf.eliminate.pub.battle.PropType;
import com.thinkerwolf.eliminate.pub.reward.AbstractRewardIdNum;
import com.thinkerwolf.eliminate.pub.reward.RewardType;

/**
 * 游戏道具
 *
 * @author wukai
 */
public class RewardProp extends AbstractRewardIdNum {

    public RewardProp(int id, int num) {
        super(id, num);
    }

    @Override
    public RewardType getRewardType() {
        return RewardType.PROP;
    }

    @Override
    public String getName() {
        return PropType.idOf(getId()).getName();
    }
}
