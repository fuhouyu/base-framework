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

import com.fuhouyu.framework.common.enums.ResponseStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serial;

/**
 * <p>
 * 成功响应
 * </p>
 *
 * @author fuhouyu
 * @since 2024/10/16 20:30
 */
public class SuccessResponse<T> implements BaseResponse<T> {

    @Serial
    private static final long serialVersionUID = 1625431681231238161L;

    @Schema(name = "code", description = "响应码", requiredMode = Schema.RequiredMode.REQUIRED)
    private final Integer code;

    @Schema(name = "message", description = "响应消息", requiredMode = Schema.RequiredMode.REQUIRED)
    private final String message;

    @Schema(name = "data", description = "响应数据", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private final T data;

    public SuccessResponse() {
        this(null);
    }

    public SuccessResponse(T data) {
        this.data = data;
        this.code = ResponseStatusEnum.SUCCESS.getCode();
        this.message = ResponseStatusEnum.SUCCESS.getMessage();

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
        return true;
    }

    @Override
    public T getData() {
        return this.data;
    }
}
