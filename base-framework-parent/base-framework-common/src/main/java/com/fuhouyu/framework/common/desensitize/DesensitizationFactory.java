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
package com.fuhouyu.framework.common.desensitize;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>
 * 脱敏器工厂
 * </p>
 *
 * @author fuhouyu
 * @since 2025/3/30 18:24
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class DesensitizationFactory {

    private static final Map<Class<?>, Desensitization<?>> DESENSITIZATION_MAP = new ConcurrentHashMap<>();

    /**
     * 初始化脱敏器
     *
     * @param clazz clazz
     * @return 脱敏类
     */
    @SuppressWarnings("java:S1452") // 忽略未使用的参数警告
    public static Desensitization<?> getDesensitization(Class<?> clazz) {
        return DESENSITIZATION_MAP.computeIfAbsent(clazz, key -> {
            try {
                Constructor<?> constructor = clazz.getConstructor();
                return (Desensitization<?>) constructor.newInstance();
            } catch (NoSuchMethodException | InvocationTargetException | InstantiationException |
                     IllegalAccessException e) {
                throw new UnsupportedOperationException(e.getMessage(), e);
            }
        });
    }
}
