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

package com.fuhouyu.framework.context.request;

import com.fuhouyu.framework.context.Context;
import com.fuhouyu.framework.context.ContextHolderStrategy;

import java.util.Objects;

/**
 * <p>
 * 请求上下文实现
 * </p>
 *
 * @author fuhouyu
 * @since 2024/8/18 16:01
 */
public class RequestContextHolder {

    private static final ContextHolderStrategy<Context<Request>> CONTEXT_HOLDER_STRATEGY;

    static {
        CONTEXT_HOLDER_STRATEGY = new ThreadLocalRequestContextHolder();
    }

    public static void clearContext() {
        CONTEXT_HOLDER_STRATEGY.clearContext();
    }


    public static Context<Request> getContext() {
        return CONTEXT_HOLDER_STRATEGY.getContext();
    }

    public static void setContext(Context<Request> context) {
        if (Objects.isNull(context)) {
            throw new IllegalArgumentException("Request context is null");
        }
        CONTEXT_HOLDER_STRATEGY.setContext(context);
    }

    public static Context<Request> createEmptyContext() {
        return CONTEXT_HOLDER_STRATEGY.createEmptyContext();
    }
}
