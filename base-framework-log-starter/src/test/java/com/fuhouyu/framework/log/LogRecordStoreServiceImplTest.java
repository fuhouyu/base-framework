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

package com.fuhouyu.framework.log;

import com.fuhouyu.framework.log.annotaions.LogRecord;
import com.fuhouyu.framework.log.core.LogRecordStoreService;
import com.fuhouyu.framework.log.enums.OperationTypeEnum;
import com.fuhouyu.framework.response.ResponseHelper;
import com.fuhouyu.framework.response.RestResult;
import com.fuhouyu.framework.utils.LoggerUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.context.annotation.Bean;
import org.springframework.util.Assert;

/**
 * <p>
 * 日志存储测试类
 * </p>
 *
 * @author fuhouyu
 * @since 2024/8/17 23:26
 */
@TestComponent
public class LogRecordStoreServiceImplTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(LogRecordStoreServiceImplTest.class);

    public String getUsername() {
        return "test-user";
    }

    @LogRecord(content = """
            #{'currentUserName = ' + getUsername() + " result is: " + #result.getData()}""", operationType = OperationTypeEnum.QUERY)
    public RestResult<Boolean> success() {
        return ResponseHelper.success(true);
    }

    @Bean
    public LogRecordStoreService logRecordStoreServiceTest() {
        return logRecord -> {
            LoggerUtil.info(LOGGER, "测试默认的日志记录存储:{}", logRecord);
            Assert.notNull(logRecord, "logRecord is null");
        };
    }
}
