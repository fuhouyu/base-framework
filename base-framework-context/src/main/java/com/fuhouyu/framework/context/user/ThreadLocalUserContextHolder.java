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

package com.fuhouyu.framework.context.user;

import com.fuhouyu.framework.context.AbstractThredLocalContextHolderStrategy;
import com.fuhouyu.framework.context.Context;

/**
 * <p>
 * 本地线程的用户上下文实现
 * </p>
 *
 * @author fuhouyu
 * @since 2024/8/14 10:40
 */
public class ThreadLocalUserContextHolder extends AbstractThredLocalContextHolderStrategy<User> {

    private static final InheritableThreadLocal<Context<User>> CONTEXT_HOLDER = new InheritableThreadLocal<>();

    protected ThreadLocalUserContextHolder() {
        super(CONTEXT_HOLDER);
    }


    @Override
    public Context<User> createEmptyContext() {
        UserContextImpl userContext = new UserContextImpl();
        super.setContext(userContext);
        return userContext;
    }

}
