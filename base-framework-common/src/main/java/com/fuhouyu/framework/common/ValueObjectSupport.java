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
package com.fuhouyu.framework.common;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * <p>
 * 值对象的基类
 * </p>
 *
 * @author fuhouyu
 * @since 2024/10/17 20:07
 */
public class ValueObjectSupport<T extends ValueObject<T>> implements ValueObject<T> {

    private transient int cachedHashCode = 0;

    @Override
    public boolean sameValueAs(T other) {
        return other != null && EqualsBuilder.reflectionEquals(this, other, false);
    }

    @Override
    public final int hashCode() {
        int h = cachedHashCode;
        if (h == 0) {
            h = HashCodeBuilder.reflectionHashCode(this, false);
            cachedHashCode = h;
        }

        return h;
    }

    @Override
    @SuppressWarnings("unchecked")
    public final boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        return sameValueAs((T) o);
    }

}
