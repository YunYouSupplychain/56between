package com.yunyou.core.security.shiro.cache;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Collection;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * TODO
 *
 * @author yunyou
 * @since 2023/3/11 0:26
 */
public class RedisCache<K, V> implements Cache<K, V> {
    /**
     * 缓存的超时时间，单位为s
     */
    private final long expireTime = 3600;
    /**
     * 通过构造方法注入该对象
     */
    private RedisTemplate<K, V> redisTemplate;
    /**
     * 缓存名称
     */
    private String cacheName;

    public RedisCache() {
        super();
    }

    public RedisCache(RedisTemplate<K, V> redisTemplate, String cacheName) {
        super();
        this.redisTemplate = redisTemplate;
        this.cacheName = cacheName;
    }

    /**
     * 通过key来获取对应的缓存对象
     * 通过源码我们可以发现，shiro需要的key的类型为Object，V的类型为AuthorizationInfo对象
     */
    @Override
    public V get(K key) throws CacheException {
        return redisTemplate.opsForValue().get(cacheName + ":" + key);
    }

    /**
     * 将权限信息加入缓存中
     */
    @Override
    public V put(K key, V value) throws CacheException {
        redisTemplate.opsForValue().set((K) (cacheName + ":" + key), value, this.expireTime, TimeUnit.SECONDS);
        return value;
    }

    /**
     * 将权限信息从缓存中删除
     */
    @Override
    public V remove(K key) throws CacheException {
        redisTemplate.opsForValue().getOperations().delete((K) (cacheName + ":" + key));
        return null;
    }

    @Override
    public void clear() throws CacheException {
        keys().forEach(this::remove);
    }

    @Override
    public int size() {
        return keys().size();
    }

    @Override
    public Set<K> keys() {
        return redisTemplate.keys((K) (cacheName + ":*"));
    }

    @Override
    public Collection<V> values() {
        return keys().stream().map(k -> redisTemplate.opsForValue().get(k)).collect(Collectors.toList());
    }

}
