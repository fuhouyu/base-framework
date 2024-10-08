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

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

/**
 * <p>
 * 文件下载结果实体
 * </p>
 *
 * @author fuhouyu
 * @since 2024/8/16 18:49
 */
@ToString
@Getter
@Setter
public class GetResourceResult extends BaseResourceRequest implements Closeable {

    private InputStream objectContent;

    private ResourceMetadata resourceMetadata;

    /**
     * 构造函数
     *
     * @param bucketName 桶名
     * @param objectKey  对象key
     */
    public GetResourceResult(String bucketName, String objectKey) {
        super(bucketName, objectKey);
    }

    @Override
    public void close() throws IOException {
        if (Objects.nonNull(objectContent)) {
            objectContent.close();
        }
    }
}
