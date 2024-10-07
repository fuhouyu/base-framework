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

import com.fuhouyu.framework.cache.service.CacheService;
import com.fuhouyu.framework.security.core.passwordencoder.PasswordEncoderFactory;
import com.fuhouyu.framework.security.service.DefaultUserAuthServiceImpl;
import com.fuhouyu.framework.security.service.UserAuthService;
import com.fuhouyu.framework.security.token.TokenStore;
import com.fuhouyu.framework.security.token.TokenStoreCache;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

/**
 * <p>
 * 自动装配类
 * </p>
 *
 * @author fuhouyu
 * @since 2024/8/15 16:22
 */
@ComponentScan(basePackageClasses = SecurityAutoConfigure.class)
@Import({OpenPlatformAutoConfigure.class})
public class SecurityAutoConfigure {

    /**
     * redisToken存储.
     *
     * @param cacheService 缓存对象
     * @return token存储.
     */
    @Bean
    @ConditionalOnMissingBean(TokenStore.class)
    public TokenStore tokenStore(CacheService<String, Object> cacheService) {
        return new TokenStoreCache("user", cacheService);
    }


    /**
     * 认证管理器配置这里可以进行除其他登录模式的扩展，需要实现{@link AuthenticationProvider}
     *
     * @param authenticationProviders 认证提供者集合
     * @return 认证管理器
     */
    @Bean("authenticationManager")
    @Primary
    public AuthenticationManager authenticationManager(
            List<AuthenticationProvider> authenticationProviders,
            UserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder) {
        authenticationProviders.add(daoAuthenticationProvider(userDetailsService, passwordEncoder));
        return new ProviderManager(authenticationProviders);
    }

    /**
     * 创建userAuth bean
     *
     * @param tokenStore            token 存储对象
     * @param authenticationManager 认证管理器
     * @return userAuth Bean
     */
    @Bean
    @Primary
    public UserAuthService userAuthService(TokenStore tokenStore,
                                           @Qualifier("authenticationManager") AuthenticationManager authenticationManager) {
        return new DefaultUserAuthServiceImpl(tokenStore, authenticationManager);
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

    /**
     * dao层实现
     *
     * @param passwordEncoder 密码管理器
     * @param userDetailsService 用户详情接口
     * @return dao默认实现
     */
    private AuthenticationProvider daoAuthenticationProvider(UserDetailsService userDetailsService,
                                                             PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider(passwordEncoder);
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        return daoAuthenticationProvider;
    }


}
