package com.fuhouyu.framework.cache;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.data.redis.serializer.GenericJacksonJsonRedisSerializer;
import tools.jackson.databind.DefaultTyping;
import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import tools.jackson.databind.module.SimpleModule;
import tools.jackson.databind.ser.std.ToStringSerializer;

import java.time.LocalDateTime;

/**
 * <p>
 * 缓存基类
 * </p>
 *
 * @author fuhouyu
 * @since 2026/3/5 22:35
 */
public interface BaseCacheConfiguration {

    /**
     * 创建一个 ObjectMapper 实例，用于序列化和反序列化 Redis 数据
     *
     * @return ObjectMapper 实例
     */
    default GenericJacksonJsonRedisSerializer createGenericJacksonJsonRedisSerializer() {
        SimpleModule timeModule = new SimpleModule();
        timeModule.addSerializer(LocalDateTime.class, ToStringSerializer.instance);
        BasicPolymorphicTypeValidator ptv =
                BasicPolymorphicTypeValidator.builder()
                        .allowIfSubType("java.util")
                        .build();
        ObjectMapper objectMapper = JsonMapper.builder()
                .addModule(timeModule)
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .activateDefaultTyping(
                        ptv,
                        DefaultTyping.NON_FINAL,
                        JsonTypeInfo.As.PROPERTY
                )
                .build();
        return new GenericJacksonJsonRedisSerializer(objectMapper);
    }
}
