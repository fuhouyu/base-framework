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

package com.fuhouyu.framework.resource.model;

/**
 * <p>
 * 文件分片取消上传
 * </p>
 *
 * @author fuhouyu
 * @since 2024/8/16 18:00
 */
public class UploadAbortMultipartRequest extends BaseResourceRequest {

    /**
     * 文件上传id
     */
    private String uploadId;


    public UploadAbortMultipartRequest(String bucketName, String objectKey) {
        super(bucketName, objectKey);
    }

    public UploadAbortMultipartRequest(String bucketName, String objectKey, String versionId) {
        super(bucketName, objectKey, versionId);
    }

    public String getUploadId() {
        return uploadId;
    }

    public void setUploadId(String uploadId) {
        this.uploadId = uploadId;
    }

    @Override
    public String toString() {
        return "AbortMultipartUploadRequest{" +
                "uploadId='" + uploadId + '\'' +
                '}';
    }
}
