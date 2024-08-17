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

package com.fuhouyu.framework.web;

import com.fuhouyu.framework.web.config.WebMvcAutoConfigure;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

/**
 * <p>
 * web测试类
 * </p>
 *
 * @author fuhouyu
 * @since 2024/8/17 11:47
 */
@SpringBootTest(classes = {
        WebAutoConfigure.class
})
@TestPropertySource(locations = {"classpath:application.yaml"})
@EnabledIfSystemProperty(named = "run.tests", matches = "true")
class WebHandlerTest {


    @Autowired
    private WebMvcAutoConfigure webMvcAutoConfigure;


    @Test
    void testAutoConfigure() {
        InterceptorRegistry interceptorRegistry = new InterceptorRegistry();
        webMvcAutoConfigure.addInterceptors(interceptorRegistry);
    }
}
