/*
 * Copyright 2024-2025 fuhouyu.
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
package com.fuhouyu.framework.database;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fuhouyu.framework.context.ContextHolderStrategy;
import com.fuhouyu.framework.context.DefaultListableContextFactory;
import com.fuhouyu.framework.context.user.UserEntity;
import com.fuhouyu.framework.database.mapper.UserMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


/**
 * <p>
 * 数据库测试类
 * </p>
 *
 * @author fuhouyu
 * @since 2024/12/18 21:22
 */
@ExtendWith({SpringExtension.class})
@SpringBootTest
@SpringBootApplication
@MapperScan("com.fuhouyu.framework.database.mapper")
@TestPropertySource(locations = {"classpath:application.yaml"})
@Disabled
class DatabaseTest {

    @Autowired
    private UserMapper userMapper;

    @BeforeEach
    void setup() {
        DefaultListableContextFactory context = new DefaultListableContextFactory();
        UserEntity user = new UserEntity();
        user.setUsername("admin");
        user.setTenantId(1L);
        context.setUser(user);
        ContextHolderStrategy.setContext(context);
    }

    @Test
    void testUser() {
        List<Users> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Users users;
            if (i % 2 == 0) {
                users = this.buildUsers(i, ContextHolderStrategy.getContext().getUser().getTenantId());
            } else {
                users = this.buildUsers(i, 2L);
            }

            list.add(users);
        }
        this.userMapper.insert(list);
        Page<Users> page = new Page<>(1, 10);
        IPage<Users> results = this.userMapper.queryList(page);
        Assertions.assertEquals(results.getTotal(), list.size() / 2);


    }


    private Users buildUsers(Integer id, Long ownerTenantId) {
        Users users = new Users();
        users.setId(Long.valueOf(id));
        users.setUsername(UUID.randomUUID().toString());
        users.setPassword(UUID.randomUUID().toString());
        users.setCreateAt(LocalDateTime.now());
        users.setCreatedBy("admin");
        users.setOwnerTenantId(ownerTenantId);
        users.setUpdatedAt(LocalDateTime.now());
        users.setUpdatedBy("admin");
        return users;
    }
}
