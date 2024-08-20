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

import com.fuhouyu.framework.resource.constants.FileResourceMetadataConstant;
import com.fuhouyu.framework.resource.exception.ResourceException;
import com.fuhouyu.framework.resource.model.*;
import com.fuhouyu.framework.resource.service.ResourceService;
import com.fuhouyu.framework.service.Callback;
import com.fuhouyu.framework.utils.FileUtil;
import com.fuhouyu.framework.utils.LoggerUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.stream.Stream;

/**
 * <p>
 * 本地文件上传下载管理的实现类
 * </p>
 *
 * @author fuhouyu
 * @since 2024/8/18 16:55
 */
public class LocalFileServiceImpl implements ResourceService {

    private static final Logger LOGGER = LoggerFactory.getLogger(LocalFileServiceImpl.class);

    private static final int DEFAULT_BYTES_LENGTH = 8192;

    private static final String TMP_DIRECT = "/tmp";

    private static final String SPACER = ":";

    private static final MessageDigest MESSAGE_DIGEST;

    static {
        MESSAGE_DIGEST = initMessageDigest();
    }

    /**
     * 初始化md5摘要算法
     *
     * @return md5摘要算法
     */
    private static MessageDigest initMessageDigest() {
        try {
            return MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public GetResourceResult getFile(GetResourceRequest genericDownloadFileRequest) throws ResourceException {
        String bucketName = genericDownloadFileRequest.getBucketName();
        String objectKey = genericDownloadFileRequest.getObjectKey();
        byte[] bytes = this.doDownloadFile(bucketName, objectKey, genericDownloadFileRequest.getRange());
        GetResourceResult getResourceResult = new GetResourceResult(bucketName, objectKey);
        getResourceResult.setObjectContent(new ByteArrayInputStream(bytes));
        getResourceResult.setResourceMetadata(this.getFileResourceMetadata(Paths.get(bucketName, objectKey)));
        return getResourceResult;
    }

    @Override
    public ResourceMetadata getFile(GetResourceRequest genericDownloadFileRequest, File file) throws ResourceException {
        String bucketName = genericDownloadFileRequest.getBucketName();
        String objectKey = genericDownloadFileRequest.getObjectKey();
        Path sourcePath = Paths.get(bucketName, objectKey);
        try {
            FileUtil.copyFile(sourcePath,
                    file.toPath());
            return this.getFileResourceMetadata(sourcePath);
        } catch (IOException e) {
            throw new ResourceException(e.getMessage(), e);
        }
    }


    @Override
    public DownloadResourceResult downloadFile(DownloadResourceRequest ossDownloadFileRequest) throws ResourceException {
        GetResourceRequest getResourceRequest = new GetResourceRequest(ossDownloadFileRequest.getBucketName(), ossDownloadFileRequest.getObjectKey());
        ResourceMetadata resourceMetadata = this.getFile(getResourceRequest, new File(ossDownloadFileRequest.getDownloadFile()));
        return new DownloadResourceResult(resourceMetadata);
    }

    @Override
    public PutResourceResult uploadFile(PutResourceRequest putResourceRequest) throws ResourceException {
        String objectKey = putResourceRequest.getObjectKey();
        String bucketName = putResourceRequest.getBucketName();
        File file = putResourceRequest.getFile();

        Path path = Paths.get(bucketName, objectKey);

        FileUtil.createDirectorIfNotExists(path.getParent());
        FileUtil.deleteFileIfExists(path);
        try (InputStream inputStream = Objects.nonNull(file) ? new FileInputStream(file) : putResourceRequest.getInputStream()) {
            FileUtil.copyFile(inputStream, path);
            // 设置文件的元数据
            String etag = FileUtil.calculateFileDigest(path, MESSAGE_DIGEST);
            FileUtil.setFileAttribute(path, FileResourceMetadataConstant.ETAG, etag);
            FileUtil.setFileLastModifiedTime(path);

            return new PutResourceResult(bucketName, objectKey, etag);
        } catch (FileNotFoundException e) {
            throw new ResourceException(String.format("%s 文件不存在", file.getAbsolutePath()), e);
        } catch (IOException e) {
            throw new ResourceException(e.getMessage(), e);
        }
    }


    @Override
    public InitiateUploadMultipartResult initiateMultipartUpload(InitiateUploadMultipartRequest request) throws ResourceException {
        String bucketName = request.getBucketName();
        String objectKey = request.getObjectKey();
        String uploadId = UUID.randomUUID().toString().replace("-", "")
                .substring(16);
        Path path = Paths.get(TMP_DIRECT, uploadId);
        FileUtil.createDirectorIfNotExists(path);
        try (ByteArrayInputStream byteArrayInputStream =
                     new ByteArrayInputStream(String.format("%s%s%s", bucketName, SPACER, objectKey).getBytes(StandardCharsets.UTF_8))) {
            // 写入桶和objectKey，后续使用
            FileUtil.writeFile(Paths.get(TMP_DIRECT, uploadId, uploadId),
                    byteArrayInputStream);
        } catch (IOException e) {
            throw new ResourceException(e.getMessage(), e);
        }
        return new InitiateUploadMultipartResult(bucketName, objectKey, uploadId);
    }

    @Override
    public UploadMultipartResult multipartFileUpload(UploadMultipartRequest fileResourceRequest) throws ResourceException {
        String uploadId = fileResourceRequest.getUploadId();
        InputStream inputStream = fileResourceRequest.getInputStream();
        int partNumber = fileResourceRequest.getPartNumber();
        Path tmpUploadFilePath = Paths.get(TMP_DIRECT, uploadId);
        if (!Files.isDirectory(tmpUploadFilePath)) {
            throw new ResourceException("上传id不正确 " + uploadId);
        }
        try (inputStream) {
            Path partFile = Paths.get(TMP_DIRECT, uploadId, String.valueOf(partNumber));
            FileUtil.writeFile(partFile, inputStream);
            String etag = FileUtil.calculateFileDigest(partFile, MESSAGE_DIGEST);

            FileUtil.setFileAttribute(partFile, FileResourceMetadataConstant.ETAG, etag);
            FileUtil.setFileLastModifiedTime(partFile);

            return new UploadMultipartResult(partNumber, fileResourceRequest.getPartSize(), etag);
        } catch (IOException e) {
            throw new ResourceException(e.getMessage(), e);
        }
    }

    @Override
    public UploadCompleteMultipartResult completeMultipartUpload(UploadCompleteMultipartRequest request) throws ResourceException {
        String uploadId = request.getUploadId();
        Path tmpUploadFilePath = Paths.get(TMP_DIRECT, uploadId);
        if (!Files.isDirectory(tmpUploadFilePath)) {
            throw new ResourceException("当前上传的文件分片不存在  " + uploadId);
        }
        String[] bucketNameAndObjectKey;
        try {
            Path bucketNameAndObjectPath = Paths.get(TMP_DIRECT, uploadId, uploadId);
            bucketNameAndObjectKey = new String(Files.readAllBytes(bucketNameAndObjectPath)).split(SPACER);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String bucketName = bucketNameAndObjectKey[0];
        String objectKey = bucketNameAndObjectKey[1];
        Path targetPath = Paths.get(bucketName, objectKey);

        FileUtil.deleteFileIfExists(targetPath);
        FileUtil.createDirectorIfNotExists(targetPath.getParent());
        String etag = this.mergeFile(tmpUploadFilePath, targetPath, uploadId);

        UploadCompleteMultipartResult uploadCompleteMultipartResult
                = new UploadCompleteMultipartResult();
        uploadCompleteMultipartResult.setBucketName(bucketName);
        uploadCompleteMultipartResult.setObjectKey(objectKey);
        uploadCompleteMultipartResult.setEtag(etag);
        return uploadCompleteMultipartResult;
    }


    @Override
    public ListMultipartResult listParts(ListMultipartRequest listMultipartRequest) throws ResourceException {
        String uploadId = listMultipartRequest.getUploadId();
        int maxParts = listMultipartRequest.getMaxParts();
        int partNumberMaker = listMultipartRequest.getPartNumberMaker();
        Path tmpPath = Paths.get(TMP_DIRECT, uploadId);

        ListMultipartResult listMultipartResult = new ListMultipartResult(uploadId);
        List<PartInfoResult> list = new LinkedList<>();
        listMultipartResult.setPartInfoResult(list);
        listMultipartResult.setNextPartNumberMaker(partNumberMaker + maxParts);


        this.listFilePathCallback(tmpPath, uploadId, (paths) ->
                paths.filter(f -> !f.getFileName().endsWith(uploadId))
                        .sorted(Comparator.comparingInt(o -> Integer.parseInt(o.getFileName().toString())))
                        .skip(partNumberMaker)
                        .limit(maxParts).forEach(part -> {
                            try {
                                list.add(this.getFilePartInfoResult(part));
                            } catch (ResourceException e) {
                                throw new IllegalArgumentException(e);
                            }
                        }));
        return listMultipartResult;
    }


    @Override
    public void abortMultipartFileUpload(UploadAbortMultipartRequest uploadAbortMultipartRequest) throws ResourceException {
        String uploadId = uploadAbortMultipartRequest.getUploadId();
        Path tmpPath = Paths.get(TMP_DIRECT, uploadId);
        try {
            FileUtil.deleteDirect(tmpPath);
        } catch (IOException e) {
            throw new ResourceException(e.getMessage(), e);
        }
    }

    @Override
    public boolean doesObjectExist(String bucketName, String objectKey) {
        return Files.exists(Paths.get(bucketName, objectKey));
    }

    @Override
    public ListResourceResult listFiles(ListResourceRequest listFileRequest) throws ResourceException {
        throw new UnsupportedOperationException("这里暂时不做实现");
    }

    @Override
    public CopyResourceResult copyFile(String sourceBucketName, String sourceObjectKey, String destBucketName, String destObjectKey) throws ResourceException {
        try {
            Path targetPath = Paths.get(destBucketName, destBucketName);
            FileUtil.copyFile(Paths.get(sourceBucketName, sourceObjectKey),
                    targetPath);
            String etag = FileUtil.calculateFileDigest(targetPath, MESSAGE_DIGEST);
            FileUtil.setFileAttribute(targetPath,
                    FileResourceMetadataConstant.ETAG,
                    etag);
            Date lastModifiedTime = FileUtil.setFileLastModifiedTime(targetPath);
            CopyResourceResult copyResourceResult = new CopyResourceResult(destBucketName, destObjectKey);
            copyResourceResult.setEtag(etag);
            copyResourceResult.setLastModified(lastModifiedTime);
            return copyResourceResult;
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public void deleteFile(String bucketName, String objectKey) throws ResourceException {
        FileUtil.deleteFileIfExists(Paths.get(bucketName, objectKey));
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
     * 设置文件属性
     *
     * @param path             路径
     * @param resourceMetadata 文件资源元数据
     * @throws IOException io异常
     */
    private void setFileMetadata(Path path, ResourceMetadata resourceMetadata) throws IOException {
        if (Objects.isNull(resourceMetadata)) {
            return;
        }
        FileUtil.setFileAttributeAll(path, resourceMetadata.getUserMetadata());
    }

    /**
     * 从文件中获取元数据返回
     *
     * @param path path
     * @return 资源元数据
     * @throws ResourceException 读取资源元数据异常
     */
    private ResourceMetadata getFileResourceMetadata(Path path) throws ResourceException {
        ResourceMetadata resourceMetadata = new ResourceMetadata();
        Map<String, String> fileAttributes;
        try {
            fileAttributes = FileUtil.readFileAttributes(path);
            resourceMetadata.setHeader(FileResourceMetadataConstant.LAST_MODIFIED, FileUtil.getFileLastModifiedTime(path));
        } catch (IOException e) {
            LoggerUtil.error(LOGGER, "获取资源文件:{} 中的用户自定义元数据错误:{}",
                    path.getFileName(), e.getMessage(), e);
            throw new ResourceException(e.getMessage(), e);
        }
        resourceMetadata.setUserMetadata(fileAttributes);
        return resourceMetadata;
    }

    /**
     * 合并文件，返回文件etag
     *
     * @param tmpUploadFilePath 分片文件路径
     * @param targetPath        目标路径
     * @param uploadId          上传的文件id
     * @return etag
     */
    public String mergeFile(Path tmpUploadFilePath, Path targetPath, String uploadId) throws ResourceException {
        try {
            this.listFilePathCallback(tmpUploadFilePath, uploadId, (paths) -> {
                paths.filter(f -> !f.getFileName().endsWith(uploadId))
                        .sorted(Comparator.comparingInt(o -> Integer.parseInt(o.getFileName().toString())))
                        .forEach(filePath -> {
                            try (FileChannel sourceFileChannel = FileChannel.open(filePath, StandardOpenOption.READ);
                                 FileChannel targetFileChannel = FileChannel.open(targetPath,
                                         StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.APPEND)) {
                                sourceFileChannel.transferTo(0, sourceFileChannel.size(), targetFileChannel);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        });
            });
            FileUtil.deleteDirect(tmpUploadFilePath);
            return FileUtil.calculateFileDigest(targetPath, MESSAGE_DIGEST);
        } catch (IOException e) {
            throw new ResourceException(e.getMessage(), e);
        }
    }

    /**
     * 列出父级路径下的所有分片的文件
     *
     * @param parentPath 父级路径
     * @param uploadId   上传的文件id
     * @return 上传的文件路径
     * @throws ResourceException 资源服务异常
     */
    private void listFilePathCallback(Path parentPath, String uploadId,
                                      Callback<Stream<Path>> filesCallback) throws ResourceException {
        if (!Files.exists(parentPath)) {
            throw new ResourceException("分片文件不存在");
        }
        try (Stream<Path> pathStream = Files.list(parentPath)) {
            filesCallback.call(pathStream);
        } catch (IOException e) {
            throw new ResourceException(e.getMessage(), e);
        }
    }

    /**
     * 获取文件分片详情
     *
     * @param part 分片文件路径
     * @return 分片文件详情
     * @throws ResourceException 资源异常
     */
    private PartInfoResult getFilePartInfoResult(Path part) throws ResourceException {
        Date fileLastModifiedTime;
        String etag;
        try {
            fileLastModifiedTime = FileUtil.getFileLastModifiedTime(part);
            etag = FileUtil.readFileAttribute(part, FileResourceMetadataConstant.ETAG);
        } catch (IOException e) {
            throw new ResourceException(e.getMessage(), e);
        }

        return new PartInfoResult(
                Integer.parseInt(part.getFileName().toString()),
                fileLastModifiedTime,
                etag,
                part.toFile().length()
        );
    }
}
