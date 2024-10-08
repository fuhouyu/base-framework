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

package com.fuhouyu.framework.web.handler;

import com.fuhouyu.framework.context.user.User;

/**
 * <p>
 * 用户扩展的抽象类，如果需要有用户的扩展信息，可以扩展该类
 * </p>
 *
 * @author fuhouyu
 * @since 2024/8/15 19:59
 */
@FunctionalInterface
public interface UserExtensionHandler {

    /**
     * 扩展用户的信息
     * 只有在请求中解析出了用户，才会执行该类
     *
     * @param user 用户信息
     * @param <T> 用户子类
     */
    <T extends User> void userExtensionHandlerInterceptor(T user);

}
