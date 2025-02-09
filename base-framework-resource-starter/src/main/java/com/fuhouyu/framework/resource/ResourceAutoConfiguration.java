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

import com.fuhouyu.framework.resource.properties.S3Properties;
import io.minio.MinioClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Assert;

/**
 * <p>
 * resource 资源自动装配类入口
 * </p>
 *
 * @author fuhouyu
 * @since 2024/8/16 18:36
 */
@EnableConfigurationProperties(S3Properties.class)
@Configuration(proxyBeanMethods = false)
@RequiredArgsConstructor
public class ResourceAutoConfiguration implements InitializingBean {

    private final S3Properties s3Properties;

    /**
     * minio 客户端初始化
     *
     * @return minioClient
     */
    @Bean
    public MinioClient minioClient() {
        return
                MinioClient.builder()
                        .endpoint(s3Properties.getEndpoint())
                        .credentials(s3Properties.getAccessKeyId(), s3Properties.getSecretKey())
                        .build();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(s3Properties.getEndpoint(), "s3 endpoint未设置");
        Assert.notNull(s3Properties.getAccessKeyId(), "s3 accessKey未设置");
        Assert.notNull(s3Properties.getSecretKey(), "s3 secretKey未设置");
    }

}
