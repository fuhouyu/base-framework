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

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <p>
 * 获取文件的请求实体
 * </p>
 *
 * @author fuhouyu
 * @since 2024/8/16 12:09
 */
@ToString
@Getter
@Setter
public class GetResourceRequest extends BaseResourceRequest {

    /**
     * 流量限制，
     */
    private int trafficLimit;

    /**
     * 下载的范围，如果该值存在，则进行范围下载
     */
    private long[] range;

    public GetResourceRequest(String bucketName, String objectKey) {
        super(bucketName, objectKey);
    }

    public GetResourceRequest(String bucketName, String objectKey, String versionId) {
        super(bucketName, objectKey, versionId);
    }
}
