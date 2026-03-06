/*
 * Copyright 2024-present fuhouyu.
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
import com.fuhouyu.framework.common.enums.ResponseStatusEnum;
import com.fuhouyu.framework.common.response.R;
import com.fuhouyu.framework.common.utils.JacksonUtil;
import com.fuhouyu.framework.security.core.CacheOAuth2AuthorizationService;
import com.fuhouyu.framework.security.core.passwordencoder.PasswordEncoderFactory;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.security.oauth2.client.autoconfigure.OAuth2ClientAutoConfiguration;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.authorization.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.token.DelegatingOAuth2TokenGenerator;
import org.springframework.security.oauth2.server.authorization.token.OAuth2RefreshTokenGenerator;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

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
@RequiredArgsConstructor
public class AuthenticationAutoConfiguration {

    private final CacheService<String, Object> cacheService;

    private final RegisteredClientRepository registeredClientRepository;

    private final MessageSource messageSource;

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


    @Bean
    public OAuth2TokenGenerator<?> tokenGenerator() {
        OAuth2TokenGenerator<OAuth2AccessToken> accessTokenGenerator = context -> {
            if (!OAuth2TokenType.ACCESS_TOKEN.equals(context.getTokenType())) {
                return null;
            }
            // 获取该客户端配置的有效期
            Duration tokenTimeToLive = context.getRegisteredClient()
                    .getTokenSettings()
                    .getAccessTokenTimeToLive();
            Instant issuedAt = Instant.now();
            Instant expiresAt = issuedAt.plus(tokenTimeToLive);
            String tokenValue = UUID.randomUUID().toString().replace("-", "");
            return new OAuth2AccessToken(
                    OAuth2AccessToken.TokenType.BEARER,
                    tokenValue,
                    issuedAt,
                    expiresAt,
                    context.getAuthorizedScopes()
            );
        };
        OAuth2RefreshTokenGenerator refreshTokenGenerator = new OAuth2RefreshTokenGenerator();
        return new DelegatingOAuth2TokenGenerator(accessTokenGenerator, refreshTokenGenerator);
    }


    @Bean
    public AuthorizationServerSettings authorizationServerSettings() {
        return AuthorizationServerSettings.builder()
                .build();
    }

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    @ConditionalOnMissingBean(SecurityFilterChain.class)
    public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http) throws Exception {

        CacheOAuth2AuthorizationService authorizationService = new CacheOAuth2AuthorizationService(cacheService);

        OAuth2AuthorizationServerConfigurer authorizationServerConfigurer =
                new OAuth2AuthorizationServerConfigurer();

        http.with(authorizationServerConfigurer, (configurer) -> {
            configurer
                    .authorizationService(authorizationService)
                    .registeredClientRepository(registeredClientRepository);
        });
        RequestMatcher endpointsMatcher = authorizationServerConfigurer.getEndpointsMatcher();
        R<Void> unauthorized = R.fail(ResponseStatusEnum.UNAUTHORIZED);
        unauthorized.updateMessage(messageSource.getMessage(unauthorized.getMessage(), null,
                LocaleContextHolder.getLocale()));
        http
                .securityMatcher(endpointsMatcher)
                .cors(Customizer.withDefaults())
                .csrf(csrf -> csrf.ignoringRequestMatchers(endpointsMatcher))
                .authorizeHttpRequests(authorize -> authorize.anyRequest().authenticated())
                .exceptionHandling(exceptions -> exceptions
                        // 根据请求头决定是重定向还是回 JSON
                        .defaultAuthenticationEntryPointFor(
                                new LoginUrlAuthenticationEntryPoint("/login"),
                                new MediaTypeRequestMatcher(MediaType.TEXT_HTML)
                        )
                        .defaultAuthenticationEntryPointFor(
                                (request, response, authException) -> {
                                    response.setCharacterEncoding(StandardCharsets.UTF_8);
                                    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                                    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                                    response.getWriter().write(JacksonUtil.toJsonString(unauthorized));
                                },
                                new MediaTypeRequestMatcher(MediaType.APPLICATION_JSON)
                        )
                );

        return http.build();
    }

}
