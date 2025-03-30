/*
 * Copyright 2012-2020 the original author or authors.
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

import com.fuhouyu.framework.database.FieldCipherUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 字段加解密的拦截器
 * </p>
 *
 * @author fuhouyu
 * @since 2024/9/17 22:13
 */
@Intercepts(
        {
                @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
                @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class}),
                @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class}),
        }
)
@Slf4j
@RequiredArgsConstructor
public class FieldCipherInterceptor implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object[] args = invocation.getArgs();
        MappedStatement mappedStatement = (MappedStatement) args[0];
        Object parameter = args[1];

        if (mappedStatement.getSqlCommandType() != SqlCommandType.SELECT) {
            handleEncryption(parameter);
        }

        Object result = invocation.proceed();

        if (mappedStatement.getSqlCommandType() == SqlCommandType.SELECT) {
            if (result instanceof List<?> resultList) {
                resultList.forEach(res -> FieldCipherUtil.encryptOrDecrypt(res, false));
            } else {
                FieldCipherUtil.encryptOrDecrypt(result, false);
            }
        }

        return result;
    }

    /**
     * 处理参数加密
     *
     * @param parameter 传入的参数
     */
    private void handleEncryption(Object parameter) {
        if (parameter == null) {
            return;
        }
        if (parameter instanceof Map<?, ?> paramMap) {
            for (Object value : new HashSet<>(paramMap.values())) {
                if (value != null) {
                    FieldCipherUtil.encryptOrDecrypt(value, true);
                }
            }
        } else {
            // 直接加密对象
            FieldCipherUtil.encryptOrDecrypt(parameter, true);
        }
    }
}