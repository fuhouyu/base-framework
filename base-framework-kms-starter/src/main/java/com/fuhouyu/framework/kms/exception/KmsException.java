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

package com.fuhouyu.framework.kms.exception;

/**
 * <p>
 * kms异常类
 * </p>
 *
 * @author fuhouyu
 * @since 2024/1/9 09:29
 */
public class KmsException extends RuntimeException {

    public KmsException() {
    }

    public KmsException(String message) {
        super(message);
    }

    public KmsException(String message, Throwable cause) {
        super(message, cause);
    }

    public KmsException(Throwable cause) {
        super(cause);
    }

    public KmsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
