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
 * 初始化分片文件上传
 * </p>
 *
 * @author fuhouyu
 * @since 2024/8/16 17:34
 */
@ToString
@Getter
@Setter
public class InitiateUploadMultipartRequest extends BaseResourceRequest {

    private ResourceMetadata resourceMetadata;


    /**
     * 构造函数
     *
     * @param bucketName 桶名
     * @param objectKey  对象key
     */
    public InitiateUploadMultipartRequest(String bucketName, String objectKey) {
        super(bucketName, objectKey);
    }

    /**
     * 构造函数
     * @param bucketName 桶名
     * @param objectKey 对象key
     * @param versionId 上传id
     */
    public InitiateUploadMultipartRequest(String bucketName, String objectKey, String versionId) {
        super(bucketName, objectKey, versionId);
    }

    /**
     * 构造函数
     * @param bucketName 桶名
     * @param objectKey 对象key
     * @param resourceMetadata 资源元数据
     */
    public InitiateUploadMultipartRequest(String bucketName, String objectKey, ResourceMetadata resourceMetadata) {
        super(bucketName, objectKey);
        this.resourceMetadata = resourceMetadata;
    }

    /**
     * 构造函数
     * @param bucketName 桶名
     * @param objectKey 对象key
     * @param versionId 版本id
     * @param resourceMetadata 资源元数据
     */
    public InitiateUploadMultipartRequest(String bucketName, String objectKey, String versionId, ResourceMetadata resourceMetadata) {
        super(bucketName, objectKey, versionId);
        this.resourceMetadata = resourceMetadata;
    }

}
