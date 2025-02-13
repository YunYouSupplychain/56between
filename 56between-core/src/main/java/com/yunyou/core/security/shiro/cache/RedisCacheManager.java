package com.yunyou.core.security.shiro.cache;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * TODO
 *
 * @author yunyou
 * @since 2023/3/11 0:25
 */
public class RedisCacheManager implements CacheManager {
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public <K, V> Cache<K, V> getCache(String name) throws CacheException {
        return new RedisCache<K, V>(redisTemplate, name);
    }

}