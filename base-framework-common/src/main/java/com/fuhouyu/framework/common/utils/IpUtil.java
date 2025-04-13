/*
 * Copyright 2024-2025 fuhouyu.
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

import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * <p>
 * ip工具类
 * </p>
 *
 * @author fuhouyu
 * @since 2024/12/14 17:51
 */
@Slf4j
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class IpUtil {

    private static final String[] POSSIBLE_HEADERS = new String[]{
            "X-Forwarded-For", "X-Real-IP", "Proxy-Client-IP",
            "WL-Proxy-Client-IP", "HTTP_CLIENT_IP", "HTTP_X_FORWARDED_FOR"
    };

    private static final String IP_V4 = "127.0.0.1";

    private static final String IP_V6 = "0:0:0:0:0:0:0:1";

    private static final String UNKNOWN = "unknown";


    /**
     * 获取客户端id
     *
     * @param request http请求
     * @return 真实ip
     */
    public static String getRequestIp(HttpServletRequest request) {
        String ip;
        for (String header : POSSIBLE_HEADERS) {
            ip = request.getHeader(header);
            if (!StringUtils.isEmpty(ip) && !UNKNOWN.equalsIgnoreCase(ip)) {
                return ip.contains(",") ?
                        ip.substring(0, ip.indexOf(','))
                        : ip;
            }
        }
        ip = request.getRemoteAddr();
        if (IP_V6.equals(ip) || IP_V4.equals(ip)) {
            return getLocalRealIp();
        }
        return ip;
    }

    /**
     * 获取本机IP地址
     *
     * @return 本机ip
     */
    public static String getLocalRealIp() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            LoggerUtil.error(log, "获取ip异常:{} 返回:{}", e.getMessage(), IP_V4);
            return IP_V4;
        }
    }


}
