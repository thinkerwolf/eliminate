package com.thinkerwolf.eliminate.pub.player;

import com.google.common.collect.Maps;
import com.thinkerwolf.eliminate.common.OpResult;
import com.thinkerwolf.eliminate.common.PushCommand;
import com.thinkerwolf.gamer.common.log.InternalLoggerFactory;
import com.thinkerwolf.gamer.common.log.Logger;
import com.thinkerwolf.gamer.core.mvc.model.JsonModel;
import com.thinkerwolf.gamer.core.servlet.Session;
import com.thinkerwolf.gamer.remoting.Content;

import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 玩家管理器
 *
 * @author wukai
 */
public final class Players {

    private static final Logger LOG = InternalLoggerFactory.getLogger(Players.class);

    private static final Map<Integer, Session> player2Sessions = Maps.newHashMap();

    private static final Map<String, PlayerDto> session2Players = Maps.newHashMap();

    private static final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    public static void addDto(Session session, PlayerDto playerDto) {
        lock.writeLock().lock();
        try {
            internalRemoveSession(playerDto.getPlayerId());
            player2Sessions.put(playerDto.getPlayerId(), session);
            session2Players.put(session.getId(), playerDto);
        } finally {
            lock.writeLock().unlock();
        }
    }

    public static PlayerDto getDto(String sessionId) {
        lock.readLock().lock();
        try {
            return session2Players.get(sessionId);
        } finally {
            lock.readLock().unlock();
        }
    }

    public static PlayerDto getDto(Session session) {
        if (session == null) {
            return null;
        }
        return getDto(session.getId());
    }

    public static Session getSession(int playerId) {
        lock.readLock().lock();
        try {
            return player2Sessions.get(playerId);
        } finally {
            lock.readLock().unlock();
        }
    }

    public static void removeSession(int playerId) {
        lock.writeLock().lock();
        try {
            internalRemoveSession(playerId);
        } finally {
            lock.writeLock().unlock();
        }
    }

    public static void removeDto(Session session) {
        if (session == null) {
            return;
        }
        lock.writeLock().lock();
        try {
            PlayerDto playerDto = session2Players.remove(session.getId());
            if (playerDto != null) {
                player2Sessions.remove(playerDto.getPlayerId());
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    private static void internalRemoveSession(int playerId) {
        Session session = player2Sessions.remove(playerId);
        if (session != null) {
            session2Players.remove(session.getId());
        }
    }

    /**
     * 给某个玩家推送消息
     *
     * @param playerId
     * @param pushCommand
     * @param data
     */
    public static void push(int playerId, PushCommand pushCommand, Object data) {
        lock.readLock().lock();
        Session session;
        try {
            session = player2Sessions.get(playerId);
        } finally {
            lock.readLock().unlock();
        }

        if (session != null) {
            push(session, pushCommand, data);
            if (LOG.isDebugEnabled()) {
                LOG.debug("Push to#[{}] {}-{}:{}", playerId, pushCommand.getCommand(), pushCommand.getModule(), data);
            }
        }
    }

    /**
     * 给一个指定Session推送消息
     *
     * @param session
     * @param pushCommand
     * @param data
     */
    public static void push(Session session, PushCommand pushCommand, Object data) {
        OpResult op = OpResult.push(pushCommand.getCommand(), pushCommand.getModule(), session.getId(), data);
        JsonModel model = new JsonModel(op);
        session.push(Content.CONTENT_JSON, pushCommand.getCommand(), model.getBytes());
    }

    /**
     * 全服推送
     *
     * @param pushCommand
     * @param data
     */
    public static void push(PushCommand pushCommand, Object data) {
        lock.readLock().lock();
        try {
            for (Session session : player2Sessions.values()) {
                push(session, pushCommand, data);
            }
        } finally {
            lock.readLock().unlock();
        }
    }

}
