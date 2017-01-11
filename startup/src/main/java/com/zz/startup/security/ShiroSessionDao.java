package com.zz.startup.security;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

public class ShiroSessionDao extends AbstractSessionDAO {

    private static Logger logger = LoggerFactory.getLogger(ShiroSessionDao.class);
    @Autowired
    private JedisShiroSessionRepository jedisShiroSessionRepository;

    private LoadingCache<Serializable, Session> sessionCache = CacheBuilder.newBuilder().maximumSize(1024)
            .expireAfterWrite(3, TimeUnit.SECONDS).build(new CacheLoader<Serializable, Session>() {
                public Session load(Serializable sessionId) {
                    Session s = jedisShiroSessionRepository.getSession(sessionId);
                    logger.debug("get session key: {}", sessionId);
                    return s;
                }
            });

    public void update(Session session) throws UnknownSessionException {
        jedisShiroSessionRepository.saveSession(session);
    }

    public void delete(Session session) {
        if (session == null) {
            logger.error("session can not be null,delete failed");
            return;
        }
        Serializable id = session.getId();
        if (id != null) {
            jedisShiroSessionRepository.deleteSession(id);
        }
    }

    public Collection<Session> getActiveSessions() {
        return jedisShiroSessionRepository.getAllSessions();
    }

    @Override
    protected Serializable doCreate(Session session) {
        Serializable sessionId = this.generateSessionId(session);
        this.assignSessionId(session, sessionId);
        jedisShiroSessionRepository.saveSession(session);
        return sessionId;
    }

    @Override
    protected Session doReadSession(Serializable sessionId) {
        try {
            // 当redis 数据过期时, sessionCache取不到数据,抛异常, shiro会重新生成一个session
            return sessionCache.get(sessionId);
        } catch (Exception e) {
            logger.warn("get session {} null", sessionId);
            return null;
        }
    }

}
