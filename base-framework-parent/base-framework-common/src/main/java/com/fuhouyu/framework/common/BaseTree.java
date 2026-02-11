/*
 * Copyright 2024-present fuhouyu.
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

import java.util.List;

/**
 * <p>
 * 树接口基类
 * </p>
 *
 * @author fuhouyu
 * @since 2024/10/9 17:35
 */
public interface BaseTree<T extends BaseTree<T>> {

    /**
     * 主键id
     *
     * @return id
     */
    Long getId();

    /**
     * 父类id
     *
     * @return 父级id
     */
    Long getParentId();

    /**
     * 获取子集
     *
     * @return 子集
     */
    List<T> getChildren();

    /**
     * 设置子集
     *
     * @param children 子集
     */
    void setChildren(List<T> children);

}

