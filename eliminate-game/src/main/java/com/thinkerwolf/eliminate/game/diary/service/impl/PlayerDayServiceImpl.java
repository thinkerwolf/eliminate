package com.thinkerwolf.eliminate.game.diary.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.thinkerwolf.eliminate.game.diary.entity.PlayerDay;
import com.thinkerwolf.eliminate.game.diary.mapper.PlayerDayMapper;
import com.thinkerwolf.eliminate.game.diary.service.IPlayerDayService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author wukai
 * @since 2020-08-05
 */
@Service("playerDayService")
public class PlayerDayServiceImpl extends ServiceImpl<PlayerDayMapper, PlayerDay> implements IPlayerDayService {

    @Override
    public PlayerDay find(int playerId, int day) {
        QueryWrapper<PlayerDay> qw = new QueryWrapper<>();
        qw.eq("player_id", playerId);
        qw.eq("day", day);
        return getOne(qw);
    }

    @Override
    public PlayerDay findOrCreate(int playerId, int day) {
        PlayerDay pd = find(playerId, day);
        if (pd == null) {
            pd = new PlayerDay();
            pd.setPlayerId(playerId);
            pd.setDay(day);
            pd.setCurrentProcess(0);
            pd.setNextProcess(0);
            saveOrUpdate(pd);
        }
        return pd;
    }
}
