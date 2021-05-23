package com.thinkerwolf.eliminate.stage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.thinkerwolf.eliminate.pub.battle.player.PlayerState;
import com.thinkerwolf.eliminate.pub.sdata.SDataBusManager;
import com.thinkerwolf.eliminate.pub.sdata.entity.Stage;
import com.thinkerwolf.eliminate.stage.entity.PlayerStage;
import com.thinkerwolf.eliminate.stage.mapper.PlayerStageMapper;
import com.thinkerwolf.eliminate.stage.service.IPlayerStageService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author wukai
 * @since 2020-06-05
 */
@Service("playerStageService")
public class PlayerStageServiceImpl extends ServiceImpl<PlayerStageMapper, PlayerStage> implements IPlayerStageService {

    @Override
    public PlayerStage getStage(int playerId, int stageId) {
        QueryWrapper<PlayerStage> qw = new QueryWrapper<>();
        qw.eq("player_id", playerId).eq("stage_id", stageId);
        return getOne(qw);
    }

    @Override
    public PlayerStage getStageSafty(int playerId, int stageId) {
        PlayerStage ps = getStage(playerId, stageId);
        if (ps == null) {
            ps = new PlayerStage();
            ps.setPlayerId(playerId);
            ps.setStageId(stageId);
            ps.setState(0);
            saveOrUpdate(ps);
        }
        return ps;
    }

    @Override
    public List<PlayerStage> findList(int playerId) {
        QueryWrapper<PlayerStage> qw = new QueryWrapper<>();
        qw.eq("player_id", playerId);
        qw.orderByAsc("stage_id");
        return list(qw);
    }

    @Override
    public int getBeginStageId(int playerId) {
        List<PlayerStage> list = findList(playerId);
        if (list == null || list.isEmpty()) {
            Stage stage = SDataBusManager.getDataGetter().getStageCache().nextStage(0);
            return stage == null ? 0 : stage.getId();
        }

        PlayerStage playerStage = list.get(list.size() - 1);
        if (playerStage.getState() < 2) {
            return playerStage.getStageId();
        }
        Stage stage = SDataBusManager.getDataGetter().getStageCache().nextStage(playerStage.getStageId());
        return stage == null ? 0 : stage.getId();
    }

}
