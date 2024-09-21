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

import com.fuhouyu.framework.cache.properties.CacheServiceProperties;
import com.fuhouyu.framework.cache.service.CacheService;
import com.fuhouyu.framework.cache.service.impl.CaffeineCacheServiceImpl;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.Expiry;
import org.checkerframework.checker.index.qual.NonNegative;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * <p>
 * caffeine 缓存自动装配类
 * </p>
 *
 * @author fuhouyu
 * @since 2024/8/15 14:25
 */
@Configuration
@ConditionalOnProperty(prefix = CacheServiceProperties.PREFIX,
        name = "cache-service-type",
        havingValue = "caffeine")
@AutoConfigureAfter(RedisAutoConfiguration.class)
public class CaffeineCacheAutoconfigure {

    @Bean
    @Primary
    public CacheService<String, Object> cacheService() {
        Cache<String, Object> cache = Caffeine.newBuilder()
                // 这里先行固定写死，永不过期
                .expireAfter(new Expiry<String, Object>() {
                    @Override
                    public long expireAfterCreate(String key, Object value, long currentTime) {
                        return Long.MAX_VALUE;
                    }

                    @Override
                    public long expireAfterUpdate(String key, Object value, long currentTime, @NonNegative long currentDuration) {
                        return currentDuration;
                    }

                    @Override
                    public long expireAfterRead(String key, Object value, long currentTime, @NonNegative long currentDuration) {
                        return currentDuration;
                    }
                })
                .initialCapacity(10000)
                .maximumSize(10000)
                .build();
        return new CaffeineCacheServiceImpl<>(cache);

    }
}
