/*
 * Copyright 2024-2034 the original author or authors.
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

package com.fuhouyu.framework.kms;

import com.fuhouyu.framework.kms.service.KmsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.util.Assert;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * <p>
 * kms测试类
 * </p>
 *
 * @author fuhouyu
 * @since 2024/8/17 17:55
 */
@SpringBootTest(classes = {
        KmsAutoConfigure.class
})
@TestPropertySource(locations = {"classpath:application.yaml"})
class KmsServiceTest {

    private static final String ORIGIN_DATA = "test_enc";

    @Autowired
    private KmsService kmsService;

    @Test
    void testKmsService() {
        // 非对称加密
        byte[] asymmetricEncrypt = kmsService.asymmetricEncrypt(ORIGIN_DATA.getBytes(StandardCharsets.UTF_8));
        Assert.isTrue(Objects.equals(ORIGIN_DATA,
                new String(kmsService.asymmetricDecrypt(asymmetricEncrypt))), "非对称解密结果不一致");

        // 签名
        byte[] signature = kmsService.signature(ORIGIN_DATA.getBytes(StandardCharsets.UTF_8));
        Assert.isTrue(kmsService.verifyDigest(signature, ORIGIN_DATA.getBytes(StandardCharsets.UTF_8)),
                "摘要签名结果不一致");

        // 对称加解密
        byte[] symmetryEncrypt = kmsService.symmetryEncrypt(ORIGIN_DATA.getBytes(StandardCharsets.UTF_8));
        Assert.isTrue(Objects.equals(ORIGIN_DATA,
                new String(kmsService.symmetryDecrypt(symmetryEncrypt))), "对称解密结果不一致");


    }
}
