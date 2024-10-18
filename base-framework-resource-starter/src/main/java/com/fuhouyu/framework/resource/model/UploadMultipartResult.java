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
 * 分片文件上传结果
 * </p>
 *
 * @author fuhouyu
 * @since 2024/8/16 17:40
 */
@ToString
@Getter
@Setter
public class UploadMultipartResult extends BaseResourceResult {

    /**
     * 当前片号
     */
    private int partNumber;

    /**
     * 分片大小
     */
    private long partSize;

    /**
     * etag
     */
    private String etag;

    /**
     * 构造函数
     *
     * @param partNumber 分页号
     * @param partSize   大小
     * @param etag       etag
     */
    public UploadMultipartResult(int partNumber, long partSize, String etag) {
        this.partNumber = partNumber;
        this.partSize = partSize;
        this.etag = etag;
    }

    /**
     * 构造函数
     * @param bucketName 桶名
     * @param objectKey objectKey
     */
    public UploadMultipartResult(String bucketName, String objectKey) {
        super(bucketName, objectKey);
    }

    /**
     * 构造函数
     * @param bucketName 桶名
     * @param objectKey 对象key
     * @param partNumber 页号
     * @param partSize 大小
     * @param etag etag
     */
    public UploadMultipartResult(String bucketName, String objectKey, int partNumber, long partSize, String etag) {
        super(bucketName, objectKey);
        this.partNumber = partNumber;
        this.partSize = partSize;
        this.etag = etag;
    }

}
