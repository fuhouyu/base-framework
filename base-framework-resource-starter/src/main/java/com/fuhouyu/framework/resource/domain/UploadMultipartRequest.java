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

import java.io.InputStream;

/**
 * <p>
 * 分片上传文件请求
 * </p>
 *
 * @author fuhouyu
 * @since 2024/8/16 17:38
 */
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


    public UploadMultipartRequest(String bucketName, String objectKey) {
        super(bucketName, objectKey);
    }

    public UploadMultipartRequest(String bucketName, String objectKey, String versionId) {
        super(bucketName, objectKey, versionId);
    }

    public String getUploadId() {
        return uploadId;
    }

    public void setUploadId(String uploadId) {
        this.uploadId = uploadId;
    }

    public int getPartNumber() {
        return partNumber;
    }

    public void setPartNumber(int partNumber) {
        this.partNumber = partNumber;
    }

    public long getPartSize() {
        return partSize;
    }

    public void setPartSize(long partSize) {
        this.partSize = partSize;
    }

    public String getMd5Digest() {
        return md5Digest;
    }

    public void setMd5Digest(String md5Digest) {
        this.md5Digest = md5Digest;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    @Override
    public String toString() {
        return "MultipartFileResourceRequest{" +
                "uploadId='" + uploadId + '\'' +
                ", partNumber=" + partNumber +
                ", partSize=" + partSize +
                ", md5Digest='" + md5Digest + '\'' +
                ", inputStream=" + inputStream +
                '}';
    }
}
