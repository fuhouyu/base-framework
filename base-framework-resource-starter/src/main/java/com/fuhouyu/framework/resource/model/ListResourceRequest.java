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

/**
 * <p>
 * 列出文件资源的请求实体
 * </p>
 *
 * @author fuhouyu
 * @since 2024/8/16 18:05
 */
public class ListResourceRequest extends BaseResourceRequest {

    private static final int MAX_LENGTH = 1000;

    /**
     * 查询前缀.
     */
    private String prefix;

    /**
     * 此次列举文件的起点.
     */
    private String startAfter;

    /**
     * 列举文件的最大个数.
     */
    private int maxKeys;

    /**
     * 对文件名称进行分组的字符.
     */
    private String delimiter;

    /**
     * 指明列举文件是否被截断。
     * 列举完没有截断，返回值为false.
     * 没列举完就有截断，返回值为true.
     */
    private boolean truncated;

    /**
     * 指明返回结果中编码使用的类型.
     */
    private String encodingType;

    /**
     * 下一个标记的位置
     */
    private String nextMarker;

    public ListResourceRequest(String bucketName, String prefix) {
        super(bucketName);
        this.maxKeys = MAX_LENGTH;
        this.prefix = prefix;
    }

    public ListResourceRequest(String bucketName) {
        super(bucketName);
        this.maxKeys = MAX_LENGTH;
    }


    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getStartAfter() {
        return startAfter;
    }

    public void setStartAfter(String startAfter) {
        this.startAfter = startAfter;
    }

    public int getMaxKeys() {
        return maxKeys;
    }

    public void setMaxKeys(int maxKeys) {
        this.maxKeys = maxKeys;
    }

    public String getDelimiter() {
        return delimiter;
    }

    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }

    public boolean isTruncated() {
        return truncated;
    }

    public void setTruncated(boolean truncated) {
        this.truncated = truncated;
    }

    public String getEncodingType() {
        return encodingType;
    }

    public void setEncodingType(String encodingType) {
        this.encodingType = encodingType;
    }

    public String getNextMarker() {
        return nextMarker;
    }

    public void setNextMarker(String nextMarker) {
        this.nextMarker = nextMarker;
    }

    @Override
    public String toString() {
        return "ListFileResourceRequest{" +
                "prefix='" + prefix + '\'' +
                ", startAfter='" + startAfter + '\'' +
                ", maxKeys=" + maxKeys +
                ", delimiter='" + delimiter + '\'' +
                ", truncated=" + truncated +
                ", encodingType='" + encodingType + '\'' +
                ", nextMarker='" + nextMarker + '\'' +
                '}';
    }
}