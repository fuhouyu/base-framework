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
package com.fuhouyu.framework.security.core.authentication.refreshtoken;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fuhouyu.framework.security.token.TokenStore;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 刷新令牌provider
 * </p>
 *
 * @author fuhouyu
 * @since 2024/10/22 22:17
 */
@RequiredArgsConstructor
public class RefreshAuthenticationProvider implements AuthenticationProvider {

    private final TokenStore tokenStore;


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String refreshToken = (String) authentication.getPrincipal();
        if (!StringUtils.hasText(refreshToken)) {
            throw new IllegalArgumentException("刷新令牌为空");
        }
        Authentication storeTokenAuthentication = tokenStore.readAuthenticationForRefreshToken(
                refreshToken);
        if (Objects.isNull(storeTokenAuthentication)) {
            throw new IllegalArgumentException("刷牌令牌已过期");
        }
        tokenStore.removeAccessTokenUsingRefreshToken(refreshToken);
        tokenStore.removeRefreshToken(refreshToken);
        return storeTokenAuthentication;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return RefreshAuthenticationToken.class.isAssignableFrom(authentication);
    }

    @EqualsAndHashCode(callSuper = true)
    public static class RefreshAuthenticationToken extends AbstractAuthenticationToken {

        private final String refreshToken;

        /**
         * 构造函数
         *
         * @param refreshToken 刷新令牌
         */
        @JsonCreator
        public RefreshAuthenticationToken(
                @JsonProperty("refreshToken") String refreshToken) {
            super(List.of());
            this.refreshToken = refreshToken;
        }

        @Override
        public Object getCredentials() {
            return null;
        }

        @Override
        public Object getPrincipal() {
            return this.refreshToken;
        }
    }
}
