/*
 * Copyright 2024-2034 the original author or authors.
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

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.common.auth.CredentialsProviderFactory;
import com.aliyun.oss.common.auth.STSAssumeRoleSessionCredentialsProvider;
import com.aliyuncs.exceptions.ClientException;
import com.fuhouyu.framework.resource.properties.AliYunOssProperties;
import com.fuhouyu.framework.resource.properties.BaseOssResourceProperties;
import com.fuhouyu.framework.resource.properties.ResourceProperties;
import com.fuhouyu.framework.resource.service.ResourceService;
import com.fuhouyu.framework.resource.service.impl.AliYunOssServiceImpl;
import com.fuhouyu.framework.utils.LoggerUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;

import java.util.Objects;

/**
 * <p>
 * 阿里云oss自动装配类
 * </p>
 *
 * @author fuhouyu
 * @since 2024/8/16 18:35
 */
@ConditionalOnClass(OSS.class)
public class AliYunOssAutoConfigure implements InitializingBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(AliYunOssAutoConfigure.class);

    private final ResourceProperties resourceProperties;

    public AliYunOssAutoConfigure(ResourceProperties resourceProperties) {
        this.resourceProperties = resourceProperties;
    }

    @Bean
    public OSS ossClient() {
        AliYunOssProperties ossConfig = resourceProperties.getAliOssConfig();

        if (ossConfig.isEnableSts()) {
            STSAssumeRoleSessionCredentialsProvider credentialsProvider;
            try {
                credentialsProvider = CredentialsProviderFactory
                        .newSTSAssumeRoleSessionCredentialsProvider(
                                ossConfig.getRegion(),
                                ossConfig.getAccessKey(),
                                ossConfig.getSecretKey(),
                                ossConfig.getSts().getRoleArn()
                        );
            } catch (ClientException e) {
                LoggerUtil.error(LOGGER, "阿里云oss客户端初始化失败, ossConfig:{}, 错误信息:{}", ossConfig, e.getMessage(), e);
                throw new IllegalArgumentException(String.format("阿里云oss客户端初始化失败:%s", e.getMessage()));
            }
            return new OSSClientBuilder().build(
                    ossConfig.getEndpoint(),
                    credentialsProvider);
        }
        return new OSSClientBuilder()
                .build(ossConfig.getEndpoint(),
                        ossConfig.getAccessKey(),
                        ossConfig.getSecretKey());
    }

    @Bean
    public ResourceService resourceService(OSS oss) {
        return new AliYunOssServiceImpl(oss);
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        BaseOssResourceProperties ossConfig = resourceProperties.getAliOssConfig();
        if (Objects.isNull(ossConfig)) {
            throw new IllegalArgumentException("阿里云oss未设置相应的ak/sk");
        }
    }
}
