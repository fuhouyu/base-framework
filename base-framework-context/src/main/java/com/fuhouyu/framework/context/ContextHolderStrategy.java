/*
 * Copyright 2024-2034 the original author or authors.
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

package com.fuhouyu.framework.context;

/**
 * <p>
 * 上下文策略类
 * </p>
 *
 * @author fuhouyu
 * @since 2024/8/14 10:08
 */
public interface ContextHolderStrategy<T> {

    /**
     * 清除上下文
     */
    void clearContext();

    /**
     * 获取上下文
     *
     * @return 上下文
     */
    T getContext();

    /**
     * 设置上下文
     *
     * @param context 上下文
     */
    void setContext(T context);

    /**
     * 创建空的上下文
     *
     * @return 上下文
     */
    T createEmptyContext();

    /**
     * 是否为空的上下文
     *
     * @return true/false
     */
    Boolean isEmptyContext();
}
