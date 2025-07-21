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
package com.fuhouyu.framework.database.base;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 分页查询的dto对象
 * </p>
 *
 * @author fuhouyu
 * @since 2024/9/20 17:49
 */
@ToString(callSuper = true)
@Getter
@Setter
@Slf4j
@Schema(name = "PageQueryDTO", description = "pageQueryDTO")
public class PageQueryDTO<T> extends Page<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = 123876123971923123L;

    @Schema(name = "pageNum", description = "页号", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long pageNum = 1L;

    @Schema(name = "pageSize", description = "每页显示条数", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long pageSize = 10L;

    public PageQueryDTO() {
        super();
        super.addOrder(OrderItem.desc("updated_by"));
    }

    public PageQueryDTO(List<OrderItem> orderItems) {
        super();
        super.setOrders(orderItems);
    }

    public PageQueryDTO(Long pageNum, Long pageSize) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
    }

    @Override
    public long getCurrent() {
        return this.pageNum;
    }

    @Override
    public long getSize() {
        return this.pageSize;
    }
}
