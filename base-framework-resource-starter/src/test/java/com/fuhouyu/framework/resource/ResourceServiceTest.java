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

package com.fuhouyu.framework.resource;


import com.fuhouyu.framework.resource.exception.ResourceException;
import com.fuhouyu.framework.resource.model.*;
import com.fuhouyu.framework.resource.service.ResourceService;
import com.fuhouyu.framework.utils.FileUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.util.Assert;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

/**
 * <p>
 * 资源测试
 * </p>
 *
 * @author fuhouyu
 * @since 2024/8/16 20:24
 */
@SpringBootTest(classes = {
//        ResourceAutoConfigure.class,
        LocalFileResourceAutoConfiguration.class
})
@TestPropertySource(locations = {"classpath:application.yaml"})
class ResourceServiceTest {

    private static final String BUCKET_NAME = "/tmp";
    private static final String LOCAL_FILE_PARENT = "./";
    private static final InputStream FILE_INPUT_STREAM =
            new ByteArrayInputStream("test_file".getBytes(StandardCharsets.UTF_8));
    private final String objectKey = "text.txt";

    @Autowired
    private ResourceService resourceService;

    @Test
    void testResourceUpload() throws ResourceException {
        PutResourceResult putResourceResult =
                resourceService.uploadFile(new PutResourceRequest(BUCKET_NAME, objectKey, FILE_INPUT_STREAM));
        Assert.notNull(putResourceResult, "文件上传失败");
    }


    @Test
    void testResourceDownload() throws ResourceException {
        String filePath = LOCAL_FILE_PARENT + "resource_download.xml";
        DownloadResourceResult downloadResourceResult =
                resourceService.downloadFile(new DownloadResourceRequest("./", "pom.xml", filePath, 1000));
        Assert.notNull(downloadResourceResult, "文件下载失败");
        FileUtil.deleteFileIfExists(Path.of(filePath));
    }

    @Test
    void testResourceDelete() throws ResourceException {
        resourceService.deleteFile(BUCKET_NAME, objectKey);
        Assert.isTrue((!resourceService.doesObjectExist(BUCKET_NAME, objectKey)), "文件未被删除");
    }

    @Test
    void testGetFile() throws ResourceException {
        GetResourceRequest getResourceRequest = new GetResourceRequest(BUCKET_NAME,
                objectKey);
        GetResourceResult getResourceResult = resourceService.getFile(getResourceRequest);
        InputStream objectContent = getResourceResult.getObjectContent();
        Assert.notNull(objectContent, "资源文件下载失败");
        // 测试下载文件的写入
        String localFilePath = LOCAL_FILE_PARENT + "get_file.txt";
        try (objectContent;
             FileOutputStream fileOutputStream = new FileOutputStream(localFilePath)) {
            fileOutputStream.write(objectContent.readAllBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        FileUtil.deleteFileIfExists(Path.of(localFilePath));
    }

    @Test
    void testUploadFile() throws ResourceException {
        PutResourceRequest putResourceRequest = new PutResourceRequest(BUCKET_NAME, objectKey, FILE_INPUT_STREAM);
        PutResourceResult putResourceResult = this.resourceService.uploadFile(putResourceRequest);
        Assert.notNull(putResourceResult, "返回结果为空");

        GetResourceRequest getResourceRequest = new GetResourceRequest(BUCKET_NAME, objectKey);
        GetResourceResult getResourceResult = resourceService.getFile(getResourceRequest);
        ResourceMetadata resourceMetadata = getResourceResult.getResourceMetadata();
        Assert.notNull(resourceMetadata, "文件元数据为空");
    }


    @Test
    void testInitUploadId() throws ResourceException, IOException {
        String localFilePath = LOCAL_FILE_PARENT + "pom.xml";

        InitiateUploadMultipartRequest initiateUploadMultipartRequest = new InitiateUploadMultipartRequest(BUCKET_NAME,
                objectKey);
        InitiateUploadMultipartResult initiateUploadMultipartResult = resourceService.initiateMultipartUpload(initiateUploadMultipartRequest);
        Assert.notNull(initiateUploadMultipartResult, "初始化上传id 返回的结果为空");

        uploadPartFile(localFilePath, initiateUploadMultipartResult.getUploadId());

        ListMultipartRequest listMultipartRequest = new ListMultipartRequest(BUCKET_NAME);
        listMultipartRequest.setUploadId(initiateUploadMultipartResult.getUploadId());
        ListMultipartResult listMultipartResult = resourceService.listParts(listMultipartRequest);
        Assert.notNull(listMultipartResult, "列出分片 返回的文件不正确");


        UploadCompleteMultipartRequest uploadCompleteMultipartRequest = new UploadCompleteMultipartRequest(BUCKET_NAME);
        uploadCompleteMultipartRequest.setUploadId(initiateUploadMultipartResult.getUploadId());

        resourceService.completeMultipartUpload(uploadCompleteMultipartRequest);
    }


    private void uploadPartFile(String filePath,
                                String uploadId) throws IOException, ResourceException {
        // 单次上传1m
        int length = 1024 * 1024;
        try (RandomAccessFile fileAccess = new RandomAccessFile(filePath, "r")) {
            long fileSize = fileAccess.length();
            long count = fileSize / length;
            int seek = 0;
            for (int i = 0; i <= count; i++) {
                byte[] bytes = new byte[length];
                fileAccess.seek(seek);
                fileAccess.read(bytes);
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
                UploadMultipartRequest uploadMultipartRequest = new UploadMultipartRequest(BUCKET_NAME);
                uploadMultipartRequest.setUploadId(uploadId);
                uploadMultipartRequest.setInputStream(byteArrayInputStream);
                uploadMultipartRequest.setPartNumber(i);
                resourceService.multipartFileUpload(uploadMultipartRequest);
                seek += length;
            }

        }

    }

}
