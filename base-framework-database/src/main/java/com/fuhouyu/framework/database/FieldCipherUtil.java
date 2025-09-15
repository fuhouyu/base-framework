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
package com.fuhouyu.framework.database;

import com.fuhouyu.framework.common.utils.LoggerUtil;
import com.fuhouyu.framework.database.annotations.FieldCipher;
import com.fuhouyu.framework.kms.service.KmsService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * <p>
 * kms工具类
 * </p>
 *
 * @author fuhouyu
 * @since 2025/3/29 23:08
 */
@Slf4j
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class FieldCipherUtil {

    // 缓存已处理的类字段结构
    private static final Map<Class<?>, List<Field>> FIELD_CACHE = new HashMap<>();
    // 防止循环引用的递归栈
    private static final ThreadLocal<Set<Object>> PROCESSED_OBJECTS = ThreadLocal.withInitial(HashSet::new);
    @Setter(value = AccessLevel.PROTECTED)
    private static KmsService kmsService;

    /**
     * 加密或解密里面的数据
     *
     * @param obj       obj
     * @param isEncrypt true 加密 false 解密
     */
    public static void encryptOrDecrypt(Object obj, boolean isEncrypt) {
        if (obj == null || isPrimitiveOrWrapper(obj.getClass())) {
            return;
        }

        if (obj instanceof List<?> list) {
            list.forEach(o -> doEncryptOrDecrypt(o, isEncrypt));
        } else {
            doEncryptOrDecrypt(obj, isEncrypt);
        }
    }


    /**
     * 执行数据加解密
     *
     * @param obj       obj
     * @param isEncrypt 是否加密，true 加密
     */
    private static void doEncryptOrDecrypt(Object obj, boolean isEncrypt) {
        Set<Object> processed = PROCESSED_OBJECTS.get();
        if (processed.contains(obj)) {
            return; // 避免循环引用
        }
        processed.add(obj);

        try {
            for (Field field : getAllFields(obj.getClass())) {
                if (Modifier.isFinal(field.getModifiers())
                        || Modifier.isStatic(field.getModifiers())) {
                    continue;
                }
                // 只处理string类型
                ReflectionUtils.makeAccessible(field);
                Object value = ReflectionUtils.getField(field, obj);

                // 1. 处理当前对象的加密字段
                FieldCipher encryptAnnotation = field.getAnnotation(FieldCipher.class);
                if (field.getType().isAssignableFrom(String.class) && encryptAnnotation != null && value != null) {
                    String encryptedValue =
                            isEncrypt ?
                                    encrypt(value.toString(), encryptAnnotation.algorithm())
                                    : decrypt(value.toString(), encryptAnnotation.algorithm());
                    ReflectionUtils.setField(field, obj, encryptedValue);
                }

                // 2. 递归处理嵌套对象
                if (value != null && !isJavaClass(value.getClass())) {
                    encryptOrDecrypt(value, isEncrypt);
                }
            }
        } finally {
            processed.remove(obj);
            PROCESSED_OBJECTS.remove();
        }
    }


    /**
     * 获取类及其父类的所有字段（包括私有字段）
     *
     * @param clazz clazz
     * @return field
     */
    private static List<Field> getAllFields(Class<?> clazz) {
        return FIELD_CACHE.computeIfAbsent(clazz, k -> {
            List<Field> fields = new ArrayList<>();
            Class<?> currentClass = k;
            while (currentClass != null && currentClass != Object.class) {
                Collections.addAll(fields, currentClass.getDeclaredFields());
                currentClass = currentClass.getSuperclass();
            }
            return fields;
        });
    }


    /**
     * 判断是否为 Java 原生类（避免处理 String、Integer 等）
     *
     * @param clazz clazz
     * @return true / false
     */
    private static boolean isJavaClass(Class<?> clazz) {
        return clazz != null && (clazz.getClassLoader() == null || clazz.getName().startsWith("java."));
    }

    /**
     * 判断是基础类型还是包装类
     *
     * @param clazz clazz
     * @return true / false
     */
    private static boolean isPrimitiveOrWrapper(Class<?> clazz) {
        return clazz.isPrimitive() ||
                clazz == String.class ||
                clazz == Integer.class ||
                clazz == Long.class ||
                clazz == Double.class ||
                clazz == Boolean.class;
    }

    /**
     * 加密数据
     *
     * @param data      原始数据
     * @param algorithm 算法
     * @return 加密base64编码后的数据
     */
    private static String encrypt(String data, FieldCipher.Algorithm algorithm) {
        byte[] encrypts;
        if (Objects.requireNonNull(algorithm) == FieldCipher.Algorithm.RSA) {
            encrypts = kmsService.asymmetricEncrypt(data.getBytes(StandardCharsets.UTF_8));
        } else {
            encrypts = kmsService.symmetryEncrypt(data.getBytes(StandardCharsets.UTF_8));
        }
        return Base64.getEncoder().encodeToString(encrypts);
    }

    /**
     * 解密
     *
     * @param data      需要解密的数据
     * @param algorithm 算法
     * @return 解密后的数据
     */
    private static String decrypt(String data, FieldCipher.Algorithm algorithm) {
        if (!StringUtils.hasText(data)) {
            return data;
        }
        byte[] decryptData;
        try {
            if (Objects.requireNonNull(algorithm) == FieldCipher.Algorithm.RSA) {
                decryptData = kmsService.asymmetricDecrypt(Base64.getDecoder().decode(data.getBytes(StandardCharsets.UTF_8)));
            } else {
                decryptData = kmsService.symmetryDecrypt(Base64.getDecoder().decode(data.getBytes(StandardCharsets.UTF_8)));
            }
            return new String(decryptData);
        } catch (Exception e) {
            LoggerUtil.error(log, "[{}]数据解密失败:[{}],返回原始数据", data, e.getMessage());
            return data;
        }

    }


}
