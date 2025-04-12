/*
 * Copyright 2024-2025 fuhouyu.
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

package com.fuhouyu.framework.security;

import com.fuhouyu.framework.security.token.OAuth2Token;
import com.fuhouyu.framework.security.token.TokenStore;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;

import java.util.Collections;

/**
 * <p>
 * token存储测试类
 * </p>
 *
 * @author fuhouyu
 * @since 2024/8/14 22:35
 */
class TokenStoreTest extends BaseTest {

    @Autowired
    private TokenStore tokenStore;

    private Authentication authentication;

    @BeforeEach
    void setup() {
        User user = new User("testUser", "testPassword", Collections.emptyList());
        authentication = new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities());
    }

    @Test
    void testTokenStore() {

        OAuth2Token auth2Token = tokenStore.createToken(authentication, 60, 60);
        Assertions.assertNotNull(auth2Token, "生成的token不能为空");

        Authentication tokenAuthentication = tokenStore.readAuthentication(auth2Token.getAccessToken());
        Assertions.assertNotNull(tokenAuthentication, "未获取到token认证的对象");

        OAuth2RefreshToken oAuth2RefreshToken = tokenStore.readRefreshToken(auth2Token.getRefreshToken().getTokenValue());
        Assertions.assertNotNull(oAuth2RefreshToken, "未获取到刷新令牌对象");

        Authentication authenticationByRefreshToken = tokenStore.readAuthenticationForRefreshToken(oAuth2RefreshToken);
        Assertions.assertNotNull(authenticationByRefreshToken, "未通过刷新令牌获取到认证对象");

        tokenStore.removeAuth2Token(auth2Token);
        OAuth2Token notExists = tokenStore.readAuth2Token(auth2Token.getAccessToken().getTokenValue());
        Assertions.assertNull(notExists, "access token 未被清除");

        Assertions.assertNull(tokenStore.readRefreshToken(auth2Token.getRefreshToken().getTokenValue()), "refresh token 未被清除");
    }


}
