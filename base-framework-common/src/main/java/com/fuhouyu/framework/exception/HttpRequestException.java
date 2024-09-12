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

/**
 * <p>
 * http请求异常
 * </p>
 *
 * @author fuhouyu
 * @since 2024/8/15 15:40
 */
public class HttpRequestException extends RuntimeException {

    private int errCode;

    private String errMsg;

    private Throwable causeThrowable;

    public HttpRequestException(Throwable cause, int errCode, String errMsg, Throwable causeThrowable) {
        super(cause);
        this.errCode = errCode;
        this.errMsg = errMsg;
        this.causeThrowable = causeThrowable;
    }

    public HttpRequestException(int errCode, String errMsg) {
        super(errMsg);
        this.errCode = errCode;
        this.errMsg = errMsg;
    }

    public HttpRequestException(int errCode, String errMsg, Throwable causeThrowable) {
        super(errMsg, causeThrowable);
        this.errCode = errCode;
        this.errMsg = errMsg;
        this.causeThrowable = causeThrowable;
    }

    public HttpRequestException(String message, int errCode, String errMsg, Throwable causeThrowable) {
        super(message);
        this.errCode = errCode;
        this.errMsg = errMsg;
        this.causeThrowable = causeThrowable;
    }

    public int getErrCode() {
        return errCode;
    }

    public void setErrCode(int errCode) {
        this.errCode = errCode;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public Throwable getCauseThrowable() {
        return causeThrowable;
    }

    public void setCauseThrowable(Throwable causeThrowable) {
        this.causeThrowable = causeThrowable;
    }
}
