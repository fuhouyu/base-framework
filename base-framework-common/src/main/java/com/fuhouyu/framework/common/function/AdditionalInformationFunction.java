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

import java.util.Map;

/**
 * <p>
 * 附加信息接口
 * </p>
 *
 * @author fuhouyu
 * @since 2024/10/16 21:22
 */
@FunctionalInterface
public interface AdditionalInformationFunction {

    /**
     * 获取扩展信息
     *
     * @return 扩展信息
     */
    Map<String, Object> getAdditionalInformation();

    /**
     * 添加附加信息
     *
     * @param key   key
     * @param value value
     */
    default void putAdditionalInformation(String key, Object value) {
        this.getAdditionalInformation().put(key, value);
    }

    /**
     * 批量添加附加消息
     *
     * @param additionalInformationMap map对象
     */
    default void putAdditionalInformationAll(Map<String, Object> additionalInformationMap) {
        this.getAdditionalInformation().putAll(additionalInformationMap);
    }

    /**
     * 添加value值，key为当前类型的类名
     *
     * @param value value
     */
    default void putAdditionalInformation(Object value) {
        this.getAdditionalInformation().put(value.getClass().getCanonicalName(), value);
    }

    /**
     * 获取附加信息值
     *
     * @param clazz 类型
     * @param <T>   泛型
     * @return 附加信息值
     */
    @SuppressWarnings("unchecked")
    default <T> T getAdditionalInformation(Class<T> clazz) {
        return (T) this.getAdditionalInformation().get(clazz.getCanonicalName());
    }

    /**
     * 获取附加信息值
     *
     * @param key 获取类型
     * @param <T> 泛型
     * @return 附加信息值
     */
    @SuppressWarnings("unchecked")
    default <T> T getAdditionalInformation(String key) {
        return (T) this.getAdditionalInformation().get(key);
    }
}
