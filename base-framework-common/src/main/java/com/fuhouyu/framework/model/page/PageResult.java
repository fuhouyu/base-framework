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

package com.fuhouyu.framework.model.page;

import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 分页响应
 * </p>
 *
 * @author fuhouyu
 * @since 2024/8/13 18:03
 */
@Schema(name = "PageResult", description = "分页响应结果")
public class PageResult<E> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 列表总数
     */
    @Schema(name = "totalCount", description = "列表总数", requiredMode = Schema.RequiredMode.REQUIRED)
    private int totalCount;

    /**
     * 页号.
     */
    @Schema(name = "pageNumber", description = "页号", requiredMode = Schema.RequiredMode.REQUIRED)
    private int pageNumber;

    /**
     * 每页显示条数.
     */
    @Schema(name = "pageSize", description = "每页显示条数", requiredMode = Schema.RequiredMode.REQUIRED)
    private int pageSize;

    /**
     * 数据集.
     */
    @Schema(name = "list", description = "该值可能为空", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private List<E> list = Collections.emptyList();

    @Override
    public String toString() {
        return "PageResult{" +
                "totalCount=" + totalCount +
                ", pageNumber=" + pageNumber +
                ", pageSize=" + pageSize +
                ", list=" + list +
                '}';
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public List<E> getList() {
        return list;
    }

    public void setList(List<E> list) {
        this.list = list;
    }
}
