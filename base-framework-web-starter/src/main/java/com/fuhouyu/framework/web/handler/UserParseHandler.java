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
package com.fuhouyu.framework.web.handler;

import com.fuhouyu.framework.context.user.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;

/**
 * <p>
 * 用户解析处理器
 * </p>
 *
 * @author fuhouyu
 * @since 2024/10/5 23:53
 */
@FunctionalInterface
public interface UserParseHandler {


    /**
     * 解析用户信息
     *
     * @param request      请求
     * @param response     响应
     * @param userSubClass 用户的子类型
     * @param <T>          泛型
     * @return 用户信息
     */
    <T extends User> T parseUser(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
                                 @NonNull Class<T> userSubClass);
}
