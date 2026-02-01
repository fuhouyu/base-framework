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

package com.fuhouyu.framework.web;

import com.fuhouyu.framework.common.constants.HttpRequestHeaderConstant;
import com.fuhouyu.framework.common.utils.JacksonUtil;
import com.fuhouyu.framework.context.user.UserEntity;
import com.fuhouyu.framework.kms.KmsAutoConfiguration;
import com.fuhouyu.framework.web.components.ParseHttpRequest;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
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
@Import({WebMvcConfiguration.class, Ip2RegionConfiguration.class})
@ComponentScan(basePackageClasses = WebAutoConfiguration.class)
@ConfigurationPropertiesScan(basePackages = "com.fuhouyu.framework.web.properties")
@AutoConfigureAfter({KmsAutoConfiguration.class})
public class WebAutoConfiguration {


    @Bean
    @ConditionalOnMissingBean(ParseHttpRequest.class)
    public ParseHttpRequest parseHttpRequest() {
        return (request, response, handler) -> {
            String userinfoHeader = request.getHeader(HttpRequestHeaderConstant.USERINFO_HEADER);
            if (Objects.isNull(userinfoHeader)) {
                return null;
            }
            String userinfoJsonStr = URLDecoder.decode(userinfoHeader, StandardCharsets.UTF_8);
            return JacksonUtil.readValue(userinfoJsonStr, UserEntity.class);
        };
    }

}
