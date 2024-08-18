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

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 分片文件上传合并请求
 * </p>
 *
 * @author fuhouyu
 * @since 2024/8/16 17:43
 */
public class UploadCompleteMultipartRequest extends BaseResourceRequest {

    /**
     * 文件上传的id
     */
    private String uploadId;

    /**
     * 分片上传的etag对象，如果该值不存在
     * 会再请求一次接口，获取相应的etag，再进行合并操作
     */
    private List<PartEtag> partEtagList = new ArrayList<>();


    public UploadCompleteMultipartRequest(String bucketName, String objectKey) {
        super(bucketName, objectKey);
    }

    public String getUploadId() {
        return uploadId;
    }

    public void setUploadId(String uploadId) {
        this.uploadId = uploadId;
    }

    public List<PartEtag> getPartEtagList() {
        return partEtagList;
    }

    public void setPartEtagList(List<PartEtag> partEtagList) {
        this.partEtagList = partEtagList;
    }

    @Override
    public String toString() {
        return "CompleteMultipartFileResourceRequest{" +
                "uploadId='" + uploadId + '\'' +
                ", partEtagList=" + partEtagList +
                '}';
    }
}
