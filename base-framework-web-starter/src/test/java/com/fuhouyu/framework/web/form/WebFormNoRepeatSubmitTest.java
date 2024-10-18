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

package com.fuhouyu.framework.web.form;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fuhouyu.framework.cache.CacheAutoConfigure;
import com.fuhouyu.framework.kms.KmsAutoConfigure;
import com.fuhouyu.framework.response.BaseResponse;
import com.fuhouyu.framework.utils.JacksonUtil;
import com.fuhouyu.framework.web.WebAutoConfigure;
import com.fuhouyu.framework.web.annotaions.NoRepeatSubmit;
import com.fuhouyu.framework.web.enums.ErrorLevelEnum;
import com.fuhouyu.framework.web.reponse.ErrorResponse;
import com.fuhouyu.framework.web.reponse.ResponseHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * <p>
 * 表单防重复提交测试类
 * </p>
 *
 * @author fuhouyu
 * @since 2024/8/17 23:10
 */
@SpringBootTest(classes = {
        KmsAutoConfigure.class,
        WebAutoConfigure.class,
        CacheAutoConfigure.class
})
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@EnableWebMvc
@TestPropertySource(locations = {"classpath:application.yaml"})
@EnableAspectJAutoProxy
class WebFormNoRepeatSubmitTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testNoRepeatSubmit() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/v1/noRepeatSubmit")
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk()).andReturn();

        BaseResponse<ErrorLevelEnum> baseResponse = JacksonUtil.readValue(mvcResult.getResponse().getContentAsString(),
                new TypeReference<ErrorResponse<ErrorLevelEnum>>() {
                });
        Assertions.assertEquals(400, baseResponse.getCode());
    }

    @RestController
    public static class FormTokenController {

        @NoRepeatSubmit
        @PostMapping("/v1/noRepeatSubmit")
        public BaseResponse<Boolean> success() {
            return ResponseHelper.success(true);
        }

    }
}


