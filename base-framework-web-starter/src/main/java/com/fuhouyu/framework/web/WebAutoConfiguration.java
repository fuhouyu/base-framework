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

package com.fuhouyu.framework.web;

import com.fuhouyu.framework.constants.HttpRequestHeaderConstant;
import com.fuhouyu.framework.kms.service.KmsService;
import com.fuhouyu.framework.utils.JacksonUtil;
import com.fuhouyu.framework.web.entity.UserEntity;
import com.fuhouyu.framework.web.filter.DefaultHttpBodyFilter;
import com.fuhouyu.framework.web.filter.HttpBodyFilter;
import com.fuhouyu.framework.web.handler.ParseHttpRequest;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * <p>
 * web 组件自动装配类
 * </p>
 *
 * @author fuhouyu
 * @since 2024/8/15 20:18
 */
@Import({WebMvcAutoConfiguration.class, FormAutoConfiguration.class})
@ComponentScan(basePackageClasses = WebAutoConfiguration.class)
@ConfigurationPropertiesScan(basePackages = "com.fuhouyu.framework.web.properties")
public class WebAutoConfiguration {


    /**
     * 返回一个默认加解密body的过滤器
     *
     * @param kmsService kms
     * @return body 过滤器
     */
    @Bean
    @ConditionalOnMissingBean(HttpBodyFilter.class)
    @ConditionalOnBean(KmsService.class)
    public HttpBodyFilter httpBodyFilter(KmsService kmsService) {
        return new DefaultHttpBodyFilter(kmsService);
    }


    @Bean
    @ConditionalOnMissingBean(ParseHttpRequest.class)
    public ParseHttpRequest parseHttpRequest() {
        return request -> {
            String userinfoHeader = request.getHeader(HttpRequestHeaderConstant.USERINFO_HEADER);
            if (Objects.isNull(userinfoHeader)) {
                return null;
            }
            String userinfoJsonStr = URLDecoder.decode(userinfoHeader, StandardCharsets.UTF_8);
            return JacksonUtil.readValue(userinfoJsonStr, UserEntity.class);
        };
    }

}
