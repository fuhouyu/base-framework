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

import com.fuhouyu.framework.common.utils.LoggerUtil;
import com.fuhouyu.framework.web.model.Ip2Region;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * <p>
 * ip2region template
 * </p>
 *
 * @author fuhouyu
 * @since 2025/3/12 21:05
 */
@Slf4j
@SpringBootTest(classes = {
        Ip2RegionConfiguration.class
})
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
class Ip2RegionTemplateTest {

    @Autowired
    private Ip2RegionTemplate ip2RegionTemplate;

    @Test
    void testIp2RegionTemplate() {
        Ip2Region ip2Region = this.ip2RegionTemplate.searchIp("103.127.218.229");
        Assertions.assertNotNull(ip2Region);
        LoggerUtil.info(log, "ip解析出的地址信息: {}", ip2Region);
    }
}
