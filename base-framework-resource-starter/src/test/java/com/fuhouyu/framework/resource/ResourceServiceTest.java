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

package com.fuhouyu.framework.resource;


import com.fuhouyu.framework.resource.exception.ResourceException;
import com.fuhouyu.framework.resource.model.*;
import com.fuhouyu.framework.resource.service.ResourceService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.util.Assert;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * <p>
 * 资源测试
 * </p>
 *
 * @author fuhouyu
 * @since 2024/8/16 20:24
 */
@SpringBootTest(classes = {
//        ResourceAutoConfigure.class
        LocalFileResourceAutoConfigure.class
})
@TestPropertySource(locations = {"classpath:application.yaml"})
@EnabledIfSystemProperty(named = "run.tests", matches = "true")
class ResourceServiceTest {

    private static final String BUCKET_NAME = "resources-bucketss";

    private static final String OBJECT_KEY = "docker-compose.yml";

    private static final String LOCAL_FILE_PARENT = "/Users/fuhouyu/Documents";

    @Autowired
    private ResourceService resourceService;

    @Test
    void testResourceUpload() throws ResourceException {
        File file = new File("/Users/fuhouyu/Downloads/docker-compose.yml");
        PutResourceResult putResourceResult =
                resourceService.uploadFile(new PutResourceRequest(BUCKET_NAME, OBJECT_KEY, file));
        Assert.notNull(putResourceResult, "文件上传失败");
    }


    @Test
    void testResourceDownload() throws ResourceException {
        File file = new File("/Users/fuhouyu/Downloads/docker-compose.ymlbak");
        DownloadResourceResult downloadResourceResult =
                resourceService.downloadFile(new DownloadResourceRequest(BUCKET_NAME, OBJECT_KEY, file.getAbsolutePath(), 1000));
        Assert.notNull(downloadResourceResult, "文件下载失败");
    }

    @Test
    void testResourceDelete() throws ResourceException {
        resourceService.deleteFile(BUCKET_NAME, OBJECT_KEY);
        Assert.isTrue((!resourceService.doesObjectExist(BUCKET_NAME, OBJECT_KEY)), "文件未被删除");
    }

    @Test
    void testGetFile() throws ResourceException {
        GetResourceRequest getResourceRequest = new GetResourceRequest(LOCAL_FILE_PARENT,
                "2024-7-11-23-03-31-EFI.zip");
        GetResourceResult getResourceResult = resourceService.getFile(getResourceRequest);
        InputStream objectContent = getResourceResult.getObjectContent();
        Assert.notNull(objectContent, "资源文件下载失败");
        // 测试下载文件的写入
        try (objectContent;
             FileOutputStream fileOutputStream = new FileOutputStream("/Users/fuhouyu/Downloads/2024-7-11-23-03-31-EFI.zipbak")) {
            fileOutputStream.write(objectContent.readAllBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        ResourceMetadata resourceMetadata = resourceService.getFile(getResourceRequest, new File("/Users/fuhouyu/Downloads/2024-7-11-23-03-31-EFI.zipbak2"));
    }

    @Test
    void testUploadFile() throws ResourceException {
        PutResourceRequest putResourceRequest = new PutResourceRequest("/Users/fuhouyu/Downloads/", "tmp/1.zip", new File(LOCAL_FILE_PARENT + "/2024-7-11-23-03-31-EFI.zip"));
        PutResourceResult putResourceResult = this.resourceService.uploadFile(putResourceRequest);
        System.out.println(putResourceResult);
    }
}
