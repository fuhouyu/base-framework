/*
 * Copyright 2024-2034 the original author or authors.
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

/**
 * <p>
 * 初始化分片文件上传
 * </p>
 *
 * @author fuhouyu
 * @since 2024/8/16 17:34
 */
public class InitiateUploadMultipartRequest extends BaseResourceRequest {

    private ResourceMetadata resourceMetadata;


    public InitiateUploadMultipartRequest(String bucketName, String objectKey) {
        super(bucketName, objectKey);
    }

    public InitiateUploadMultipartRequest(String bucketName, String objectKey, String versionId) {
        super(bucketName, objectKey, versionId);
    }

    public InitiateUploadMultipartRequest(String bucketName, String objectKey, ResourceMetadata resourceMetadata) {
        super(bucketName, objectKey);
        this.resourceMetadata = resourceMetadata;
    }

    public InitiateUploadMultipartRequest(String bucketName, String objectKey, String versionId, ResourceMetadata resourceMetadata) {
        super(bucketName, objectKey, versionId);
        this.resourceMetadata = resourceMetadata;
    }

    public ResourceMetadata getFileResourceMetadata() {
        return resourceMetadata;
    }

    public void setFileResourceMetadata(ResourceMetadata resourceMetadata) {
        this.resourceMetadata = resourceMetadata;
    }

    @Override
    public String toString() {
        return "InitiateMultipartUploadRequest{" +
                "fileResourceMetadata=" + resourceMetadata +
                '}';
    }
}
