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
package com.fuhouyu.framework.common.function;

import java.io.Serializable;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;


/**
 * <p>
 * 扩展信息功能接口
 * </p>
 * <p>
 * 提供对领域模型中附加信息（Additional Information）的统一存取能力。
 * 建议实现类在初始化时确保 {@link #getAdditionalInformation()} 返回一个非空的 Map。
 * </p>
 *
 * @author fuhouyu
 * @since 2024/10/16
 */
@FunctionalInterface
public interface AdditionalInformationFunction extends Serializable {

    /**
     * 获取存储扩展信息的底层 Map 对象
     *
     * @return 扩展信息 Map，不建议返回 null
     */
    Map<String, Object> getAdditionalInformation();

    /**
     * 添加附加信息
     *
     * @param key   键
     * @param value 值
     * @return 当前对象（支持链式调用）
     */
    default AdditionalInformationFunction putAdditionalInformation(String key, Object value) {
        Map<String, Object> info = getAdditionalInformation();
        if (info != null) {
            info.put(key, value);
        }
        return this;
    }

    /**
     * 批量添加附加信息
     *
     * @param additionalInformationMap 附加信息 Map 对象
     * @return 当前对象
     */
    default AdditionalInformationFunction putAdditionalInformationAll(Map<String, Object> additionalInformationMap) {
        if (additionalInformationMap != null && !additionalInformationMap.isEmpty()) {
            Map<String, Object> info = getAdditionalInformation();
            if (info != null) {
                info.putAll(additionalInformationMap);
            }
        }
        return this;
    }

    /**
     * 以类型名为 Key 添加附加信息
     * <p>使用场景：通常用于存储某种特定类型的元数据对象</p>
     *
     * @param value 附加信息值对象
     * @return 当前对象
     */
    default AdditionalInformationFunction putAdditionalInformation(Object value) {
        if (value != null) {
            // 使用 getName() 替代 getCanonicalName() 以规避某些特殊类加载器下的 null 问题
            // 建议配合 Spring 的 ClassUtils 处理代理对象
            this.putAdditionalInformation(value.getClass().getName(), value);
        }
        return this;
    }

    /**
     * 根据 Key 获取附加信息值，并进行类型转换
     *
     * @param key 键
     * @param <T> 预期的返回值泛型类型
     * @return 附加信息值，若不存在或类型不匹配则返回 null
     * @throws ClassCastException 当实际存储类型与预期类型不符时抛出
     */
    @SuppressWarnings("unchecked")
    default <T> T getAdditionalInformation(String key) {
        return (T) Optional.ofNullable(getAdditionalInformation())
                .map(m -> m.get(key))
                .orElse(null);
    }

    /**
     * 根据类型名获取对应的附加信息值
     *
     * @param clazz 类型
     * @param <T>   泛型类型
     * @return 附加信息值对象
     */
    default <T> T getAdditionalInformation(Class<T> clazz) {
        Objects.requireNonNull(clazz, "Class type must not be null");
        return getAdditionalInformation(clazz.getName());
    }

    /**
     * 获取附加信息值，支持提供默认值
     *
     * @param key          键
     * @param defaultValue 默认值
     * @param <T>          泛型类型
     * @return 附加信息值，若为空则返回默认值
     */
    default <T> T getAdditionalInformationOrDefault(String key, T defaultValue) {
        T value = getAdditionalInformation(key);
        return value != null ? value : defaultValue;
    }

    /**
     * 判断是否存在指定的 Key
     *
     * @param key 键
     * @return true 存在 / false 不存在
     */
    default boolean containsAdditionalKey(String key) {
        return Optional.ofNullable(getAdditionalInformation())
                .map(m -> m.containsKey(key))
                .orElse(false);
    }
}