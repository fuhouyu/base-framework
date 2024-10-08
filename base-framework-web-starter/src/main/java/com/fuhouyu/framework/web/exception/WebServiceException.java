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

package com.fuhouyu.framework.web.exception;

import com.fuhouyu.framework.response.ResponseCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <p>
 * web服务异常类
 * </p>
 *
 * @author fuhouyu
 * @since 2024/8/15 20:20
 */
@ToString
@Getter
@Setter
public class WebServiceException extends RuntimeException {

    private final int status;

    private final String message;

    private final transient ResponseCode responseStatus;

    /**
     * 构造函数
     *
     * @param responseStatus 响应状态
     */
    public WebServiceException(ResponseCode responseStatus) {
        this.responseStatus = responseStatus;
        this.status = responseStatus.getCode();
        this.message = responseStatus.getMessage();
    }

    /**
     * 构造函数
     * @param responseStatus 响应状态
     * @param errorMessage 错误信息
     */
    public WebServiceException(ResponseCode responseStatus, String errorMessage) {
        this.status = responseStatus.getCode();
        this.message = errorMessage;
        this.responseStatus = responseStatus;
    }

}
