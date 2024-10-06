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

package com.fuhouyu.framework.web.utils;

import com.fuhouyu.framework.context.user.DefaultUserDetail;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * <p>
 * bean测试类
 * </p>
 *
 * @author fuhouyu
 * @since 2024/8/19 18:37
 */

class BeanConvertUtilTest {

    private DefaultUserDetail userDetail;

    @BeforeEach
    void setUp() {
        userDetail = new DefaultUserDetail();
        userDetail.setUsername("test");
        userDetail.setGender("男");
        userDetail.setId(11L);
    }

    @Test
    void testBeanConvert() {
        DefaultUserDetail copyUserDetail = BeanConvertUtil.convertTo(userDetail, DefaultUserDetail::new);
        Assertions.assertEquals(copyUserDetail.getId(), userDetail.getId(), "对象复制失败");

    }
}
