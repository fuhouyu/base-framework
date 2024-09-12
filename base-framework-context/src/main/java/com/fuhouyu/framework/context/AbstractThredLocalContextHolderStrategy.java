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

package com.fuhouyu.framework.context;

/**
 * <p>
 * 抽象的上下文策略类
 * </p>
 *
 * @author fuhouyu
 * @since 2024/8/18 16:06
 */
public abstract class AbstractThredLocalContextHolderStrategy<T> implements ContextHolderStrategy<Context<T>> {

    private final ThreadLocal<Context<T>> THREAD_LOCAL;

    protected AbstractThredLocalContextHolderStrategy(ThreadLocal<Context<T>> threadLocal) {
        THREAD_LOCAL = threadLocal;
    }

    @Override
    public void clearContext() {
        THREAD_LOCAL.remove();
    }

    @Override
    public Context<T> getContext() {
        return THREAD_LOCAL.get();
    }

    @Override
    public void setContext(Context<T> context) {
        THREAD_LOCAL.set(context);
    }


    @Override
    public Boolean isEmptyContext() {
        return THREAD_LOCAL.get() == null;
    }
}
