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

package com.fuhouyu.framework.web.fileter;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fuhouyu.framework.kms.service.KmsService;
import com.fuhouyu.framework.utils.HexUtil;
import com.fuhouyu.framework.utils.JacksonUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * <p>
 * http controller测试类
 * </p>
 *
 * @author fuhouyu
 * @since 2024/8/21 23:15
 */
@SpringBootTest
@ExtendWith(SpringExtension.class)
@TestPropertySource(locations = {"classpath:application.yaml"})
@AutoConfigureMockMvc
class HttpControllerTest {


    @Autowired
    private KmsService kmsService;
    @Autowired
    private MockMvc mockMvc;

    @Test
    void testEnc() throws Exception {
        ObjectNode objectNode = JacksonUtil.getObjectMapper().createObjectNode();
        objectNode.put("username", "fuhouyu");
        objectNode.put("password", "fuhouyu");

        byte[] bodyBytes = kmsService.asymmetricEncrypt(JacksonUtil.writeValueAsBytes(objectNode));

        Map<String, String> map = new HashMap<>();
        map.put("body", HexUtil.encodeToHexString(bodyBytes));
//        mockMvc.perform(
//                MockMvcRequestBuilders.get("/base/v1/form/token")
//
//        ).andExpect(status().isOk());
        mockMvc.perform(
                MockMvcRequestBuilders.post("/v1/test/enc")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JacksonUtil.writeValueAsBytes(map))
        ).andExpect(status().isOk());

    }

}
