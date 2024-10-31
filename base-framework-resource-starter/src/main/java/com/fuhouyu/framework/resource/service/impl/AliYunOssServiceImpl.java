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

package com.fuhouyu.framework.resource.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fuhouyu.framework.common.utils.JacksonUtil;
import com.fuhouyu.framework.common.utils.LoggerUtil;
import com.fuhouyu.framework.resource.assembler.AliOssResourceAssembler;
import com.fuhouyu.framework.resource.exception.ResourceException;
import com.fuhouyu.framework.resource.model.*;
import com.fuhouyu.framework.resource.service.ResourceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 阿里云文件上传实现
 * </p>
 *
 * @author fuhouyu
 * @since 2024/8/16 18:35
 */
@RequiredArgsConstructor
@Slf4j
public class AliYunOssServiceImpl implements ResourceService {

    private final OSS ossClient;

    private static final AliOssResourceAssembler ALI_OSS_RESOURCE_ASSEMBLER = AliOssResourceAssembler.INSTANCE;

    @Override
    public GetResourceResult getFile(GetResourceRequest getResourceRequest) throws ResourceException {
        GetObjectRequest getObjectRequest = new GetObjectRequest(
                getResourceRequest.getBucketName(),
                getResourceRequest.getObjectKey());
        long[] range = getResourceRequest.getRange();
        if (Objects.nonNull(range)) {
            getObjectRequest.setRange(range[0], range[1]);
        }
        try {
            OSSObject object = ossClient.getObject(getObjectRequest);
            GetResourceResult fileResult = new GetResourceResult(
                    object.getBucketName(),
                    object.getKey());
            fileResult.setObjectContent(object.getObjectContent());
            fileResult.setResourceMetadata(ALI_OSS_RESOURCE_ASSEMBLER.toResourceMetadata(object.getObjectMetadata()));
            return fileResult;
        } catch (OSSException e) {
            LoggerUtil.error(log, "阿里云oss下载失败，桶名:{}, 下载的对象:{}, 错误信息:{}",
                    getResourceRequest.getBucketName(), getResourceRequest.getObjectKey(),
                    e.getMessage());
            throw new ResourceException(e.getMessage(), e);
        }
    }

    @Override
    public ResourceMetadata getFile(GetResourceRequest request, File file) throws ResourceException {
        GetObjectRequest getObjectRequest = new GetObjectRequest(
                request.getBucketName(),
                request.getObjectKey());
        try {
            ObjectMetadata objectMetadata = ossClient.getObject(getObjectRequest, file);
            return ALI_OSS_RESOURCE_ASSEMBLER.toResourceMetadata(objectMetadata);
        } catch (OSSException e) {
            LoggerUtil.error(log, "阿里云oss下载到文件:{} 失败，桶名:{}, 下载的对象:{}, 错误信息:{} ",
                    file.getAbsolutePath(),
                    request.getBucketName(), request.getObjectKey(), e.getMessage());
            throw new ResourceException(e.getMessage(), e);
        }
    }

    @Override
    public DownloadResourceResult downloadFile(DownloadResourceRequest request) throws ResourceException {
        DownloadFileRequest downloadFileRequest = new DownloadFileRequest(request.getBucketName(),
                request.getObjectKey(),
                request.getDownloadFile(), request.getPartSize(), request.getTaskNum(),
                request.isEnableCheckpoint(), request.getCheckpointFile());
        try {
            DownloadFileResult downloadFileResult = ossClient.downloadFile(downloadFileRequest);
            return new DownloadResourceResult(
                    ALI_OSS_RESOURCE_ASSEMBLER.toResourceMetadata(
                            downloadFileResult.getObjectMetadata()));
        } catch (Throwable e) {
            LoggerUtil.error(log, "阿里云oss资源下载失败，桶名:{}, 下载的对象:{}, 下载请求:{}, 错误信息:{}",
                    request.getBucketName(), request.getObjectKey(),
                    request, e.getMessage());
            throw new ResourceException(e.getMessage(), e);
        }
    }

    @Override
    public PutResourceResult uploadFile(PutResourceRequest putResourceRequest) throws ResourceException {
        PutObjectResult putResourceResult;
        try {
            putResourceResult = ossClient.putObject(
                    ALI_OSS_RESOURCE_ASSEMBLER.toOssPutObjectRequest(putResourceRequest));
        } catch (OSSException e) {
            LoggerUtil.error(log, "oss：{} 文件上传失败:", putResourceRequest, e);
            throw new ResourceException(e.getMessage(), e);
        }
        return new PutResourceResult(putResourceRequest.getBucketName(),
                putResourceRequest.getObjectKey(),
                putResourceResult.getETag(), putResourceRequest.getVersionId());
    }

    @Override
    public InitiateUploadMultipartResult initiateMultipartUpload(InitiateUploadMultipartRequest request) throws ResourceException {
        InitiateMultipartUploadRequest initiateUploadMultipartRequest =
                new InitiateMultipartUploadRequest(request.getBucketName(),
                        request.getObjectKey());
        if (Objects.nonNull(request.getResourceMetadata())) {
            initiateUploadMultipartRequest.setObjectMetadata(
                    ALI_OSS_RESOURCE_ASSEMBLER.toObjectMetadata(request.getResourceMetadata()));
        }
        InitiateMultipartUploadResult initiateMultipartUploadResult;
        try {
            initiateMultipartUploadResult
                    = ossClient.initiateMultipartUpload(
                    initiateUploadMultipartRequest);
        } catch (OSSException e) {
            LoggerUtil.error(log, "oss：{} 初始化分片id失败:", request, e);
            throw new ResourceException(e.getMessage(), e);
        }
        return new InitiateUploadMultipartResult(
                initiateMultipartUploadResult.getBucketName(),
                initiateMultipartUploadResult.getKey(),
                initiateMultipartUploadResult.getUploadId());
    }

    @Override
    public UploadMultipartResult multipartFileUpload(UploadMultipartRequest uploadFileRequest) throws ResourceException {
        String uploadId = uploadFileRequest.getUploadId();
        UploadPartRequest uploadPartRequest = new UploadPartRequest(
                uploadFileRequest.getBucketName(),
                uploadFileRequest.getObjectKey());
        uploadPartRequest.setUploadId(uploadId);
        uploadPartRequest.setInputStream(uploadFileRequest.getInputStream());
        uploadPartRequest.setPartSize(uploadFileRequest.getPartSize());
        uploadPartRequest.setPartNumber(uploadFileRequest.getPartNumber());
        UploadPartResult uploadPartResult;
        try {
            uploadPartResult = ossClient.uploadPart(uploadPartRequest);
        } catch (OSSException e) {
            LoggerUtil.error(log, "oss：{} 分片上传失败:", uploadFileRequest, e);
            throw new ResourceException(e.getMessage(), e);
        }
        return new UploadMultipartResult(uploadFileRequest.getBucketName(),
                uploadFileRequest.getObjectKey(),
                uploadFileRequest.getPartNumber(),
                uploadFileRequest.getPartSize(),
                uploadPartResult.getETag());
    }

    @Override
    public UploadCompleteMultipartResult completeMultipartUpload(UploadCompleteMultipartRequest request) throws ResourceException {
        List<PartEtag> partEtagList = request.getPartEtagList();
        CompleteMultipartUploadRequest completeMultipartUploadRequest =
                new CompleteMultipartUploadRequest(request.getBucketName(),
                        request.getObjectKey(), request.getUploadId(),
                        JacksonUtil.tryParse(() -> Objects.isNull(partEtagList) ? Collections.emptyList()
                                : JacksonUtil.getObjectMapper().convertValue(partEtagList,
                                new TypeReference<ArrayList<PartETag>>() {
                                })));
        CompleteMultipartUploadResult completeMultipartUploadResult;
        try {
            completeMultipartUploadResult =
                    ossClient.completeMultipartUpload(
                            completeMultipartUploadRequest);
        } catch (OSSException e) {
            LoggerUtil.error(log, "oss：{} 分片合并失败:", request, e);
            throw new ResourceException(e.getMessage(), e);
        }
        return new UploadCompleteMultipartResult(
                completeMultipartUploadResult.getBucketName(),
                completeMultipartUploadResult.getKey(),
                completeMultipartUploadResult.getETag(),
                completeMultipartUploadResult.getVersionId());
    }

    @Override
    public ListMultipartResult listParts(ListMultipartRequest listMultipartRequest) throws ResourceException {
        ListPartsRequest listPartsRequest = new ListPartsRequest(
                listMultipartRequest.getBucketName(),
                listMultipartRequest.getObjectKey(), listMultipartRequest.getUploadId());
        PartListing partListing;
        try {
            partListing = ossClient.listParts(listPartsRequest);
        } catch (OSSException e) {
            LoggerUtil.error(log, "oss：{} 列出分片失败:", listPartsRequest, e);
            throw new ResourceException(e.getMessage(), e);
        }
        return ALI_OSS_RESOURCE_ASSEMBLER.toListMultipartResult(partListing);
    }

    @Override
    public void abortMultipartFileUpload(UploadAbortMultipartRequest uploadAbortMultipartRequest) throws ResourceException {
        AbortMultipartUploadRequest abortMultipartUploadRequest;
        try {
            abortMultipartUploadRequest =
                    new AbortMultipartUploadRequest(uploadAbortMultipartRequest.getBucketName(),
                            uploadAbortMultipartRequest.getObjectKey(),
                            uploadAbortMultipartRequest.getUploadId());
        } catch (OSSException e) {
            LoggerUtil.error(log, "oss：{} 取消分片失败:", uploadAbortMultipartRequest, e);
            throw new ResourceException(e.getMessage(), e);
        }
        ossClient.abortMultipartUpload(abortMultipartUploadRequest);
    }

    @Override
    public boolean doesObjectExist(String bucketName, String objectKey) {
        return ossClient.doesObjectExist(bucketName, objectKey);
    }

    @Override
    public ListResourceResult listFiles(ListResourceRequest listFileRequest) throws ResourceException {
        ListObjectsV2Request listObjectsV2Request = ALI_OSS_RESOURCE_ASSEMBLER.toOssListObjectsRequest(listFileRequest);
        ListObjectsV2Result listObjectsV2Result = ossClient.listObjectsV2(listObjectsV2Request);
        List<OSSObjectSummary> objectSummaries = listObjectsV2Result.getObjectSummaries();
        ListResourceResult listFileResponse = new ListResourceResult(listObjectsV2Result.getBucketName(),
                JacksonUtil.tryParse(() ->
                        Objects.isNull(objectSummaries) ? Collections.emptyList() :
                                JacksonUtil.getObjectMapper().convertValue(objectSummaries,
                                        new TypeReference<ArrayList<ResourceSummary>>() {
                                        })));
        listFileResponse.setCommonPrefixes(listObjectsV2Result.getCommonPrefixes());
        listFileResponse.setTruncated(listObjectsV2Result.isTruncated());
        listFileResponse.setPrefix(listObjectsV2Result.getPrefix());
        listFileResponse.setStartAfter(listObjectsV2Result.getStartAfter());
        listFileResponse.setMaxKeys(listObjectsV2Result.getMaxKeys());
        listFileResponse.setDelimiter(listObjectsV2Result.getDelimiter());
        listFileResponse.setNextMarker(listObjectsV2Result.getNextContinuationToken());
        return listFileResponse;
    }

    @Override
    public CopyResourceResult copyFile(String sourceBucketName, String sourceObjectKey, String destBucketName, String destObjectKey)
            throws ResourceException {
        CopyObjectResult copyObjectResult;
        try {
            copyObjectResult = this.ossClient.copyObject(sourceBucketName, sourceObjectKey,
                    destBucketName, destObjectKey);
        } catch (OSSException e) {
            LoggerUtil.error(log, "oss 复制资源失败, 源桶:{}, 源文件:{}, 目标桶:{}, 目标文件:{}",
                    sourceBucketName, sourceObjectKey, destBucketName, destObjectKey, e);
            throw new ResourceException(e.getMessage(), e);
        }
        CopyResourceResult copyFileResponse = new CopyResourceResult(destBucketName, destObjectKey);
        copyFileResponse.setEtag(copyObjectResult.getETag());
        copyFileResponse.setLastModified(copyObjectResult.getLastModified());
        return copyFileResponse;
    }

    @Override
    public void deleteFile(String bucketName, String objectKey) throws ResourceException {
        try {
            this.ossClient.deleteObject(bucketName, objectKey);
        } catch (OSSException e) {
            LoggerUtil.error(log, "阿里云删除桶 {} 文件 {} 失败",
                    bucketName, objectKey, e);
            throw new ResourceException(e.getMessage(), e);
        }
    }




}
