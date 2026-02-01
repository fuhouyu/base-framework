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

import com.aliyuncs.IAcsClient;
import com.fuhouyu.framework.s3.properties.S3Properties;
import com.fuhouyu.framework.s3.properties.StsProperties;
import com.fuhouyu.framework.s3.service.StsOperation;
import com.fuhouyu.framework.s3.service.impl.AliOssStsOperationImpl;
import com.fuhouyu.framework.s3.service.impl.S3StsOperationImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.sts.StsClient;

import java.net.URI;

/**
 * <p>
 * sts自动装配类
 * </p>
 *
 * @author fuhouyu
 * @since 2025/2/22 19:47
 */
@EnableConfigurationProperties({StsProperties.class})
@Configuration
@RequiredArgsConstructor
public class S3StsConfiguration {

    private final StsProperties stsProperties;

    private final S3Properties s3Properties;

    /**
     * s3 客户端初始化
     *
     * @param awsCredentialsProvider awsCredentialsProvider
     * @return s3Client
     */
    @Bean(destroyMethod = "close")
    public S3Client s3Client(AwsCredentialsProvider awsCredentialsProvider) {
        return S3Client.builder()
                .region(Region.AWS_GLOBAL)
                .endpointOverride(URI.create(s3Properties.getEndpoint()))
                .credentialsProvider(awsCredentialsProvider)
                .serviceConfiguration(builder -> builder.pathStyleAccessEnabled(s3Properties.getPathStyleEnabled()))
                .build();
    }

    /**
     * sts 客户端初始化
     * 阿里云依赖不存在时加载该类
     *
     * @param stsClient stsClient
     * @return sts 操作类
     */
    @Bean
    @ConditionalOnMissingBean(StsOperation.class)
    public StsOperation s3StsOperation(StsClient stsClient) {
        return new S3StsOperationImpl(stsClient, stsProperties, s3Properties);
    }


    /**
     * 阿里云 oss 依赖
     *
     * @return 阿里oss
     */
    @Bean
    @ConditionalOnClass(IAcsClient.class)
    @ConditionalOnMissingBean(StsOperation.class)
    public StsOperation aliStsOperation() {
        return new AliOssStsOperationImpl(stsProperties, s3Properties);
    }
}
