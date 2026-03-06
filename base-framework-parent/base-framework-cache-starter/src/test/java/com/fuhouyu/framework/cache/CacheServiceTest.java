/*
 * Copyright 2024-present fuhouyu.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.fuhouyu.framework.cache;

import com.fuhouyu.framework.cache.service.CacheService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.redis.autoconfigure.DataRedisAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * <p>
 * 缓存测试类
 * </p>
 *
 * @author fuhouyu
 * @since 2024/8/13 22:06
 */
@SpringBootTest(classes = {
        RedisAutoConfiguration.class,
        DataRedisAutoConfiguration.class,
})
@ActiveProfiles("test")
class CacheServiceTest {

    @SuppressWarnings("resource")
    static final GenericContainer<?> REDIS_GENERIC_CONTAINER =
            new GenericContainer<>(DockerImageName.parse("redis:7.2-alpine"))
                    .withCommand("redis-server --requirepass password")
                    .withExposedPorts(6379);

    static {
        REDIS_GENERIC_CONTAINER.start();
        System.setProperty("spring.data.redis.host", REDIS_GENERIC_CONTAINER.getHost());
        System.setProperty("spring.data.redis.port", REDIS_GENERIC_CONTAINER.getMappedPort(6379).toString());
    }

    private String cacheBigKey;
    private String cacheValue;
    @Autowired
    private CacheService<String, Object> cacheService;

    @BeforeEach
    void setup() {
        cacheBigKey = "test:key:" + UUID.randomUUID();
        cacheValue = UUID.randomUUID().toString().replace("-", "").substring(8);
        cacheService.delete(cacheBigKey);
    }

    @Test
    void testStringCache() {
        cacheService.set(cacheBigKey, cacheValue);
        assertEquals(cacheValue, cacheService.get(cacheBigKey));

        // 测试 SET NX (setIfAbsent)
        boolean firstSet = cacheService.setIfAbsent(cacheBigKey, "newValue");
        assertFalse(firstSet, "Key已存在，setIfAbsent 应该返回 false");

        String lockKey = cacheBigKey + ":lock";
        boolean secondSet = cacheService.setIfAbsent(lockKey, "locked", 5, TimeUnit.SECONDS);
        assertTrue(secondSet, "Key不存在，setIfAbsent 应该返回 true");
        cacheService.delete(lockKey);
    }

    @Test
    void testAtomicOperations() {
        String numKey = cacheBigKey + ":num";
        cacheService.delete(numKey); // 确保初始为空

        // 测试自增
        long val = cacheService.increment(numKey, 5);
        assertEquals(5, val);
        assertEquals(10, cacheService.increment(numKey, 5));

        // 测试自减
        assertEquals(7, cacheService.decrement(numKey, 3));

        // 测试 TTL 获取
        cacheService.expire(numKey, 10, TimeUnit.SECONDS);
        long expire = cacheService.getExpire(numKey, TimeUnit.SECONDS);
        assertTrue(expire > 0 && expire <= 10, "过期时间获取不正确");

        cacheService.delete(numKey);
    }

    @Test
    void testZSetCache() {
        String zsetKey = cacheBigKey + ":zset";
        cacheService.addToZSet(zsetKey, "member1", 1.0);
        cacheService.addToZSet(zsetKey, "member2", 2.0);
        cacheService.addToZSet(zsetKey, "member3", 0.5);

        // 测试范围查询 (按分数排序应该为: member3, member1, member2)
        Set<Object> range = cacheService.rangeFromZSet(zsetKey, 0, -1);
        List<Object> list = new ArrayList<>(range);
        assertEquals("member3", list.get(0));
        assertEquals("member2", list.get(2));

        // 测试删除
        cacheService.removeFromZSet(zsetKey, "member1", "member3");
        assertEquals(1, cacheService.rangeFromZSet(zsetKey, 0, -1).size());

        cacheService.delete(zsetKey);
    }

    @Test
    void testHashCache() {
        String hashKey = "field1";
        cacheService.putHash(cacheBigKey, hashKey, cacheValue);
        assertEquals(cacheValue, cacheService.getHash(cacheBigKey, hashKey));

        Map<String, Object> map = Map.of("field2", "val2", "field3", "val3");
        cacheService.putHashAll(cacheBigKey, map);

        assertEquals(3, cacheService.getHashAll(cacheBigKey).size());
        // 测试 size 方法针对 Hash 的表现
        assertEquals(3, cacheService.size(cacheBigKey));

        cacheService.delete(cacheBigKey);
    }

    @Test
    void testListCache() {
        cacheService.pushToList(cacheBigKey, "first");
        cacheService.pushToList(cacheBigKey, "second");

        List<Object> list = cacheService.getList(cacheBigKey);
        assertEquals(2, list.size());

        Object popped = cacheService.popFromList(cacheBigKey);
        assertEquals("first", popped, "LPOP 应该是弹出第一个元素");

        cacheService.delete(cacheBigKey);
    }

    @Test
    void testCommonOps() {
        cacheService.set(cacheBigKey, cacheValue);
        assertTrue(cacheService.exists(cacheBigKey));

        // 测试前缀搜索
        String prefix = "test:key:";
        Set<String> keys = cacheService.keys(prefix + "*");
        assertNotNull(keys);
        assertFalse(keys.isEmpty());

        // 测试批量删除
        cacheService.delete(cacheBigKey);
        assertFalse(cacheService.exists(cacheBigKey));
    }

    @Test
    void testBytes() {
        byte[] key = (cacheBigKey + ":bytes").getBytes(StandardCharsets.UTF_8);
        byte[] val = "byteValue".getBytes(StandardCharsets.UTF_8);

        cacheService.set(key, val, 10, TimeUnit.SECONDS);
        byte[] result = cacheService.get(key);

        assertArrayEquals(val, result);

        // 测试 byte[] 前缀搜索
        Set<byte[]> keys = cacheService.keys((cacheBigKey + ":*").getBytes(StandardCharsets.UTF_8));
        assertFalse(keys.isEmpty());

        cacheService.delete(key);
    }
}
