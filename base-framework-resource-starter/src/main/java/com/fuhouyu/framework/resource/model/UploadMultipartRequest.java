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

import java.io.InputStream;

/**
 * <p>
 * 分片上传文件请求
 * </p>
 *
 * @author fuhouyu
 * @since 2024/8/16 17:38
 */
@ToString
@Getter
@Setter
public class UploadMultipartRequest extends BaseResourceRequest {

    /**
     * 初始化文件上传的id
     */
    private String uploadId;

    /**
     * 分片号
     */
    private int partNumber;

    /**
     * 每片大小
     */
    private long partSize = -1L;

    /**
     * md5签名
     */
    private String md5Digest;

    /**
     * 当前文件流
     */
    private InputStream inputStream;


    /**
     * 构造函数
     *
     * @param bucketName 桶名
     */
    public UploadMultipartRequest(String bucketName) {
        super(bucketName, null);
    }

    /**
     * 构造函数
     *
     * @param bucketName 桶名
     * @param key        对象key
     */
    public UploadMultipartRequest(String bucketName, String key) {
        super(bucketName, key);
    }

    /**
     * 构造函数
     *
     * @param bucketName  桶名
     * @param key         对象key
     * @param uploadId    上传id
     * @param partNumber  分页号
     * @param inputStream 输入流
     * @param partSize    每页大小
     */
    public UploadMultipartRequest(String bucketName, String key, String uploadId, int partNumber,
                                  long partSize, InputStream inputStream) {
        super(bucketName, key);
        this.uploadId = uploadId;
        this.partNumber = partNumber;
        this.inputStream = inputStream;
        this.partSize = partSize;
    }

}
