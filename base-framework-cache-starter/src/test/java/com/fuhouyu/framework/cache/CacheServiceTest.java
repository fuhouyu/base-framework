/*
 * Copyright 2012-2020 the original author or authors.
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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * <p>
 * 缓存测试类
 * </p>
 *
 * @author fuhouyu
 * @since 2024/8/13 22:06
 */
@SpringBootTest(classes = {
        CacheServiceAutoConfigure.class,
        RedisAutoConfiguration.class
})
@TestPropertySource(locations = {"classpath:application.yaml"})
@EnabledIfSystemProperty(named = "run.tests", matches = "true")
class CacheServiceTest {

    private static final String BIG_KEY = "big_key";

    private static final String HASH_KEY = "hash_key";

    private static final String VALUE_STR = "value";

    private static final List<String> LIST_OBJ = List.of("value");

    @Autowired
    private CacheService<String, Object> cacheService;

    @Test
    void testStringCache() throws InterruptedException {
        cacheService.set(BIG_KEY, VALUE_STR);
        assertEquals(VALUE_STR, cacheService.get(BIG_KEY));
        assertNotEquals("", VALUE_STR);


        cacheService.set(BIG_KEY, VALUE_STR, 3, TimeUnit.SECONDS);
        assertEquals(VALUE_STR, cacheService.get(BIG_KEY));
        Thread.sleep(1000 * 3);
        assertNull(cacheService.get(BIG_KEY));

        cacheService.set(BIG_KEY, LIST_OBJ);
        assertEquals(LIST_OBJ, cacheService.get(BIG_KEY));
        cacheService.delete(BIG_KEY);
        assertNull(cacheService.get(BIG_KEY));
    }

    @Test
    void testHashCache() throws InterruptedException {
        cacheService.putHash(BIG_KEY, HASH_KEY, VALUE_STR);
        assertEquals(VALUE_STR, cacheService.getHash(BIG_KEY, HASH_KEY));


        Map<String, Object> hashMap = cacheService.getHashAll(BIG_KEY);
        hashMap.forEach((key, value) -> {
            assertEquals(HASH_KEY, key);
            assertEquals(VALUE_STR, value);
        });

        cacheService.putHash(BIG_KEY, HASH_KEY, VALUE_STR, 3, TimeUnit.SECONDS);
        assertEquals(VALUE_STR, cacheService.getHash(BIG_KEY, HASH_KEY));
        Thread.sleep(1000 * 3);
        assertNull(cacheService.getHash(BIG_KEY, HASH_KEY));

        cacheService.putHash(BIG_KEY, HASH_KEY, LIST_OBJ);
        assertEquals(LIST_OBJ, cacheService.getHash(BIG_KEY, HASH_KEY));
        cacheService.deleteHash(BIG_KEY, HASH_KEY);
        assertNull(cacheService.getHash(BIG_KEY, HASH_KEY));

    }

    @Test
    void testSetCache() throws InterruptedException {
        cacheService.addToSet(BIG_KEY, VALUE_STR);
        cacheService.getSet(BIG_KEY).forEach(value -> assertEquals(VALUE_STR, value));

        assertNotEquals("", VALUE_STR);

        cacheService.addToSet(BIG_KEY, VALUE_STR, 3, TimeUnit.SECONDS);
        cacheService.getSet(BIG_KEY).forEach(value -> assertEquals(VALUE_STR, value));
        Thread.sleep(1000 * 3);
        assertTrue(cacheService.getSet(BIG_KEY).isEmpty());
    }

    @Test
    void testListCache() throws InterruptedException {
        cacheService.pushToList(BIG_KEY, VALUE_STR);
        cacheService.getList(BIG_KEY).forEach(value -> assertEquals(VALUE_STR, value));

        assertNotEquals("", VALUE_STR);

        cacheService.pushToList(BIG_KEY, VALUE_STR, 3, TimeUnit.SECONDS);
        cacheService.getList(BIG_KEY).forEach(value -> assertEquals(VALUE_STR, value));
        Thread.sleep(1000 * 3);
        assertTrue(cacheService.getList(BIG_KEY).isEmpty());
    }

    @Test
    void testBytes() {
        cacheService.set(BIG_KEY.getBytes(StandardCharsets.UTF_8), VALUE_STR.getBytes(StandardCharsets.UTF_8));
        byte[] bytes = cacheService.get(BIG_KEY.getBytes(StandardCharsets.UTF_8));
        assertEquals(VALUE_STR, new String(bytes, StandardCharsets.UTF_8));
    }
}
