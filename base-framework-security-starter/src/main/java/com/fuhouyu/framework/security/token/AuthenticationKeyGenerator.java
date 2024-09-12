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

package com.fuhouyu.framework.security.token;

import org.springframework.security.core.Authentication;

/**
 * <p>
 * 从认证信息中提取key信息
 * </p>
 *
 * @author fuhouyu
 * @since 2024/8/14 22:09
 */
public interface AuthenticationKeyGenerator {

    /**
     * 提取关键key信息
     *
     * @param authentication 认证信息
     * @return key
     */
    String extractKey(Authentication authentication);
}
