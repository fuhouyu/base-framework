/*
 * Copyright 2024-2024 the original author or authors.
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

package com.fuhouyu.framework.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;

/**
 * <p>
 * 基础响应
 * </p>
 *
 * @author fuhouyu
 * @since 2024/8/13 16:27
 */
@Schema(name = "RestResult", description = "基础响应，所有的响应都会被该类包装返回")
@ToString
@Getter
@Setter
@Builder
@AllArgsConstructor
public class RestResult<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 是否成功： true/false
     */
    @Schema(name = "isSuccess", description = "是否成功：true 成功", requiredMode = Schema.RequiredMode.REQUIRED)
    private Boolean isSuccess;

    /**
     * 响应码
     */
    @Schema(name = "code", description = "响应码", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer code;

    /**
     * 响应信息
     */
    @Schema(name = "message", description = "响应信息", requiredMode = Schema.RequiredMode.REQUIRED)
    private String message;

    /**
     * 响应数据
     */
    @Schema(name = "data", description = "响应数据，该值可能为空", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private transient T data;

    private RestResult() {

    }

    public Boolean getSuccess() {
        return isSuccess;
    }

    public void setSuccess(Boolean success) {
        isSuccess = success;
    }

}
