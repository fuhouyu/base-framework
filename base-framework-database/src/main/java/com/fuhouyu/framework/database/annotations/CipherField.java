/*
 * Copyright 2012-2020 the original author or authors.
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

package com.fuhouyu.framework.database.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * 字段加解密注解
 * </p>
 *
 * @author fuhouyu
 * @since 2024/9/17 22:11
 */
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface CipherField {

    /**
     * 加密算法
     *
     * @return 算法
     */
    Algorithm algorithm() default Algorithm.AES;


    /**
     * 算法枚举
     */
    enum Algorithm {
        /**
         * AES 对称算法
         */
        AES,
        /**
         * 非对称加密算法
         */
        RSA,
        /**
         * HMAC
         */
        HMAC,
        /**
         * 摘要算法
         */
        DIGEST,
    }
}
