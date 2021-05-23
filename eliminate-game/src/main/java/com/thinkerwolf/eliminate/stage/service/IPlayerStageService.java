package com.thinkerwolf.eliminate.stage.service;

import com.thinkerwolf.eliminate.stage.entity.PlayerStage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wukai
 * @since 2020-06-05
 */
public interface IPlayerStageService extends IService<PlayerStage> {

    PlayerStage getStage(int playerId, int stageId);

    PlayerStage getStageSafty(int playerId, int stageId);

    List<PlayerStage> findList(int playerId);

    int getBeginStageId(int playerId);
}
