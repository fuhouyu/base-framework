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

package com.fuhouyu.framework.security.service;

import com.fuhouyu.framework.security.model.dto.ApplicationDTO;
import com.fuhouyu.framework.security.token.DefaultOAuth2Token;
import com.fuhouyu.framework.security.token.TokenStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * <p>
 * 用户认证相关接口实现
 * </p>
 *
 * @author fuhouyu
 * @since 2024/8/15 17:07
 */
public class DefaultUserAuthServiceImpl implements UserAuthService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultUserAuthServiceImpl.class);

    private final TokenStore tokenStore;

    private final AuthenticationManager authenticationManager;

    public DefaultUserAuthServiceImpl(TokenStore tokenStore, AuthenticationManager authenticationManager) {
        this.tokenStore = tokenStore;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public DefaultOAuth2Token userLogin(Authentication authentication) throws Exception {
        Authentication authenticate = authenticationManager.authenticate(authentication);
        return this.getAccessToken(authenticate);
    }


    /**
     * 用户登录，获取token.
     *
     * @param authentication 认证详情
     * @return dto对象
     */
    private DefaultOAuth2Token getAccessToken(Authentication authentication) {
        ApplicationDTO contextApplication = (ApplicationDTO) SecurityContextHolder.getContext().getAuthentication()
                .getDetails();

        int accessTokenValidity = contextApplication.getAccessTokenExpireTime();
        int refreshTokenValidity = contextApplication.getRefreshTokenExpireTime();

        return tokenStore.createToken(authentication,
                accessTokenValidity,
                refreshTokenValidity);
    }
}
