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

import com.fuhouyu.framework.security.core.ExtensionUserDetailsService;
import com.fuhouyu.framework.security.core.passwordencoder.PasswordEncoderFactory;
import com.fuhouyu.framework.security.core.provider.oidc.OidcAuthenticationProvider;
import com.fuhouyu.framework.security.core.provider.refreshtoken.RefreshAuthenticationProvider;
import com.fuhouyu.framework.security.properties.OpenPlatformProperties;
import com.fuhouyu.framework.security.token.TokenStore;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;

import java.util.List;

/**
 * <p>
 * oidc配置
 * </p>
 *
 * @author fuhouyu
 * @since 2024/11/4 22:06
 */
@Configuration(proxyBeanMethods = false)
@AutoConfigureAfter({
        SecurityAutoConfiguration.class,
        OAuth2ClientAutoConfiguration.class
})
@EnableConfigurationProperties(OpenPlatformProperties.class)
public class AuthenticationAutoConfiguration {

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
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider(userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);
        return daoAuthenticationProvider;
    }

    /**
     * oidc provider
     *
     * @param userDetailsService           用户详情service
     * @param clientRegistrationRepository 客户端仓库信息
     * @return oidcProvider
     */
    @Bean
    @ConditionalOnBean({ExtensionUserDetailsService.class, ClientRegistrationRepository.class})
    public AuthenticationProvider oidcAuthenticationProvider(ExtensionUserDetailsService userDetailsService,
                                                             ClientRegistrationRepository clientRegistrationRepository) {
        return new OidcAuthenticationProvider(new DefaultOAuth2UserService(), userDetailsService, clientRegistrationRepository);
    }


    /**
     * 刷新令牌提供者
     *
     * @param tokenStore token存储
     * @return provider
     */
    @Bean
    @ConditionalOnBean(TokenStore.class)
    public AuthenticationProvider refreshTokenAuthenticationProvider(TokenStore tokenStore) {
        return new RefreshAuthenticationProvider(tokenStore);
    }


    /**
     * 认证管理器配置这里可以进行除其他登录模式的扩展，需要实现{@link AuthenticationProvider}
     *
     * @param authenticationProviders 认证提供者集合
     * @return 认证管理器
     */
    @Bean
    @Primary
    @ConditionalOnBean({AuthenticationProvider.class})
    public AuthenticationManager authenticationManager(List<AuthenticationProvider> authenticationProviders) {
        return new ProviderManager(authenticationProviders);
    }

    /**
     * 返回sm3 密码编码器的bean，当passwordEncoder不存在时，则会创建。
     *
     * @return sm3 密码编码器bean
     */
    @Bean
    @ConditionalOnMissingBean(PasswordEncoder.class)
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactory.createDelegatingPasswordEncoder("sm3");
    }


}
