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

package com.fuhouyu.framework.context.user;

import com.fuhouyu.framework.context.ContextHolderStrategy;

/**
 * <p>
 * 本地线程的用户上下文实现
 * </p>
 *
 * @author fuhouyu
 * @since 2024/8/14 10:40
 */
public class ThreadLocalUserContextHolder implements ContextHolderStrategy<UserContext> {

    private static final InheritableThreadLocal<UserContext> USER_CONTEXT_THREAD_LOCAL;

    static {
        USER_CONTEXT_THREAD_LOCAL = new InheritableThreadLocal<>();
    }

    @Override
    public void clearContext() {
        USER_CONTEXT_THREAD_LOCAL.remove();
    }

    @Override
    public UserContext getContext() {
        return USER_CONTEXT_THREAD_LOCAL.get();
    }

    @Override
    public void setContext(UserContext context) {
        USER_CONTEXT_THREAD_LOCAL.set(context);
    }

    @Override
    public UserContext createEmptyContext() {
        UserContextImpl userContext = new UserContextImpl();
        USER_CONTEXT_THREAD_LOCAL.set(userContext);
        return userContext;
    }

    @Override
    public Boolean isEmptyContext() {
        return USER_CONTEXT_THREAD_LOCAL.get() == null;
    }
}
