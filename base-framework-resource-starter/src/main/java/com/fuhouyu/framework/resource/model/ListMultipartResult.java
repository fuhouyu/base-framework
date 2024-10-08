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

import java.util.List;

/**
 * <p>
 * 文件资源分片返回结果实体
 * </p>
 *
 * @author fuhouyu
 * @since 2024/8/16 17:57
 */
@ToString
@Getter
@Setter
public class ListMultipartResult extends BaseResourceResult {

    private Boolean truncated;

    private Integer nextPartNumberMaker;

    private String uploadId;

    private List<PartInfoResult> partInfoResult;


    /**
     * 构造函数
     *
     * @param uploadId 上传id
     */
    public ListMultipartResult(String uploadId) {
        this.uploadId = uploadId;
    }


    /**
     * 构造函数
     * @param bucketName 桶名
     * @param objectKey 对象key
     * @param uploadId 上传id
     */
    public ListMultipartResult(String bucketName, String objectKey,
                               String uploadId) {
        super(bucketName, objectKey);
        this.uploadId = uploadId;
    }

}
