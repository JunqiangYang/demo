package com.zz.startup.security;

import com.google.common.collect.Sets;
import org.apache.shiro.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public class JedisShiroSessionRepository {

    public static final String SHIRO_WEB_REDIS_PREFIX = "SHIRO_WEB_";

    @Autowired
    private RedisTemplate<Serializable, Session> redisTemplate;

    public void saveSession(final Session session) {
        redisTemplate.opsForHash().put(SHIRO_WEB_REDIS_PREFIX, session.getId(), session);
    }

    public void deleteSession(Serializable sessionId) {
        redisTemplate.opsForHash().delete(SHIRO_WEB_REDIS_PREFIX, sessionId);
    }

    public Session getSession(Serializable sessionId) {
        return (Session) redisTemplate.opsForHash().get(SHIRO_WEB_REDIS_PREFIX, sessionId);
    }

    public Collection<Session> getAllSessions() {
        Set<Session> sessions = Sets.newHashSet();
        List<Object> list = redisTemplate.opsForHash().values(SHIRO_WEB_REDIS_PREFIX);
        for (Object obj : list) {
            sessions.add((Session) obj);
        }
        return sessions;
    }
}
