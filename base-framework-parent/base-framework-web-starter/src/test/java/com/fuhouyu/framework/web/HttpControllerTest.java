/*
 * Copyright 2024-present fuhouyu.
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

import com.fuhouyu.framework.cache.RedisCacheAutoConfiguration;
import com.fuhouyu.framework.common.response.R;
import com.fuhouyu.framework.common.utils.HexUtil;
import com.fuhouyu.framework.common.utils.JacksonUtil;
import com.fuhouyu.framework.kms.KmsAutoConfiguration;
import com.fuhouyu.framework.kms.service.KmsService;
import com.fuhouyu.framework.web.annotaions.PrepareHttpBody;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.context.MessageSourceAutoConfiguration;
import org.springframework.boot.data.redis.autoconfigure.DataRedisAutoConfiguration;
import org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration;
import org.springframework.boot.jdbc.autoconfigure.JdbcTemplateAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

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
@SpringBootTest(classes = {
        RedisCacheAutoConfiguration.class,
        DataRedisAutoConfiguration.class,
        MessageSourceAutoConfiguration.class,
        DataSourceAutoConfiguration.class,
        JdbcTemplateAutoConfiguration.class,
        KmsAutoConfiguration.class,
        WebAutoConfiguration.class,
})
@AutoConfigureMockMvc
@ActiveProfiles("test")
@EnableWebMvc
class HttpControllerTest {
    @SuppressWarnings("resource")
    static final GenericContainer<?> REDIS_GENERIC_CONTAINER =
            new GenericContainer<>(DockerImageName.parse("redis:7.2-alpine"))
                    .withCommand("redis-server --requirepass password")
                    .withExposedPorts(6379);

    static {
        REDIS_GENERIC_CONTAINER.start();
        System.setProperty("spring.data.redis.host", REDIS_GENERIC_CONTAINER.getHost());
        System.setProperty("spring.data.redis.port", REDIS_GENERIC_CONTAINER.getMappedPort(6379).toString());
    }


    @Autowired
    private KmsService kmsService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testEnc() throws Exception {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("username", "fuhouyu");
        paramMap.put("password", "fuhouyu");

        byte[] bodyBytes = kmsService.asymmetricEncrypt(JacksonUtil.toBytes(paramMap));

        Map<String, String> map = new HashMap<>();
        map.put("body", HexUtil.encodeToHexString(bodyBytes));
        mockMvc.perform(
                MockMvcRequestBuilders.post("/v1/test/enc")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JacksonUtil.toBytes(map))
        ).andExpect(status().isOk());

    }

    @RestController
    public static class HttpController {


        @PostMapping("/v1/test/enc")
        @PrepareHttpBody
        public R<Object> post(@RequestBody Map<String, String> body) {
            return R.ok(body);
        }

    }

}
