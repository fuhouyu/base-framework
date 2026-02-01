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
package com.fuhouyu.framework.database.base;


import java.util.Collection;
import java.util.List;

/**
 * <p>
 * 转换基类
 * </p>
 *
 * @author fuhouyu
 * @since 2024/9/28 16:47
 */
public interface BaseAssembler<E extends BaseEntity, T extends BaseDTO> {

    /**
     * 转换为dto对象
     *
     * @param source entity对象
     * @return dto对象
     */
    T toDTO(E source);

    /**
     * 转换为dto对象
     *
     * @param sourceList 实体对象集合
     * @return dto对象集合
     */
    List<T> toDTO(Collection<E> sourceList);

    /**
     * 转换为实体对象
     *
     * @param source dto对象
     * @return 实体对象
     */
    E toEntity(T source);

    /**
     * 转换为实体对象
     *
     * @param sourceList 源始对象集合
     * @return dto对象集合
     */
    List<E> toEntity(Collection<T> sourceList);
}
