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
 * 文件资源下载的请求类
 * </p>
 *
 * @author fuhouyu
 * @since 2024/8/16 17:11
 */
@ToString
@Getter
@Setter
public class DownloadResourceRequest extends BaseResourceRequest {

    /**
     * 每个分片下载的大小，默认 1mb
     */
    private long partSize = 1024 * 1024L;

    /**
     * 下载的线程数
     */
    private int taskNum = 1;

    /**
     * 文件下载的本地路径
     */
    private String downloadFile;

    /**
     * 下载速度
     */
    private int trafficLimit;

    /**
     * 分段下载
     */
    private long[] range;

    /**
     * 是否启用断点下载
     */
    private boolean enableCheckpoint;

    /**
     * 设置断点记录文件的完整路径，例如D:\\localpath\\examplefile.txt.dcp。
     * 只有当Object下载中断产生了断点记录文件后，如果需要继续下载该Object，才需要设置对应的断点记录文件。下载完成后，该文件会被删除。
     */
    private String checkpointFile;


    public DownloadResourceRequest(String bucketName, String objectKey) {
        super(bucketName, objectKey);
    }


    public DownloadResourceRequest(String bucketName, String objectKey, String downloadFile, long partSize) {
        this(bucketName, objectKey, downloadFile, partSize, 1, false);
    }

    public DownloadResourceRequest(String bucketName, String objectKey, String downloadFile, long partSize,
                                   int taskNum,
                                   boolean enableCheckpoint) {
        this(bucketName, objectKey, downloadFile, partSize, taskNum, enableCheckpoint, null);
    }

    public DownloadResourceRequest(String bucketName, String key, String downloadFile, long partSize, int taskNum, boolean enableCheckpoint,
                                   String checkpointFile) {
        super(bucketName, key);
        this.partSize = partSize;
        this.taskNum = taskNum;
        this.downloadFile = downloadFile;
        this.enableCheckpoint = enableCheckpoint;
        this.checkpointFile = checkpointFile;
    }

}
