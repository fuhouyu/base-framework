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
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.function.Function;

/**
 * <p>
 * 分页返回的dto对象
 * </p>
 *
 * @author fuhouyu
 * @since 2024/10/11 15:22
 */
@Getter
@Setter
@ToString(callSuper = true)
@Schema(name = "PageResultDTO", description = "分页结果的dto对象")
public class PageResultDTO<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1238719237918263821L;

    @Schema(name = "pageNum", description = "页号", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long pageNum;

    @Schema(name = "pageSize", description = "每页显示条数", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long pageSize;

    @Schema(name = "total", description = "总条数", requiredMode = Schema.RequiredMode.REQUIRED, implementation = String.class)
    private Long total;

    @Schema(name = "list", description = "分页数据")
    private transient List<T> list;

    public PageResultDTO(Long pageNum, Long pageSize, Long total) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.total = total;
    }

    public PageResultDTO(Long pageNum, Long pageSize, Long total, List<T> list) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.total = total;
        this.list = list;
    }

    /**
     * 构建分页结果
     *
     * @param page 分页
     * @return 分页结果
     */
    public static <T> PageResultDTO<T> buildPageResult(IPage<T> page) {
        return new PageResultDTO<>(page.getCurrent(), page.getSize(), page.getTotal(), page.getRecords());
    }


    /**
     * 构建分页结果
     *
     * @param page      分页
     * @param converter 转换器
     * @return 分页结果
     */
    public static <T, R> PageResultDTO<R> buildPageResult(IPage<T> page, Function<List<T>, List<R>> converter) {
        return new PageResultDTO<>(page.getCurrent(), page.getSize(), page.getTotal(), converter.apply(page.getRecords()));
    }
}
