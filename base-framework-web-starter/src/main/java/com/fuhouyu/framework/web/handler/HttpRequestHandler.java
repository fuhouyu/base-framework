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

import com.fuhouyu.framework.context.ContextHolderStrategy;
import com.fuhouyu.framework.context.DefaultListableContextFactory;
import com.fuhouyu.framework.context.Request;
import com.fuhouyu.framework.context.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.AsyncHandlerInterceptor;

/**
 * <p>
 * http请求用户拦截处理器
 * </p>
 *
 * @author fuhouyu
 * @since 2024/8/15 19:52
 */
@RequiredArgsConstructor
@Slf4j
public class HttpRequestHandler implements AsyncHandlerInterceptor {

    private final ParseHttpRequest parseHttpRequest;


    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) throws Exception {
        User user = parseHttpRequest.parseUser(request);
        Request requestEntity = parseHttpRequest.parseRequest(request);
        DefaultListableContextFactory context = new DefaultListableContextFactory();
        context.setUser(user);
        context.setRequest(requestEntity);
        ContextHolderStrategy.setContext(context);
        return AsyncHandlerInterceptor.super.preHandle(request, response, handler);
    }


    @Override
    public void afterCompletion(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler, Exception ex)
            throws Exception {
        ContextHolderStrategy.clearContext();
        AsyncHandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
