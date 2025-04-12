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
package com.fuhouyu.framework.database.handle;

import com.fuhouyu.framework.context.ContextHolderStrategy;
import com.fuhouyu.framework.database.annotations.TenantQuery;
import lombok.SneakyThrows;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;

import java.lang.reflect.Method;
import java.util.Objects;


/**
 * <p>
 * 租户查询
 * </p>
 *
 * @author fuhouyu
 * @since 2024/12/17 22:02
 */
public class TenantExpressionHandle implements SqlExpressionHandle {

    @Override
    @SneakyThrows
    public Expression getSqlSegment(Expression where, Method method) {
        TenantQuery tenantQueryAnnotation = method.getAnnotation(TenantQuery.class);
        if (Objects.isNull(tenantQueryAnnotation)) {
            return where;
        }
        String tenantQuery = String.format("%s = %s", tenantQueryAnnotation.column(), ContextHolderStrategy.getContext().getUser().getTenantId());
        if (Objects.isNull(where)) {
            return CCJSqlParserUtil.parseExpression(tenantQuery);
        }
        return new AndExpression(where, CCJSqlParserUtil.parseExpression(tenantQuery));
    }
}
