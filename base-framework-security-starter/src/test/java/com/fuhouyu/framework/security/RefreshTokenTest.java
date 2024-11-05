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
package com.fuhouyu.framework.security;

import com.fuhouyu.framework.security.core.GrantTypeAuthenticationTokenEnum;
import com.fuhouyu.framework.security.entity.GrantTypeAuthenticationEntity;
import com.fuhouyu.framework.security.entity.TokenEntity;
import com.fuhouyu.framework.security.token.TokenStore;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

/**
 * <p>
 * 刷新令牌测试类
 * </p>
 *
 * @author fuhouyu
 * @since 2024/10/22 22:21
 */
class RefreshTokenTest extends BaseTest {

    @Autowired
    private TokenStore tokenStore;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Test
    void testRefreshToken() {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken("admin", "admin");
        Authentication authenticate = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        TokenEntity tokenEntity = tokenStore.createToken(authenticate, 6000, 6000);
        Assertions.assertNotNull(tokenEntity);
        GrantTypeAuthenticationEntity grantTypeAuthenticationEntity =
                new GrantTypeAuthenticationEntity(GrantTypeAuthenticationTokenEnum.PASSWORD.getGrantType(),
                        "admin", "admin");
//        grantTypeAuthenticationEntity.addParameter("refreshToken", tokenEntity.getRefreshToken().getTokenValue());
        AbstractAuthenticationToken abstractAuthenticationToken = grantTypeAuthenticationEntity.createAuthenticationToken();
        Authentication refreshAuthenticate = authenticationManager.authenticate(abstractAuthenticationToken);
        TokenEntity refreshTokenEntity = tokenStore.createToken(refreshAuthenticate, 6000, 6000);
        Assertions.assertNotNull(refreshTokenEntity);
    }

}
