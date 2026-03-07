/*
 * Copyright 2024-present fuhouyu.
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
import org.springframework.http.server.reactive.ServerHttpRequest;

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

    private static final String IP_V4_LOCAL = "127.0.0.1";
    private static final String IP_V6_LOCAL = "0:0:0:0:0:0:0:1";
    private static final String IP_V6_LOCAL_SHORT = "::1";
    private static final String UNKNOWN = "unknown";


    /**
     * 获取客户端 IP (Servlet 模式)
     *
     * @param request HTTP 请求
     * @return 真实 IP
     */
    public static String getRequestIp(HttpServletRequest request) {
        if (request == null) {
            return IP_V4_LOCAL;
        }

        String ip;
        for (String header : POSSIBLE_HEADERS) {
            ip = request.getHeader(header);
            if (StringUtils.isNotBlank(ip) && !UNKNOWN.equalsIgnoreCase(ip)) {
                return extractFirstIp(ip);
            }
        }

        ip = request.getRemoteAddr();
        return isLocalIp(ip) ? getLocalRealIp() : ip;
    }

    /**
     * 获取客户端 IP (WebFlux 模式)
     *
     * @param request Reactive HTTP 请求
     * @return 真实 IP
     */
    public static String getRequestIp(ServerHttpRequest request) {
        if (request == null) {
            return IP_V4_LOCAL;
        }

        String ip;
        for (String header : POSSIBLE_HEADERS) {
            ip = request.getHeaders().getFirst(header);
            if (StringUtils.isNotBlank(ip) && !UNKNOWN.equalsIgnoreCase(ip)) {
                return extractFirstIp(ip);
            }
        }

        ip = request.getRemoteAddress() != null
                ? request.getRemoteAddress().getAddress().getHostAddress()
                : null;

        return isLocalIp(ip) ? getLocalRealIp() : ip;
    }

    /**
     * 从逗号分隔的 IP 列表中提取第一个
     *
     * @param ip 可能包含多个 IP 的字符串
     * @return 第一个 IP
     */
    private static String extractFirstIp(String ip) {
        if (ip == null) {
            return IP_V4_LOCAL;
        }
        int index = ip.indexOf(',');
        return index > 0 ? ip.substring(0, index).trim() : ip.trim();
    }

    /**
     * 判断是否为本地 IP
     *
     * @param ip IP 地址
     * @return true 如果是本地地址
     */
    private static boolean isLocalIp(String ip) {
        if (ip == null) {
            return true;
        }
        return IP_V4_LOCAL.equals(ip)
                || IP_V6_LOCAL.equals(ip)
                || IP_V6_LOCAL_SHORT.equals(ip);
    }

    /**
     * 获取本机 IP 地址
     *
     * @return 本机 IP，获取失败返回 127.0.0.1
     */
    public static String getLocalRealIp() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            log.error("获取本机 IP 失败: {}", e.getMessage(), e);
            return IP_V4_LOCAL;
        }
    }
}