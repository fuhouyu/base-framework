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
 * 文件合并后的结果
 * </p>
 *
 * @author fuhouyu
 * @since 2024/8/16 17:50
 */
public class UploadCompleteMultipartResult extends BaseResourceResult {

    /**
     * etag
     */
    private String etag;

    /**
     * 版本id
     */
    private String versionId;

    public UploadCompleteMultipartResult() {
    }

    public UploadCompleteMultipartResult(String etag, String versionId) {
        this.etag = etag;
        this.versionId = versionId;
    }


    public UploadCompleteMultipartResult(String bucketName, String objectKey, String etag, String versionId) {
        super(bucketName, objectKey);
        this.etag = etag;
        this.versionId = versionId;
    }

    public String getEtag() {
        return etag;
    }

    public void setEtag(String etag) {
        this.etag = etag;
    }

    public String getVersionId() {
        return versionId;
    }

    public void setVersionId(String versionId) {
        this.versionId = versionId;
    }

    @Override
    public String toString() {
        return "CompleteMultipartUploadResult{" +
                "etag='" + etag + '\'' +
                ", versionId='" + versionId + '\'' +
                '}';
    }
}