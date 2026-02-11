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

import com.fuhouyu.framework.common.exception.JsonParseException;
import tools.jackson.core.JacksonException;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.ext.javatime.deser.LocalDateTimeDeserializer;
import tools.jackson.databind.ext.javatime.ser.LocalDateSerializer;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.databind.module.SimpleModule;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
public class JacksonUtil {

    private static final ObjectMapper OBJECT_MAPPER;

    static {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addSerializer(LocalDate.class, new LocalDateSerializer(dateTimeFormatter));
        simpleModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(dateFormatter));
        simpleModule.addSerializer(LocalDate.class, new LocalDateSerializer(dateFormatter));
        simpleModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(dateFormatter));
        OBJECT_MAPPER = JsonMapper.builder()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .addModule(simpleModule)
                .build();
    }

    private JacksonUtil() {

    }

    public static ObjectMapper getObjectMapper() {
        return OBJECT_MAPPER;
    }


    /**
     * 将对象转换为字符串
     *
     * @param o 需要转换成字符串的对象
     * @return 转换后的对象
     */
    public static byte[] writeValueAsBytes(Object o) {
        return tryParse(() -> OBJECT_MAPPER.writeValueAsBytes(o));
    }

    /**
     * 将对象转换为字符串
     *
     * @param o 需要转换成字符串的对象
     * @return 转换后的对象
     */
    public static String writeValueAsString(Object o) {
        return tryParse(() -> OBJECT_MAPPER.writeValueAsString(o));
    }

    /**
     * 从字符串中读取对象
     *
     * @param str   json字符串
     * @param clazz 转换后的对象
     * @param <T>   转换的类型
     * @return 转换后的对象
     */
    public static <T> T readValue(String str, Class<T> clazz) {
        return tryParse(() -> OBJECT_MAPPER.readValue(str, clazz));
    }

    /**
     * 从字节数组中进行lfrw
     *
     * @param bytes 需要转换的字符串
     * @param clazz 转换后的对象
     * @param <T>   转换的类型
     * @return 转换后的对象
     */
    public static <T> T readValue(byte[] bytes, Class<T> clazz) {
        return tryParse(() -> OBJECT_MAPPER.readValue(bytes, clazz));
    }

    /**
     * 从字节数组中进行lfrw
     *
     * @param bytes        需要转换的字符串
     * @param valueTypeRef 转换后的对象
     * @param <T>          转换的类型
     * @return 转换后的对象
     */
    public static <T> T readValue(byte[] bytes, TypeReference<T> valueTypeRef) {
        return tryParse(() -> OBJECT_MAPPER.readValue(bytes, valueTypeRef));
    }

    /**
     * 从字符串中读取对象
     *
     * @param str          json字符串
     * @param valueTypeRef 所属类型
     * @param <T>          转换的类型
     * @return 转换后的对象
     */
    public static <T> T readValue(String str, TypeReference<T> valueTypeRef) {
        return tryParse(() -> OBJECT_MAPPER.readValue(str, valueTypeRef));
    }

    /**
     * 尝试解析
     *
     * @param parser 解析器
     * @param <T>    结果泛型
     * @return 解析后的结果
     */
    public static <T> T tryParse(Callable<T> parser) {
        return tryParse(parser, JacksonException.class);
    }

    /**
     * 尝试解析，如果出现错误则抛出异常
     *
     * @param parser 解析器
     * @param check  异常检查
     * @param <T>    对象类型
     * @return 转换后的对象
     */
    public static <T> T tryParse(Callable<T> parser, Class<? extends Exception> check) {
        try {
            return parser.call();
        } catch (Exception ex) {
            if (check.isAssignableFrom(ex.getClass())) {
                throw new JsonParseException(ex);
            }
            throw new IllegalStateException(ex);
        }
    }


}
