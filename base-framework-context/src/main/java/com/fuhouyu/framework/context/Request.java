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

package com.fuhouyu.framework.context;

import com.fuhouyu.framework.common.service.AdditionalInformationFunction;

import java.io.Serializable;

/**
 * <p>
 * request请求
 * </p>
 *
 * @author fuhouyu
 * @since 2024/8/18 14:51
 */
public interface Request extends AdditionalInformationFunction, Serializable {

    /**
     * 从请求头中获取认证信息
     *
     * @return 请求中头的认证信息，例如token
     */
    String getAuthorization();

    /**
     * 当前请求的ip
     *
     * @return 来源ip
     */
    String getRequestIp();


    /**
     * 当前请求的host
     *
     * @return 请求的host
     */
    String getRequestHost();

    /**
     * 获取请求的目标方法
     *
     * @return 获取请求的目标方法
     */
    String getRequestTarget();

    /**
     * 获取用户请求代理
     *
     * @return 用户请求代理
     */
    String getUserAgent();

}
