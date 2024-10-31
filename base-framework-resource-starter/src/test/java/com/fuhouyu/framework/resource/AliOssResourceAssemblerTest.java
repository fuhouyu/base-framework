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

import com.aliyun.oss.model.ListObjectsV2Request;
import com.aliyun.oss.model.PutObjectRequest;
import com.fuhouyu.framework.resource.assembler.AliOssResourceAssembler;
import com.fuhouyu.framework.resource.model.ListResourceRequest;
import com.fuhouyu.framework.resource.model.PutResourceRequest;
import com.fuhouyu.framework.resource.model.ResourceMetadata;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;

/**
 * <p>
 * 阿里云转换测试
 * </p>
 *
 * @author fuhouyu
 * @since 2024/10/31 21:11
 */
class AliOssResourceAssemblerTest {

    public static final AliOssResourceAssembler ASSEMBLER = AliOssResourceAssembler.INSTANCE;

    private static final String BUCKET = "bucket";

    private static final String OBJECT_KEY = "object";

    @Test
    void testListResource2ListObjectsRequest() {
        ListResourceRequest listResourceRequest = new ListResourceRequest(BUCKET, OBJECT_KEY);
        listResourceRequest.setPrefix("prefix");
        listResourceRequest.setDelimiter("delimiter");
        listResourceRequest.setEncodingType("encodingType");
        listResourceRequest.setMaxKeys(1);
        listResourceRequest.setNextMarker("nextMarker");
        listResourceRequest.setStartAfter("startAfter");
        ListObjectsV2Request ossListObjectsRequest = ASSEMBLER.toOssListObjectsRequest(listResourceRequest);

        Assertions.assertEquals(BUCKET, ossListObjectsRequest.getBucketName());
        Assertions.assertEquals(listResourceRequest.getObjectKey(), ossListObjectsRequest.getKey());
        Assertions.assertEquals(listResourceRequest.getPrefix(), ossListObjectsRequest.getPrefix());
        Assertions.assertEquals(listResourceRequest.getDelimiter(), ossListObjectsRequest.getDelimiter());
        Assertions.assertEquals(listResourceRequest.getEncodingType(), ossListObjectsRequest.getEncodingType());
        Assertions.assertEquals(listResourceRequest.getMaxKeys(), ossListObjectsRequest.getMaxKeys());
        Assertions.assertEquals(listResourceRequest.getStartAfter(), ossListObjectsRequest.getStartAfter());
    }

    @Test
    void testOssPUtObjectRequest() {
        ResourceMetadata resourceMetadata = new ResourceMetadata();
        resourceMetadata.setHeader("test", "test");
        PutResourceRequest putResourceRequest = new PutResourceRequest(BUCKET, OBJECT_KEY, new File(""), resourceMetadata);
        PutObjectRequest ossPutObjectRequest = ASSEMBLER.toOssPutObjectRequest(putResourceRequest);

        Assertions.assertEquals(BUCKET, ossPutObjectRequest.getBucketName());
        Assertions.assertEquals(OBJECT_KEY, ossPutObjectRequest.getKey());
        Assertions.assertEquals(resourceMetadata.getMetadata().get("test"), ossPutObjectRequest.getMetadata().getRawMetadata().get("test"));
    }
}
