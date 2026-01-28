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

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.BlockAttackInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.TenantLineInnerInterceptor;
import com.fuhouyu.framework.database.handler.BaseEntityHandle;
import com.fuhouyu.framework.database.handler.CustomTenantLineHandler;
import com.fuhouyu.framework.database.interceptor.FieldCipherInterceptor;
import com.fuhouyu.framework.database.properties.DatabaseProperties;
import com.fuhouyu.framework.kms.KmsAutoConfiguration;
import com.fuhouyu.framework.kms.service.KmsService;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.plugin.Interceptor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p>
 * database 自动装配类
 * </p>
 *
 * @author fuhouyu
 * @since 2024/12/18 21:19
 */
@RequiredArgsConstructor
@Configuration
@AutoConfigureBefore({KmsAutoConfiguration.class})
@EnableConfigurationProperties({DatabaseProperties.class})
public class DatabaseAutoConfigure implements InitializingBean {

    private final KmsService kmsService;

    private final DatabaseProperties databaseProperties;

    /**
     * mybatis plus 相关拦截器
     * @return mybatis plus 拦截器
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        // 租户拦载器
        if (databaseProperties.getTenant().isEnabled()) {
            TenantLineInnerInterceptor tenantInterceptor = new TenantLineInnerInterceptor();
            tenantInterceptor.setTenantLineHandler(new CustomTenantLineHandler(databaseProperties.getTenant()));
            interceptor.addInnerInterceptor(tenantInterceptor);
        }

        // 分页插件
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor());
        // 防止全表更新和删除
        interceptor.addInnerInterceptor(new BlockAttackInnerInterceptor());
        return interceptor;
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

    /**
     * baseEntity 处理器
     * @return MetaObjectHandler
     */
    @Bean
    public MetaObjectHandler baseEntityHandle() {
        return new BaseEntityHandle();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        FieldCipherUtil.setKmsService(kmsService);
    }
}
