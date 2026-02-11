/*
 * Copyright 2024-present fuhouyu.
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
package com.fuhouyu.framework.common.annotations;

import com.fuhouyu.framework.common.response.BaseResponseStatus;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * <p>
 * 参数错误码注解
 * </p>
 *
 * @author fuhouyu
 * @since 2025/4/9 10:02
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ParamErrorResponse {

    /**
     * 需要使用的响应枚举
     *
     * @return 响应枚举
     */
    Class<? extends BaseResponseStatus> using();

    /**
     * 枚举中的枚举名称
     *
     * @return 枚举名称
     */
    @AliasFor("enumName")
    String value() default "";

    /**
     * 枚举中的常量名称
     *
     * @return 枚举中的常量名称
     */
    @AliasFor("value")
    String enumName() default "";
}
