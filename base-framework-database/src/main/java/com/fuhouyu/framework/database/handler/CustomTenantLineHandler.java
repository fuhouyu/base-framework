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
import com.fuhouyu.framework.context.ContextHolderStrategy;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;

/**
 * <p>
 * 租户拦截器实现
 * </p>
 *
 * @author fuhouyu
 * @since 2025/5/7 19:00
 */
public class CustomTenantLineHandler implements TenantLineHandler {
    @Override
    public Expression getTenantId() {
        Long tenantId = ContextHolderStrategy.getContext().getUser().getTenantId();
        return new LongValue(tenantId);
    }

    @Override
    public String getTenantIdColumn() {
        return "owner_tenant_id";
    }

    @Override
    public boolean ignoreTable(String tableName) {
        return TenantLineHandler.super.ignoreTable(tableName);
    }
}
