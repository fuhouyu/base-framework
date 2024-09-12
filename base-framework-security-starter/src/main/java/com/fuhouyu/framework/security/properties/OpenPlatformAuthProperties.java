/*
 * Copyright 2024-2034 the original author or authors.
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

package com.fuhouyu.framework.security.properties;

import com.fuhouyu.framework.constants.ConfigPropertiesConstant;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

/**
 * <p>
 * 开放平台配置项
 * </p>
 *
 * @author fuhouyu
 * @since 2024/8/15 12:39
 */
@ConfigurationProperties(prefix = OpenPlatformAuthProperties.PREFIX)
public class OpenPlatformAuthProperties {

    public static final String PREFIX = ConfigPropertiesConstant.PROPERTIES_PREFIX + "open-platform";

    /**
     * 客户端相关配置
     */
    private Map<OpenPlatformAuthTypeEnum, AuthDetail> auth;

    public Map<OpenPlatformAuthTypeEnum, AuthDetail> getAuth() {
        return auth;
    }

    public void setAuth(Map<OpenPlatformAuthTypeEnum, AuthDetail> auth) {
        this.auth = auth;
    }

    @Override
    public String toString() {
        return "OpenPlatformAuthProperties{" +
                "auth=" + auth +
                '}';
    }

    /**
     * 平台类型
     */
    public enum OpenPlatformAuthTypeEnum {
        /**
         * 微信小程序
         */
        WECHAT_APPLET,


    }

    /**
     * 授权的详情
     */
    public static class AuthDetail {

        /**
         * 客户端id
         */
        private String clientId;

        /**
         * 客户端密钥
         */
        private String clientSecret;

        /**
         * 登录的url，一般为换取token
         */
        private String loginUrl;

        /**
         * 用户详情的url
         */
        private String userInfoUrl;

        public String getClientId() {
            return clientId;
        }

        public void setClientId(String clientId) {
            this.clientId = clientId;
        }

        public String getClientSecret() {
            return clientSecret;
        }

        public void setClientSecret(String clientSecret) {
            this.clientSecret = clientSecret;
        }

        public String getLoginUrl() {
            return loginUrl;
        }

        public void setLoginUrl(String loginUrl) {
            this.loginUrl = loginUrl;
        }

        public String getUserInfoUrl() {
            return userInfoUrl;
        }

        public void setUserInfoUrl(String userInfoUrl) {
            this.userInfoUrl = userInfoUrl;
        }

        @Override
        public String toString() {
            return "AuthDetail{" +
                    "clientId='" + clientId + '\'' +
                    ", clientSecret='" + clientSecret + '\'' +
                    ", loginUrl='" + loginUrl + '\'' +
                    ", userInfoUrl='" + userInfoUrl + '\'' +
                    '}';
        }
    }
}
