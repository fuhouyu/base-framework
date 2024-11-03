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
package com.fuhouyu.framework.security.entity;

import com.fuhouyu.framework.security.core.GrantTypeAuthenticationTokenEnum;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.authentication.AbstractAuthenticationToken;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 类型认证实体
 * </p>
 *
 * @author fuhouyu
 * @since 2024/11/3 17:59
 */
@Getter
@Setter
@ToString
public class GrantTypeAuthenticationEntity {


    private final String grantType;

    private final Map<String, Object> parameters;

    public GrantTypeAuthenticationEntity(String grantType,
                                         String principal) {
        this(grantType, principal, null);
    }

    public GrantTypeAuthenticationEntity(String grantType) {
        this(grantType, null, null);
    }

    public GrantTypeAuthenticationEntity(String grantType,
                                         String principal,
                                         String credentials) {
        this.grantType = grantType;
        this.parameters = new HashMap<>(8);
        this.parameters.put("principal", principal);
        this.parameters.put("credentials", credentials);
    }


    /**
     * 添加参数映射
     *
     * @param key   key
     * @param value value
     */
    public void addParameter(String key, Object value) {
        this.parameters.put(key, value);
    }

    /**
     * 创建authenticationToken对象
     *
     * @return token对象
     */
    public AbstractAuthenticationToken createAuthenticationToken() {
        GrantTypeAuthenticationTokenEnum grantTypeAuthenticationTokenEnum = GrantTypeAuthenticationTokenEnum.safeEnumValueOf(this.grantType);
        return grantTypeAuthenticationTokenEnum.loadAuthenticationToken(this.parameters);
    }


}
