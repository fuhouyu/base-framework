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

package com.fuhouyu.framework.resource.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <p>
 * 文件下载的结果
 * </p>
 *
 * @author fuhouyu
 * @since 2024/8/16 17:19
 */
@ToString
@Getter
@Setter
public class DownloadResourceResult {

    /**
     * 文件元数据
     */
    private ResourceMetadata resourceMetadata;

    /**
     * 构造函数
     */
    public DownloadResourceResult() {
    }

    /**
     * 构造函数
     *
     * @param resourceMetadata 资源元数据
     */
    public DownloadResourceResult(ResourceMetadata resourceMetadata) {
        this.resourceMetadata = resourceMetadata;
    }

}
