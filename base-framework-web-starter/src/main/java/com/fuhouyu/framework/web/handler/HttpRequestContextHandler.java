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

package com.fuhouyu.framework.web.handler;

import com.fuhouyu.framework.context.Context;
import com.fuhouyu.framework.context.request.Request;
import com.fuhouyu.framework.context.request.RequestContextHolder;
import com.fuhouyu.framework.utils.HttpRequestUtil;
import com.fuhouyu.framework.web.model.HttpRequestModel;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.AsyncHandlerInterceptor;

import java.util.Locale;

/**
 * <p>
 * http请求上下文拦截器
 * </p>
 *
 * @author fuhouyu
 * @since 2024/8/18 16:30
 */
public class HttpRequestContextHandler implements AsyncHandlerInterceptor {

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request,
                             @NonNull HttpServletResponse response,
                             @NonNull Object handler) throws Exception {
        HttpRequestModel httpRequestModel = new HttpRequestModel();
        httpRequestModel.setRequestHost(request.getServerName());
        httpRequestModel.setRequestIp(HttpRequestUtil.getRemoteIp(request));
        httpRequestModel.setUserAgent(HttpRequestUtil.getUserAgent(request));
        httpRequestModel.setRequestTarget(String.format("%s %s",
                request.getMethod().toUpperCase(Locale.ROOT), request.getRequestURI()));

        Context<Request> requestContext = RequestContextHolder.createEmptyContext();
        requestContext.setObject(httpRequestModel);

        return AsyncHandlerInterceptor.super.preHandle(request, response, handler);
    }

    @Override
    public void afterCompletion(@NonNull HttpServletRequest request,
                                @NonNull HttpServletResponse response,
                                @NonNull Object handler, Exception ex) throws Exception {
        RequestContextHolder.clearContext();
        AsyncHandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
