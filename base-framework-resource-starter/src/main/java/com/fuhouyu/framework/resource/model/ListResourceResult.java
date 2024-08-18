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

import java.util.List;

/**
 * <p>
 * 列兴的文件资源结果集
 * </p>
 *
 * @author fuhouyu
 * @since 2024/8/16 18:09
 */
public class ListResourceResult extends BaseResourceResult {

    private List<ResourceSummary> resourceSummaryList;

    private List<String> commonPrefixes;

    private boolean truncated;

    private String prefix;

    private String startAfter;

    private int maxKeys;

    private String delimiter;

    private String nextMarker;

    private String location;


    public ListResourceResult(String bucketName,
                              List<ResourceSummary> resourceSummaryList) {
        super();
        super.setBucketName(bucketName);
        this.resourceSummaryList = resourceSummaryList;
    }

    public List<ResourceSummary> getFileResourceSummaryList() {
        return resourceSummaryList;
    }

    public void setFileResourceSummaryList(List<ResourceSummary> resourceSummaryList) {
        this.resourceSummaryList = resourceSummaryList;
    }

    public List<String> getCommonPrefixes() {
        return commonPrefixes;
    }

    public void setCommonPrefixes(List<String> commonPrefixes) {
        this.commonPrefixes = commonPrefixes;
    }

    public boolean isTruncated() {
        return truncated;
    }

    public void setTruncated(boolean truncated) {
        this.truncated = truncated;
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

    public String getNextMarker() {
        return nextMarker;
    }

    public void setNextMarker(String nextMarker) {
        this.nextMarker = nextMarker;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "ListFileResourceResult{" +
                "fileResourceSummaryList=" + resourceSummaryList +
                ", commonPrefixes=" + commonPrefixes +
                ", truncated=" + truncated +
                ", prefix='" + prefix + '\'' +
                ", startAfter='" + startAfter + '\'' +
                ", maxKeys=" + maxKeys +
                ", delimiter='" + delimiter + '\'' +
                ", nextMarker='" + nextMarker + '\'' +
                ", location='" + location + '\'' +
                '}';
    }
}
