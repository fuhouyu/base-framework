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

package com.fuhouyu.framework.log.core;

import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.expression.Expression;
import org.springframework.expression.ParserContext;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;

/**
 * <p>
 *
 * </p>
 *
 * @author fuhouyu
 * @since 2024/8/14 12:02
 */
public class LogEvaluator {

    /**
     * SpEL解析器
     */
    private final SpelExpressionParser parser = new SpelExpressionParser();

    /**
     * 参数解析器
     */
    private final ParameterNameDiscoverer discoverer = new DefaultParameterNameDiscoverer();

    /**
     * 表达式模板
     */
    private final ParserContext template = new TemplateParserContext();

    /**
     * 解析
     *
     * @param expression 表达式
     * @param context    日志表达式上下文
     * @return 表达式结果
     */
    public Object parse(String expression, MethodBasedEvaluationContext context) {
        Expression parseExpression = getExpression(expression);
        return parseExpression.getValue(context);
    }

    /**
     * 获取翻译后表达式
     *
     * @param expression 字符串表达式
     * @return 翻译后表达式
     */
    private Expression getExpression(String expression) {
        return parser.parseExpression(expression, template);
    }

    public ParameterNameDiscoverer getDiscoverer() {
        return discoverer;
    }
}
