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

package com.fuhouyu.framework.resource.service;

import com.fuhouyu.framework.resource.exception.ResourceException;
import com.fuhouyu.framework.resource.model.*;

import java.io.File;

/**
 * <p>
 * 文件资源接口，提供文件的上传/下载等方法
 * </p>
 *
 * @author fuhouyu
 * @since 2024/8/16 12:07
 */
public interface ResourceService {

    /**
     * 文件下载.
     * 如果设置了range参数，将会进行范围下载
     *
     * @param genericDownloadFileRequest 通用的文件下载请求.
     * @throws ResourceException 资源异常
     * @return 文件资源下载结果
     */
    GetResourceResult getFile(GetResourceRequest genericDownloadFileRequest) throws ResourceException;


    /**
     * 下载文件到本地.
     *
     * @param genericDownloadFileRequest 通用的下载请求.
     * @param file                       文件保存的路径.
     * @throws ResourceException 资源异常
     * @return 资源元数据
     */
    ResourceMetadata getFile(GetResourceRequest genericDownloadFileRequest, File file) throws ResourceException;

    /**
     * 文件下载.
     *
     * @param ossDownloadFileRequest 文件下载请求.
     * @return oss文件下载的响应.
     * @throws ResourceException 资源异常
     */
    DownloadResourceResult downloadFile(DownloadResourceRequest ossDownloadFileRequest) throws ResourceException;

    /**
     * 文件上传.
     *
     * @param putResourceRequest 文件上传请求对象.
     * @return 文件上传响应.
     * @throws ResourceException 资源异常
     */
    PutResourceResult uploadFile(PutResourceRequest putResourceRequest) throws ResourceException;

    /**
     * 初始化分片上传请求的id.
     *
     * @param request 分片上传的请求.
     * @return 分片上传的响应结果.
     * @throws ResourceException 资源异常
     */
    InitiateUploadMultipartResult initiateMultipartUpload(
            InitiateUploadMultipartRequest request) throws ResourceException;

    /**
     * 文件分片上传.
     *
     * @param fileResourceRequest 文件分片上传.
     * @return 分片响应.
     * @throws ResourceException 资源异常
     */
    UploadMultipartResult multipartFileUpload(UploadMultipartRequest fileResourceRequest) throws ResourceException;

    /**
     * 完成分片上传后的合并.
     *
     * @param request 请求.
     * @return 分片上传后的响应.
     * @throws ResourceException 资源异常
     */
    UploadCompleteMultipartResult completeMultipartUpload(
            UploadCompleteMultipartRequest request) throws ResourceException;

    /**
     * 列举分片上传接口.
     *
     * @param listMultipartRequest 列举分片上传的请求.
     * @return 分片上传的响应.
     * @throws ResourceException 资源异常
     */
    ListMultipartResult listParts(ListMultipartRequest listMultipartRequest) throws ResourceException;

    /**
     * 取消分片上传.
     *
     * @param uploadAbortMultipartRequest 取消分片上传的请求类.
     * @throws ResourceException 资源异常
     */
    void abortMultipartFileUpload(UploadAbortMultipartRequest uploadAbortMultipartRequest) throws ResourceException;


    /**
     * 判断当前对象是否存在
     * 不存在则返回false.
     *
     * @param bucketName 桶名.
     * @param objectKey  对象名.
     * @return 布尔值 true/false.
     */
    boolean doesObjectExist(String bucketName,
                            String objectKey);


    /**
     * 列举文件下载
     *
     * @param listFileRequest 文件请求.
     * @return 文件列表.
     * @throws ResourceException 资源异常
     */
    ListResourceResult listFiles(ListResourceRequest listFileRequest) throws ResourceException;


    /**
     * 复制文件.
     *
     * @param sourceBucketName 源树名.
     * @param sourceObjectKey  源对象名.
     * @param destBucketName   目标桶名.
     * @param destObjectKey    目标对象名.
     * @return 复制对象的响应.
     * @throws ResourceException 资源异常
     */
    CopyResourceResult copyFile(String sourceBucketName,
                                String sourceObjectKey,
                                String destBucketName,
                                String destObjectKey) throws ResourceException;


    /**
     * 删除文件.
     *
     * @param bucketName 桶名.
     * @param objectKey  对象名.
     * @throws ResourceException 资源异常
     */
    void deleteFile(String bucketName,
                    String objectKey) throws ResourceException;
}
