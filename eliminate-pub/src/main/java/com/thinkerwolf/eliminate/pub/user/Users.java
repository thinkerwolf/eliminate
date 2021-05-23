package com.thinkerwolf.eliminate.pub.user;

import com.google.common.collect.Maps;
import com.thinkerwolf.gamer.core.servlet.Session;

import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 用户管理器
 *
 * @author wukai
 */
public final class Users {

    /**
     * sessionId -> userDto
     */
    private static final Map<String, UserDto> session2Users = Maps.newHashMap();
    /**
     * username -> session
     */
    private static final Map<String, Session> user2Sessions = Maps.newHashMap();

    private static final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    public static void addDto(Session session, UserDto userDto) {
        lock.writeLock().lock();
        try {
            session2Users.put(session.getId(), userDto);
            user2Sessions.put(userDto.getUsername(), session);
        } finally {
            lock.writeLock().unlock();
        }
    }

    public static Session getSession(String username) {
        lock.readLock().lock();
        try {
            return user2Sessions.get(username);
        } finally {
            lock.readLock().unlock();
        }
    }

    public static UserDto getDto(Session session) {
        if (session != null) {
            lock.readLock().lock();
            try {
                return session2Users.get(session.getId());
            } finally {
                lock.readLock().unlock();
            }
        }
        return null;
    }

    public static UserDto removeDto(Session session) {
        if (session == null) {
            return null;
        }
        lock.writeLock().lock();
        try {
            UserDto userDto = session2Users.get(session.getId());
            if (userDto != null) {
                session2Users.remove(session.getId());
                user2Sessions.remove(userDto.getUsername());
            }
            return userDto;
        } finally {
            lock.writeLock().unlock();
        }
    }

}
