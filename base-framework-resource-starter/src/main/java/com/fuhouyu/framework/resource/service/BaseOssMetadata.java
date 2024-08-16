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

package com.fuhouyu.framework.resource.service;

import com.fuhouyu.framework.resource.domain.ResourceMetadata;

/**
 * <p>
 * oss元数据转换的基类
 * </p>
 *
 * @author fuhouyu
 * @since 2024/8/16 17:05
 */
public interface BaseOssMetadata<T> {

    /**
     * 将文件资源元数据，转换为oss的元数据类型
     *
     * @return oss文件元数据类型
     */
    T getOssMetadata(ResourceMetadata resourceMetadata);

    /**
     * 将oss元数据类型转换为自定义的文件资源元数据
     *
     * @param ossMetadata oss元数据
     * @return 文件资源元数据
     */
    ResourceMetadata getFileResourceMetadata(T ossMetadata);
}
