package com.thinkerwolf.eliminate.login.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.thinkerwolf.eliminate.login.entity.Player;
import com.thinkerwolf.eliminate.login.mapper.PlayerMapper;
import com.thinkerwolf.eliminate.login.service.IPlayerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author wukai
 * @since 2020-06-25
 */
@Service("playerService")
public class PlayerServiceImpl extends ServiceImpl<PlayerMapper, Player> implements IPlayerService {

    @Override
    public List<Player> getPlayerList(int userId) {
        QueryWrapper<Player> qw = new QueryWrapper<>();
        qw.eq("user_id", userId);
        return list(qw);
    }
}
