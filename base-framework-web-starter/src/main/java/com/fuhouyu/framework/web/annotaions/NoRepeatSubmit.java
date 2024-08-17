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

package com.fuhouyu.framework.web.annotaions;

import java.lang.annotation.*;

/**
 * <p>
 * 防重复提交注解
 * </p>
 *
 * @author fuhouyu
 * @since 2024/8/17 18:31
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
public @interface NoRepeatSubmit {

    /**
     * 重复提交的错误信息
     *
     * @return 重复提交的错误信息
     */
    String message() default "Duplicate submission is not allowed.";

    /**
     * 会从请求头中获取当前的token value
     * 如果值不存在，或该值与cache中的不匹配，将会抛出
     *
     * @return token键名
     * @see NoRepeatSubmit#message()
     */
    String headerToken() default "X-Form-Token";


    /**
     * 默认对post、put生效
     *
     * @return post put生效
     */
    String[] httpMethods() default {"POST", "PUT"};

}
