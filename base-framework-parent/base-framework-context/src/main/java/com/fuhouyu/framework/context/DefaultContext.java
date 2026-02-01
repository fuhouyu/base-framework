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
package com.fuhouyu.framework.context;

import com.fuhouyu.framework.context.request.Request;
import com.fuhouyu.framework.context.user.User;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>
 * 上下文默认实现
 * </p>
 *
 * @author fuhouyu
 * @since 2024/10/16 20:17
 */
@ToString
public class DefaultContext implements Context {

    @Getter @Setter
    private User user;

    @Getter @Setter
    private Request request;

    // 仅保留类型映射，足以应对 99% 的扩展场景
    private final Map<Class<?>, Object> attributes = new ConcurrentHashMap<>();

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getAttribute(Class<T> type) {
        return (T) attributes.get(type);
    }

    @Override
    public void setAttribute(Object attribute) {
        if (attribute != null) {
            // 注意：这里建议在 set 时处理一下代理对象问题
            attributes.put(attribute.getClass(), attribute);
        }
    }
}
