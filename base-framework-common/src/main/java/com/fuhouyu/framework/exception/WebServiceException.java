/*
 * Copyright 2024-2034 the original author or authors.
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

package com.fuhouyu.framework.exception;

import com.fuhouyu.framework.response.ResponseCode;
import com.fuhouyu.framework.response.ResponseCodeEnum;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <p>
 * http请求异常
 * </p>
 *
 * @author fuhouyu
 * @since 2024/8/15 15:40
 */
@Getter
@Setter
@ToString
public class WebServiceException extends RuntimeException {

    private int errCode;

    private String errMsg;

    public WebServiceException() {
        this(ResponseCodeEnum.SERVER_ERROR.getCode(), ResponseCodeEnum.SERVER_ERROR.getMessage());
    }

    public WebServiceException(String messagePattern, Object... arguments) {
        super(String.format(messagePattern, arguments));
        this.errCode = ResponseCodeEnum.SERVER_ERROR.getCode();
        this.errMsg = super.getMessage();
    }

    public WebServiceException(ResponseCode responseCode) {
        super(responseCode.getMessage());
        this.errCode = ResponseCodeEnum.SERVER_ERROR.getCode();
        this.errMsg = ResponseCodeEnum.SERVER_ERROR.getMessage();
    }

    public WebServiceException(Throwable throwable) {
        super(throwable);
        this.errCode = ResponseCodeEnum.SERVER_ERROR.getCode();
        this.errMsg = throwable.getMessage();
    }


    public WebServiceException(ResponseCode responseCode, String messagePattern, Object... arguments) {
        super(String.format(messagePattern, arguments));

        this.errCode = responseCode.getCode();
        this.errMsg = super.getMessage();
    }


    public WebServiceException(Throwable cause, int errCode, String errMsg, Throwable causeThrowable) {
        super(cause);
        this.errCode = errCode;
        this.errMsg = errMsg;
    }

    public WebServiceException(int errCode, String errMsg) {
        super(errMsg);
        this.errCode = errCode;
        this.errMsg = errMsg;
    }

    public WebServiceException(int errCode, String errMsg, Throwable causeThrowable) {
        super(errMsg, causeThrowable);
        this.errCode = errCode;
        this.errMsg = errMsg;
    }

    public WebServiceException(String message, int errCode, String errMsg, Throwable causeThrowable) {
        super(message);
        this.errCode = errCode;
        this.errMsg = errMsg;
    }

}
