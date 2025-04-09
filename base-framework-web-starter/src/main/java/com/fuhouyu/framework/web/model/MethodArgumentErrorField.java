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
package com.fuhouyu.framework.web.model;

import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serial;
import java.io.Serializable;

/**
 * <p>
 * 方法参数异常
 * </p>
 *
 * @author fuhouyu
 * @since 2025/4/9 10:14
 */
@Schema(name = "MethodArgumentErrorField", description = "方法参数异常")
public record MethodArgumentErrorField(@Schema(name = "code", description = "错误码") Integer code,
                                       @Schema(name = "field", description = "字段") String field,
                                       @Schema(name = "message", description = "错误信息") String message,
                                       @Schema(name = "errorLevel", description = "错误等级") String errorLevel) implements Serializable {

    @Serial
    private static final long serialVersionUID = -9128375761423651243L;

}
