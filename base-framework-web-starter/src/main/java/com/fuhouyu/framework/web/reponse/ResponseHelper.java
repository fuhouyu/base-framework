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
import com.fuhouyu.framework.response.BaseResponseCode;

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

    public static <T> BaseResponse<T> success(T data) {
        return new SuccessResponse<>(data);
    }


    public static <T> BaseResponse<T> success() {
        return success(null);
    }

    public static <T> BaseResponse<T> failed(int code, String message, T data) {
        return new ErrorResponse<>(code, message, data);
    }

    public static <T> BaseResponse<T> failed(BaseResponseCode baseResponseCode) {
        return failed(baseResponseCode.getCode(), baseResponseCode.getMessage(), null);
    }

    public static <T> BaseResponse<T> failed(BaseResponseCode baseResponseCode, String message) {
        return failed(baseResponseCode.getCode(), message, null);
    }

    public static <T> BaseResponse<T> failed(BaseResponseCode baseResponseCode, T data) {
        return failed(baseResponseCode.getCode(), baseResponseCode.getMessage(), data);
    }

    public static <T> BaseResponse<T> failed(BaseResponseCode baseResponseCode, String message, T data) {
        return failed(baseResponseCode.getCode(), message, data);
    }

}
