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
 * 上传结果实体
 * </p>
 *
 * @author fuhouyu
 * @since 2024/8/16 17:27
 */
@ToString
@Getter
@Setter
public class PutResourceResult extends BaseResourceResult {

    /**
     * etag
     */
    private String etag;

    /**
     * 文件版本id
     */
    private String versionId;

    /**
     * 构造函数
     */
    public PutResourceResult() {
    }


    /**
     * 构造函数
     *
     * @param bucketName 桶名
     * @param objectKey  对象key
     * @param etag       etag
     */
    public PutResourceResult(String bucketName, String objectKey, String etag) {
        super(bucketName, objectKey);
        this.etag = etag;
    }

    /**
     * 构造函数
     *
     * @param etag      eag
     * @param versionId 版本id
     */
    public PutResourceResult(String etag, String versionId) {
        this.etag = etag;
        this.versionId = versionId;
    }

    /**
     * 构造函数
     *
     * @param bucketName 桶名
     * @param objectKey  对象key
     * @param etag       etag
     * @param versionId  版本id
     */
    public PutResourceResult(String bucketName, String objectKey, String etag, String versionId) {
        super(bucketName, objectKey);
        this.etag = etag;
        this.versionId = versionId;
    }

}
