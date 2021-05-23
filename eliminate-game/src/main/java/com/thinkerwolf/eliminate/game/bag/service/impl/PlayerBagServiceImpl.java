package com.thinkerwolf.eliminate.game.bag.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.thinkerwolf.eliminate.game.bag.entity.PlayerBag;
import com.thinkerwolf.eliminate.game.bag.mapper.PlayerBagMapper;
import com.thinkerwolf.eliminate.game.bag.service.IPlayerBagService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author wukai
 * @since 2020-06-10
 */
@Service("playerBagService")
public class PlayerBagServiceImpl extends ServiceImpl<PlayerBagMapper, PlayerBag> implements IPlayerBagService {

    @Override
    public PlayerBag getBag(int playerId, int type, int detailId) {
        QueryWrapper<PlayerBag> qw = new QueryWrapper<>();
        qw.eq("player_id", playerId).eq("type", type).eq("detail_id", detailId);
        return getOne(qw);

    }

    @Override
    public PlayerBag getBagSafty(int playerId, int type, int detailId) {
        PlayerBag pb = getBag(playerId, type, detailId);
        if (pb == null) {
            pb = new PlayerBag();
            pb.setPlayerId(playerId);
            pb.setType(type);
            pb.setDetailId(detailId);
            pb.setNum(0);
            saveOrUpdate(pb);
        }
        return pb;
    }
}
