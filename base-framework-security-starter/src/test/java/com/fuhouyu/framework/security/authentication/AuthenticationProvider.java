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

package com.fuhouyu.framework.security.authentication;

import org.springframework.boot.test.context.TestComponent;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.web.client.RestTemplate;

/**
 * <p>
 *
 * </p>
 *
 * @author fuhouyu
 * @since 2024/8/24 22:19
 */
@TestComponent
public class AuthenticationProvider {


    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new InMemoryUserDetailsManager();
    }

//    @Bean
//    @Primary
//    public AuthenticationManager authenticationManager(
//            List<AuthenticationProvider> authenticationProviders,
//            OpenPlatformAuthProperties openPlatformAuthProperties,
//            RestTemplate restTemplate) {
//        WechatAppletsPlatformProvider wechatAppletsPlatformProvider = new WechatAppletsPlatformProvider(restTemplate,
//                new InMemoryUserDetailsManager(), openPlatformAuthProperties.getAuth()
//                .get(OpenPlatformAuthProperties.OpenPlatformAuthTypeEnum.WECHAT_APPLET));
//        authenticationProviders.add(wechatAppletsPlatformProvider);
//        return new ProviderManager(authenticationProviders);
//    }
//
//    @Bean
//    @Primary
//    @ConditionalOnMissingBean(UserAuthService.class)
//    public UserAuthService userAuthService(TokenStore tokenStore, AuthenticationManager authenticationManager) {
//        return new DefaultUserAuthServiceImpl(tokenStore, authenticationManager);
//    }
}
