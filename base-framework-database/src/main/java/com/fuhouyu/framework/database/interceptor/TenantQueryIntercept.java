/*
 * Copyright 2024-2024 the original author or authors.
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
package com.fuhouyu.framework.database.interceptor;

import com.fuhouyu.framework.context.ContextHolderStrategy;
import com.fuhouyu.framework.database.annotations.TenantQuery;
import com.fuhouyu.framework.database.utils.MappedStatementUtil;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.select.PlainSelect;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.util.Objects;


/**
 * <p>
 * 租户查询
 * </p>
 *
 * @author fuhouyu
 * @since 2024/12/17 22:02
 */
@Intercepts(
        {
                @Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class}),
        }
)
@SuppressWarnings("unchecked")
public class TenantQueryIntercept implements Interceptor {

    public static final DefaultReflectorFactory DEFAULT_REFLECTOR_FACTORY = new DefaultReflectorFactory();

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
        // StatementHandler
        StatementHandler sh = (StatementHandler) invocation.getTarget();
        MetaObject metaObject = getMetaObject(sh);
        MappedStatement mappedStatement = (MappedStatement) metaObject.getValue("delegate.mappedStatement");
        SqlCommandType sqlCommandType = mappedStatement.getSqlCommandType();
        // 非查询，直接返回
        if (sqlCommandType != SqlCommandType.SELECT) {
            return invocation.proceed();
        }
        BoundSql boundSql = statementHandler.getBoundSql();
        Method method = MappedStatementUtil.resolveMethodFromMappedStatement(mappedStatement);
        TenantQuery tenantQueryAnnotation = method.getAnnotation(TenantQuery.class);
        if (Objects.isNull(tenantQueryAnnotation)) {
            return invocation.proceed();
        }
        //获取到原始sql语句
        String sql = boundSql.getSql();
        PlainSelect plainSelect = (PlainSelect) CCJSqlParserUtil.parse(sql);
        Expression where = plainSelect.getWhere();

        String tenantQuery = String.format("%s = %s", tenantQueryAnnotation.column(), ContextHolderStrategy.getContext().getUser().getTenantId());
        AndExpression expression = new AndExpression(where, CCJSqlParserUtil.parseExpression(tenantQuery));
        plainSelect.setWhere(expression);

        //通过反射修改sql语句
        Field field = boundSql.getClass().getDeclaredField("sql");
        ReflectionUtils.makeAccessible(field);
        ReflectionUtils.setField(field, boundSql, plainSelect.toString());
        //执行结果
        return invocation.proceed();
    }


    private MetaObject getMetaObject(Object object) {
        return MetaObject.forObject(object, SystemMetaObject.DEFAULT_OBJECT_FACTORY, SystemMetaObject.DEFAULT_OBJECT_WRAPPER_FACTORY, DEFAULT_REFLECTOR_FACTORY);
    }
}
