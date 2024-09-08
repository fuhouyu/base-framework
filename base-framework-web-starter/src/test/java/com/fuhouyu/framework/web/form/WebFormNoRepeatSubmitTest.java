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

package com.fuhouyu.framework.web.form;

import com.fuhouyu.framework.cache.CacheAutoConfigure;
import com.fuhouyu.framework.cache.CaffeineCacheAutoconfigure;
import com.fuhouyu.framework.model.response.RestResult;
import com.fuhouyu.framework.web.WebAutoConfigure;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.test.context.TestPropertySource;
import org.springframework.util.Assert;

/**
 * <p>
 * 表单防重复提交测试类
 * </p>
 *
 * @author fuhouyu
 * @since 2024/8/17 23:10
 */
@SpringBootTest(classes = {
        WebAutoConfigure.class,
        CacheAutoConfigure.class,
        CaffeineCacheAutoconfigure.class,
})
@TestPropertySource(locations = {"classpath:application.yaml"})
@EnableAspectJAutoProxy
class WebFormNoRepeatSubmitTest {

    @Autowired
    private FormTokenControllerTest formTokenControllerTest;

    @Test
    void testNoRepeatSubmit() {
        RestResult<Boolean> restResult = formTokenControllerTest.success();
        Assert.isTrue(restResult.getData(), "表单防重复提交验证失败。");
    }
}


