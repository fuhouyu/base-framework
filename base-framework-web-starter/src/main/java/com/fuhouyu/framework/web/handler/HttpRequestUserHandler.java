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

import com.fuhouyu.framework.context.Context;
import com.fuhouyu.framework.context.user.User;
import com.fuhouyu.framework.context.user.UserContextHolder;
import com.fuhouyu.framework.utils.LoggerUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.util.CollectionUtils;
import org.springframework.web.servlet.AsyncHandlerInterceptor;

import java.util.List;
import java.util.Objects;

/**
 * <p>
 * http请求用户拦截处理器
 * </p>
 *
 * @author fuhouyu
 * @since 2024/8/15 19:52
 */
@RequiredArgsConstructor
public class HttpRequestUserHandler implements AsyncHandlerInterceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpRequestUserHandler.class);

    private final List<UserExtensionHandler> userExtensionInterceptors;

    private final UserParseHandler userParseHandler;

    /**
     * 需要转换的用户类型
     */
    private final Class<? extends User> subUserType;


    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) throws Exception {
        User user = userParseHandler.parseUser(request, response, subUserType);
        if (Objects.isNull(user)) {
            LoggerUtil.debug(LOGGER, "从请求头获取到的用户信息为空, 请求方法:{}", request.getMethod());
            return AsyncHandlerInterceptor.super.preHandle(request, response, handler);
        }
        Context<User> context = UserContextHolder.createEmptyContext();
        context.setObject(user);
        if (CollectionUtils.isEmpty(userExtensionInterceptors)) {
            return AsyncHandlerInterceptor.super.preHandle(request, response, handler);
        }
        for (UserExtensionHandler userExtensionInterceptor : userExtensionInterceptors) {
            userExtensionInterceptor.userExtensionHandlerInterceptor(context.getObject());
        }
        return AsyncHandlerInterceptor.super.preHandle(request, response, handler);
    }


    @Override
    public void afterCompletion(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler, Exception ex)
            throws Exception {
        UserContextHolder.clearContext();
        AsyncHandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
