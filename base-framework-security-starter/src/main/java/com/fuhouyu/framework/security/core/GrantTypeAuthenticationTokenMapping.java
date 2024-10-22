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
package com.fuhouyu.framework.security.core;

import com.fuhouyu.framework.utils.JacksonUtil;
import org.springframework.security.authentication.AbstractAuthenticationToken;

/**
 * <p>
 * 授权类型和AuthenticationToken映射
 * </p>
 *
 * @author fuhouyu
 * @since 2024/10/22 21:18
 */
public interface GrantTypeAuthenticationTokenMapping {

    /**
     * 获取授权类型
     *
     * @return 授权类型
     */
    String getGrantType();

    /**
     * 获取授权token类型
     *
     * @param <T> 具体的子类
     * @return 授权token类型
     */
    <T extends AbstractAuthenticationToken> Class<T> getAuthenticationTokenClass();

    /**
     * 加载该类
     *
     * @param param 参数映射
     * @return AbstractAuthenticationToken 子类
     */
    default AbstractAuthenticationToken loadAuthenticationToken(Object param) {
        Class<AbstractAuthenticationToken> authenticationTokenClass = this.getAuthenticationTokenClass();
        return JacksonUtil.tryParse(() ->
                JacksonUtil.getObjectMapper().convertValue(param, authenticationTokenClass));
    }

}
