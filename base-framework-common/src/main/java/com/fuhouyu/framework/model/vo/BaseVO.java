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

package com.fuhouyu.framework.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 基础vo对象
 * </p>
 *
 * @author fuhouyu
 * @since 2024/8/13 18:25
 */
@Schema(name = "BaseVO", description = "基础响应的vo对象")
@ToString
@Getter
@Setter
public class BaseVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(name = "createUser", description = "创建者", requiredMode = Schema.RequiredMode.REQUIRED)
    private String createUser;

    @Schema(name = "createTime", description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;

    @Schema(name = "updateUser", description = "更新者", requiredMode = Schema.RequiredMode.REQUIRED)
    private String updateUser;

    @Schema(name = "updateTime", description = "更新时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime updateTime;
}
