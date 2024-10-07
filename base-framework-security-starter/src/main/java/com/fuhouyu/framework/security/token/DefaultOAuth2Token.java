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

package com.fuhouyu.framework.security.token;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;

import java.time.Instant;
import java.util.Set;

/**
 * <p>
 * token
 * </p>
 *
 * @author fuhouyu
 * @since 2024/8/14 21:03
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = true)
public class DefaultOAuth2Token extends OAuth2AccessToken {

    /**
     * token 过期秒数
     */
    private final int tokenExpireSeconds;

    /**
     * 刷新令牌
     */
    private OAuth2RefreshToken auth2RefreshToken;


    /**
     * 构造函数
     *
     * @param tokenType          token类型
     * @param tokenValue         token值
     * @param issuedAt           token签发时间
     * @param tokenExpireSeconds token过期时间
     */
    public DefaultOAuth2Token(TokenType tokenType, String tokenValue, Instant issuedAt,
                              int tokenExpireSeconds) {
        super(tokenType, tokenValue, issuedAt, issuedAt.plusSeconds(tokenExpireSeconds));
        this.tokenExpireSeconds = tokenExpireSeconds;
    }

    /**
     * 构造函数
     * @param tokenType token类型
     * @param tokenValue token值
     * @param issuedAt token签发时间
     * @param tokenExpireSeconds token过期时间
     * @param scopes token授权范围
     */
    public DefaultOAuth2Token(TokenType tokenType, String tokenValue, Instant issuedAt,
                              int tokenExpireSeconds, Set<String> scopes) {
        super(tokenType, tokenValue, issuedAt, issuedAt.plusSeconds(tokenExpireSeconds), scopes);
        this.tokenExpireSeconds = tokenExpireSeconds;
    }

}
