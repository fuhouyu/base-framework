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
 * 文件请求基类
 * </p>
 *
 * @author fuhouyu
 * @since 2024/8/16 10:59
 */
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

    public BaseResourceRequest() {
        this(null, null);
    }

    public BaseResourceRequest(String bucketName) {
        this(bucketName, null, null);
    }

    public BaseResourceRequest(String bucketName, String objectKey) {
        this(bucketName, objectKey, null);
    }

    public BaseResourceRequest(String bucketName, String objectKey, String versionId) {
        this.bucketName = bucketName;
        this.objectKey = objectKey;
        this.versionId = versionId;
    }


    public String getBucketName() {
        return bucketName;
    }

    public String getObjectKey() {
        return objectKey;
    }

    public String getVersionId() {
        return versionId;
    }

    @Override
    public String toString() {
        return "BaseFilRequest{" +
                "bucketName='" + bucketName + '\'' +
                ", objectKey='" + objectKey + '\'' +
                ", versionId='" + versionId + '\'' +
                '}';
    }
}
