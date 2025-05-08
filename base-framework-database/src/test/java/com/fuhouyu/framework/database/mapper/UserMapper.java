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
package com.fuhouyu.framework.database.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fuhouyu.framework.database.Users;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author fuhouyu
 * @since 2024/12/18 21:24
 */
@Mapper
public interface UserMapper {

    void insert(@Param("list") List<Users> list);

    void delete(Long id);

    Users queryById(Long id);

    IPage<Users> queryList(IPage<Users> page);
}
