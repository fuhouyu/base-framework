/*
 * Copyright 2024-present fuhouyu.
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
package com.fuhouyu.framework.s3;

import com.fuhouyu.framework.s3.enums.StsActionEnum;
import com.fuhouyu.framework.s3.model.StsTokenResponse;
import com.fuhouyu.framework.s3.properties.S3Properties;
import com.fuhouyu.framework.s3.service.BaseS3ClientService;
import com.fuhouyu.framework.s3.service.StsOperation;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.containers.FixedHostPortGenericContainer;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.InternetProtocol;
import org.testcontainers.containers.wait.strategy.Wait;
import software.amazon.awssdk.auth.credentials.AwsSessionCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 *
 * </p>
 *
 * @author fuhouyu
 * @since 2025/2/9 15:23
 */
@SpringBootTest(classes = {S3AutoConfiguration.class})
@ActiveProfiles("test")
@Slf4j
class S3ClientTest {

    private static final int PORT = 9000;

    private static final String ACCESS_KEY = "test_username";

    private static final String SECRET_KEY = "test_password";
    static final GenericContainer<?> MINIO_CONTAINER =
            new FixedHostPortGenericContainer<>("quay.io/minio/minio")
                    .withFixedExposedPort(PORT, PORT, InternetProtocol.TCP)
                    .withEnv("MINIO_ROOT_USER", ACCESS_KEY)
                    .withEnv("MINIO_ROOT_PASSWORD", SECRET_KEY)
                    .withCommand("server", "/data")
                    .waitingFor(Wait.forHttp("/minio/health/live").forPort(9000));
    private static final String BUCKET_NAME = "test-bucket";
    private static final String PUT_OBJECT_KEY = "test-put-object-key";
    private static final String PUT_OBJECT_CONTENT = "This is a test object content.";

    static {
        MINIO_CONTAINER.start();
    }

    @Autowired
    private S3Client s3Client;
    @Autowired
    private StsOperation stsOperation;
    @Autowired
    private S3Properties s3Properties;

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        String endpoint = String.format("http://%s:%d",
                MINIO_CONTAINER.getHost(),
                MINIO_CONTAINER.getMappedPort(9000));

        registry.add("s3.endpoint", () -> endpoint);
        registry.add("s3.access-key", () -> ACCESS_KEY);
        registry.add("s3.secret-key", () -> SECRET_KEY);
    }

    @Test
    void testS3Client() throws IOException {

        final BaseS3ClientService s3clientOperation = new BaseS3ClientService() {
            @Override
            public S3Client getS3Client() {
                return s3Client;
            }

            @Override
            public String getBucketName() {
                return BUCKET_NAME;
            }

            @Override
            public Logger getLogger() {
                return log;
            }
        };


        // 创建 Bucket
        this.createBucketIfNotExists();

        // 上传文件
        PutObjectResponse putObjectResponse = s3clientOperation.putObject(putObjectRequest -> {
            putObjectRequest.bucket(BUCKET_NAME);
            putObjectRequest.key(PUT_OBJECT_KEY);
        }, RequestBody.fromBytes(PUT_OBJECT_CONTENT.getBytes(StandardCharsets.UTF_8)));
        Assertions.assertNotNull(putObjectResponse.eTag());

        // 文件已存在
        Assertions.assertTrue(s3clientOperation.doesObjectExist(PUT_OBJECT_KEY));

        // 列出文件
        List<S3Object> s3Objects = s3clientOperation.listObjects();
        Assertions.assertEquals(1, s3Objects.size(), "对象数量不匹配");

        // 获取文件
        ResponseInputStream<GetObjectResponse> getObjectResponseResponseInputStream = s3clientOperation.getObject(PUT_OBJECT_KEY);
        Assertions.assertEquals(PUT_OBJECT_CONTENT,
                new String(getObjectResponseResponseInputStream.readAllBytes(), StandardCharsets.UTF_8));

        // 删除文件
        DeleteObjectResponse deleteObjectResponse = s3clientOperation.deleteObject(PUT_OBJECT_KEY);
        Assertions.assertFalse(s3clientOperation.doesObjectExist(PUT_OBJECT_KEY));
    }


    @Test
    void testStsOperation() throws IOException {
        // 创建 Bucket
        this.createBucketIfNotExists();

        // 获取临时 Token
        StsTokenResponse stsTokenResponse = this.stsOperation.generateStsToken(
                BUCKET_NAME, PUT_OBJECT_KEY, StsActionEnum.PutObject,
                StsActionEnum.GetObject, StsActionEnum.DeleteObject, StsActionEnum.ListBucket);

        // 使用生成的临时凭证构造测试客户端
        try (S3Client stsS3Client = S3Client.builder()
                .endpointOverride(URI.create(s3Properties.getEndpoint()))
                .region(s3Properties.getRegion())
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsSessionCredentials.create(
                                stsTokenResponse.getAccessKey(),
                                stsTokenResponse.getSecretAccessKey(),
                                stsTokenResponse.getSessionToken())
                ))
                .build()) {

            // 上传文件
            PutObjectResponse putObjectResponse = stsS3Client.putObject(
                    PutObjectRequest.builder().bucket(BUCKET_NAME).key(PUT_OBJECT_KEY).build(),
                    RequestBody.fromBytes(PUT_OBJECT_CONTENT.getBytes(StandardCharsets.UTF_8))
            );
            Assertions.assertNotNull(putObjectResponse.eTag());

            // 文件已存在
            Assertions.assertNotNull(stsS3Client.headObject(builder -> builder.bucket(BUCKET_NAME).key(PUT_OBJECT_KEY)));

            // 列出文件
            ListObjectsResponse listObjectsResponse = stsS3Client.listObjects(builder -> builder.bucket(BUCKET_NAME));
            List<S3Object> contents = listObjectsResponse.contents();
            Assertions.assertEquals(1, contents.size(), "对象数量不匹配");

            // 获取文件
            ResponseInputStream<GetObjectResponse> getObjectResponseResponseInputStream = stsS3Client.getObject(builder -> builder.bucket(BUCKET_NAME).key(PUT_OBJECT_KEY));
            Assertions.assertEquals(PUT_OBJECT_CONTENT,
                    new String(getObjectResponseResponseInputStream.readAllBytes(), StandardCharsets.UTF_8));

            // 删除文件
            stsS3Client.deleteObject(builder -> builder.bucket(BUCKET_NAME).key(PUT_OBJECT_KEY));
            Assertions.assertThrows(S3Exception.class, () -> {
                stsS3Client.headObject(builder -> builder.bucket(BUCKET_NAME).key(PUT_OBJECT_KEY));
            });
        }
    }

    /**
     * 如果bucket 不存在，创建bucket
     */
    private void createBucketIfNotExists() {
        ListBucketsResponse listBucketsResponse = this.s3Client.listBuckets();
        boolean bucketExists = listBucketsResponse.buckets().stream()
                .anyMatch(bucket -> Objects.equals(bucket.name(), BUCKET_NAME));
        if (!bucketExists) {
            this.s3Client.createBucket(b -> b.bucket(BUCKET_NAME));
        }
        listBucketsResponse = this.s3Client.listBuckets();
        Assertions.assertTrue(listBucketsResponse.buckets().stream()
                .anyMatch(bucket -> Objects.equals(bucket.name(), BUCKET_NAME)));
    }
}