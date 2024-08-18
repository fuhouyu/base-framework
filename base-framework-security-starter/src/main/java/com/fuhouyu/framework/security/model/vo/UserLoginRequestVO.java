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

import java.io.Serial;
import java.io.Serializable;

/**
 * <p>
 * 用户登录的vo对象
 * </p>
 *
 * @author fuhouyu
 * @since 2024/8/15 16:44
 */
@Schema(name = "userLoginVO", description = "用户登录的vo对象")
public class UserLoginRequestVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(name = "username", description = "登录的用户名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String username;

    @Schema(name = "password", description = "登录密码", requiredMode = Schema.RequiredMode.REQUIRED)
    private String password;

    @Schema(name = "grantType", description = "登录的用户名称", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String grantType;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGrantType() {
        return grantType;
    }

    public void setGrantType(String grantType) {
        this.grantType = grantType;
    }

    @Override
    public String toString() {
        return "UserSignInVO{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", grantType='" + grantType + '\'' +
                '}';
    }
}
