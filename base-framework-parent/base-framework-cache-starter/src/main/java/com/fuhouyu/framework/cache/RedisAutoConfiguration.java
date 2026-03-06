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

import com.fuhouyu.framework.cache.properties.CacheServiceProperties;
import com.fuhouyu.framework.cache.service.CacheService;
import com.fuhouyu.framework.cache.service.impl.RedisCacheService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJacksonJsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * <p>
 * redisTemplate自动装配类
 * </p>
 *
 * @author fuhouyu
 * @since 2024/8/13 21:06
 */
@ConditionalOnProperty(prefix = CacheServiceProperties.PREFIX,
        name = "type",
        havingValue = "redis")
@Slf4j
@ConditionalOnClass(RedisConnectionFactory.class)
@ConditionalOnMissingBean(RedisTemplate.class)
public class RedisAutoConfiguration implements BaseRedisConfiguration {


    @Bean
    @ConditionalOnMissingBean(RedisTemplate.class)
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(connectionFactory);
        redisTemplate.setKeySerializer(stringRedisSerializer);
        redisTemplate.setHashKeySerializer(stringRedisSerializer);

        GenericJacksonJsonRedisSerializer jackson2JsonRedisSerializer = this.createGenericJacksonJsonRedisSerializer();
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    @Bean
    @ConditionalOnMissingBean(CacheService.class)
    @ConditionalOnBean(RedisTemplate.class)
    public CacheService<String, Object> redisCacheService(RedisTemplate<String, Object> redisTemplate) {
        return new RedisCacheService<>(redisTemplate);
    }
}
