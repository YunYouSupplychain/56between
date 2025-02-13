package com.yunyou.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import redis.clients.jedis.JedisPoolConfig;

import java.nio.charset.StandardCharsets;

/**
 * Redis缓存配置
 *
 * @author yunyou
 */
@EnableCaching
@Configuration
@PropertySource(value = "classpath:/properties/yunyou.properties", ignoreResourceNotFound = true)
public class RedisConfig extends CachingConfigurerSupport {

    @Bean
    public JedisPoolConfig jedisPoolConfig(Environment env) {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxWaitMillis(Integer.parseInt(env.getProperty("redis.maxWaitMillis")));
        config.setMaxTotal(Integer.parseInt(env.getProperty("redis.maxTotal")));
        config.setMaxIdle(Integer.parseInt(env.getProperty("redis.maxIdle")));
        config.setMinIdle(Integer.parseInt(env.getProperty("redis.minIdle")));
        config.setMinEvictableIdleTimeMillis(Integer.parseInt(env.getProperty("redis.minEvictableIdleTimeMillis")));
        config.setNumTestsPerEvictionRun(Integer.parseInt(env.getProperty("redis.numTestsPerEvictionRun")));
        config.setSoftMinEvictableIdleTimeMillis(Integer.parseInt(env.getProperty("redis.softMinEvictableIdleTimeMillis")));
        config.setTimeBetweenEvictionRunsMillis(Integer.parseInt(env.getProperty("redis.timeBetweenEvictionRunsMillis")));
        config.setBlockWhenExhausted(false);
        config.setTestWhileIdle(false);
        config.setTestOnReturn(true);
        config.setTestOnBorrow(true);
        config.setLifo(false);
        return config;
    }

    @Bean
    public RedisConnectionFactory redisConnectionFactory(Environment env) {
        JedisConnectionFactory factory = new JedisConnectionFactory();
        factory.setHostName(env.getProperty("redis.host"));
        factory.setPassword(env.getProperty("redis.password"));
        factory.setPort(Integer.parseInt(env.getProperty("redis.port")));
        factory.setDatabase(Integer.parseInt(env.getProperty("redis.database")));
        factory.setTimeout(Integer.parseInt(env.getProperty("redis.timeout")));
        factory.setPoolConfig(jedisPoolConfig(env));
        return factory;
    }

    @Bean(name = "redisTemplate")
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setKeySerializer(new MyRedisSerializer());
        redisTemplate.setValueSerializer(new JdkSerializationRedisSerializer());
        redisTemplate.setHashKeySerializer(new MyRedisSerializer());
        redisTemplate.setHashValueSerializer(new JdkSerializationRedisSerializer());
        redisTemplate.setConnectionFactory(connectionFactory);
        return redisTemplate;
    }

    @Bean
    public CacheManager cacheManager(RedisTemplate redisTemplate) {
        return new RedisCacheManager(redisTemplate);
    }

    public static class MyRedisSerializer implements RedisSerializer {

        @Override
        public byte[] serialize(Object o) throws SerializationException {
            return o.toString().getBytes(StandardCharsets.UTF_8);
        }

        @Override
        public Object deserialize(byte[] bytes) throws SerializationException {
            return new String(bytes, StandardCharsets.UTF_8);
        }
    }

}