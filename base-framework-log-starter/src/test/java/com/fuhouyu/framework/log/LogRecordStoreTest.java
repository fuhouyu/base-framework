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

import com.fuhouyu.framework.context.ContextHolderStrategy;
import com.fuhouyu.framework.context.DefaultListableContextFactory;
import com.fuhouyu.framework.context.User;
import com.fuhouyu.framework.log.annotaions.LogRecord;
import com.fuhouyu.framework.log.core.LogRecordStoreService;
import com.fuhouyu.framework.log.enums.OperationTypeEnum;
import com.fuhouyu.framework.utils.LoggerUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.test.context.TestPropertySource;
import org.springframework.util.Assert;

import java.util.Map;

/**
 * <p>
 * 日志记录测试
 * </p>
 *
 * @author fuhouyu
 * @since 2024/8/17 23:27
 */
@SpringBootTest
@SpringBootApplication
@TestPropertySource(locations = {"classpath:application.yaml"})
@EnableAspectJAutoProxy
class LogRecordStoreTest {

    @Autowired
    private WebController webController;

    @BeforeEach
    void setUp() {
        DefaultListableContextFactory contextFactory = new DefaultListableContextFactory();
        contextFactory.setUser(new User() {
            @Override
            public Long getId() {
                return 0L;
            }

            @Override
            public String getUsername() {
                return "test-username";
            }

            @Override
            public String getRealName() {
                return "";
            }

            @Override
            public String getNickname() {
                return "";
            }

            @Override
            public String getGender() {
                return "";
            }

            @Override
            public String getRefAccountId() {
                return "";
            }

            @Override
            public Map<String, Object> getAdditionalInformation() {
                return Map.of();
            }
        });

        ContextHolderStrategy.setContext(contextFactory);
    }

    @Test
    void testLogStoreService() {
        boolean result = webController.success("success");
        Assert.isTrue(result, "日志返回结果不正确");
    }

    @TestComponent
    @Slf4j
    public static class WebController {


        @LogRecord(content = """
                #{'currentUserName = ' + T(com.fuhouyu.framework.context.ContextHolderStrategy).context.user.username 
                + " query param is: " + #query
                + " result is: " + #result}""", operationType = OperationTypeEnum.QUERY)
        public Boolean success(String query) {
            return true;
        }

        @Bean
        public LogRecordStoreService logRecordStoreServiceTest() {
            return logRecord -> {
                LoggerUtil.info(log, "测试默认的日志记录存储:{}", logRecord);
                Assert.notNull(logRecord, "logRecord is null");
            };
        }
    }
}
