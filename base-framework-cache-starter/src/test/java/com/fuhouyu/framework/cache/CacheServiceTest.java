/*
 * Copyright 2024-2024 the original author or authors.
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
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.UUID;
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
        CacheAutoConfiguration.class,
        RedisAutoConfiguration.class
})
@TestPropertySource(locations = {"classpath:application.yaml"})
class CacheServiceTest {


    private String cacheBigKey;

    private String cacheValue;

    @Autowired
    private CacheService<String, Object> cacheService;

    @BeforeEach
    void setup() {
        cacheBigKey = UUID.randomUUID().toString();
        cacheValue = UUID.randomUUID().toString().replace("-", "").substring(8);
    }

    @Test
    void testStringCache() {

        cacheService.set(cacheBigKey, cacheValue);
        assertEquals(cacheValue, cacheService.get(cacheBigKey));
        assertNotEquals("", cacheValue);


        cacheService.set(cacheBigKey, cacheValue, 3, TimeUnit.SECONDS);
        assertEquals(cacheValue, cacheService.get(cacheBigKey));

        List<String> list = List.of(cacheValue);
        cacheService.set(cacheBigKey, list);
        assertEquals(list, cacheService.get(cacheBigKey));
        cacheService.delete(cacheBigKey);
        assertNull(cacheService.get(cacheBigKey));
    }

    @Test
    void testHashCache() {
        String hashKey = UUID.randomUUID().toString().replace("-", "").substring(8);
        cacheService.putHash(cacheBigKey, hashKey, cacheValue);
        assertEquals(cacheValue, cacheService.getHash(cacheBigKey, hashKey));


        Map<String, Object> hashMap = cacheService.getHashAll(cacheBigKey);
        hashMap.forEach((key, value) -> {
            assertEquals(hashKey, key);
            assertEquals(cacheValue, value);
        });

        cacheService.putHash(cacheBigKey, hashKey, cacheValue, 3, TimeUnit.SECONDS);
        assertEquals(cacheValue, cacheService.getHash(cacheBigKey, hashKey));

        List<String> list = List.of(cacheValue);
        cacheService.putHash(cacheBigKey, hashKey, list);
        assertEquals(list, cacheService.getHash(cacheBigKey, hashKey));
        cacheService.deleteHash(cacheBigKey, hashKey);
        assertNull(cacheService.getHash(cacheBigKey, hashKey));

    }

    @Test
    void testSetCache() {
        cacheService.addToSet(cacheBigKey, cacheValue);
        cacheService.getSet(cacheBigKey).forEach(value -> assertEquals(cacheValue, value));

        assertNotEquals("", cacheValue);

        cacheService.addToSet(cacheBigKey, cacheValue, 3, TimeUnit.SECONDS);
        cacheService.getSet(cacheBigKey).forEach(value -> assertEquals(cacheValue, value));
    }

    @Test
    void testListCache() {
        cacheService.pushToList(cacheBigKey, cacheValue);
        cacheService.getList(cacheBigKey).forEach(value -> assertEquals(cacheValue, value));

        assertNotEquals("", cacheValue);

        cacheService.pushToList(cacheBigKey, cacheValue, 3, TimeUnit.SECONDS);
        cacheService.getList(cacheBigKey).forEach(value -> assertEquals(cacheValue, value));
    }

    @Test
    void testBytes() {
        cacheService.set(cacheBigKey.getBytes(StandardCharsets.UTF_8), cacheValue.getBytes(StandardCharsets.UTF_8));
        byte[] bytes = cacheService.get(cacheBigKey.getBytes(StandardCharsets.UTF_8));
        assertEquals(cacheValue, new String(bytes, StandardCharsets.UTF_8));
        cacheService.delete(cacheBigKey);
    }
}
