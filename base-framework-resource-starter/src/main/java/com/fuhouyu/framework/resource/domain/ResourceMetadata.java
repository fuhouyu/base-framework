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

package com.fuhouyu.framework.resource.domain;

import java.io.Serializable;
import java.util.Map;
import java.util.TreeMap;

/**
 * <p>
 * 文件资源的元数据信息
 * </p>
 *
 * @author fuhouyu
 * @since 2024/8/16 15:42
 */
public class ResourceMetadata implements Serializable {

    /**
     * http规范请求头元数据
     */
    protected Map<String, Object> metadata = new TreeMap<String, Object>(String.CASE_INSENSITIVE_ORDER);

    /**
     * 用户自定义的元数据信息
     */
    private Map<String, String> userMetadata = new TreeMap<String, String>(String.CASE_INSENSITIVE_ORDER);

    /**
     * 添加自定义元数据
     *
     * @param key   元数据key
     * @param value 元数据value
     */
    public void addUserMetadata(String key, String value) {
        userMetadata.put(key, value);
    }

    /**
     * 添加所有的元数据
     *
     * @param userMetadata 用户元数据
     */
    public void addUserMetadataAll(Map<String, String> userMetadata) {
        this.userMetadata.clear();
        if (userMetadata != null && !userMetadata.isEmpty()) {
            this.userMetadata.putAll(userMetadata);
        }
    }


    /**
     * 添加标准的请求头信息
     *
     * @param key   key
     * @param value value
     */
    public void setHeader(String key, Object value) {
        this.metadata.put(key, value);
    }


    /**
     * 添加所有的请求头
     *
     * @param headers 请求头数据
     */
    public void addHeaderAll(Map<String, Object> headers) {
        this.userMetadata.clear();
        if (headers != null && !headers.isEmpty()) {
            this.metadata.putAll(headers);
        }
    }


    public Map<String, String> getUserMetadata() {
        return userMetadata;
    }

    public void setUserMetadata(Map<String, String> userMetadata) {
        this.userMetadata = userMetadata;
    }

    public Map<String, Object> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, Object> metadata) {
        this.metadata = metadata;
    }
}
