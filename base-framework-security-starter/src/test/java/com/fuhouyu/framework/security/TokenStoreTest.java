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

import com.fuhouyu.framework.cache.CacheAutoConfigure;
import com.fuhouyu.framework.cache.service.CacheService;
import com.fuhouyu.framework.security.token.DefaultOAuth2Token;
import com.fuhouyu.framework.security.token.TokenStore;
import com.fuhouyu.framework.security.token.TokenStoreCache;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

/**
 * <p>
 * token存储测试类
 * </p>
 *
 * @author fuhouyu
 * @since 2024/8/14 22:35
 */
@SpringBootTest(classes = {
        CacheAutoConfigure.class,
})
@TestPropertySource(locations = {"classpath:application.yaml"})
class TokenStoreTest {


    @Autowired
    private CacheService<String, Object> cacheService;

    private TokenStore tokenStore;

    private Authentication authentication;

    @BeforeEach
    void setup() {
        tokenStore = new TokenStoreCache(cacheService);
        User user = new User("testUser", "testPassword", Collections.emptyList());
        authentication = new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities());
    }

    @Test
    void testTokenStore() {

        DefaultOAuth2Token accessToken = tokenStore.createToken(authentication, 60, 60);
        Assertions.assertNotNull(accessToken, "生成的token不能为空");

        Authentication tokenAuthentication = tokenStore.readAuthentication(accessToken);
        Assertions.assertNotNull(tokenAuthentication, "未获取到token认证的对象");

        OAuth2RefreshToken oAuth2RefreshToken = tokenStore.readRefreshToken(accessToken.getAuth2RefreshToken().getTokenValue());
        Assertions.assertNotNull(oAuth2RefreshToken, "未获取到刷新令牌对象");

        Authentication authenticationByRefreshToken = tokenStore.readAuthenticationForRefreshToken(oAuth2RefreshToken);
        Assertions.assertNotNull(authenticationByRefreshToken, "未通过刷新令牌获取到认证对象");

        tokenStore.removeAllToken(accessToken);
        OAuth2AccessToken oAuth2AccessToken = tokenStore.readAccessToken(accessToken.getTokenValue());
        Assertions.assertNull(oAuth2AccessToken, "access token 未被清除");

        Assertions.assertNull(tokenStore.readRefreshToken(accessToken.getAuth2RefreshToken().getTokenValue()), "refresh token 未被清除");
    }

    @TestComponent
    public static class AuthenticationProvider {


        @Bean
        public RestTemplate restTemplate() {
            return new RestTemplate();
        }

        @Bean
        public UserDetailsService userDetailsService() {
            return new InMemoryUserDetailsManager();
        }

    }

}
