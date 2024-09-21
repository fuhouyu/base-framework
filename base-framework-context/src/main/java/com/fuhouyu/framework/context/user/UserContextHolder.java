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

import com.fuhouyu.framework.context.Context;
import com.fuhouyu.framework.context.ContextHolderStrategy;

import java.util.Objects;

/**
 * <p>
 * 用户上下文
 * </p>
 *
 * @author fuhouyu
 * @since 2024/8/14 11:25
 */
public class UserContextHolder {

    private static final ContextHolderStrategy<Context<User>> CONTEXT_HOLDER_STRATEGY;

    static {
        CONTEXT_HOLDER_STRATEGY = new ThreadLocalUserContextHolder();
    }

    public static void clearContext() {
        CONTEXT_HOLDER_STRATEGY.clearContext();
    }


    public static Context<User> getContext() {
        return CONTEXT_HOLDER_STRATEGY.getContext();
    }

    public static void setContext(Context<User> context) {
        if (Objects.isNull(context)) {
            throw new IllegalArgumentException("user context is null");
        }
        CONTEXT_HOLDER_STRATEGY.setContext(context);
    }

    public static Context<User> createEmptyContext() {
        return CONTEXT_HOLDER_STRATEGY.createEmptyContext();
    }
}
