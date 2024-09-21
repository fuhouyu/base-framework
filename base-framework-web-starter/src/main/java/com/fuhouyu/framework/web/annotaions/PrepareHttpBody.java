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

package com.fuhouyu.framework.web.annotaions;

import java.lang.annotation.*;

/**
 * <p>
 * 处理http body的请求处理
 * </p>
 *
 * @author fuhouyu
 * @since 2024/8/21 22:47
 */
@Target(value = ElementType.METHOD)
@Retention(value = RetentionPolicy.RUNTIME)
@Documented
public @interface PrepareHttpBody {

    /**
     * 在http请求前处理body中的数据进行解密
     *
     * @return true 解密
     */
    boolean preDecryption() default true;

    /**
     * 在http 返回结果前对body 数据进行加密
     *
     * @return true /false
     */
    boolean postEncryption() default false;
}
