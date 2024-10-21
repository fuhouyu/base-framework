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
package com.fuhouyu.framework.security.entity;

import com.fuhouyu.framework.Entity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;

import java.io.Serial;

/**
 * <p>
 * token实体
 * </p>
 *
 * @author fuhouyu
 * @since 2024/10/21 20:39
 */
@Getter
@Setter
@ToString
public class TokenEntity implements Entity<TokenEntity> {

    @Serial
    private static final long serialVersionUID = 1897123986123785612L;

    private final OAuth2AccessToken accessToken;

    private OAuth2RefreshToken refreshToken;

    public TokenEntity(OAuth2AccessToken accessToken) {
        this(accessToken, null);
    }

    public TokenEntity(OAuth2AccessToken accessToken, OAuth2RefreshToken refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    @Override
    public boolean sameIdentityAs(TokenEntity other) {
        return EqualsBuilder.reflectionEquals(this, other);
    }
}
