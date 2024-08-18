/*
 * Copyright 2012-2020 the original author or authors.
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

package com.fuhouyu.framework.security.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * <p>
 * 用户登录返回的vo对象
 * </p>
 *
 * @author fuhouyu
 * @since 2024/8/15 16:53
 */
@Schema(name = "userLoginResponseVO", description = "用户登录响应的vo对象")
public class UserLoginResponseVO {

    /**
     * token类型
     */
    @Schema(name = "tokenType", description = "token类型")
    private String tokenType;

    /**
     * 登录的账号。
     */
    @Schema(name = "account", description = "登录账号")
    private String account;

    /**
     * 账户的登录类型
     */
    @Schema(name = "accountType", description = "账户的登录类型")
    private String accountType;

    /**
     * 过期时间
     */
    @Schema(name = "expiresIn", description = "过期时间")
    private Integer expiresIn;

    /**
     * 用户唯一id
     */
    @Schema(name = "internalUserId", description = "用户唯一id")
    private String internalUserId;

    /**
     * 认证token
     */
    @Schema(name = "accessToken", description = "认证token")
    private String accessToken;

    /**
     * 刷新令牌
     */
    @Schema(name = "refreshToken", description = "刷新令牌")
    private String refreshToken;

    /**
     * 刷新令牌过期时间
     */
    @Schema(name = "refreshTokenExpiresIn", description = "刷新令牌过期时间")
    private Integer refreshTokenExpiresIn;

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public Integer getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(Integer expiresIn) {
        this.expiresIn = expiresIn;
    }

    public String getInternalUserId() {
        return internalUserId;
    }

    public void setInternalUserId(String internalUserId) {
        this.internalUserId = internalUserId;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public Integer getRefreshTokenExpiresIn() {
        return refreshTokenExpiresIn;
    }

    public void setRefreshTokenExpiresIn(Integer refreshTokenExpiresIn) {
        this.refreshTokenExpiresIn = refreshTokenExpiresIn;
    }

    @Override
    public String toString() {
        return "UserLoginResponseVO{" +
                "tokenType='" + tokenType + '\'' +
                ", account='" + account + '\'' +
                ", accountType='" + accountType + '\'' +
                ", expiresIn=" + expiresIn +
                ", internalUserId='" + internalUserId + '\'' +
                ", accessToken='" + accessToken + '\'' +
                ", refreshToken='" + refreshToken + '\'' +
                ", refreshTokenExpiresIn=" + refreshTokenExpiresIn +
                '}';
    }
}
