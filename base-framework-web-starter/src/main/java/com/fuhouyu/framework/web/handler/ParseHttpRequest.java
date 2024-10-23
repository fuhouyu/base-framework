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

import com.fuhouyu.framework.common.utils.HttpRequestUtil;
import com.fuhouyu.framework.context.Request;
import com.fuhouyu.framework.context.User;
import com.fuhouyu.framework.web.entity.RequestEntity;
import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import org.springframework.http.HttpHeaders;

import java.util.Locale;

/**
 * <p>
 * 解析http请求的接口
 * </p>
 *
 * @author fuhouyu
 * @since 2024/10/16 20:24
 */
@FunctionalInterface
public interface ParseHttpRequest {

    /**
     * 解析请求
     *
     * @param request 请求
     * @return 用户详情
     */
    User parseUser(@NonNull HttpServletRequest request);

    /**
     * 解析请求
     *
     * @param request 请求
     * @return 请求实体
     */
    default Request parseRequest(@NonNull HttpServletRequest request) {
        RequestEntity requestEntity = new RequestEntity();
        requestEntity.setAuthorization(request.getHeader(HttpHeaders.AUTHORIZATION));
        requestEntity.setRequestHost(request.getServerName());
        requestEntity.setRequestIp(HttpRequestUtil.getRemoteIp(request));
        requestEntity.setUserAgent(HttpRequestUtil.getUserAgent(request));
        requestEntity.setRequestTarget(String.format("%s %s",
                request.getMethod().toUpperCase(Locale.ROOT), request.getRequestURI()));
        return requestEntity;
    }
}
