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

package com.fuhouyu.framework.model.response;

/**
 * <p>
 * 响应码枚举类
 * </p>
 *
 * @author fuhouyu
 * @since 2024/8/13 17:40
 */
public enum ResponseCodeStatusEnum implements ResponseCodeStatus {

    // 200

    /**
     * 成功
     */
    SUCCESS(200, "成功"),

    // 400
    /**
     * 参数错误
     */
    INVALID_PARAM(400, "参数错误"),

    /**
     * 无权限
     */
    NOT_AUTH(401, "用户无权限"),

    /**
     * 资源不存在或已被删除
     */
    NOT_FOUND(404, "资源不存在或已被删除"),

    /**
     * 不支持的方法
     */
    METHOD_NOT_ALLOWED(405, "不支持的方法"),

    /**
     * 服务内部错误
     */
    SERVER_ERROR(500, "服务器内部错误"),
    ;


    private final int code;

    private final String message;


    ResponseCodeStatusEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public int getCode() {
        return this.code;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
