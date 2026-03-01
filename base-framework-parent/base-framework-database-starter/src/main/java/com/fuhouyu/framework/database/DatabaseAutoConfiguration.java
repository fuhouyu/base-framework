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
package com.fuhouyu.framework.database;

import com.fuhouyu.framework.kms.KmsAutoConfiguration;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * <p>
 * 数据库自动装配类
 * </p>
 *
 * @author fuhouyu
 * @since 2026/2/4 20:27
 */
@Configuration(proxyBeanMethods = false)
@RequiredArgsConstructor
@AutoConfigureAfter(KmsAutoConfiguration.class)
@ComponentScan(basePackageClasses = DatabaseAutoConfiguration.class)
public class DatabaseAutoConfiguration {
}
