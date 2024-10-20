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

import com.fuhouyu.framework.security.entity.TokenEntity;
import lombok.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;

/**
 * <p>
 * token存储接口
 * </p>
 *
 * @author fuhouyu
 * @since 2024/8/14 21:02
 */
public interface TokenStore {

    /**
     * 创建认证令牌和刷新令牌，返回token实体.
     *
     * @param authentication            认证信息
     * @param accessTokenExpireSeconds  认证令牌过期时间(s)
     * @param refreshTokenExpireSeconds 刷新令牌过期时间(s)
     * @return 返回token实体类
     */
    TokenEntity createToken(Authentication authentication,
                            Integer accessTokenExpireSeconds,
                            Integer refreshTokenExpireSeconds);

    /**
     * 创建认证令牌.
     *
     * @param authentication           认证信息
     * @param accessTokenExpireSeconds 认证令牌秒数
     * @return 认证token
     */
    OAuth2AccessToken createAccessToken(Authentication authentication,
                                         Integer accessTokenExpireSeconds);


    /**
     * 创建刷新令牌.
     *
     * @param refreshTokenExpireSeconds 刷新令牌的过期时间
     * @return 刷新令牌
     */
    OAuth2RefreshToken createRefreshToken(Integer refreshTokenExpireSeconds);

    /**
     * 读取token中存储的认证信息.
     *
     * @param accessToken 认证令牌
     * @return 认证信息
     */
    Authentication readAuthentication(OAuth2AccessToken accessToken);


    /**
     * 读取token中存储的认证信息.
     *
     * @param accessToken 认证令牌
     * @return 认证信息
     */
    Authentication readAuthentication(String accessToken);


    /**
     * 存储token实体
     *
     * @param tokenEntity token实体
     * @param authentication 认证信息
     */
    void storeTokenEntity(TokenEntity tokenEntity, Authentication authentication);


    /**
     * 通过accessToken读取token实体.
     *
     * @param tokenValue 认证token
     * @return token实体
     */
    TokenEntity readTokenEntity(String tokenValue);


    /**
     * 删除token.
     *
     * @param token 认证令牌
     */
    void removeAccessToken(OAuth2AccessToken token);

    /**
     * 通过token实体删除所有存储的实体及token
     *
     * @param tokenEntity token实体
     */
    void removeTokenEntity(@NonNull TokenEntity tokenEntity);

    /**
     * 删除所有的token
     *
     * @param accessToken 认证令牌
     */
    void removeTokenEntity(String accessToken);

    /**
     * 删除token.
     *
     * @param token 认证令牌
     */
    void removeAccessToken(String token);


    /**
     * 存储刷新令牌.
     *
     * @param refreshToken   刷新令牌
     * @param authentication 认证信息
     */
    void storeRefreshToken(OAuth2RefreshToken refreshToken, Authentication authentication);


    /**
     * 读取刷新令牌.
     *
     * @param refreshToken 刷新token
     * @return 刷新令牌
     */
    OAuth2RefreshToken readRefreshToken(String refreshToken);


    /**
     * 删除刷新令牌.
     *
     * @param token 刷新令牌
     */
    void removeRefreshToken(OAuth2RefreshToken token);

    /**
     * 删除刷新令牌.
     *
     * @param token 刷新令牌
     */
    void removeRefreshToken(String token);

    /**
     * 使用刷新令牌删除访问令牌.
     *
     * @param refreshToken 刷新令牌
     */
    void removeAccessTokenUsingRefreshToken(OAuth2RefreshToken refreshToken);

    /**
     * 使用刷新令牌删除访问令牌.
     *
     * @param refreshToken 刷新令牌
     */
    void removeAccessTokenUsingRefreshToken(String refreshToken);

    /**
     * 通过刷新令牌读取认证信息.
     *
     * @param token 刷新令牌
     * @return 认证信息
     */
    Authentication readAuthenticationForRefreshToken(OAuth2RefreshToken token);

    /**
     * 通过刷新令牌读取认证信息.
     *
     * @param refreshToken 刷新令牌
     * @return 认证信息
     */
    Authentication readAuthenticationForRefreshToken(String refreshToken);

    /**
     * 通过认证信息，获取token实体.
     *
     * @param authentication 认证信息
     * @return token实体
     */
    TokenEntity getTokenEntity(Authentication authentication);
}
