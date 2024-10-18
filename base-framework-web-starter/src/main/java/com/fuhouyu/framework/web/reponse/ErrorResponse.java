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
package com.fuhouyu.framework.web.reponse;

import com.fuhouyu.framework.response.BaseResponse;
import com.fuhouyu.framework.web.enums.ErrorLevelEnum;
import com.fuhouyu.framework.web.enums.ResponseCodeEnum;

/**
 * <p>
 * 错误响应
 * </p>
 *
 * @author fuhouyu
 * @since 2024/10/16 20:35
 */
public class ErrorResponse<T> implements BaseResponse<T> {

    private final Integer code;

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
        this(ResponseCodeEnum.SERVER_ERROR.getCode(), ResponseCodeEnum.SERVER_ERROR.getMessage());
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
