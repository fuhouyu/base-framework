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
package com.fuhouyu.framework.security.core;

import com.fuhouyu.framework.security.core.authentication.refreshtoken.RefreshAuthenticationProvider;
import com.fuhouyu.framework.security.core.authentication.wechat.WechatAppletsPlatformProvider;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AbstractAuthenticationToken;

/**
 * <p>
 * 默认的映射枚举类
 * </p>
 *
 * @author fuhouyu
 * @since 2024/10/22 21:25
 */
@RequiredArgsConstructor
@Getter
@SuppressWarnings("unchecked")
public enum GrantTypeAuthenticationTokenEnum implements GrantTypeAuthenticationTokenMapping {

    /**
     * 微信小程序
     */
    WECHAT_APPLETS("WECHAT_APPLETS") {
        @Override
        public <T extends AbstractAuthenticationToken> Class<T> getAuthenticationTokenClass() {
            return (Class<T>) WechatAppletsPlatformProvider.WechatAppletsAuthenticationToken.class;
        }
    },

    /**
     * 刷新令牌
     */
    REFRESH_TOKEN("REFRESH_TOKEN") {
        @Override
        public <T extends AbstractAuthenticationToken> Class<T> getAuthenticationTokenClass() {
            return (Class<T>) RefreshAuthenticationProvider.RefreshAuthenticationToken.class;
        }
    }
    ;

    private final String grantType;

    /**
     * 安全获取枚举类
     *
     * @param grantType 授权类型
     * @return 枚举类
     */
    public static GrantTypeAuthenticationTokenEnum safeEnumValueOf(String grantType) {
        try {
            return GrantTypeAuthenticationTokenEnum.valueOf(grantType);
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new IllegalArgumentException("Invalid value for enum: " + grantType);
        }
    }

    @Override
    public abstract <T extends AbstractAuthenticationToken> Class<T> getAuthenticationTokenClass();
}
