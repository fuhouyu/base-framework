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

package com.fuhouyu.framework.security.authentication;

import com.fuhouyu.framework.security.core.authentication.wechat.WechatAppletsPlatformProvider;
import com.fuhouyu.framework.security.properties.OpenPlatformAuthProperties;
import com.fuhouyu.framework.security.service.DefaultUserAuthServiceImpl;
import com.fuhouyu.framework.security.service.UserAuthService;
import com.fuhouyu.framework.security.token.TokenStore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author fuhouyu
 * @since 2024/8/24 22:19
 */
@TestComponent
public class AuthenticationProviderTest {


    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    @Primary
    public AuthenticationManager authenticationManager(
            List<AuthenticationProvider> authenticationProviders,
            OpenPlatformAuthProperties openPlatformAuthProperties,
            RestTemplate restTemplate) {
        WechatAppletsPlatformProvider wechatAppletsPlatformProvider = new WechatAppletsPlatformProvider(restTemplate,
                new InMemoryUserDetailsManager(), openPlatformAuthProperties.getAuth()
                .get(OpenPlatformAuthProperties.OpenPlatformAuthTypeEnum.WECHAT_APPLET));
        authenticationProviders.add(wechatAppletsPlatformProvider);
        return new ProviderManager(authenticationProviders);
    }

    @Bean
    @Primary
    @ConditionalOnMissingBean(UserAuthService.class)
    public UserAuthService userAuthService(TokenStore tokenStore, AuthenticationManager authenticationManager) {
        return new DefaultUserAuthServiceImpl(tokenStore, authenticationManager);
    }
}
