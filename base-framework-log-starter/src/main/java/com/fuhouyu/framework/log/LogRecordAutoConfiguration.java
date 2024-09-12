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

package com.fuhouyu.framework.log;

import com.fuhouyu.framework.log.core.LogRecordAspectj;
import com.fuhouyu.framework.log.core.LogRecordStoreService;
import com.fuhouyu.framework.log.properties.LogRecordProperties;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Primary;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 日志记录的自动装配类
 * </p>
 *
 * @author fuhouyu
 * @since 2024/8/14 12:16
 */
@ConfigurationPropertiesScan(basePackages = "com.fuhouyu.framework.log.properties")
@ConditionalOnProperty(prefix = LogRecordProperties.LOG_RECORD_PREFIX,
        name = "enabled",
        havingValue = "true")
@ComponentScan(basePackageClasses = LogRecordAutoConfiguration.class)
public class LogRecordAutoConfiguration implements InitializingBean {

    private final LogRecordProperties logRecordProperties;

    private final ApplicationContext applicationContext;

    private final BeanFactory beanFactory;

    public LogRecordAutoConfiguration(LogRecordProperties logRecordProperties, ApplicationContext applicationContext, BeanFactory beanFactory) {
        this.logRecordProperties = logRecordProperties;
        this.applicationContext = applicationContext;
        this.beanFactory = beanFactory;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(logRecordProperties.getSystemName(), "系统名称未设置");
    }

    /**
     * 日志记录切面bean
     *
     * @return 日志记录bean
     */
    @Bean
    @Primary
    public LogRecordAspectj logRecordAspectj() {
        List<LogRecordStoreService> logRecordStoreServiceList = this.logRecordStoreServiceList();
        return new LogRecordAspectj(logRecordStoreServiceList, logRecordProperties.getSystemName(),
                new BeanFactoryResolver(beanFactory));
    }

    /**
     * 获取日志存储的集合
     *
     * @return 日志存储集合
     */
    private List<LogRecordStoreService> logRecordStoreServiceList() {
        String[] beanNamesForType = applicationContext.getBeanNamesForType(LogRecordStoreService.class);
        if (beanNamesForType.length == 0) {
            throw new IllegalArgumentException("至少需要一个日志存储实现");
        }
        List<LogRecordStoreService> logRecordStoreServiceList = new ArrayList<>(beanNamesForType.length);
        for (String beanName : beanNamesForType) {
            logRecordStoreServiceList.add(applicationContext.getBean(beanName, LogRecordStoreService.class));
        }
        return logRecordStoreServiceList;
    }
}
