/*
 * Copyright 2024-2024 the original author or authors.
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

/**
 * <p>
 * class工具类
 * </p>
 *
 * @author fuhouyu
 * @since 2024/8/17 11:58
 */
@SuppressWarnings("unchecked")
public class ClassUtil {

    private ClassUtil() {
    }

    /**
     * 加载class
     *
     * @param className className
     * @param <T>       转换后的泛型
     * @return class对象
     */
    public static <T> Class<T> loadClass(String className) {
        try {
            return (Class<T>) Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException(String.format("%s class not found", className), e);
        }
    }
}
