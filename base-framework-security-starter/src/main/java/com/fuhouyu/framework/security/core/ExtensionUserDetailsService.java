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
package com.fuhouyu.framework.security.core;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * <p>
 * 扩展的用户接口
 * </p>
 *
 * @author fuhouyu
 * @since 2024/11/13 21:07
 */
public interface ExtensionUserDetailsService extends UserDetailsService {


    /**
     * 通过账号和账号类型加载用户详情
     *
     * @param account     账号
     * @param accountType 账号类型
     * @return 账号详情
     * @throws UsernameNotFoundException 用户不存在的异常
     */
    UserDetails loadUserByUsername(String account,
                                   String accountType) throws UsernameNotFoundException;
}
