package com.thinkerwolf.eliminate.player.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Maps;
import com.thinkerwolf.eliminate.bag.entity.PlayerBag;
import com.thinkerwolf.eliminate.bag.service.IPlayerBagService;
import com.thinkerwolf.eliminate.building.service.IPlayerBuildingService;
import com.thinkerwolf.eliminate.common.*;
import com.thinkerwolf.eliminate.databus.DataBusManager;
import com.thinkerwolf.eliminate.player.comm.VitRecoverTask;
import com.thinkerwolf.eliminate.player.entity.Player;
import com.thinkerwolf.eliminate.player.mapper.PlayerMapper;
import com.thinkerwolf.eliminate.player.service.IPlayerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.thinkerwolf.eliminate.pub.battle.BattleManager;
import com.thinkerwolf.eliminate.pub.player.PlayerDto;
import com.thinkerwolf.eliminate.pub.player.PlayerRoleInfo;
import com.thinkerwolf.eliminate.pub.player.PlayerRoleInfoManager;
import com.thinkerwolf.eliminate.pub.player.Players;
import com.thinkerwolf.eliminate.pub.reward.RewardType;
import com.thinkerwolf.eliminate.pub.sdata.SDataBusManager;
import com.thinkerwolf.eliminate.task.diary.PlayerDiarysManager;
import com.thinkerwolf.gamer.common.log.InternalLoggerFactory;
import com.thinkerwolf.gamer.common.log.Logger;
import com.thinkerwolf.gamer.core.servlet.Request;
import com.thinkerwolf.gamer.core.servlet.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author wukai
 * @since 2020-05-17
 */
@Service("playerService")
public class PlayerServiceImpl extends ServiceImpl<PlayerMapper, Player> implements IPlayerService {

    private static final Logger LOG = InternalLoggerFactory.getLogger(PlayerServiceImpl.class);

    @Autowired
    IPlayerBuildingService playerBuildingService;

    @Autowired
    IPlayerBagService playerBagService;

    private final Map<Integer, VitRecoverTask> vitRecovers = Maps.newConcurrentMap();

    @Override
    public OpResult loginPlayer(Request request, int playerId) {
        Player player = getById(playerId);
        if (player == null) {
            return OpResult.fail(LocalMessages.T_PLAYER_1);
        }
        PlayerDto playerDto = new PlayerDto();
        playerDto.setPlayerId(player.getPlayerId());
        playerDto.setUserId(player.getUserId());
        playerDto.setPic(player.getPic());
        playerDto.setPlayerName(player.getPlayerName());
        Session session = request.getSession(false);
        session.setAttribute(playerId, playerDto);

        PlayerRoleInfo roleInfo = new PlayerRoleInfo();
        roleInfo.setPlayerId(playerId);
        roleInfo.setPlayerName(player.getPlayerName());
        roleInfo.setVit(player.getVit());
        roleInfo.setPic(player.getPic());
        PlayerRoleInfoManager.getInstance().setRole(roleInfo);
        PlayerBag startBag = DataBusManager.getDataBus().getPlayerBagService().getBagSafty(playerId, RewardType.STAR.getResId(), 1);

        Map<String, Object> data = Maps.newHashMapWithExpectedSize(5);
        data.put("playerId", playerId);
        data.put("vit", player.getVit());
        data.put("pic", player.getPic());
        data.put("playerName", player.getPlayerName());
        data.put("star", startBag.getNum());

        EliminateConstants.COMMON_SCHEDULED.schedule(() -> {
            try {
                PlayerServiceImpl.this.doAfterPlayerLogin(playerId);
            } catch (Exception e) {
                LOG.error("Login error", e);
            }
        }, 200, TimeUnit.MILLISECONDS);

        return OpResult.ok(data);
    }

    @Override
    public OpResult gwLogin(Request request, int playerId) {
        Player player = getById(playerId);
        if (player == null) {
            return OpResult.fail(LocalMessages.T_PLAYER_1);
        }
        Session session = Players.getSession(playerId);
        if (session == null) {
            return OpResult.fail(LocalMessages.T_PLAYER_2);
        }
        if (session.getPush() == null) {
            session.setPush(request.newPush());
        }
        EliminateConstants.COMMON_SCHEDULED.schedule(() -> {
            try {
                PlayerServiceImpl.this.doAfterPlayerLogin(playerId);
            } catch (Exception e) {
                LOG.error("Login error", e);
            }
        }, 200, TimeUnit.MILLISECONDS);

        return OpResult.ok();
    }

    @Override
    public Integer getMaxId() {
        QueryWrapper<Player> qw = new QueryWrapper<>();
        qw.select("MAX(player_id) AS K");
        Map<String, Object> obj = getMap(qw);
        return (Integer) obj.get("K");
    }

    @Override
    public void doAfterPlayerLogin(int playerId) {
        try {
            handleLogin(playerId);
        } catch (Exception e) {
            LOG.error("Login vit", e);
        }

        try {
            playerBuildingService.handleLogin(playerId);
        } catch (Exception e) {
            LOG.error("Login building", e);
        }

        try {
            PlayerDiarysManager.handleLogin(playerId);
        } catch (Exception e) {
            LOG.error("Login diaryTask", e);
        }

        try {
            BattleManager.handleLogin(playerId);
        } catch (Exception e) {
            LOG.error("Login battle", e);
        }
    }


    @Override
    public void vitRecover(int playerId) {
        VitRecoverTask task = newVitRecoverTask(playerId);
        if (task != null) {
            vitRecovers.put(playerId, task);
            EliminateConstants.COMMON_SCHEDULED.schedule(task, task.getDelay(), TimeUnit.MILLISECONDS);
        }
        pushVit(playerId);
    }

    @Override
    public void pushStar(int playerId) {
        PlayerBag pb = DataBusManager.getDataBus().getPlayerBagService().getBagSafty(playerId, RewardType.STAR.getResId(), 0);
        Map<String, Object> data = Maps.newHashMapWithExpectedSize(1);
        data.put("num", pb.getNum());
        Players.push(playerId, PushCommand.PLAYER_STAR, data);
    }

    private void handleLogin(int playerId) {
        vitRecovers.computeIfAbsent(playerId, k -> {
            VitRecoverTask vt = newVitRecoverTask(k);
            if (vt != null) {
                EliminateConstants.COMMON_SCHEDULED.schedule(vt, vt.getDelay(), TimeUnit.MILLISECONDS);
            }
            return vt;
        });
        // 推送体力
        pushVit(playerId);
        // 星星推送
        pushStar(playerId);
    }

    private VitRecoverTask newVitRecoverTask(int playerId) {
        PlayerRoleInfo roleInfo = PlayerRoleInfoManager.getInstance().getRole(playerId);
        int maxVit = SDataBusManager.getDataGetter().getMiscCache().getInt(MiscKey.VIT_MAX);
        if (roleInfo.getVit() >= maxVit) {
            return null;
        }
        int timeVit = SDataBusManager.getDataGetter().getMiscCache().getInt(MiscKey.VIT_TIME);
        long delay = TimeUnit.MINUTES.toMillis(timeVit);
        return new VitRecoverTask(playerId, delay);
    }

    public void pushVit(int playerId) {
        VitRecoverTask task = vitRecovers.get(playerId);
        PlayerRoleInfo roleInfo = PlayerRoleInfoManager.getInstance().getRole(playerId);
        int maxVit = SDataBusManager.getDataGetter().getMiscCache().getInt(MiscKey.VIT_MAX);
        Map<String, Object> data = Maps.newHashMapWithExpectedSize(3);
        data.put("maxVit", maxVit);
        data.put("vit", roleInfo.getVit());
        if (task != null && roleInfo.getVit() < maxVit) {
            long t = TimeUnit.MILLISECONDS.toSeconds(task.getDelay());
            data.put("time", t);
        }
        Players.push(playerId, PushCommand.PLAYER_VIT, data);
    }


}
