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
import com.fuhouyu.framework.common.utils.LoggerUtil;
import com.fuhouyu.framework.security.serializer.KryoSerializer;
import com.fuhouyu.framework.security.serializer.SerializationStrategy;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.utils.Base64;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.keygen.BytesKeyGenerator;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
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

    /**
     * 默认的token 过期时间
     */
    private static final long TOKEN_EXPIRE_SECONDS = Duration.ofHours(1).getSeconds();

    /**
     * 默认的刷新令牌过期时间
     */
    private static final long REFRESH_TOKEN_EXPIRE_SECONDS = Duration.ofDays(1).getSeconds();

    private final AuthenticationKeyGenerator authenticationKeyGenerator
            = new DefaultAuthenticationKeyGenerator();

    private final String prefix;

    private final ZoneId zoneId;

    private final SerializationStrategy serializationStrategy
            = new KryoSerializer();

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
     *
     * @param zoneId       时区
     * @param cacheService 缓存接口
     */
    public TokenStoreCache(ZoneId zoneId, CacheService<String, Object> cacheService) {
        this("", zoneId, cacheService);
    }

    /**
     * 构造函数
     *
     * @param cacheService 缓存接口
     */
    public TokenStoreCache(CacheService<String, Object> cacheService) {
        this("", ZoneId.of("Asia/Shanghai"), cacheService);
    }

    /**
     * 构造函数
     *
     * @param prefix       token前缀
     * @param zoneId       时区
     * @param cacheService 缓存接口
     */
    public TokenStoreCache(String prefix, ZoneId zoneId, CacheService<String, Object> cacheService) {
        this.cacheService = cacheService;
        this.prefix = prefix;
        this.zoneId = zoneId;
    }

    @Override
    public OAuth2AccessToken createAccessToken(Authentication authentication, long accessTokenExpireSeconds) {
        String tokenValue = Base64.encodeBase64String(DEFAULT_TOKEN_GENERATOR.generateKey());
        Instant now = Instant.now().atZone(zoneId).toInstant();
        return new OAuth2AccessToken(
                OAuth2AccessToken.TokenType.BEARER,
                tokenValue,
                now,
                now.plusSeconds(accessTokenExpireSeconds));
    }

    @Override
    public OAuth2Token createToken(Authentication authentication) {
        return this.createToken(authentication, TOKEN_EXPIRE_SECONDS, REFRESH_TOKEN_EXPIRE_SECONDS);
    }

    @Override
    public OAuth2Token createToken(Authentication authentication,
                                   long accessTokenExpireSeconds,
                                   long refreshTokenExpireSeconds) {
        OAuth2Token auth2Token = this.getTokenEntity(
                authentication);
        Instant now = Instant.now().atZone(zoneId).toInstant();
        // 如果存在则验证这个token是否过期，过期则进去删除。
        if (Objects.nonNull(auth2Token)) {
            OAuth2AccessToken existingAccessToken = auth2Token.getAccessToken();
            OAuth2RefreshToken auth2RefreshToken = auth2Token.getRefreshToken();
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
                auth2Token.setRefreshToken(refreshToken);
                this.storeAuth2Token(auth2Token, authentication);
            }
            return auth2Token;
        }
        // accessToken 和refresh Token都不存在时，生成
        OAuth2AccessToken accessToken = this.createAccessToken(
                authentication, accessTokenExpireSeconds);
        OAuth2RefreshToken refreshToken = this.createRefreshToken(refreshTokenExpireSeconds);
        this.storeRefreshToken(refreshToken, authentication);
        auth2Token = new OAuth2Token(accessToken, refreshToken);
        this.storeAuth2Token(auth2Token, authentication);
        return auth2Token;
    }

    @Override
    public OAuth2RefreshToken createRefreshToken(long refreshTokenExpireSeconds) {
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
    public void storeAuth2Token(OAuth2Token auth2Token, Authentication authentication) {

        byte[] tokenEntityBytes = this.serialize(auth2Token);
        byte[] serializedAuth = serialize(authentication);

        OAuth2AccessToken accessToken = auth2Token.getAccessToken();
        long accessTokenExpireTime = this.expireTimeSeconds(accessToken.getExpiresAt());
        cacheService.set(this.serializeKey(ACCESS + accessToken.getTokenValue()),
                tokenEntityBytes,
                accessTokenExpireTime, TimeUnit.SECONDS);
        cacheService.set(this.serializeKey(AUTH + accessToken.getTokenValue()),
                serializedAuth, accessTokenExpireTime, TimeUnit.SECONDS);
        cacheService.set(this
                        .serializeKey(AUTH_TO_ACCESS + authenticationKeyGenerator.extractKey(authentication)),
                tokenEntityBytes, accessTokenExpireTime, TimeUnit.SECONDS);
        OAuth2RefreshToken refreshToken = auth2Token.getRefreshToken();
        if (Objects.nonNull(refreshToken) && Objects.nonNull(
                refreshToken.getTokenValue())) {
            // 刷新令牌指向accessToken
            this.cacheService.set(this.serializeKey(REFRESH_TO_ACCESS + refreshToken.getTokenValue()),
                    this.serialize(accessToken.getTokenValue()), accessTokenExpireTime, TimeUnit.SECONDS);
            this.storeRefreshToken(refreshToken, authentication);
        }
    }


    @Override
    public OAuth2Token readAuth2Token(String tokenValue) {
        return serializationStrategy.deserialize(cacheService.get(this.serializeKey(ACCESS + tokenValue)));
    }

    @Override
    public void removeAuth2Token(@NonNull OAuth2Token auth2Token) {
        this.removeAccessToken(auth2Token.getAccessToken());
        this.removeRefreshToken(auth2Token.getRefreshToken());

    }

    @Override
    public void removeAuth2Token(String accessToken) {
        this.removeAuth2Token(this.readAuth2Token(accessToken));
    }

    @Override
    public void removeAccessToken(OAuth2AccessToken token) {
        if (Objects.isNull(token)) {
            return;
        }
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
        if (Objects.isNull(token)) {
            return;
        }
        this.removeRefreshToken(token.getTokenValue());
    }

    @Override
    public void removeRefreshToken(String tokenValue) {
        byte[] refreshKey = this.serializeKey(REFRESH + tokenValue);
        byte[] refreshAuthKey = this.serializeKey(REFRESH_AUTH + tokenValue);
        byte[] refresh2AccessKey = this.serializeKey(REFRESH_TO_ACCESS + tokenValue);
        cacheService.delete(refreshKey);
        cacheService.delete(refreshAuthKey);

        byte[] accessTokenBytes = cacheService.get(refresh2AccessKey);
        if (Objects.isNull(accessTokenBytes)) {
            return;
        }
        String accessTokenValue = serializationStrategy.deserializeString(accessTokenBytes);
        byte[] access2RefreshKey = this.serializeKey(ACCESS_TO_REFRESH + accessTokenValue);
        cacheService.delete(refresh2AccessKey);
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
    public OAuth2Token getTokenEntity(Authentication authentication) {
        String key = authenticationKeyGenerator.extractKey(authentication);
        byte[] serializedKey = this.serializeKey(AUTH_TO_ACCESS + key);
        byte[] bytes = cacheService.get(serializedKey);

        OAuth2Token auth2Token = serializationStrategy.deserialize(bytes);
        if (Objects.isNull(auth2Token)) {
            return null;
        }
        OAuth2AccessToken accessToken = auth2Token.getAccessToken();
        Authentication storedAuthentication = readAuthentication(accessToken.getTokenValue());
        // 如果认证信息已过期，重新设置
        if ((storedAuthentication == null || !key.equals(
                authenticationKeyGenerator.extractKey(storedAuthentication)))) {
            storeAuth2Token(auth2Token, authentication);
        }
        return auth2Token;
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
