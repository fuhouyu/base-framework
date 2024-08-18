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

package com.fuhouyu.framework.resource.service.impl;

import com.fuhouyu.framework.resource.exception.ResourceException;
import com.fuhouyu.framework.resource.model.*;
import com.fuhouyu.framework.resource.service.ResourceService;
import com.fuhouyu.framework.utils.FileUtil;
import org.apache.commons.codec.binary.Hex;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;
import java.util.UUID;

/**
 * <p>
 * 本地文件上传下载管理的实现类
 * </p>
 *
 * @author fuhouyu
 * @since 2024/8/18 16:55
 */
public class LocalFileServiceImpl implements ResourceService {

    private static final int DEFAULT_BYTES_LENGTH = 1024 * 1024;

    private static final String TMP_DIRECT = "/tmp";

    @Override
    public GetResourceResult getFile(GetResourceRequest genericDownloadFileRequest) throws ResourceException {
        String bucketName = genericDownloadFileRequest.getBucketName();
        String objectKey = genericDownloadFileRequest.getObjectKey();
        byte[] bytes = this.doDownloadFile(bucketName, objectKey, genericDownloadFileRequest.getRange());
        GetResourceResult getResourceResult = new GetResourceResult(bucketName, objectKey);
        getResourceResult.setObjectContent(new ByteArrayInputStream(bytes));
        return getResourceResult;
    }

    @Override
    public ResourceMetadata getFile(GetResourceRequest genericDownloadFileRequest, File file) throws ResourceException {
        String bucketName = genericDownloadFileRequest.getBucketName();
        String objectKey = genericDownloadFileRequest.getObjectKey();
        try (FileChannel readFileChannel = FileChannel.open(Paths.get(bucketName, objectKey), StandardOpenOption.READ);
             FileChannel writeFileChannel = FileChannel.open(file.toPath(), StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE)) {
            readFileChannel.transferTo(0, readFileChannel.size(), writeFileChannel);
        } catch (IOException e) {
            throw new ResourceException(e.getMessage(), e);
        }
        // 这里先返回一个空的metadata
        return new ResourceMetadata();
    }

    @Override
    public DownloadResourceResult downloadFile(DownloadResourceRequest ossDownloadFileRequest) throws ResourceException {
        GetResourceRequest getResourceRequest = new GetResourceRequest(ossDownloadFileRequest.getBucketName(), ossDownloadFileRequest.getObjectKey());
        this.getFile(getResourceRequest, new File(ossDownloadFileRequest.getDownloadFile()));
        return new DownloadResourceResult(new ResourceMetadata());
    }

    @Override
    public PutResourceResult uploadFile(PutResourceRequest putResourceRequest) throws ResourceException {
        String objectKey = putResourceRequest.getObjectKey();
        String bucketName = putResourceRequest.getBucketName();
        File file = putResourceRequest.getFile();
        byte[] bytes = new byte[DEFAULT_BYTES_LENGTH];
        String etag;
        Path path = Paths.get(bucketName, objectKey);

        FileUtil.createDirectorIfNotExists(path.getParent());
        FileUtil.deleteFileIfExists(path);
        try (InputStream origanlInputStream = Objects.nonNull(file) ? new FileInputStream(file)
                : putResourceRequest.getInputStream();
             FileChannel writeFileChannel = FileChannel.open(path, StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE)) {

            int readLength = bytes.length;
            int totalFileSize = origanlInputStream.available();
            MessageDigest messageDigest = this.getMd5();

            int position = 0;

            while (origanlInputStream.read(bytes) != -1) {
                if (position + readLength > totalFileSize) {
                    readLength = totalFileSize - position;
                }
                int ignored = writeFileChannel.write(ByteBuffer.wrap(bytes, 0, readLength));
                messageDigest.update(bytes, 0,
                        readLength);
                position += readLength;
            }

            etag = Hex.encodeHexString(messageDigest.digest());
        } catch (FileNotFoundException e) {
            throw new ResourceException(
                    String.format("%s 上传的文件不存在", file.getAbsolutePath()), e);
        } catch (IOException e) {
            throw new ResourceException(e.getMessage(), e);
        }
        return new PutResourceResult(bucketName, objectKey, etag);
    }

    @Override
    public InitiateUploadMultipartResult initiateMultipartUpload(InitiateUploadMultipartRequest request) throws ResourceException {
        String bucketName = request.getBucketName();
        String uploadId = UUID.randomUUID().toString().replace("-", "")
                .substring(16);
        Path path = Paths.get(TMP_DIRECT, bucketName, uploadId);
        FileUtil.createDirectorIfNotExists(path);
        return null;
    }

    @Override
    public UploadMultipartResult multipartFileUpload(UploadMultipartRequest fileResourceRequest) throws ResourceException {
        return null;
    }

    @Override
    public UploadCompleteMultipartResult completeMultipartUpload(UploadCompleteMultipartRequest request) throws ResourceException {
        return null;
    }

    @Override
    public ListMultipartsResult listParts(ListMultipartsRequest listMultipartRequest) throws ResourceException {
        return null;
    }

    @Override
    public void abortMultipartFileUpload(UploadAbortMultipartRequest uploadAbortMultipartRequest) throws ResourceException {

    }

    @Override
    public boolean doesObjectExist(String bucketName, String objectKey) {
        return false;
    }

    @Override
    public ListResourceResult listFiles(ListResourceRequest listFileRequest) throws ResourceException {
        return null;
    }

    @Override
    public CopyResourceResult copyFile(String sourceBucketName, String sourceObjectKey, String destBucketName, String destObjectKey) throws ResourceException {
        return null;
    }

    @Override
    public void deleteFile(String bucketName, String objectKey) throws ResourceException {

    }


    /**
     * 执行文件下载
     *
     * @param bucketName 桶名
     * @param objectKey  对象key
     * @param ranges     是否范围下载，如果为空，则下载所有的文件
     * @return 文件下载的字节数组
     * @throws ResourceException 资源异常
     */
    private byte[] doDownloadFile(String bucketName,
                                  String objectKey,
                                  long[] ranges) throws ResourceException {

        try (FileChannel fileChannel = FileChannel.open(Paths.get(bucketName, objectKey), StandardOpenOption.READ)) {
            if (Objects.nonNull(ranges)) {
                return this.rangesDownload(ranges, fileChannel);
            }
            ByteBuffer byteBuffer = ByteBuffer.allocate(DEFAULT_BYTES_LENGTH);
            byte[] bytes = new byte[(int) fileChannel.size()];
            this.doReadFileToBytes(fileChannel, byteBuffer, bytes);
            return bytes;
        } catch (IOException e) {
            throw new ResourceException(e.getMessage(), e);
        }
    }

    /**
     * 范围下载
     *
     * @param ranges      范围下载的数组对象
     * @param fileChannel 文件通道
     * @return 字节数组
     * @throws IOException io异常
     */
    private byte[] rangesDownload(long[] ranges,
                                  FileChannel fileChannel) throws IOException {
        // 读取的总大小
        long totalSize = ranges[1] - ranges[0];
        if (totalSize < 0) {
            throw new IllegalArgumentException("文件资源范围下载取值不正常");
        }

        ByteBuffer byteBuffer = ByteBuffer.allocate((int) totalSize);
        byte[] bytes = new byte[(int) totalSize];
        this.doReadFileToBytes(fileChannel, byteBuffer, bytes);
        return bytes;
    }

    /**
     * 读取文件字节流
     *
     * @param fileChannel 文件通道
     * @param byteBuffer  byteBuffer
     * @param bytes       字节数组
     * @throws IOException io异常
     */
    private void doReadFileToBytes(FileChannel fileChannel,
                                   ByteBuffer byteBuffer,
                                   byte[] bytes) throws IOException {
        int position = 0;
        int readLength = byteBuffer.capacity();
        int totalFileSize = (int) fileChannel.size();
        while ((fileChannel.read(byteBuffer)) > 0) {
            byteBuffer.flip();
            if (position + readLength > totalFileSize) {
                readLength = totalFileSize - position;
            }
            byteBuffer.get(bytes, Math.min(position, totalFileSize),
                    readLength);
            position += readLength;
            byteBuffer.clear();
        }
    }

    /**
     * 获取md5
     *
     * @return 摘要算法
     */
    private MessageDigest getMd5() {
        try {
            return MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
