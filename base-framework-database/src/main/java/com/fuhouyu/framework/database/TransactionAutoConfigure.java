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

import com.fuhouyu.framework.database.properties.DatabaseProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.aop.aspectj.AspectJExpressionPointcutAdvisor;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.transaction.TransactionAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionManager;
import org.springframework.transaction.interceptor.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 事务自动装配类
 * </p>
 *
 * @author fuhouyu
 * @since 2024/11/14 20:30
 */
@AutoConfigureAfter(TransactionAutoConfiguration.class)
@EnableConfigurationProperties(DatabaseProperties.class)
@RequiredArgsConstructor
public class TransactionAutoConfigure {

    private final DatabaseProperties databaseProperties;


    @Bean
    @ConditionalOnBean(TransactionManager.class)
    public AspectJExpressionPointcutAdvisor txAdvisor(TransactionManager transactionManager) {
        AspectJExpressionPointcutAdvisor advisor = new AspectJExpressionPointcutAdvisor();
        advisor.setExpression(databaseProperties.getTransactionExpression());
        advisor.setAdvice(txInterceptor(transactionManager));
        advisor.setOrder(Ordered.LOWEST_PRECEDENCE - 100);
        return advisor;
    }

    private TransactionInterceptor txInterceptor(TransactionManager transactionManager) {
        TransactionInterceptor interceptor = new TransactionInterceptor();
        interceptor.setTransactionManager(transactionManager);
        interceptor.setTransactionAttributeSource(txAttrSource());
        return interceptor;
    }

    private TransactionAttributeSource txAttrSource() {
        NameMatchTransactionAttributeSource source = new NameMatchTransactionAttributeSource();
        RuleBasedTransactionAttribute required = new RuleBasedTransactionAttribute();
        required.setRollbackRules(
                Collections.singletonList(new RollbackRuleAttribute(Exception.class)));
        required.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        RuleBasedTransactionAttribute readOnly = new RuleBasedTransactionAttribute();
        readOnly.setReadOnly(true);
        readOnly.setPropagationBehavior(TransactionDefinition.PROPAGATION_SUPPORTS);
        Map<String, TransactionAttribute> txMap = new HashMap<>();
        // 添加
        txMap.put("add*", required);
        txMap.put("insert*", required);
        txMap.put("new*", required);
        txMap.put("save*", required);
        // 修改
        txMap.put("update*", required);
        txMap.put("modify*", required);
        txMap.put("edit*", required);
        // 删除
        txMap.put("delete*", required);
        txMap.put("remove*", required);
        txMap.put("move*", required);
        txMap.put("copy*", required);
        // 查询，只读事务
        txMap.put("list*", readOnly);
        txMap.put("search*", readOnly);
        txMap.put("query*", readOnly);
        txMap.put("find*", readOnly);
        txMap.put("paging*", readOnly);
        txMap.put("load*", readOnly);
        txMap.put("*", readOnly);
        txMap.put("get*", readOnly);

        source.setNameMap(txMap);
        return source;
    }
}
