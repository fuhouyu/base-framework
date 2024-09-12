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

import java.util.List;

/**
 * <p>
 * 文件资源分片返回结果实体
 * </p>
 *
 * @author fuhouyu
 * @since 2024/8/16 17:57
 */
public class ListMultipartResult extends BaseResourceResult {

    private boolean truncated;

    private int nextPartNumberMaker;

    private String uploadId;

    private List<PartInfoResult> partInfoResult;


    public ListMultipartResult(String uploadId) {
        this.uploadId = uploadId;
    }


    public ListMultipartResult(String bucketName, String objectKey,
                               String uploadId) {
        super(bucketName, objectKey);
        this.uploadId = uploadId;
    }

    public boolean isTruncated() {
        return truncated;
    }

    public void setTruncated(boolean truncated) {
        this.truncated = truncated;
    }

    public int getNextPartNumberMaker() {
        return nextPartNumberMaker;
    }

    public void setNextPartNumberMaker(int nextPartNumberMaker) {
        this.nextPartNumberMaker = nextPartNumberMaker;
    }

    public String getUploadId() {
        return uploadId;
    }

    public void setUploadId(String uploadId) {
        this.uploadId = uploadId;
    }

    public List<PartInfoResult> getPartInfoResult() {
        return partInfoResult;
    }

    public void setPartInfoResult(List<PartInfoResult> partInfoResult) {
        this.partInfoResult = partInfoResult;
    }

    @Override
    public String toString() {
        return "FileResourceListPartsResult{" +
                "truncated=" + truncated +
                ", nextPartNumberMaker=" + nextPartNumberMaker +
                ", uploadId='" + uploadId + '\'' +
                ", partInfoResponse=" + partInfoResult +
                '}';
    }
}
