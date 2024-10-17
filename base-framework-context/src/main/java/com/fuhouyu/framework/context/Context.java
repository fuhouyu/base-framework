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
import lombok.NonNull;

/**
 * <p>
 * 上下文接口
 * </p>
 *
 * @author fuhouyu
 * @since 2024/8/14 10:33
 */
public interface Context {

    /**
     * 设置用户到上下文中
     *
     * @param user 用户上下文
     */
    void setUser(User user);

    /**
     * 获取用户上下文
     *
     * @return 用户上下文
     */
    User getUser();

    /**
     * 设置请求上下文
     *
     * @param request 请求上下文
     */
    void setRequest(Request request);

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
     */
    Object getContextByName(String contextName) throws ContextNotFoundException;

    /**
     * 根据名称设置上下文
     *
     * @param contextName 上下文名称
     * @param context     上下文
     */
    void putContextByName(@NonNull String contextName, @NonNull Object context);

    /**
     * 通过上下文名称设置上下文
     *
     * @param contextName 上下文名称
     */
    void removeContextByName(@NonNull String contextName);
}
