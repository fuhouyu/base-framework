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

package com.fuhouyu.framework.web;

import com.fuhouyu.framework.web.components.HttpRequestHandler;
import com.fuhouyu.framework.web.components.ParseHttpRequest;
import com.fuhouyu.framework.web.utils.JacksonCustomizer;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverters;
import org.springframework.http.converter.json.JacksonJsonHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * <p>
 * webmvc配置
 * </p>
 *
 * @author fuhouyu
 * @since 2024/8/15 19:49
 */
@RequiredArgsConstructor
@Configuration(proxyBeanMethods = false)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@ComponentScan(basePackageClasses = WebMvcAutoConfiguration.class)
public class WebMvcAutoConfiguration implements WebMvcConfigurer {
    private final ApplicationContext applicationContext;

    @Override
    public void configureMessageConverters(HttpMessageConverters.ServerBuilder builder) {
        JacksonJsonHttpMessageConverter jacksonJsonHttpMessageConverter = new JacksonJsonHttpMessageConverter(JacksonCustomizer.createJsonMapper());
        builder.addCustomConverter(jacksonJsonHttpMessageConverter);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new HttpRequestHandler(applicationContext.getBean(ParseHttpRequest.class)))
                .addPathPatterns("/**");
    }
}
