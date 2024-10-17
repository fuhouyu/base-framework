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
package com.fuhouyu.framework.context;

import com.fuhouyu.framework.context.exception.ContextNotFoundException;
import lombok.NonNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * <p>
 * 上下文默认实现
 * </p>
 *
 * @author fuhouyu
 * @since 2024/10/16 20:17
 */
public class ContextImpl implements Context {

    private User user;

    private Request request;

    private final Map<String, Object> contextMap;

    public ContextImpl() {
        this.contextMap = new HashMap<>(4);
    }

    @Override
    public User getUser() {
        return this.user;
    }

    @Override
    public void setUser(User user) {
        this.user = user;
        contextMap.put("user", user);
    }

    @Override
    public Request getRequest() {
        return this.request;
    }

    @Override
    public void setRequest(Request request) {
        this.request = request;
        contextMap.put("request", request);
    }

    @Override
    public Object getContextByName(String contextName) throws ContextNotFoundException {
        Object contextObject = this.contextMap.get(contextName);
        if (Objects.isNull(contextObject)) {
            throw new ContextNotFoundException(String.format("Context [%s] not found.", contextName));
        }
        return contextName;
    }

    @Override
    public void putContextByName(@NonNull String contextName, @NonNull Object context) {
        this.contextMap.put(contextName, context);
    }

    @Override
    public void removeContextByName(@NonNull String contextName) {
        this.contextMap.remove(contextName);
    }
}
