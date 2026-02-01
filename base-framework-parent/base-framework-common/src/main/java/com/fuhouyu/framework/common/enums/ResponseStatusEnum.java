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

package com.fuhouyu.framework.common.enums;

import com.fuhouyu.framework.common.response.BaseResponseStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 系统通用响应状态枚举
 * <p>
 * 仅存放跨模块、跨项目的公共状态码。
 * </p>
 * @author fuhouyu
 * @since 2024/8/13 17:40
 */
@Getter
@AllArgsConstructor
public enum ResponseStatusEnum implements BaseResponseStatus {

    /**
     * 操作成功
     */
    SUCCESS("00000", "success"),

    /**
     * 用户端通用错误
     */
    CLIENT_ERROR("A0001", "client.error"),

    /**
     * 参数校验失败
     */
    INVALID_PARAM("A0400", "invalid.parameter"),

    /**
     * 未认证/登录失效
     */
    UNAUTHORIZED("A0401", "unauthorized"),

    /**
     * 刷新令牌已过期（Refresh Token 过期，必须重新登录）
     */
    REFRESH_TOKEN_EXPIRE("A0402", "refresh.token.expire"),

    /**
     * 权限不足
     */
    FORBIDDEN("A0403", "forbidden"),

    /**
     * 资源不存在
     */
    NOT_FOUND("A0404", "not.found"),

    /**
     * 请求方法不支持
     */
    METHOD_NOT_ALLOWED("A0405", "method.not.allowed"),

    /**
     * 不支持的媒体类型 (415)
     */
    NOT_SUPPORT_MEDIA_TYPE("A0415", "not.support.media.type"),


    /**
     * 服务器内部错误
     */
    SERVER_ERROR("B0001", "server.error"),

    /**
     * 业务处理异常
     */
    BUSINESS_ERROR("B0100", "business.process.error"),

    /**
     * 系统限流
     */
    FLOW_LIMIT("B0210", "flow.limit"),

    /**
     * 第三方服务异常
     */
    THIRD_PARTY_ERROR("C0001", "third.party.error");


    private final String code;
    private final String message;
}
