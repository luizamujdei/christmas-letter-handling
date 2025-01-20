package com.christmas.letter.processor.cache;

import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheWriter;

public class ConfigurableRedisCache extends RedisCache {

    protected ConfigurableRedisCache(String name, RedisCacheWriter cacheWriter, RedisCacheConfiguration cacheConfiguration){
        super(name, cacheWriter, cacheConfiguration);
    }

}
