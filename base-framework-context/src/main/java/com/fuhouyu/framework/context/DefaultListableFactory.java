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
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

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
@ToString
public class DefaultListableFactory implements ContextFactory {

    private final Map<Class<?>, Object> contextTypeMap;
    @Getter
    @Setter
    private User user;

    private final Map<String, Object> contextMap;
    @Getter
    @Setter
    private Request request;

    public DefaultListableFactory() {
        this.contextMap = new HashMap<>(4);
        this.contextTypeMap = new HashMap<>(4);
    }


    @Override
    public Object getContext(String contextName) throws ContextNotFoundException {
        Object contextObject = this.contextMap.get(contextName);
        if (Objects.isNull(contextObject)) {
            throw new ContextNotFoundException(String.format("Context [%s] not found.", contextName));
        }
        return contextName;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getContext(Class<T> type) throws ContextNotFoundException {
        Object context = this.contextTypeMap.get(type);
        if (Objects.isNull(context)) {
            throw new ContextNotFoundException(String.format("Context type [%s] not found.", type));
        }
        return (T) context;
    }

    /**
     * 根据名称设置上下文
     *
     * @param contextName 上下文名称
     * @param context     上下文
     */
    public void setContext(String contextName, Object context) {
        this.contextMap.put(contextName, context);
    }

    /**
     * 根据类型设置上下文
     *
     * @param context 上下文
     */
    public void setContext(Object context) {
        contextTypeMap.put(context.getClass(), context);
    }
}
