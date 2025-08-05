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
package com.fuhouyu.framework.database.properties;

import com.fuhouyu.framework.common.constants.ConfigPropertiesConstant;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 数据库配置类
 * </p>
 *
 * @author fuhouyu
 * @since 2024/11/14 21:00
 */
@ConfigurationProperties(prefix = DatabaseProperties.PREFIX)
@Getter
@Setter
@ToString
public class DatabaseProperties {

    public static final String PREFIX = ConfigPropertiesConstant.PROPERTIES_PREFIX + "database";


    /**
     * 事务表达式切面
     */
    private String transactionExpression;

    /**
     * 租户配置
     */
    private TenantProperties tenant = new TenantProperties();


    @Getter
    @Setter
    @ToString
    public static class TenantProperties {

        /**
         * 启用租户过滤
         */
        private boolean enabled = true;

        /**
         * 租户字段
         */
        private String column = "owner_tenant_id";

        /**
         * 忽略的表
         */
        private List<String> ignoreTables = Collections.emptyList();
    }
}
