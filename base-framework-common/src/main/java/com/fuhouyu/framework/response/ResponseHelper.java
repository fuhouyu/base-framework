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

package com.fuhouyu.framework.response;


/**
 * <p>
 *
 * </p>
 *
 * @author fuhouyu
 * @since 2024/8/13 17:32
 */
public class ResponseHelper {

    private ResponseHelper() {
    }

    public static <T> RestResult<T> success(T data) {
        return RestResult.<T>builder()
                .withResponseCodeStatus(ResponseCodeEnum.SUCCESS)
                .withData(data).build();
    }

    public static <T> RestResult<T> success() {
        return success(null);
    }

    public static <T> RestResult<T> failed(int code, String message) {
        return RestResult.<T>builder()
                .withCode(code)
                .withMessage(message)
                .build();
    }

    public static <T> RestResult<T> failed(ResponseCode responseCode) {
        return failed(responseCode.getCode(), responseCode.getMessage());
    }

    public static <T> RestResult<T> failed(ResponseCode responseCode, String message) {
        return failed(responseCode.getCode(), message);
    }
}
