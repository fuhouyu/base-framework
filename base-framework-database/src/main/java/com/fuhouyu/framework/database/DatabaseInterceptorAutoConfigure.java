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
package com.fuhouyu.framework.database;

import com.fuhouyu.framework.database.handle.PrepareSqlHandle;
import com.fuhouyu.framework.database.handle.SqlExpressionHandle;
import com.fuhouyu.framework.database.handle.TenantExpressionHandle;
import com.fuhouyu.framework.database.interceptor.FieldCipherInterceptor;
import com.fuhouyu.framework.kms.service.KmsService;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.plugin.Interceptor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.List;

/**
 * <p>
 * database 拦截器自动装配类
 * </p>
 *
 * @author fuhouyu
 * @since 2024/12/18 21:19
 */
@RequiredArgsConstructor
@Configuration
public class DatabaseInterceptorAutoConfigure implements InitializingBean {

    private final KmsService kmsService;

    /**
     * 租户查询表达式处理
     *
     * @return 租户查询表达式处理
     */
    @Bean
    public SqlExpressionHandle tenantExpressionHandle() {
        return new TenantExpressionHandle();
    }

    /**
     * sql处理
     *
     * @param sqlExpressionHandles sql表达式处理器
     * @return sql处理拦截器
     */
    @Bean
    @ConditionalOnBean(SqlExpressionHandle.class)
    @ConditionalOnMissingBean(PrepareSqlHandle.class)
    public Interceptor prepareHandleWithExpressions(List<SqlExpressionHandle> sqlExpressionHandles) {
        return new PrepareSqlHandle(sqlExpressionHandles);
    }

    /**
     * sql处理
     *
     * @return sql处理拦截器
     */
    @Bean
    @Primary
    @ConditionalOnMissingBean({SqlExpressionHandle.class, PrepareSqlHandle.class})
    public Interceptor prepareHandleWithoutExpressions() {
        return new PrepareSqlHandle();
    }


    /**
     * 字段加解密
     *
     * @return 字段加解密拦截器
     */
    @Bean
    public Interceptor fieldCipher() {
        return new FieldCipherInterceptor();
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        FieldCipherUtil.setKmsService(kmsService);
    }
}
