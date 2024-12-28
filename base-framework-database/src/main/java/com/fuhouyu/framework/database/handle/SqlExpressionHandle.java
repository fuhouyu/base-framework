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
package com.fuhouyu.framework.database.handle;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Expression;

import java.lang.reflect.Method;

/**
 * <p>
 * sql处理器
 * </p>
 *
 * @author fuhouyu
 * @since 2024/12/28 17:55
 */
public interface SqlExpressionHandle {

    /**
     * 获取 SQL 片段
     *
     * @param where  待执行 SQL Where 条件表达式
     * @param method sql执行的方法
     * @return JSqlParser 条件表达式，返回的条件表达式会覆盖原有的条件表达式
     */
    Expression getSqlSegment(Expression where, Method method) throws JSQLParserException;
}
