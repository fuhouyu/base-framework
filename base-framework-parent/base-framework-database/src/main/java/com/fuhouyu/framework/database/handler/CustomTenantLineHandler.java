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
package com.fuhouyu.framework.database.handler;

import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import com.fuhouyu.framework.context.ContextHolder;
import com.fuhouyu.framework.context.user.User;
import com.fuhouyu.framework.database.properties.DatabaseProperties;
import lombok.RequiredArgsConstructor;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;

import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 租户拦截器实现
 * </p>
 *
 * @author fuhouyu
 * @since 2025/5/7 19:00
 */
@RequiredArgsConstructor
public class CustomTenantLineHandler implements TenantLineHandler {

    private final DatabaseProperties.TenantProperties tenantProperties;

    @Override
    public Expression getTenantId() {
        User user = ContextHolder.getContext().getUser();
        Long tenantId = user.getTenantId();
        return new LongValue(tenantId);
    }

    @Override
    public String getTenantIdColumn() {
        return tenantProperties.getColumn();
    }

    @Override
    public boolean ignoreTable(String tableName) {
        List<String> ignoreTables = tenantProperties.getIgnoreTables();
        return ignoreTables.contains(tableName) || Objects.isNull(ContextHolder.getContext().getUser());
    }
}
