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

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 基类dto响应
 * </p>
 *
 * @author fuhouyu
 * @since 2024/10/9 16:55
 */
@Schema(name = "BaseDTO", description = "基类响应dto对象")
@Getter
@Setter
@ToString
public class BaseDTO implements Serializable {

    @Schema(name = "createdAt", description = "创建时间，仅返回", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createdAt;

    @Schema(name = "updatedAt", description = "更新时间，仅返回", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime updatedAt;

    @Schema(name = "createdBy", description = "创建人，仅返回", requiredMode = Schema.RequiredMode.REQUIRED)
    private String createdBy;

    @Schema(name = "updatedBy", description = "操作人，仅返回", requiredMode = Schema.RequiredMode.REQUIRED)
    private String updatedBy;
}
