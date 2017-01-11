package com.zz.startup.security;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.util.Destroyable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class ShiroCacheManager implements CacheManager, Destroyable {

    private static Logger logger = LoggerFactory.getLogger(ShiroCacheManager.class);

    @SuppressWarnings("rawtypes")
    @Autowired
    private JedisShiroCache jedisShiroCache;

    public void destroy() throws Exception {
        logger.warn("shiro cache destory");
    }

    @SuppressWarnings("unchecked")
    public <K, V> Cache<K, V> getCache(String name) throws CacheException {
        jedisShiroCache.setName(name);
        return jedisShiroCache;
    }

}
