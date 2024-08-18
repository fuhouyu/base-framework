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

package com.fuhouyu.framework.log;

import com.fuhouyu.framework.context.user.DefaultUserDetail;
import com.fuhouyu.framework.context.user.UserContext;
import com.fuhouyu.framework.context.user.UserContextHolder;
import com.fuhouyu.framework.model.response.RestResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.test.context.TestPropertySource;
import org.springframework.util.Assert;

/**
 * <p>
 * 日志记录测试
 * </p>
 *
 * @author fuhouyu
 * @since 2024/8/17 23:27
 */
@SpringBootTest(classes = {
        LogRecordAutoConfiguration.class
})
@TestPropertySource(locations = {"classpath:application.yaml"})
@EnabledIfSystemProperty(named = "run.tests", matches = "true")
@EnableAspectJAutoProxy
class LogRecordStoreTest {

    @Autowired
    private LogRecordStoreServiceImplTest logRecordStoreServiceImplTest;

    @BeforeEach
    void setUp() {
        UserContext context = UserContextHolder.createEmptyContext();
        DefaultUserDetail defaultUserDetail = new DefaultUserDetail();
        defaultUserDetail.setUsername("test-username");
        context.setUser(defaultUserDetail);

    }

    @Test
    void testLogStoreService() {
        RestResult<Boolean> result = logRecordStoreServiceImplTest.success();
        Assert.isTrue(result.getData(), "日志返回结果不正确");

    }
}
