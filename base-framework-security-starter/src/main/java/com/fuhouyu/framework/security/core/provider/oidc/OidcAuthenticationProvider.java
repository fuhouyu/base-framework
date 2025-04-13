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

import com.fuhouyu.framework.common.utils.LoggerUtil;
import com.fuhouyu.framework.security.core.ExtensionUserDetailsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.client.http.OAuth2ErrorResponseErrorHandler;
import org.springframework.security.oauth2.client.oidc.authentication.OidcIdTokenDecoderFactory;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.*;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.core.http.converter.OAuth2AccessTokenResponseHttpMessageConverter;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.endpoint.OidcParameterNames;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoderFactory;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.util.Assert;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

import static org.springframework.http.MediaType.APPLICATION_JSON;

/**
 * <p>
 * oidc 认证token
 * </p>
 *
 * @author fuhouyu
 * @since 2024/11/4 20:08
 */
@Slf4j
public class OidcAuthenticationProvider implements AuthenticationProvider {

    public static final String ACCOUNT_TYPE = "OIDC";

    private static final String INVALID_TOKEN_RESPONSE_ERROR_CODE = "invalid_token_response";

    private static final String INVALID_ID_TOKEN_ERROR_CODE = "invalid_id_token";

    private static final String INVALID_NONCE_ERROR_CODE = "invalid_nonce";

    private static final MediaType APPLICATION_FORM_URLENCODED_UTF8 = new MediaType(
            MediaType.APPLICATION_FORM_URLENCODED, StandardCharsets.UTF_8);

    private final GrantedAuthoritiesMapper authoritiesMapper = authorities -> authorities;

    private final ExtensionUserDetailsService userDetailsService;

    private final OAuth2UserService<OAuth2UserRequest, OAuth2User> userService;

    private final ClientRegistrationRepository clientRegistrationRepository;

    private final JwtDecoderFactory<ClientRegistration> jwtDecoderFactory = new OidcIdTokenDecoderFactory();

    private final RestOperations restOperations;

    public OidcAuthenticationProvider(OAuth2UserService<OAuth2UserRequest, OAuth2User> userService,
                                      ExtensionUserDetailsService userDetailsService,
                                      ClientRegistrationRepository clientRegistrationRepository) {
        this.userService = userService;
        this.userDetailsService = userDetailsService;
        this.clientRegistrationRepository = clientRegistrationRepository;
        RestTemplate restTemplate = new RestTemplate(
                Arrays.asList(new FormHttpMessageConverter(), new OAuth2AccessTokenResponseHttpMessageConverter()));
        restTemplate.setErrorHandler(new OAuth2ErrorResponseErrorHandler());
        this.restOperations = restTemplate;
    }

    static String createHash(String nonce) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] digest = md.digest(nonce.getBytes(StandardCharsets.US_ASCII));
        return Base64.getUrlEncoder().withoutPadding().encodeToString(digest);
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        OidcAuthenticationToken oidcAuthenticationToken = (OidcAuthenticationToken) authentication;
        ClientRegistration clientRegistration = clientRegistrationRepository.findByRegistrationId(oidcAuthenticationToken.getClientId());
        if (Objects.isNull(clientRegistration)) {
            throw new IllegalArgumentException("invalid client id " + oidcAuthenticationToken.getClientId());
        }
        RequestEntity<?> requestEntity = this.createRequestEntity(clientRegistration, oidcAuthenticationToken);
        OAuth2AccessTokenResponse accessTokenResponse = this.getResponse(requestEntity);
        Map<String, Object> additionalParameters = this.getAdditionalParameters(accessTokenResponse, clientRegistration);
        OidcIdToken idToken = createOidcToken(clientRegistration, accessTokenResponse);
        validateNonce(oidcAuthenticationToken.getNonce(), idToken);
        OAuth2User oidcUser = this.userService.loadUser(new OidcUserRequest(clientRegistration,
                accessTokenResponse.getAccessToken(), idToken, additionalParameters));
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(oidcUser.getName(), ACCOUNT_TYPE);
        Collection<? extends GrantedAuthority> mappedAuthorities = this.authoritiesMapper
                .mapAuthorities(oidcUser.getAuthorities());
        OidcAuthenticationToken result = new OidcAuthenticationToken(
                oidcAuthenticationToken.getCode(),
                oidcAuthenticationToken.getState(),
                clientRegistration.getClientId(),
                oidcUser, mappedAuthorities, accessTokenResponse.getAccessToken(), accessTokenResponse.getRefreshToken());
        result.setDetails(userDetails);
        return result;
    }

    /**
     * 获取扩展信息
     *
     * @param accessTokenResponse token 响应
     * @param clientRegistration  客户端注册信息
     * @return parameters·
     */
    private Map<String, Object> getAdditionalParameters(OAuth2AccessTokenResponse accessTokenResponse, ClientRegistration clientRegistration) {
        Map<String, Object> additionalParameters = accessTokenResponse.getAdditionalParameters();
        if (!additionalParameters.containsKey(OidcParameterNames.ID_TOKEN)) {
            OAuth2Error invalidIdTokenError = new OAuth2Error(INVALID_ID_TOKEN_ERROR_CODE,
                    "Missing (required) ID Token in Token Response for Client Registration: "
                            + clientRegistration.getRegistrationId(),
                    null);
            throw new OAuth2AuthenticationException(invalidIdTokenError, invalidIdTokenError.toString());
        }
        return additionalParameters;
    }

    /**
     * 创建oidcToken
     *
     * @param clientRegistration  客户端注册信息
     * @param accessTokenResponse token响应
     * @return oidcToken
     */
    private OidcIdToken createOidcToken(ClientRegistration clientRegistration,
                                        OAuth2AccessTokenResponse accessTokenResponse) {
        JwtDecoder jwtDecoder = this.jwtDecoderFactory.createDecoder(clientRegistration);
        Jwt jwt = getJwt(accessTokenResponse, jwtDecoder);
        return new OidcIdToken(jwt.getTokenValue(), jwt.getIssuedAt(), jwt.getExpiresAt(),
                jwt.getClaims());
    }

    /**
     * 获取jwtToken
     *
     * @param accessTokenResponse token响应
     * @param jwtDecoder          jwt解析
     * @return jwt
     */
    private Jwt getJwt(OAuth2AccessTokenResponse accessTokenResponse, JwtDecoder jwtDecoder) {
        try {
            Map<String, Object> parameters = accessTokenResponse.getAdditionalParameters();
            return jwtDecoder.decode((String) parameters.get(OidcParameterNames.ID_TOKEN));
        } catch (JwtException ex) {
            OAuth2Error invalidIdTokenError = new OAuth2Error(INVALID_ID_TOKEN_ERROR_CODE, ex.getMessage(), null);
            throw new OAuth2AuthenticationException(invalidIdTokenError, invalidIdTokenError.toString(), ex);
        }
    }

    private void validateNonce(String requestNonce, OidcIdToken idToken) {
        if (requestNonce == null) {
            return;
        }
        String nonceHash = getNonceHash(requestNonce);
        String nonceHashClaim = idToken.getNonce();
        if (nonceHashClaim == null || !nonceHashClaim.equals(nonceHash)) {
            OAuth2Error oauth2Error = new OAuth2Error(INVALID_NONCE_ERROR_CODE);
            throw new OAuth2AuthenticationException(oauth2Error, oauth2Error.toString());
        }
    }

    private String getNonceHash(String requestNonce) {
        try {
            return createHash(requestNonce);
        } catch (NoSuchAlgorithmException ex) {
            OAuth2Error oauth2Error = new OAuth2Error(INVALID_NONCE_ERROR_CODE);
            throw new OAuth2AuthenticationException(oauth2Error, oauth2Error.toString());
        }
    }

    /**
     * 获取token
     *
     * @param request 请求实体
     * @return token
     */
    private OAuth2AccessTokenResponse getResponse(RequestEntity<?> request) {
        try {
            ResponseEntity<OAuth2AccessTokenResponse> tokenResponse = this.restOperations.exchange(request, OAuth2AccessTokenResponse.class);
            Assert.notNull(tokenResponse,
                    "The authorization server responded to this Authorization Code grant request with an empty body; as such, it cannot be materialized into an OAuth2AccessTokenResponse instance. Please check the HTTP response code in your server logs for more details.");
            OAuth2AccessTokenResponse responseBody = tokenResponse.getBody();
            Assert.notNull(responseBody, "Response Body is null");
            return responseBody;
        } catch (RestClientException ex) {
            OAuth2Error oauth2Error = new OAuth2Error(INVALID_TOKEN_RESPONSE_ERROR_CODE,
                    "An error occurred while attempting to retrieve the OAuth 2.0 Access Token Response: "
                            + ex.getMessage(),
                    null);
            throw new OAuth2AuthorizationException(oauth2Error, ex);
        } catch (OAuth2AuthorizationException e) {
            LoggerUtil.error(log, "第三方平台使用失败，entity:{} , 失败原因:{}",
                    request, e.getError());
            throw new IllegalArgumentException("第三方平台登录失败");
        }
    }

    /**
     * 创建请求实体
     *
     * @param clientRegistration 客户端注册信息
     * @param oidcAuthenticationToken oidc 认证token
     * @return 请求实体
     */
    private RequestEntity<?> createRequestEntity(ClientRegistration clientRegistration,
                                                 OidcAuthenticationToken oidcAuthenticationToken) {
        URI uri = UriComponentsBuilder
                .fromUriString(clientRegistration.getProviderDetails().getTokenUri())
                .build()
                .toUri();
        return new RequestEntity<>(this.createParameters(clientRegistration, oidcAuthenticationToken),
                this.createHttpHeaders(clientRegistration), HttpMethod.POST, uri);
    }

    /**
     * 获取请求头
     *
     * @param clientRegistration 客户端信息
     * @return http请求头
     */
    private HttpHeaders createHttpHeaders(ClientRegistration clientRegistration) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(APPLICATION_JSON));
        headers.setContentType(APPLICATION_FORM_URLENCODED_UTF8);

        if (ClientAuthenticationMethod.CLIENT_SECRET_BASIC.equals(clientRegistration.getClientAuthenticationMethod())) {
            String clientId = URLEncoder.encode(clientRegistration.getClientId(), StandardCharsets.UTF_8);
            String clientSecret = URLEncoder.encode(clientRegistration.getClientSecret(), StandardCharsets.UTF_8);
            headers.setBasicAuth(clientId, clientSecret);
        }
        return headers;
    }

    /**
     * 设置参数
     *
     * @param clientRegistration 客户端注册信息
     * @param oidcAuthenticationToken oidc token
     * @return 参数对象
     */
    private MultiValueMap<String, String> createParameters(ClientRegistration clientRegistration,
                                                           OidcAuthenticationToken oidcAuthenticationToken) {
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.set(OAuth2ParameterNames.GRANT_TYPE, AuthorizationGrantType.AUTHORIZATION_CODE.getValue());
        parameters.set(OAuth2ParameterNames.CODE, oidcAuthenticationToken.getCode());
        if (!ClientAuthenticationMethod.CLIENT_SECRET_BASIC
                .equals(clientRegistration.getClientAuthenticationMethod())) {
            parameters.set(OAuth2ParameterNames.CLIENT_ID, clientRegistration.getClientId());
        }
        if (ClientAuthenticationMethod.CLIENT_SECRET_POST.equals(clientRegistration.getClientAuthenticationMethod())) {
            parameters.set(OAuth2ParameterNames.CLIENT_SECRET, clientRegistration.getClientSecret());
        }

        String redirectUri = clientRegistration.getRedirectUri();
        if (redirectUri != null) {
            parameters.add(OAuth2ParameterNames.REDIRECT_URI, redirectUri);
        }
        return parameters;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return OidcAuthenticationToken.class.isAssignableFrom(authentication);
    }

}
