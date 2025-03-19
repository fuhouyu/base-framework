/*
 * Copyright 2024-2025 fuhouyu.
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

import com.fuhouyu.framework.common.utils.LoggerUtil;
import com.fuhouyu.framework.s3.enums.StsActionEnum;
import com.fuhouyu.framework.s3.model.StsTokenResponse;
import com.fuhouyu.framework.s3.properties.S3Properties;
import com.fuhouyu.framework.s3.service.impl.S3StsOperationImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.containers.FixedHostPortGenericContainer;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.InternetProtocol;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import software.amazon.awssdk.auth.credentials.AwsSessionCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ListBucketsResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * <p>
 *
 * </p>
 *
 * @author fuhouyu
 * @since 2025/2/9 15:23
 */
@SpringBootTest(classes = {
        S3AutoConfiguration.class
})
@TestPropertySource(locations = {"classpath:application.yaml"})
@Testcontainers
@Slf4j
@Disabled
class S3ClientTest {

    private static final String accessKey = "test_username";

    private static final String secretKey = "test_password";

    private static final Integer defaultPort = 9000;

    @Container
    private static final GenericContainer<?> MINIO_CONTAINER =
            new FixedHostPortGenericContainer<>("quay.io/minio/minio")
                    .withFixedExposedPort(defaultPort, defaultPort, InternetProtocol.TCP)
                    .withEnv("MINIO_ROOT_USER", accessKey)
                    .withEnv("MINIO_ROOT_PASSWORD", secretKey)
                    .withCommand("server", "/data");


    @Autowired
    private S3Client s3Client;

    @Autowired
    private S3StsOperationImpl s3StsOperationImpl;

    @Autowired
    private S3Properties s3Properties;

    @BeforeAll
    static void setup() {
        MINIO_CONTAINER.start();
    }

    @AfterAll
    static void close() {
        MINIO_CONTAINER.close();
    }

    @Test
    void testClient() {
        final String bucketName = "test-bucket";
        this.s3Client.createBucket(builder -> builder.bucket(bucketName).build());
        ListBucketsResponse listBucketsResponse = this.s3Client.listBuckets();
        Assertions.assertTrue(listBucketsResponse.buckets().stream().anyMatch(bucket -> Objects.equals(bucket.name(), bucketName)));
        // 获取临时 Token
        StsTokenResponse stsTokenResponse = this.s3StsOperationImpl.generateStsToken(bucketName, "my-object-key", StsActionEnum.PutObject);
        LoggerUtil.info(log, "AccessKeyId: {} SecretKey: {] SessionToken: sessionToken ", stsTokenResponse.getAccessKey(),
                stsTokenResponse.getSecretAccessKey(), stsTokenResponse.getSessionToken());


        // 创建 S3 客户端
        S3Client s3Client = S3Client.builder()
                .endpointOverride(URI.create(s3Properties.getEndpoint()))
                .region(s3Properties.getRegion())
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsSessionCredentials.create(stsTokenResponse.getAccessKey(),
                                stsTokenResponse.getSecretAccessKey(), stsTokenResponse.getSessionToken())
                ))
                .build();

        // 上传文件
        try (s3Client) {
            PutObjectResponse putObjectResponse = s3Client.putObject(PutObjectRequest.builder()
                            .bucket(bucketName)
                            .key("my-object-key")
                            .build(),
                    RequestBody.fromBytes("Hello, MinIO!".getBytes(StandardCharsets.UTF_8)));

            LoggerUtil.info(log, "Successfully uploaded file to bucket response: {} ", putObjectResponse);
        } catch (S3Exception e) {
            LoggerUtil.error(log, "Error uploading file: {}", e.getMessage());
            throw e;
        }
    }
}
