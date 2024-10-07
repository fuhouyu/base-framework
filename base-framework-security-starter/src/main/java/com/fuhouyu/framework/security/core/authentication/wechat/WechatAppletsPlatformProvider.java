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

package com.fuhouyu.framework.security.core.authentication.wechat;

import com.fuhouyu.framework.security.core.AbstractAuthenticationProvider;
import com.fuhouyu.framework.security.properties.OpenPlatformAuthProperties;
import com.fuhouyu.framework.utils.JacksonUtil;
import com.fuhouyu.framework.utils.LoggerUtil;
import lombok.EqualsAndHashCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;

/**
 * <p>
 * 微信小程序提供者
 * </p>
 *
 * @author fuhouyu
 * @since 2024/8/15 12:32
 */
public class WechatAppletsPlatformProvider extends AbstractAuthenticationProvider<WechatAppletsUserInfo> {

    private static final Logger LOGGER = LoggerFactory.getLogger(WechatAppletsPlatformProvider.class);

    // TODO 这里的restTemplate 后续待抽离
    private final RestTemplate restTemplate;

    private final UserDetailsService userDetailsService;

    private final OpenPlatformAuthProperties.AuthDetail authDetail;


    /**
     * 构造函数
     *
     * @param restTemplate       restTemplate
     * @param userDetailsService 用户详情接口
     * @param authDetail         第三方平台认证相关的信息
     */
    public WechatAppletsPlatformProvider(RestTemplate restTemplate,
                                         UserDetailsService userDetailsService,
                                         OpenPlatformAuthProperties.AuthDetail authDetail) {
        super(null);
        this.restTemplate = restTemplate;
        this.userDetailsService = userDetailsService;
        this.authDetail = authDetail;
    }

    public WechatAppletsPlatformProvider(RestTemplate restTemplate,
                                         UserDetailsService userDetailsService,
                                         OpenPlatformAuthProperties.AuthDetail authDetail,
                                         Function<WechatAppletsUserInfo, UserDetails> registerFunction) {
        super(registerFunction);
        this.restTemplate = restTemplate;
        this.userDetailsService = userDetailsService;
        this.authDetail = authDetail;
    }


    @Override
    public WechatAppletsUserInfo loadPlatformUser(Authentication authentication) {
        String requestUrl = this.getRequestUrl((String) authentication.getPrincipal());
        ResponseEntity<String> wechatAppletsUserInfoEntity = restTemplate.getForEntity(requestUrl, String.class);
        if (!Objects.equals(wechatAppletsUserInfoEntity.getStatusCode().value(), 200)) {
            LoggerUtil.error(LOGGER, "微信小程序请求url失败，jsCode:{}, 请求参数:{}, 返回的状态码:{}",
                    authentication.getPrincipal(), requestUrl.replaceAll(authDetail.getClientSecret(), "*"),
                    wechatAppletsUserInfoEntity.getStatusCode().value());
            throw new IllegalArgumentException("微信小程序登录不正常，请检索配置参数是否正确");
        }
        WechatAppletsUserInfo wechatAppletsUserInfo =
                JacksonUtil.readValue(wechatAppletsUserInfoEntity.getBody(), WechatAppletsUserInfo.class);
        if (Objects.nonNull(wechatAppletsUserInfo.getErrCode()) && !Objects.equals(wechatAppletsUserInfo.getErrCode(), 0)) {
            throw new IllegalArgumentException(String.format("微信小程序登录失败:%s", wechatAppletsUserInfo.getErrMsg()));
        }
        return wechatAppletsUserInfo;
    }

    @Override
    public UserDetails loadUserDetails(WechatAppletsUserInfo wechatAppletsUserInfo) throws UsernameNotFoundException {
        return this.userDetailsService.loadUserByUsername(wechatAppletsUserInfo.getOpenId());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return WechatAppletsAuthenticationToken.class.isAssignableFrom(authentication);
    }

    /**
     * 获取app拼接后的url
     *
     * @param jsCode 登录时获取的 code，可通过wx.login获取
     * @return app参数
     */
    private String getRequestUrl(String jsCode) {
        return String.format("%s?appid=%s&secret=%s&js_code=%s&grant_type=authorization_code",
                authDetail.getLoginUrl(),
                authDetail.getClientId(),
                authDetail.getClientSecret(),
                jsCode);
    }

    /**
     * 使用它才支持微信小程序认证
     */
    @EqualsAndHashCode(callSuper = true)
    public static class WechatAppletsAuthenticationToken extends AbstractAuthenticationToken {

        /**
         * 微信一次性认证码
         */
        private final String jsCode;

        /**
         * 构造函数
         *
         * @param jsCode 登录时获取的 code，可通过wx.login获取
         */
        public WechatAppletsAuthenticationToken(String jsCode) {
            super(List.of());
            this.jsCode = jsCode;
        }

        @Override
        public Object getCredentials() {
            return null;
        }

        @Override
        public Object getPrincipal() {
            return this.jsCode;
        }
    }
}
