package com.fuhouyu.framework.database;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * <p>
 * 分页查询的vo对象
 * </p>
 *
 * @author fuhouyu
 * @since 2026/3/3 19:39
 */
@Data
@Schema(description = "分页查询的vo对象")
public class PageQueryVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "当前页码", example = "1", defaultValue = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer pageNum;

    @Schema(description = "每页数量", example = "10", defaultValue = "10", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer pageSize;

    @Schema(description = "排序字段和排序方式，格式：字段1 asc, 字段2 desc",
            example = "createdAt desc, name asc", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String orderBy;
}
