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
package com.fuhouyu.framework.common.response;

import com.fuhouyu.framework.common.enums.ErrorLevelEnum;
import com.fuhouyu.framework.common.enums.ResponseStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * <p>
 * 错误响应
 * </p>
 *
 * @author fuhouyu
 * @since 2024/10/16 20:35
 */
public class ErrorResponse<T> implements BaseResponse<T> {

    @Schema(name = "code", description = "响应码", requiredMode = Schema.RequiredMode.REQUIRED)
    private final Integer code;

    @Schema(name = "message", description = "响应消息", requiredMode = Schema.RequiredMode.REQUIRED)
    private final String message;

    private final T data;

    public ErrorResponse(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    @SuppressWarnings("unchecked")
    public ErrorResponse(Integer code, String message) {
        this(code, message, (T) ErrorLevelEnum.ERROR);
    }

    public ErrorResponse() {
        this(ResponseStatusEnum.SERVER_ERROR.getCode(), ResponseStatusEnum.SERVER_ERROR.getMessage());
    }

    @Override
    public Integer getCode() {
        return this.code;
    }

    @Override
    public String getMessage() {
        return this.message;
    }

    @Override
    public Boolean getIsSuccess() {
        return false;
    }

    @Override
    public T getData() {
        return this.data;
    }
}
