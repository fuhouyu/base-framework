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

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * <p>
 * oidc配置
 * </p>
 *
 * @author fuhouyu
 * @since 2024/11/4 22:06
 */
@Configuration(proxyBeanMethods = false)
public class AuthenticationProviderConfiguration {

    /**
     * dao层实现
     *
     * @param passwordEncoder    密码管理器
     * @param userDetailsService 用户详情接口
     * @return dao默认实现
     */
    @Bean
    public AuthenticationProvider daoAuthenticationProvider(UserDetailsService userDetailsService,
                                                            PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider(passwordEncoder);
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        return daoAuthenticationProvider;
    }

//    /**
//     * oidc provider
//     *
//     * @param userDetailsService           用户详情service
//     * @param clientRegistrationRepository 客户端仓库信息
//     * @return oidcProvider
//     */
//    @Bean
//    public AuthenticationProvider oidcAuthenticationProvider(UserDetailsService userDetailsService,
//                                                             ClientRegistrationRepository clientRegistrationRepository) {
//        return new OidcAuthenticationProvider(new DefaultOAuth2UserService(), userDetailsService, clientRegistrationRepository);
//    }


}
