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
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.http.converter.HttpMessageConverters;
import org.springframework.http.converter.json.JacksonJsonHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import tools.jackson.databind.ext.javatime.deser.LocalDateDeserializer;
import tools.jackson.databind.ext.javatime.deser.LocalDateTimeDeserializer;
import tools.jackson.databind.ext.javatime.ser.LocalDateSerializer;
import tools.jackson.databind.ext.javatime.ser.LocalDateTimeSerializer;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.databind.module.SimpleModule;
import tools.jackson.databind.ser.std.ToStringSerializer;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * <p>
 * webmvc配置
 * </p>
 *
 * @author fuhouyu
 * @since 2024/8/15 19:49
 */
@RequiredArgsConstructor
@EnableWebMvc
public class WebMvcConfiguration implements WebMvcConfigurer {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(
            "yyyy-MM-dd");
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(
            "yyyy-MM-dd HH:mm:ss");
    private final ApplicationContext applicationContext;

    @Override
    public void configureMessageConverters(HttpMessageConverters.ServerBuilder builder) {
        // 不显示为null的字段, 将Long类型，转换为String类型，否则前端会精度丢失
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addSerializer(Long.class, ToStringSerializer.instance);
        simpleModule.addSerializer(Long.TYPE, ToStringSerializer.instance);
        LocalDateTimeDeserializer localDateTimeDeserializer = new LocalDateTimeDeserializer(DATE_TIME_FORMATTER);

        // 日期时间序列化与反序列化
        simpleModule.addSerializer(LocalDate.class, new LocalDateSerializer(DATE_FORMATTER));
        simpleModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(DATE_FORMATTER));

        // 日期时间序列化与反序列化
        simpleModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DATE_TIME_FORMATTER));
        simpleModule.addDeserializer(LocalDateTime.class, localDateTimeDeserializer);

        JsonMapper.Builder mapperBuilder = JsonMapper.builder();
        mapperBuilder.addModule(simpleModule);
        JacksonJsonHttpMessageConverter jacksonJsonHttpMessageConverter = new JacksonJsonHttpMessageConverter(mapperBuilder);
        builder.addCustomConverter(jacksonJsonHttpMessageConverter);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new HttpRequestHandler(applicationContext.getBean(ParseHttpRequest.class)))
                .addPathPatterns("/**");
    }
}
