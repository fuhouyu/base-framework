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

import com.fuhouyu.framework.cache.CacheAutoConfiguration;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.redis.autoconfigure.DataRedisAutoConfiguration;
import org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration;
import org.springframework.boot.jdbc.autoconfigure.JdbcTemplateAutoConfiguration;
import org.springframework.boot.security.oauth2.client.autoconfigure.OAuth2ClientAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.util.UriComponentsBuilder;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.util.Base64;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


/**
 * <p>
 * 刷新令牌测试类
 * </p>
 *
 * @author fuhouyu
 * @since 2024/10/22 22:21
 */
@ActiveProfiles("test")
@AutoConfigureMockMvc
@EnableWebSecurity
@EnableWebMvc
@SpringBootTest(classes = {
        BaseTest.BaseComponent.class,
        CacheAutoConfiguration.class,
        DataSourceAutoConfiguration.class,
        JdbcTemplateAutoConfiguration.class,
        DataRedisAutoConfiguration.class,
        SecurityAutoConfiguration.class,
        OAuth2ClientAutoConfiguration.class,
        AuthenticationAutoConfiguration.class
})
class PKCETest extends BaseTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RegisteredClientRepository registeredClientRepository;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity()) // 必须有这一行，@WithMockUser 才会生效
                .build();
    }

    @Test
    @WithMockUser(username = "admin")
    void testPKCE() throws Exception {
        RegisteredClient registeredClient = RegisteredClient.withId("test")
                .clientId("messaging-client")
                .clientAuthenticationMethod(ClientAuthenticationMethod.NONE)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                .redirectUri("http://127.0.0.1:8080/callback")
                .scope("profile")
                .tokenSettings(TokenSettings.builder()
                        .accessTokenTimeToLive(Duration.ofMinutes(5)) // 这里会自动生成正确的 JSON
                        .refreshTokenTimeToLive(Duration.ofHours(1))
                        // 对于 Public Client (NONE 认证)，必须开启轮转 (Rotation)
                        // 否则 Spring 的生成器会直接 return null
                        .reuseRefreshTokens(false)
                        .build())
                .clientSettings(ClientSettings.builder().requireProofKey(true).build())
                .build();
        when(registeredClientRepository.findByClientId("messaging-client"))
                .thenReturn(registeredClient);
        when(registeredClientRepository.findById("test"))
                .thenReturn(registeredClient);
        // --- 准备 PKCE 参数 ---
        String codeVerifier = generateCodeVerifier();
        String codeChallenge = generateCodeChallenge(codeVerifier);

        String url = UriComponentsBuilder.fromPath("/oauth2/authorize")
                .queryParam("response_type", "code")
                .queryParam("client_id", "messaging-client")
                .queryParam("scope", "profile")
                .queryParam("redirect_uri", "http://127.0.0.1:8080/callback")
                .queryParam("code_challenge", codeChallenge)
                .queryParam("code_challenge_method", "S256")
                .build()
                .toUriString();

        MvcResult result = mockMvc.perform(get(url)
                )
                // 如果还报错，在这里加一行 .andDo(print()) 查看控制台打印的详细错误描述
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andReturn();

        // 从重定向 URL 中提取授权码 code
        String redirectedUrl = result.getResponse().getRedirectedUrl();
        Assertions.assertNotNull(redirectedUrl, "回调地址为空");
        String code = redirectedUrl.substring(redirectedUrl.indexOf("code=") + 5);

        mockMvc.perform(MockMvcRequestBuilders.post("/oauth2/token")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("grant_type", "authorization_code")
                        .param("client_id", "messaging-client")
                        .param("code", code)
                        .param("redirect_uri", "http://127.0.0.1:8080/callback")
                        .param("code_verifier", codeVerifier))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.access_token").exists());
    }


    private String generateCodeVerifier() {
        return "this-is-a-very-long-and-secure-random-code-verifier-string";
    }

    // 生成 Code Challenge (哈希值)
    private String generateCodeChallenge(String verifier) throws NoSuchAlgorithmException {
        byte[] bytes = verifier.getBytes(StandardCharsets.US_ASCII);
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        byte[] digest = messageDigest.digest(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(digest);
    }
}
