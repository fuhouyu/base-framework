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

import com.fuhouyu.framework.web.properties.Ip2RegionProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p>
 * ip2region 配置类
 * </p>
 *
 * @author fuhouyu
 * @since 2025/3/11 21:57
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(Ip2RegionProperties.class)
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = Ip2RegionProperties.PREFIX,
        name = "enabled", havingValue = "true")
public class Ip2RegionConfiguration {

    private final Ip2RegionProperties ip2RegionProperties;


    /**
     * ip2region模板
     *
     * @return ip2regionTemplate
     */
    @Bean(destroyMethod = "close")
    public Ip2RegionTemplate ip2RegionTemplate() {
        return new Ip2RegionTemplate(ip2RegionProperties.getDbPath());
    }
}
