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
 * 分片的etag对象
 * </p>
 *
 * @author fuhouyu
 * @since 2024/8/16 17:46
 */
@ToString
@Getter
@Setter
public class PartEtag {

    /**
     * 分片数量.
     */
    private final int partNumber;


    /**
     * 分片大小.
     */
    private long partSize;

    /**
     * etag.
     */
    private final String etag;

    /**
     * 构造函数
     *
     * @param partNumber 页号
     * @param etag       etag
     */
    public PartEtag(int partNumber, String etag) {
        this(partNumber, -1, etag);
    }

    /**
     * 构造函数
     * @param partNumber 页号
     * @param partSize  大小
     * @param etag etag
     */
    public PartEtag(int partNumber, long partSize, String etag) {
        this.partNumber = partNumber;
        this.partSize = partSize;
        this.etag = etag;
    }

}
