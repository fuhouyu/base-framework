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

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.containers.FixedHostPortGenericContainer;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.InternetProtocol;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ListBucketsResponse;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.time.Duration;
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
    private S3Presigner s3Presigner;

    @Autowired
    private S3Client s3Client;

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
        PutObjectPresignRequest putObjectPresignRequest = PutObjectPresignRequest
                .builder()
                .putObjectRequest(builder -> builder.bucket(bucketName).key("test-object").build())
                .signatureDuration(Duration.ofDays(1))
                .build();
        ListBucketsResponse listBucketsResponse = this.s3Client.listBuckets();
        Assertions.assertTrue(listBucketsResponse.buckets().stream().anyMatch(bucket -> Objects.equals(bucket.name(), bucketName)));
    }
}
