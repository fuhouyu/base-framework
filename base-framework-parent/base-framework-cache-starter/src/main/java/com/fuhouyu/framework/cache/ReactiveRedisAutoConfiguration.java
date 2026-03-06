package com.fuhouyu.framework.cache;

import com.fuhouyu.framework.cache.properties.CacheServiceProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.GenericJacksonJsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * <p>
 *
 * </p>
 *
 * @author fuhouyu
 * @since 2026/3/5 22:36
 */
@ConditionalOnProperty(prefix = CacheServiceProperties.PREFIX,
        name = "type",
        havingValue = "redis")
@Slf4j
@ConditionalOnClass(ReactiveRedisConnectionFactory.class)
public class ReactiveRedisAutoConfiguration implements BaseRedisConfiguration {

    @Bean
    @ConditionalOnMissingBean(ReactiveRedisTemplate.class)
    public ReactiveRedisTemplate<String, Object> reactiveRedisTemplate(ReactiveRedisConnectionFactory factory) {
        StringRedisSerializer keySerializer = new StringRedisSerializer();

        // 复用你之前的 ObjectMapper 配置逻辑
        GenericJacksonJsonRedisSerializer genericJacksonJsonRedisSerializer = this.createGenericJacksonJsonRedisSerializer();

        RedisSerializationContext<String, Object> context = RedisSerializationContext
                .<String, Object>newSerializationContext(keySerializer)
                .value(genericJacksonJsonRedisSerializer)
                .hashKey(keySerializer)
                .hashValue(genericJacksonJsonRedisSerializer)
                .build();

        return new ReactiveRedisTemplate<>(factory, context);
    }
}
