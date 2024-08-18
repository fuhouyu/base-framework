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

import java.util.Date;

/**
 * <p>
 * 分片详情结果
 * </p>
 *
 * @author fuhouyu
 * @since 2024/8/16 17:57
 */
public class PartInfoResult {

    private int partNumber;

    private Date lastModified;

    private String etag;

    private long size;

    public PartInfoResult(int partNumber, Date lastModified, String etag, long size) {
        this.partNumber = partNumber;
        this.lastModified = lastModified;
        this.etag = etag;
        this.size = size;
    }

    @Override
    public String toString() {
        return "PartInfoResult{" +
                "partNumber=" + partNumber +
                ", lastModified=" + lastModified +
                ", etag='" + etag + '\'' +
                ", size=" + size +
                '}';
    }
}
