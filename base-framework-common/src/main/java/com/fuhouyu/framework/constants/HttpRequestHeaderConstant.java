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

package com.fuhouyu.framework.constants;

/**
 * <p>
 * http请求的常量类
 * </p>
 *
 * @author fuhouyu
 * @since 2024/8/15 19:54
 */
public interface HttpRequestHeaderConstant {

    // 自定义请求头
    /**
     * 自定义的用户详情请求头
     */
    String USERINFO_HEADER = "X-Custom-Userinfo";

    // 标准请求头

    /**
     * 客户端版本
     */
    String CLIENT_VERSION_HEADER = "Client-Version";

    /**
     * 用户代理
     */
    String USER_AGENT_HEADER = "User-Agent";

    /**
     * 消息内容类型
     */
    String CONTENT_TYPE = "Content-Type";

    /**
     * 消息内容长度
     */
    String CONTENT_LENGTH = "Content-Length";

    /**
     * 消息内容编码
     */
    String CONTENT_ENCODING = "Content-Encoding";

    // nginx转发的请求头
    /**
     * 真实ip地址
     */
    String X_REAL_IP = "X-Real-IP";

    /**
     * 来自
     */
    String X_FORWARDED_FOR = "X-Forwarded-For";
}
