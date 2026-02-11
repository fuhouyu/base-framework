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

package com.fuhouyu.framework.cache;

import com.fuhouyu.framework.cache.properties.CacheServiceProperties;
import com.fuhouyu.framework.common.utils.LoggerUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * <p>
 * 缓存自动装配类
 * </p>
 *
 * @author fuhouyu
 * @since 2024/8/13 21:00
 */
@Configuration(proxyBeanMethods = false)
@Import({RedisCacheConfiguration.class})
@ConfigurationPropertiesScan(value = "com.fuhouyu.framework.cache.properties")
@RequiredArgsConstructor
@Slf4j
public class CacheAutoConfiguration implements InitializingBean {

    private final CacheServiceProperties cacheServiceProperties;


    @Override
    public void afterPropertiesSet() throws Exception {
        LoggerUtil.info(log, "使用的缓存类型:{}", cacheServiceProperties.getCacheServiceType());
    }
}
