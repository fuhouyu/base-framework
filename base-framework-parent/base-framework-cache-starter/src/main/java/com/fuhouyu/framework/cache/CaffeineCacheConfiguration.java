/*
 * Copyright 2024-2025 fuhouyu.
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
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
public class CaffeineCacheConfiguration {

    @Bean
    @ConditionalOnMissingBean(CacheService.class)
    public CacheService<String, Object> cacheService() {
        Cache<String, Object> cache = Caffeine.newBuilder()
                .initialCapacity(10000)
                .maximumSize(10000)
                .build();
        return new CaffeineCacheServiceImpl<>(cache);

    }
}
