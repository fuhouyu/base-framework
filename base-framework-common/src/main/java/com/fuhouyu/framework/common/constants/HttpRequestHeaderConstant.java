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

package com.fuhouyu.framework.common.constants;

/**
 * <p>
 * http请求的常量类
 * </p>
 *
 * @author fuhouyu
 * @since 2024/8/15 19:54
 */
public class HttpRequestHeaderConstant {

    /**
     * 自定义的用户详情请求头
     */
    public static final String USERINFO_HEADER = "X-Custom-Userinfo";

    // 自定义请求头
    /**
     * 客户端版本
     */
    public static final String CLIENT_VERSION_HEADER = "Client-Version";

    // 标准请求头
    /**
     * 用户代理
     */
    public static final String USER_AGENT_HEADER = "User-Agent";
    /**
     * 消息内容类型
     */
    public static final String CONTENT_TYPE = "Content-Type";
    /**
     * 消息内容长度
     */
    public static final String CONTENT_LENGTH = "Content-Length";
    /**
     * 消息内容编码
     */
    public static final String CONTENT_ENCODING = "Content-Encoding";
    /**
     * 真实ip地址
     */
    public static final String X_REAL_IP = "X-Real-IP";

    // nginx转发的请求头
    /**
     * 来自
     */
    public static final String X_FORWARDED_FOR = "X-Forwarded-For";

    private HttpRequestHeaderConstant() {

    }
}
