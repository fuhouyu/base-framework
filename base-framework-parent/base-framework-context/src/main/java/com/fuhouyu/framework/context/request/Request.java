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

package com.fuhouyu.framework.context.request;

import com.fuhouyu.framework.common.function.AdditionalInformationFunction;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.Serializable;

/**
 * <p>
 * 请求上下文抽象
 * </p>
 * <p>
 * 该接口封装了当前执行环境的请求元数据。通过继承 {@link AdditionalInformationFunction}，
 * 可以灵活扩展如：设备指纹、地理位置、灰度标签等非标准信息。
 * </p>
 *
 * @author fuhouyu
 * @since 2024/8/18
 */
public interface Request extends AdditionalInformationFunction {

    /**
     * 获取全局唯一的请求标识（TraceId）
     * <p>用于分布式链路追踪，建议在网关层生成并透传</p>
     *
     * @return 请求 ID
     */
    String getRequestId();

    /**
     * 获取原始的 HTTP Servlet 请求对象
     * <p>警告：在非 Web 环境（如异步任务、RPC 调用）下可能返回 null</p>
     *
     * @return {@link HttpServletRequest}
     */
    HttpServletRequest getHttpServletRequest();

    /**
     * 获取原始的 HTTP 响应对象
     *
     * @return {@link HttpServletResponse}
     */
    HttpServletResponse getResponse();

    /**
     * 从请求头中获取认证信息
     * <p>通常为 "Bearer [token]" 格式</p>
     *
     * @return 认证 Token
     */
    String getAuthorization();

    /**
     * 获取客户端来源 IP 地址
     * <p>实现类应考虑 X-Forwarded-For 等代理头信息</p>
     *
     * @return 来源 IP
     */
    String getRequestIp();

    /**
     * 获取当前请求的主机地址（Host）
     *
     * @return 请求 Host
     */
    String getRequestHost();

    /**
     * 获取请求的 URI 路径
     * <p>例如：/api/v1/user/login</p>
     *
     * @return 请求路径
     */
    String getRequestUri();

    /**
     * 获取 HTTP 请求方法
     * <p>例如：GET, POST, PUT, DELETE</p>
     *
     * @return HTTP 方法
     */
    String getRequestMethod();

    /**
     * 获取用户请求代理标识（User-Agent）
     *
     * @return UA 字符串
     */
    String getUserAgent();

    /**
     * 获取请求进入系统的毫秒时间戳
     *
     * @return 入参时间戳
     */
    long getRequestTime();

    /**
     * 获取请求的目标执行器标识
     * <p>可以是 Controller 方法的全路径名，用于埋点监控</p>
     *
     * @return 目标方法名
     */
    String getRequestTarget();
}
