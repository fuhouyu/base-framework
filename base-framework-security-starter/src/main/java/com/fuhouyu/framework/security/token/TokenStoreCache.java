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

import com.fuhouyu.framework.cache.service.CacheService;
import com.fuhouyu.framework.utils.LoggerUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.utils.Base64;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.keygen.BytesKeyGenerator;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * token 缓存存储
 * </p>
 *
 * @author fuhouyu
 * @since 2024/8/14 21:05
 */
@Slf4j
public class TokenStoreCache implements TokenStore {

    private static final String SEPARATE = ":";

    private static final String AUTH = "auth" + SEPARATE;

    private static final String AUTH_TO_ACCESS = "auth_to_access" + SEPARATE;

    private static final String ACCESS = "access" + SEPARATE;

    private static final String REFRESH_AUTH = "refresh_auth" + SEPARATE;

    private static final String ACCESS_TO_REFRESH = "access_to_refresh" + SEPARATE;

    private static final String REFRESH = "refresh" + SEPARATE;

    private static final String REFRESH_TO_ACCESS = "refresh_to_access" + SEPARATE;

    private static final BytesKeyGenerator DEFAULT_TOKEN_GENERATOR = KeyGenerators.secureRandom(20);

    private final AuthenticationKeyGenerator authenticationKeyGenerator
            = new DefaultAuthenticationKeyGenerator();

    private final String prefix;

    private final ZoneId zoneId;

    private final TokenStoreSerializationStrategy serializationStrategy
            = new KryoTokenSerializer();

    private final CacheService<String, Object> cacheService;

    /**
     * 构造函数
     *
     * @param prefix       token前缀
     * @param cacheService 缓存接口
     */
    public TokenStoreCache(String prefix, CacheService<String, Object> cacheService) {
        this(prefix, ZoneId.of("Asia/Shanghai"), cacheService);
    }

    /**
     * 构造函数
     * @param zoneId 时区
     * @param cacheService 缓存接口
     */
    public TokenStoreCache(ZoneId zoneId, CacheService<String, Object> cacheService) {
        this("", zoneId, cacheService);
    }

    /**
     * 构造函数
     * @param cacheService 缓存接口
     */
    public TokenStoreCache(CacheService<String, Object> cacheService) {
        this("", ZoneId.of("Asia/Shanghai"), cacheService);
    }

    /**
     * 构造函数
     * @param prefix token前缀
     * @param zoneId 时区
     * @param cacheService 缓存接口
     */
    public TokenStoreCache(String prefix, ZoneId zoneId, CacheService<String, Object> cacheService) {
        this.cacheService = cacheService;
        this.prefix = prefix;
        this.zoneId = zoneId;
    }

    @Override
    public DefaultOAuth2Token createAccessToken(Authentication authentication, Integer accessTokenExpireSeconds) {
        String tokenValue = Base64.encodeBase64String(DEFAULT_TOKEN_GENERATOR.generateKey());
        Instant now = Instant.now().atZone(zoneId).toInstant();
        return new DefaultOAuth2Token(
                OAuth2AccessToken.TokenType.BEARER,
                tokenValue,
                now,
                accessTokenExpireSeconds);
    }

    @Override
    public DefaultOAuth2Token createToken(Authentication authentication,
                                          Integer accessTokenExpireSeconds,
                                          Integer refreshTokenExpireSeconds) {
        DefaultOAuth2Token existingAccessToken = this.getAccessToken(
                authentication);
        Instant now = Instant.now().atZone(zoneId).toInstant();
        // 如果存在则验证这个token是否过期，过期则进去删除。
        if (Objects.nonNull(existingAccessToken)) {
            OAuth2RefreshToken auth2RefreshToken = existingAccessToken.getAuth2RefreshToken();
            if (now.isAfter(Objects.requireNonNull(existingAccessToken.getExpiresAt()))) {
                if (auth2RefreshToken != null) {
                    // 当accessToken不存在时，则删除该值关联的refreshToken
                    this.removeRefreshToken(auth2RefreshToken);
                }
                this.removeAccessToken(existingAccessToken);
            }
            // 如果refresh Token为null，则生成一个
            if (auth2RefreshToken == null) {
                OAuth2RefreshToken refreshToken = this.createRefreshToken(refreshTokenExpireSeconds);
                this.storeRefreshToken(refreshToken, authentication);
                existingAccessToken.setAuth2RefreshToken(refreshToken);
                this.storeAccessToken(existingAccessToken, authentication);
            }
            return existingAccessToken;
        }
        // accessToken 和refresh Token都不存在时，生成
        DefaultOAuth2Token accessToken = this.createAccessToken(
                authentication, accessTokenExpireSeconds);
        OAuth2RefreshToken refreshToken = this.createRefreshToken(refreshTokenExpireSeconds);
        this.storeRefreshToken(refreshToken, authentication);
        accessToken.setAuth2RefreshToken(refreshToken);
        this.storeAccessToken(accessToken, authentication);
        return accessToken;
    }

    @Override
    public OAuth2RefreshToken createRefreshToken(Integer refreshTokenExpireSeconds) {
        String tokenValue = Base64.encodeBase64String(DEFAULT_TOKEN_GENERATOR.generateKey());
        Instant now = Instant.now().atZone(zoneId).toInstant();
        return new OAuth2RefreshToken(tokenValue, now,
                now.plusSeconds(refreshTokenExpireSeconds));
    }

    @Override
    public Authentication readAuthentication(OAuth2AccessToken accessToken) {
        return readAuthentication(accessToken.getTokenValue());
    }

    @Override
    public Authentication readAuthentication(String token) {
        byte[] bytes = cacheService.get(serializeKey(AUTH + token));
        return serializationStrategy.deserialize(bytes);
    }

    @Override
    public void storeAccessToken(OAuth2AccessToken token, Authentication authentication) {
        byte[] serializedAccessToken = this.serialize(token);
        byte[] serializedAuth = serialize(authentication);
        // 存储认证令牌
        long accessTokenExpireTime = this.expireTimeSeconds(token.getExpiresAt());
        cacheService.set(this.serializeKey(ACCESS + token.getTokenValue()),
                serializedAccessToken,
                accessTokenExpireTime, TimeUnit.SECONDS);
        cacheService.set(this.serializeKey(AUTH + token.getTokenValue()),
                serializedAuth, accessTokenExpireTime, TimeUnit.SECONDS);
        cacheService.set(this
                        .serializeKey(AUTH_TO_ACCESS + authenticationKeyGenerator.extractKey(authentication)),
                serializedAccessToken, accessTokenExpireTime, TimeUnit.SECONDS);

        if (token instanceof DefaultOAuth2Token accessToken) {
            OAuth2RefreshToken auth2RefreshToken = accessToken.getAuth2RefreshToken();
            if (Objects.nonNull(auth2RefreshToken) && Objects.nonNull(
                    auth2RefreshToken.getTokenValue())) {
                this.storeRefreshToken(auth2RefreshToken, authentication);
            }
        }
    }


    @Override
    public OAuth2AccessToken readAccessToken(String tokenValue) {
        return serializationStrategy.deserialize(cacheService.get(this.serializeKey(ACCESS + tokenValue)));
    }

    @Override
    public void removeAllToken(OAuth2AccessToken accessToken) {
        if (Objects.isNull(accessToken)) {
            return;
        }
        this.removeAccessToken(accessToken);
        if (accessToken instanceof DefaultOAuth2Token defaultOAuth2Token) {
            this.removeRefreshToken(defaultOAuth2Token.getAuth2RefreshToken());
        }

    }

    @Override
    public void removeAllToken(String accessToken) {
        this.removeAllToken(this.readAccessToken(accessToken));
    }

    @Override
    public void removeAccessToken(OAuth2AccessToken token) {
        this.removeAccessToken(token.getTokenValue());
    }

    @Override
    public void removeAccessToken(String tokenValue) {
        Authentication authentication = this.readAuthentication(tokenValue);
        if (Objects.nonNull(authentication)) {
            cacheService.delete(this.serializeKey(AUTH_TO_ACCESS + authenticationKeyGenerator.extractKey(authentication)));
        }
        cacheService.delete(this.serializeKey(AUTH + tokenValue));
        cacheService.delete(this.serializeKey(ACCESS + tokenValue));
    }

    @Override
    public void storeRefreshToken(OAuth2RefreshToken refreshToken, Authentication authentication) {
        byte[] refreshKey = this.serializeKey(REFRESH + refreshToken.getTokenValue());
        byte[] refreshAuthKey = this.serializeKey(REFRESH_AUTH + refreshToken.getTokenValue());

        long expire = this.expireTimeSeconds(refreshToken.getExpiresAt());
        cacheService.set(refreshKey, serialize(refreshToken), expire, TimeUnit.SECONDS);
        cacheService.set(refreshAuthKey, serialize(authentication), expire, TimeUnit.SECONDS);

    }

    @Override
    public OAuth2RefreshToken readRefreshToken(String refreshToken) {
        byte[] key = this.serializeKey(REFRESH + refreshToken);
        return serializationStrategy.deserialize(cacheService.get(key));
    }

    @Override
    public void removeRefreshToken(OAuth2RefreshToken token) {
        this.removeRefreshToken(token.getTokenValue());
    }

    @Override
    public void removeRefreshToken(String tokenValue) {
        byte[] refreshKey = this.serializeKey(REFRESH + tokenValue);
        byte[] refreshAuthKey = this.serializeKey(REFRESH_AUTH + tokenValue);
        byte[] refresh2AccessKey = this.serializeKey(REFRESH_TO_ACCESS + tokenValue);
        cacheService.delete(refreshKey);
        cacheService.delete(refreshAuthKey);
        cacheService.delete(refresh2AccessKey);

        byte[] accessTokenBytes = cacheService.get(refreshKey);
        if (Objects.isNull(accessTokenBytes)) {
            return;
        }
        String accessTokenValue = serializationStrategy.deserializeString(accessTokenBytes);
        byte[] access2RefreshKey = this.serializeKey(ACCESS_TO_REFRESH + accessTokenValue);
        cacheService.delete(access2RefreshKey);
    }

    @Override
    public void removeAccessTokenUsingRefreshToken(OAuth2RefreshToken refreshToken) {
        this.removeAccessTokenUsingRefreshToken(refreshToken.getTokenValue());
    }

    @Override
    public void removeAccessTokenUsingRefreshToken(String refreshToken) {
        byte[] key = this.serializeKey(REFRESH_TO_ACCESS + refreshToken);
        byte[] bytes = cacheService.get(key);
        if (Objects.isNull(bytes)) {
            return;
        }
        this.removeAccessToken(serializationStrategy.deserializeString(bytes));
    }

    @Override
    public Authentication readAuthenticationForRefreshToken(OAuth2RefreshToken token) {
        if (Objects.isNull(token)) {
            LoggerUtil.warn(log, "刷新令牌为空，无法读取authentication");
            return null;
        }
        return this.readAuthenticationForRefreshToken(token.getTokenValue());
    }

    @Override
    public Authentication readAuthenticationForRefreshToken(String refreshToken) {
        byte[] bytes = cacheService.get(this.serializeKey(REFRESH_AUTH + refreshToken));
        return serializationStrategy.deserialize(bytes);
    }

    @Override
    public DefaultOAuth2Token getAccessToken(Authentication authentication) {
        String key = authenticationKeyGenerator.extractKey(authentication);
        byte[] serializedKey = this.serializeKey(AUTH_TO_ACCESS + key);
        byte[] bytes = cacheService.get(serializedKey);

        DefaultOAuth2Token accessToken = serializationStrategy.deserialize(bytes);
        if (accessToken != null) {
            Authentication storedAuthentication = readAuthentication(accessToken.getTokenValue());
            if ((storedAuthentication == null || !key.equals(
                    authenticationKeyGenerator.extractKey(storedAuthentication)))) {
                storeAccessToken(accessToken, authentication);
            }
        }
        return accessToken;
    }

    /**
     * 序列化对象.
     *
     * @param object 对象
     * @return 序列化字节数组
     */
    private byte[] serialize(Object object) {
        return serializationStrategy.serialize(object);
    }


    /**
     * 序列化key.
     *
     * @param key key
     * @return key
     */
    private byte[] serializeKey(String key) {
        return (prefix + key).getBytes(StandardCharsets.UTF_8);
    }

    /**
     * 从缓存中读取字节数组
     *
     * @param key key
     * @return 字节数组
     */
    private byte[] getCacheBytes(String key) {
        String result = (String) cacheService.get(key);
        if (Objects.isNull(result)) {
            return null;
        }
        return Base64.decodeBase64(result);
    }

    /**
     * 返回过期时间
     *
     * @param expiresAt 过期时间
     * @return 过期时间
     */
    private long expireTimeSeconds(Instant expiresAt) {
        if (Objects.isNull(expiresAt)) {
            // 如果过期时间不存在，缓存将设置为永乐
            return -1;
        }
        long epochSecond = expiresAt.getEpochSecond();
        return epochSecond - Instant.now().atZone(zoneId).toInstant().getEpochSecond();
    }
}
