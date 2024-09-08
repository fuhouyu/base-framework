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

package com.fuhouyu.framework.security;

import com.fuhouyu.framework.cache.CacheAutoConfigure;
import com.fuhouyu.framework.cache.CaffeineCacheAutoconfigure;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.util.Assert;

/**
 * <p>
 * 国密算法密码编码测试
 * </p>
 *
 * @author fuhouyu
 * @since 2024/9/7 22:32
 */
@SpringBootTest(classes = {
        SecurityAutoConfigure.class,
        CacheAutoConfigure.class,
        CaffeineCacheAutoconfigure.class,
})
@TestPropertySource(locations = {"classpath:application.yaml"})
class Sm3PasswordEncoderTest {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void testSm3PasswordEncoder() {
        String encodePassword = passwordEncoder.encode("test123");
        Assert.isTrue(passwordEncoder.matches("test123", encodePassword),
                "sm3 正确密码验证失败");

        Assert.isTrue(!passwordEncoder.matches("test124", encodePassword),
                "sm3 错误密码验证失败");
    }
}
