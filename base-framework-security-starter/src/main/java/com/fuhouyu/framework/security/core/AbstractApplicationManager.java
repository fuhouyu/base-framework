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

package com.fuhouyu.framework.security.core;

import com.fuhouyu.framework.security.domain.dto.ApplicationDTO;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Objects;

/**
 * <p>
 * 客户端管理抽象类,
 * 主要解析应用
 * </p>
 *
 * @author fuhouyu
 * @since 2024/8/15 17:12
 */
public abstract class AbstractApplicationManager implements AuthenticationManager {

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (Objects.isNull(requestAttributes)) {
            return null;
        }
        String applicationId = String.valueOf(authentication.getPrincipal());
        ApplicationDTO application = this.queryApplication(applicationId);
        if (Objects.isNull(application)) {
            throw new BadCredentialsException(String.format("%s 当前应用不存在，禁止登录", applicationId));
        }

        // 客户端密钥不匹配
        if (!Objects.equals(authentication.getCredentials(),
                application.getClientSecret())) {
            throw new BadCredentialsException(String.format("%s 无效的应用，禁止登录", applicationId));
        }
        if (authentication instanceof UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken) {
            usernamePasswordAuthenticationToken.setDetails(application);
        }
        return authentication;
    }

    public abstract ApplicationDTO queryApplication(String applicationId);
}
