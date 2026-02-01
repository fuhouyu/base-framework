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

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.ArrayList;
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


    /**
     * 页码
     */
    @Schema(name = "pageNum", description = "页码",
            requiredMode = Schema.RequiredMode.REQUIRED, defaultValue = "1")
    @JsonProperty("current")
    @JsonAlias("pageNum")
    private long current = 1;
    /**
     * 分页条数
     */
    @Schema(name = "pageSize", description = "分页条数",
            requiredMode = Schema.RequiredMode.REQUIRED, defaultValue = "10")
    @JsonProperty("size")
    @JsonAlias("pageSize")
    private long size = 10;

    @Hidden
    private List<OrderItem> orders = new ArrayList<>();


    @Override
    @Hidden
    public Page<T> setOptimizeCountSql(boolean optimizeCountSql) {
        super.setOptimizeCountSql(optimizeCountSql);
        return this;
    }

    @Override
    @Hidden
    public Page<T> setSearchCount(boolean searchCount) {
        super.setSearchCount(searchCount);
        return this;
    }

    @Override
    @Hidden
    public Page<T> setRecords(List<T> records) {
        super.setRecords(records);
        return this;
    }

    @Override
    @Hidden
    public IPage<T> setPages(long pages) {
        return super.setPages(pages);
    }

    @Override
    @Hidden
    public void setOrders(List<OrderItem> orders) {
        this.orders = orders;
    }

    @Override
    @Hidden
    public Page<T> setTotal(long total) {
        // 如果小于0，不要设置total值，让他默认就行
        if (total < 0) {
            return this;
        }
        super.setTotal(total);
        return this;
    }

    @Override
    public Page<T> setCurrent(long current) {
        this.current = current;
        return super.setCurrent(current);
    }

    @Override
    @Hidden
    public void setOptimizeJoinOfCountSql(boolean optimizeJoinOfCountSql) {
        super.setOptimizeJoinOfCountSql(optimizeJoinOfCountSql);
    }

    @Override
    @Hidden
    public void setMaxLimit(Long maxLimit) {
        super.setMaxLimit(maxLimit);
    }

    @Override
    public Page<T> setSize(long size) {
        this.size = size;
        return super.setSize(size);
    }

    public Page<T> setPageNum(long current) {
        return this.setCurrent(current);
    }

    public Page<T> setPageSize(long pageSize) {
        return this.setSize(pageSize);
    }

    @Override
    @Hidden
    public void setCountId(String countId) {
        super.setCountId(countId);
    }
}
