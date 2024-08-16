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

package com.fuhouyu.framework.resource.domain;

/**
 * <p>
 * 基础的文件响应类
 * </p>
 *
 * @author fuhouyu
 * @since 2024/8/16 15:23
 */
public class BaseResourceResult {

    /**
     * object key
     */
    private String objectKey;

    /**
     * 桶名
     */
    private String bucketName;


    public BaseResourceResult() {
    }

    public BaseResourceResult(String bucketName, String objectKey) {
        this.objectKey = objectKey;
        this.bucketName = bucketName;
    }

    public String getObjectKey() {
        return objectKey;
    }

    public void setObjectKey(String objectKey) {
        this.objectKey = objectKey;
    }

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    @Override
    public String toString() {
        return "BaseFileResourceResult{" +
                "objectKey='" + objectKey + '\'' +
                ", bucketName='" + bucketName + '\'' +
                '}';
    }
}
