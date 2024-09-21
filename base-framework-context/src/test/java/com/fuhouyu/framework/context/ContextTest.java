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

package com.fuhouyu.framework.context;


import com.fuhouyu.framework.context.user.DefaultUserDetail;
import com.fuhouyu.framework.context.user.User;
import com.fuhouyu.framework.context.user.UserContextHolder;
import com.fuhouyu.framework.context.user.UserContextImpl;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * <p>
 * 上下文测试类
 * </p>
 *
 * @author fuhouyu
 * @since 2024/9/8 12:15
 */
class ContextTest {


    @Test
    void testUserContext() {
        Context<User> context = new UserContextImpl();

        String username = UUID.randomUUID().toString().replace("-", "").substring(0, 16);
        DefaultUserDetail defaultUserDetail = new DefaultUserDetail();
        defaultUserDetail.setUsername(username);
        context.setObject(defaultUserDetail);
        UserContextHolder.setContext(context);

        assertNotNull(UserContextHolder.getContext(), "用户上下文为空");
        assertNotNull(UserContextHolder.getContext().getObject(), "用户上下文中存储的用户为空");
        assertEquals(username, UserContextHolder.getContext().getObject().getUsername());

        UserContextHolder.clearContext();
        assertNull(UserContextHolder.getContext(), "用户上下文未清除");
    }
}
