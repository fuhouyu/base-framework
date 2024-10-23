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

import java.io.Serializable;

/**
 * <p>
 * 实体接口
 * </p>
 *
 * @author fuhouyu
 * @since 2024/10/17 20:12
 */
public interface Entity<T> extends Serializable {

    /**
     * 实体通过身份进行比较。
     *
     * @param other 另个实体
     * @return 身份相同时为true
     */
    boolean sameIdentityAs(T other);

}
