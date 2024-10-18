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

package com.fuhouyu.framework.context;

import com.fuhouyu.framework.context.exception.ContextNotFoundException;

/**
 * <p>
 * 上下文接口
 * </p>
 *
 * @author fuhouyu
 * @since 2024/8/14 10:33
 */
public interface ContextFactory {

    /**
     * 获取用户上下文
     *
     * @return 用户上下文
     */
    User getUser();

    /**
     * 获取请求上下文
     *
     * @return 请求上下文
     */
    Request getRequest();

    /**
     * 通过名称获取上下文
     * @param contextName 上下文名称
     * @return 上下文对象
     * @throws ContextNotFoundException 上下文不存在时抛出该异常
     */
    Object getContext(String contextName) throws ContextNotFoundException;


    /**
     * 通过类型，获取上下文
     * @param type 上下文类型
     * @return 上下文
     * @param <T> 上下文类型
     * @throws ContextNotFoundException 上下文不存在时抛出该异常
     */
    <T> T getContext(Class<T> type) throws ContextNotFoundException;
}