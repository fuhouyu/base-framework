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

package com.fuhouyu.framework.security.service;

import com.fuhouyu.framework.security.token.DefaultOAuth2Token;
import org.springframework.security.core.Authentication;

/**
 * <p>
 * 用户认证接口
 * </p>
 *
 * @author fuhouyu
 * @since 2024/8/15 16:56
 */
public interface UserAuthService {


    /**
     * 生成oauth2 Token
     *
     * @param authentication 认证对象
     * @return token对象
     * @throws Exception 登录情况下可能会出现的各种异常问题
     */
    DefaultOAuth2Token generatorToken(Authentication authentication) throws Exception;

}
