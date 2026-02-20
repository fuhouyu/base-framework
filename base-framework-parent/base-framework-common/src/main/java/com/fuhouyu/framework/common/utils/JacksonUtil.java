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

package com.fuhouyu.framework.common.utils;

import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.SerializationFeature;
import tools.jackson.databind.ext.javatime.deser.LocalDateDeserializer;
import tools.jackson.databind.ext.javatime.deser.LocalDateTimeDeserializer;
import tools.jackson.databind.ext.javatime.deser.LocalTimeDeserializer;
import tools.jackson.databind.ext.javatime.ser.LocalDateSerializer;
import tools.jackson.databind.ext.javatime.ser.LocalDateTimeSerializer;
import tools.jackson.databind.ext.javatime.ser.LocalTimeSerializer;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.databind.module.SimpleModule;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.Callable;

/**
 * <p>
 * jackson object mapper 工具类
 * </p>
 *
 * @author fuhouyu
 * @since 2024/8/14 13:37
 */
public final class JacksonUtil {

    private static final ObjectMapper OBJECT_MAPPER;

    static {
        // 定义日期时间格式
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

        // 注册 Java8 时间模块
        SimpleModule javaTimeModule = new SimpleModule();
        // 序列化配置
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(dateTimeFormatter));
        javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer(dateFormatter));
        javaTimeModule.addSerializer(LocalTime.class, new LocalTimeSerializer(timeFormatter));
        // 反序列化配置
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(dateTimeFormatter));
        javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(dateFormatter));
        javaTimeModule.addDeserializer(LocalTime.class, new LocalTimeDeserializer(timeFormatter));

        OBJECT_MAPPER = JsonMapper.builder()
                // 忽略在 JSON 中存在但 Java 对象不存在的属性
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                // 忽略空 Bean 序列化时的错误
                .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
                // 序列化时包含所有属性（可根据需求改为 NON_NULL）
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .addModule(javaTimeModule)
                .build();
    }

    private JacksonUtil() {
        // 私有构造防止实例化
    }

    /**
     * 获取原生 ObjectMapper 实例
     *
     * @return {@link ObjectMapper}
     */
    public static ObjectMapper getObjectMapper() {
        return OBJECT_MAPPER;
    }

    /**
     * 将对象转换为 JSON 字符串
     *
     * @param obj 待转换对象
     * @return JSON 字符串，解析失败抛出运行时异常
     */
    public static String toJsonString(Object obj) {
        if (obj == null) {
            return null;
        }
        return tryParse(() -> OBJECT_MAPPER.writeValueAsString(obj));
    }

    /**
     * 将对象转换为格式化后的 JSON 字符串（用于日志打印或调试）
     *
     * @param obj 待转换对象
     * @return 格式化的 JSON 字符串
     */
    public static String toPrettyJsonString(Object obj) {
        if (obj == null) {
            return null;
        }
        return tryParse(() -> OBJECT_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(obj));
    }

    /**
     * 将对象转换为字节数组
     *
     * @param obj 待转换对象
     * @return 字节数组
     */
    public static byte[] toBytes(Object obj) {
        if (obj == null) {
            return new byte[0];
        }
        return tryParse(() -> OBJECT_MAPPER.writeValueAsBytes(obj));
    }

    /**
     * 将 JSON 字符串转换为指定类型的对象
     *
     * @param json  JSON 字符串
     * @param clazz 目标类
     * @param <T>   泛型类型
     * @return 转换后的对象
     */
    public static <T> T parseObject(String json, Class<T> clazz) {
        if (json == null || json.isEmpty()) {
            return null;
        }
        return tryParse(() -> OBJECT_MAPPER.readValue(json, clazz));
    }

    /**
     * 将 JSON 字符串转换为复杂类型（如 List&lt;User&gt;）
     *
     * @param json          JSON 字符串
     * @param typeReference 类型引用
     * @param <T>           泛型类型
     * @return 转换后的对象
     */
    public static <T> T parseObject(String json, TypeReference<T> typeReference) {
        if (json == null || json.isEmpty()) {
            return null;
        }
        return tryParse(() -> OBJECT_MAPPER.readValue(json, typeReference));
    }

    /**
     * 将字节数组转换为指定类型的对象
     *
     * @param bytes 字节数组
     * @param clazz 目标类
     * @param <T>   泛型类型
     * @return 转换后的对象
     */
    public static <T> T parseObject(byte[] bytes, Class<T> clazz) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        return tryParse(() -> OBJECT_MAPPER.readValue(bytes, clazz));
    }

    /**
     * 内部解析包装方法
     *
     * @param callable 解析逻辑
     * @param <T>      返回类型
     * @return 结果
     */
    private static <T> T tryParse(Callable<T> callable) {
        try {
            return callable.call();
        } catch (Exception e) {
            throw new RuntimeException("Jackson 解析异常", e);
        }
    }
}
