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


import com.fuhouyu.framework.common.utils.FileUtil;
import com.fuhouyu.framework.resource.exception.ResourceException;
import com.fuhouyu.framework.resource.model.*;
import com.fuhouyu.framework.resource.service.ResourceService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

/**
 * <p>
 * 资源测试
 * </p>
 *
 * @author fuhouyu
 * @since 2024/8/16 20:24
 */
@SpringBootTest(classes = {
        ResourceAutoConfiguration.class,
//        LocalFileResourceConfiguration.class
})
@TestPropertySource(locations = {"classpath:application.yaml"})
class ResourceServiceTest {

    private static final String BUCKET_NAME = "test/upload";

    private static final String DOWNLOAD_PATH
            = System.getProperty("java.io.tmpdir") + File.separator + "download";

    private static final InputStream FILE_INPUT_STREAM =
            new ByteArrayInputStream(UUID.randomUUID().toString().getBytes(StandardCharsets.UTF_8));

    private static final String OBJECT_KEY = "text.txt";

    @Autowired
    private ResourceService resourceService;


    @BeforeAll
    static void setUp() {
        FileUtil.createDirectorIfNotExists(Paths.get(BUCKET_NAME));
        FileUtil.createDirectorIfNotExists(Paths.get(DOWNLOAD_PATH));
    }

    @Test
    void testResourceUpload() throws ResourceException {
        PutResourceResult putResourceResult =
                resourceService.uploadFile(new PutResourceRequest(BUCKET_NAME, OBJECT_KEY, FILE_INPUT_STREAM));
        Assertions.assertNotNull(putResourceResult, "文件上传失败");
    }


    @Test
    void testResourceDownload() throws ResourceException {
        String filePath = DOWNLOAD_PATH + File.separator +
                UUID.randomUUID() + ".txt";
        DownloadResourceResult downloadResourceResult =
                resourceService.downloadFile(new DownloadResourceRequest(BUCKET_NAME, OBJECT_KEY, filePath, 1000));
        Assertions.assertNotNull(downloadResourceResult, "文件下载失败");
    }

    @Test
    void testGetFile() throws ResourceException {
        GetResourceRequest getResourceRequest = new GetResourceRequest(BUCKET_NAME,
                OBJECT_KEY);
        GetResourceResult getResourceResult = resourceService.getFile(getResourceRequest);
        InputStream objectContent = getResourceResult.getObjectContent();
        Assertions.assertNotNull(objectContent, "资源文件下载失败");
        // 测试下载文件的写入
        String localFilePath = DOWNLOAD_PATH + File.separator + UUID.randomUUID() + ".txt";
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
        PutResourceRequest putResourceRequest = new PutResourceRequest(BUCKET_NAME, OBJECT_KEY, FILE_INPUT_STREAM);
        PutResourceResult putResourceResult = this.resourceService.uploadFile(putResourceRequest);
        Assertions.assertNotNull(putResourceResult, "返回结果为空");

        GetResourceRequest getResourceRequest = new GetResourceRequest(BUCKET_NAME, OBJECT_KEY);
        GetResourceResult getResourceResult = resourceService.getFile(getResourceRequest);
        ResourceMetadata resourceMetadata = getResourceResult.getResourceMetadata();
        Assertions.assertNotNull(resourceMetadata, "文件元数据为空");
    }


    @Test
    void testInitUploadId() throws ResourceException, IOException {
        String localFilePath = "pom.xml";

        InitiateUploadMultipartRequest initiateUploadMultipartRequest = new InitiateUploadMultipartRequest(BUCKET_NAME,
                OBJECT_KEY);
        InitiateUploadMultipartResult initiateUploadMultipartResult = resourceService.initiateMultipartUpload(initiateUploadMultipartRequest);
        Assertions.assertNotNull(initiateUploadMultipartResult, "初始化上传id 返回的结果为空");

        uploadPartFile(localFilePath, initiateUploadMultipartResult.getUploadId());

        ListMultipartRequest listMultipartRequest = new ListMultipartRequest(BUCKET_NAME);
        listMultipartRequest.setUploadId(initiateUploadMultipartResult.getUploadId());
        ListMultipartResult listMultipartResult = resourceService.listParts(listMultipartRequest);
        Assertions.assertNotNull(listMultipartResult, "列出分片 返回的文件不正确");


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
