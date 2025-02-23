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

package com.fuhouyu.framework.s3;

import com.fuhouyu.framework.s3.properties.S3Properties;
import com.fuhouyu.framework.s3.properties.StsProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.util.Assert;
import software.amazon.awssdk.auth.credentials.*;
import software.amazon.awssdk.services.s3.S3Configuration;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.sts.StsClient;

import java.net.URI;
import java.util.Objects;

/**
 * <p>
 * resource 资源自动装配类入口
 * </p>
 *
 * @author fuhouyu
 * @since 2024/8/16 18:36
 */
@EnableConfigurationProperties({S3Properties.class, StsProperties.class})
@Configuration
@RequiredArgsConstructor
@Import(S3StsConfiguration.class)
public class S3AutoConfiguration implements InitializingBean {

    private final S3Properties s3Properties;


    /**
     * aws 证书提供者
     *
     * @return awsCredentialsProvider
     */
    @Bean
    public AwsCredentialsProvider awsCredentialsProvider() {
        AwsCredentials awsCredentials;
        if (Objects.equals(s3Properties.getStsEnabled(), Boolean.TRUE)) {
            awsCredentials = AwsSessionCredentials.create(s3Properties.getAccessKeyId(),
                    s3Properties.getSecretKey(), s3Properties.getStsToken());
        } else {
            awsCredentials = AwsBasicCredentials.create(s3Properties.getAccessKeyId(), s3Properties.getSecretKey());
        }
        return StaticCredentialsProvider.create(awsCredentials);
    }

    /**
     * s3 预签名初始化
     *
     * @param awsCredentialsProvider awsCredentialsProvider
     * @return s3Presigner
     */
    @Bean(destroyMethod = "close")
    public S3Presigner s3Presigner(AwsCredentialsProvider awsCredentialsProvider) {
        S3Configuration s3Configuration = S3Configuration.builder()
                .pathStyleAccessEnabled(s3Properties.getPathStyleEnabled())
                .build();
        return S3Presigner.builder()
                .region(s3Properties.getRegion())
                .endpointOverride(URI.create(s3Properties.getEndpoint()))
                .credentialsProvider(awsCredentialsProvider)
                .serviceConfiguration(s3Configuration)
                .build();
    }


    /**
     * sts 客户端初始化
     *
     * @param awsCredentialsProvider awsCredentialsProvider
     * @return stsClient
     */
    @Bean(destroyMethod = "close")
    public StsClient stsClient(AwsCredentialsProvider awsCredentialsProvider) {
        return StsClient.builder()
                .endpointOverride(URI.create(s3Properties.getEndpoint()))
                .region(s3Properties.getRegion())
                .credentialsProvider(awsCredentialsProvider)
                .build();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(s3Properties.getEndpoint(), "s3 endpoint未设置");
        Assert.notNull(s3Properties.getAccessKeyId(), "s3 accessKey未设置");
        Assert.notNull(s3Properties.getSecretKey(), "s3 secretKey未设置");
    }

}
