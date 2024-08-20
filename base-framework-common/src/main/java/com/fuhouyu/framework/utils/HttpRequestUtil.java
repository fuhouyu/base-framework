/*
 * Copyright 2012-2020 the original author or authors.
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

package com.fuhouyu.framework.utils;

import com.fuhouyu.framework.constants.HttpRequestHeaderConstant;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;

/**
 * <p>
 * http请求工具类
 * </p>
 *
 * @author fuhouyu
 * @since 2024/8/18 16:32
 */
public class HttpRequestUtil {


    private HttpRequestUtil() {

    }


    /**
     * 获取真实的ip地址
     *
     * @param request http请求
     * @return remote ip address.
     */
    public static String getRemoteIp(HttpServletRequest request) {
        String xForwardedFor = request.getHeader(HttpRequestHeaderConstant.X_FORWARDED_FOR);

        if (!StringUtils.isBlank(xForwardedFor)) {
            return xForwardedFor.split(",")[0].trim();
        }
        String nginxHeader = request.getHeader(HttpRequestHeaderConstant.X_REAL_IP);
        return StringUtils.isBlank(nginxHeader) ? request.getRemoteAddr() : nginxHeader;
    }


    /**
     * 获取用户代理
     *
     * @param request http请求
     * @return 用户代理
     */
    public static String getUserAgent(HttpServletRequest request) {
        String userAgent = request.getHeader(HttpRequestHeaderConstant.USER_AGENT_HEADER);
        if (StringUtils.isEmpty(userAgent)) {
            userAgent = StringUtils
                    .defaultIfEmpty(request.getHeader(HttpRequestHeaderConstant.CLIENT_VERSION_HEADER), StringUtils.EMPTY);
        }
        return userAgent;
    }
}
