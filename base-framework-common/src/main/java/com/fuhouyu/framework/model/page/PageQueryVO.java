/*
 * Copyright 2024-2034 the original author or authors.
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
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;

/**
 * <p>
 * 分页查询的vo对象
 * </p>
 *
 * @author fuhouyu
 * @since 2024/8/13 18:06
 */
@Schema(name = "pageQueryVO", description = "分页查询的vo对象")
@ToString
@Getter
@Setter
public class PageQueryVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(name = "pageNumber", description = "页号", requiredMode = Schema.RequiredMode.REQUIRED)
    private int pageNumber;

    @Schema(name = "pageSize", description = "每页显示条数", requiredMode = Schema.RequiredMode.REQUIRED)
    private int pageSize;

    @Schema(name = "orderCol", description = "排序字段", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String orderCol;

    @Schema(name = "orderDirection", description = """
            排序方式: ASC/DESC
            """,
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private PageOrderDirectionEnum orderDirection;

}
