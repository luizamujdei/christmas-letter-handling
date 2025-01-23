package com.christmas.letter.processor.utils;

import com.redis.testcontainers.RedisContainer;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@Testcontainers
public abstract class RedisTestContainer {

    @Container
    protected static final RedisContainer redisContainer = new RedisContainer(
            DockerImageName.parse("redis:7.0")).withExposedPorts(6379);

    @DynamicPropertySource
    private static void registerRedisProperties(DynamicPropertyRegistry registry) {
        registry.add("redis.host", redisContainer::getHost);
        registry.add("redis.port", () -> redisContainer.getMappedPort(6379).toString());
        registry.add("ttl", ()-> "1");
    }}
