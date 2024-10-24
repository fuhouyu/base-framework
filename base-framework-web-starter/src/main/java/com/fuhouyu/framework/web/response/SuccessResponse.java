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
package com.fuhouyu.framework.web.response;

import com.fuhouyu.framework.common.response.BaseResponse;
import com.fuhouyu.framework.common.response.BaseResponseCode;
import com.fuhouyu.framework.web.enums.ResponseCodeEnum;

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

    private final BaseResponseCode response;

    private final T data;

    public SuccessResponse() {
        this(null);
    }

    public SuccessResponse(T data) {
        this.data = data;
        this.response = ResponseCodeEnum.SUCCESS;

    }

    @Override
    public Integer getCode() {
        return this.response.getCode();
    }

    @Override
    public String getMessage() {
        return this.response.getMessage();
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
