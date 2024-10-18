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

import java.io.File;
import java.io.InputStream;

/**
 * <p>
 * 文件上传实体
 * </p>
 *
 * @author fuhouyu
 * @since 2024/8/16 17:22
 */
@ToString
@Getter
@Setter
public class PutResourceRequest extends BaseResourceRequest {

    /**
     * 需要上传的文件
     */
    private File file;

    /**
     * 文件流
     */
    private InputStream inputStream;

    /**
     * 文件元数据
     */
    private ResourceMetadata metadata;

    /**
     * 构造函数
     *
     * @param bucketName 桶名
     * @param key        对象key
     * @param file       需要上传的文件
     */
    public PutResourceRequest(String bucketName, String key, File file) {
        this(bucketName, key, file, null);
    }

    /**
     * 构造函数
     * @param bucketName 桶名
     * @param key 对象key
     * @param file 需要上传的文件
     * @param metadata 元数据
     */
    public PutResourceRequest(String bucketName, String key, File file, ResourceMetadata metadata) {
        super(bucketName, key);
        this.file = file;
        this.metadata = metadata;
    }

    /**
     * 构造函数
     * @param bucketName 桶名
     * @param key 对象key
     * @param input 需要上传的流文件
     */
    public PutResourceRequest(String bucketName, String key, InputStream input) {
        this(bucketName, key, input, null);
    }

    /**
     * 构造函数
     * @param bucketName 桶名
     * @param key 对象key
     * @param input 输入流
     * @param metadata 文件元数据
     */
    public PutResourceRequest(String bucketName, String key, InputStream input, ResourceMetadata metadata) {
        super(bucketName, key);
        this.inputStream = input;
        this.metadata = metadata;
    }

}
