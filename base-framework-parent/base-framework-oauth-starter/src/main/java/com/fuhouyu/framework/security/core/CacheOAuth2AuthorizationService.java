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

package com.fuhouyu.framework.security.core;


import com.fuhouyu.framework.cache.service.CacheService;
import com.fuhouyu.framework.common.enums.ResponseStatusEnum;
import com.fuhouyu.framework.common.exception.ServiceException;
import com.fuhouyu.framework.common.utils.LoggerUtil;
import com.fuhouyu.framework.security.constants.TokenPrefixConstant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.Nullable;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationCode;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;

import java.io.*;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 *
 * </p>
 *
 * @author fuhouyu
 * @since 2026/2/6 15:00
 */
@RequiredArgsConstructor
@Slf4j
public class CacheOAuth2AuthorizationService implements OAuth2AuthorizationService {

    private static final long TIMEOUT = 3600;
    private final CacheService<String, Object> cacheService;

    @Override
    public void save(OAuth2Authorization authorization) {
        this.cacheService.set(TokenPrefixConstant.ID_PREFIX + authorization.getId(), serialize(authorization), TIMEOUT, TimeUnit.SECONDS);
        if (authorization.getAccessToken() != null) {
            this.cacheService.set(TokenPrefixConstant.ACCESS_TOKEN_PREFIX + authorization.getAccessToken().getToken().getTokenValue(), authorization.getId(),
                    this.expireTokenSeconds(authorization.getAccessToken().getToken().getExpiresAt()), TimeUnit.SECONDS);
        }
        if (authorization.getRefreshToken() != null) {
            this.cacheService.set(TokenPrefixConstant.REFRESH_TOKEN_PREFIX + authorization.getRefreshToken().getToken().getTokenValue(),
                    authorization.getId(), this.expireTokenSeconds(authorization.getRefreshToken().getToken().getExpiresAt()), TimeUnit.SECONDS);
        }
        OAuth2Authorization.Token<OAuth2AuthorizationCode> authorizationCodeToken = authorization.getToken(OAuth2AuthorizationCode.class);
        if (Objects.nonNull(authorizationCodeToken)) {
            this.cacheService.set(TokenPrefixConstant.CODE_PREFIX + authorizationCodeToken.getToken().getTokenValue(),
                    authorization.getId(), this.expireTokenSeconds(authorizationCodeToken.getToken().getExpiresAt()), TimeUnit.SECONDS);
        }
    }

    @Override
    public void remove(OAuth2Authorization authorization) {
        this.cacheService.delete(TokenPrefixConstant.ID_PREFIX + authorization.getId());
        OAuth2Authorization.Token<OAuth2AccessToken> accessToken = authorization.getAccessToken();
        if (Objects.nonNull(accessToken)) {
            LoggerUtil.debug(log, "删除令牌:[{}]", accessToken.getToken().getTokenValue());
            this.cacheService.delete(TokenPrefixConstant.ACCESS_TOKEN_PREFIX + accessToken.getToken().getTokenValue());
        }

        OAuth2Authorization.Token<OAuth2RefreshToken> refreshToken = authorization.getRefreshToken();
        if (Objects.nonNull(refreshToken)) {
            LoggerUtil.debug(log, "删除刷新令牌:[{}]", refreshToken.getToken().getTokenValue());
            this.cacheService.delete(TokenPrefixConstant.REFRESH_TOKEN_PREFIX + refreshToken.getToken().getTokenValue());
        }
        OAuth2Authorization.Token<OAuth2AuthorizationCode> codeToken = authorization.getToken(OAuth2AuthorizationCode.class);
        if (Objects.nonNull(codeToken)) {
            LoggerUtil.debug(log, "删除临时令牌:[{}]", codeToken.getToken().getTokenValue());
            this.cacheService.delete(TokenPrefixConstant.CODE_PREFIX + codeToken.getToken().getTokenValue());
        }
    }

    @Override
    public @Nullable OAuth2Authorization findById(String id) {
        Object o = this.cacheService.get(TokenPrefixConstant.ID_PREFIX + id);
        OAuth2Authorization result = null;
        if (Objects.nonNull(o)) {
            result = deserialize(Base64.getDecoder().decode(o.toString()));
        }
        return result;
    }

    @Override
    public @Nullable OAuth2Authorization findByToken(String token, @Nullable OAuth2TokenType tokenType) {
        String tokenKey = TokenPrefixConstant.ACCESS_TOKEN_PREFIX + token;
        if (Objects.equals(tokenType, OAuth2TokenType.REFRESH_TOKEN)) {
            tokenKey = TokenPrefixConstant.REFRESH_TOKEN_PREFIX + token;
        }
        if (Objects.equals(Optional.ofNullable(tokenType).map(OAuth2TokenType::getValue).orElse(""), "code")) {
            tokenKey = TokenPrefixConstant.CODE_PREFIX + token;
        }
        Object id = this.cacheService.get(tokenKey);
        OAuth2Authorization authorization = null;
        if (Objects.nonNull(id)) {
            authorization = this.findById(id.toString());
        }
        return authorization;
    }

    /**
     * 根据过期时间获取秒数
     *
     * @param expireAt 过期时间
     * @return 秒数
     */
    private long expireTokenSeconds(Instant expireAt) {
        Instant now = Instant.now();
        return Duration.between(now, expireAt).getSeconds();
    }

    /**
     * 序列化
     *
     * @param obj 对象
     * @return 序列化后的字节数组
     */
    private byte[] serialize(Object obj) {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ObjectOutputStream out = new ObjectOutputStream(bos)) {
            out.writeObject(obj);
            return bos.toByteArray();
        } catch (IOException e) {
            LoggerUtil.error(log, "对象:{}序列化失败,{}", obj, e.getMessage(), e);
            throw new ServiceException(ResponseStatusEnum.SERVER_ERROR);
        }
    }

    /**
     * 反序列化对象
     *
     * @param <T>  类型
     * @param data data
     * @return 反序列化后的对象
     */
    @SuppressWarnings("unchecked")
    private <T> T deserialize(byte[] data) {
        try (ByteArrayInputStream bis = new ByteArrayInputStream(data);
             ObjectInputStream in = new ObjectInputStream(bis)) {
            Object obj = in.readObject();
            if (!(obj instanceof OAuth2Authorization)) {
                throw new IllegalStateException("Deserialized object is not of type " + OAuth2Authorization.class.getName());
            }
            return (T) obj;
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}