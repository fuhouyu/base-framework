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

import com.fuhouyu.framework.common.utils.LoggerUtil;
import com.fuhouyu.framework.resource.properties.ResourceProperties;
import com.fuhouyu.framework.resource.service.ResourceService;
import com.fuhouyu.framework.resource.service.impl.LocalFileServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;

/**
 * <p>
 * 本地文件资源自动装配类
 * </p>
 *
 * @author fuhouyu
 * @since 2024/8/18 17:28
 */
@Configuration(proxyBeanMethods = false)
@RequiredArgsConstructor
@Slf4j
@ConditionalOnProperty(prefix = ResourceProperties.PREFIX,
        name = "upload-type", havingValue = "local")
public class LocalFileResourceConfiguration {

    private final ResourceProperties resourceProperties;

    /**
     * 本地资源文件bean
     *
     * @return bean对象
     */
    @Bean
    @ConditionalOnMissingBean(ResourceService.class)
    public ResourceService localFileResourceService() {
        String basePath;
        if (Objects.isNull(resourceProperties.getLocalResource()) ||
                Objects.isNull(resourceProperties.getLocalResource().getBasePath())) {
            basePath = System.getProperty("java.io.tmpdir");
            LoggerUtil.warn(log, "本地路径不存在，获取系统tmp路径:{}", basePath);
        } else {
            basePath = resourceProperties.getLocalResource().getBasePath();
        }
        return new LocalFileServiceImpl(basePath);
    }
}
