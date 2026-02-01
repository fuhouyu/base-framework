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
package com.fuhouyu.framework.security.core.provider.oidc;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.lang.Nullable;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Collections;

/**
 * <p>
 * oidcToken
 * </p>
 *
 * @author fuhouyu
 * @since 2024/11/4 20:27
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = true)
public class OidcAuthenticationToken extends AbstractAuthenticationToken {

    private final String code;

    private final String state;

    private final String clientId;

    private transient OAuth2User principal;

    private OAuth2AccessToken accessToken;

    private OAuth2RefreshToken refreshToken;

    private String nonce;

    public OidcAuthenticationToken(String code, String state,
                                   String clientId,
                                   OAuth2User principal,
                                   Collection<? extends GrantedAuthority> authorities,
                                   OAuth2AccessToken accessToken,
                                   @Nullable OAuth2RefreshToken refreshToken) {
        super(authorities);
        this.code = code;
        this.state = state;
        this.clientId = clientId;
        this.principal = principal;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.setAuthenticated(true);
    }


    public OidcAuthenticationToken(String code, String state, String clientId) {
        super(Collections.emptyList());
        this.code = code;
        this.state = state;
        this.clientId = clientId;
        this.setAuthenticated(false);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public OAuth2User getPrincipal() {
        return this.principal;
    }
}
