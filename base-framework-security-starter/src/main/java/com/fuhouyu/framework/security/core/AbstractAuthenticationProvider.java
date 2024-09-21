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

import com.fuhouyu.framework.utils.LoggerUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collections;
import java.util.Objects;
import java.util.function.Function;

/**
 * <p>
 * 抽象的认证提供者
 * </p>
 *
 * @author fuhouyu
 * @since 2024/8/14 17:29
 */
public abstract class AbstractAuthenticationProvider<T> implements AuthenticationProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractAuthenticationProvider.class);

    private final Function<T, UserDetails> registerFunction;

    public AbstractAuthenticationProvider(Function<T, UserDetails> registerFunction) {
        this.registerFunction = registerFunction;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        T t = null;
        UserDetails userDetails;
        try {
            t = this.loadPlatformUser(authentication);
            userDetails = this.loadUserDetails(t);
        } catch (UsernameNotFoundException e) {
            if (Objects.isNull(registerFunction)) {
                throw e;
            }
            userDetails = this.registerIfUserNotFound(t);
        } catch (Exception e) {
            LoggerUtil.error(LOGGER, "第三方平台认证请求失败:{}", e.getMessage());
            throw new AuthenticationServiceException(e.getMessage(), e);
        }
        // 这里返回公共的认证请求
        return UsernamePasswordAuthenticationToken.authenticated(userDetails,
                authentication.getCredentials(), Collections.emptyList());
    }


    /**
     * 加载平台的用户并返回
     *
     * @param authentication 认证管理器
     * @return 第三方平台的用户详情
     * @throws Exception 请求第三方时，可能会抛出异常错误
     */
    public abstract T loadPlatformUser(Authentication authentication) throws Exception;

    /**
     * 加载用户详情
     *
     * @param authentication 认证器
     * @return 用户详情
     * @throws UsernameNotFoundException 未找到用户异常
     */
    public abstract UserDetails loadUserDetails(T t) throws UsernameNotFoundException;

    /**
     * 当用户不存在时的注册方法
     *
     * @param t 第三方的用户详情
     * @return 用户详情
     */
    private UserDetails registerIfUserNotFound(T t) {
        return registerFunction.apply(t);
    }
}
