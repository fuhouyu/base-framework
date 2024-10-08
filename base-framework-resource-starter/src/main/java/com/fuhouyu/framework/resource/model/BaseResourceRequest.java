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
 * 文件请求基类
 * </p>
 *
 * @author fuhouyu
 * @since 2024/8/16 10:59
 */
@ToString
@Getter
@Setter
public class BaseResourceRequest {


    /**
     * 桶名
     */
    private final String bucketName;

    /**
     * 文件对象key
     */
    private final String objectKey;

    /**
     * 需要下载的文件版本id
     */
    private final String versionId;


    /**
     * 构造函数
     *
     * @param bucketName 桶名
     * @param objectKey  对象key
     */
    public BaseResourceRequest(String bucketName, String objectKey) {
        this(bucketName, objectKey, null);
    }

    /**
     * 构造函数
     * @param bucketName 桶名
     * @param objectKey 对象key
     * @param versionId 版本id
     */
    public BaseResourceRequest(String bucketName, String objectKey, String versionId) {
        this.bucketName = bucketName;
        this.objectKey = objectKey;
        this.versionId = versionId;
    }

}
