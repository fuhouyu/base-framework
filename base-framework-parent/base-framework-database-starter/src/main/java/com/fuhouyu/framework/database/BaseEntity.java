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
package com.fuhouyu.framework.database;

import org.babyfish.jimmer.sql.LogicalDeleted;
import org.babyfish.jimmer.sql.MappedSuperclass;

import java.time.OffsetDateTime;

/**
 * <p>
 * 基类
 * </p>
 *
 * @author fuhouyu
 * @since 2026/2/4 20:15
 */
@MappedSuperclass
public interface BaseEntity {

    /**
     * 创建人
     *
     * @return 创建人
     */
    String createdBy();

    /**
     * 创建时间
     *
     * @return 创建时间
     */
    OffsetDateTime createdAt();

    /**
     * 更新人
     *
     * @return 更新人
     */
    String updatedBy();

    /**
     * 更新时间
     *
     * @return 更新时间
     */
    OffsetDateTime updatedAt();

    /**
     * 是否删除
     *
     * @return 是否删除标记
     */
    @LogicalDeleted(value = "true")
    boolean getIsDeleted();
}
