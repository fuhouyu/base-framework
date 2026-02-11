/*
 * Copyright 2024-present fuhouyu.
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
package com.fuhouyu.framework.common.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fuhouyu.framework.common.enums.ResponseStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * <p>
 * 统一响应结果
 * </p>
 *
 * @author fuhouyu
 * @since 2026/2/1 17:35
 */
@Data
public class R<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * 响应描述信息
     */
    @Schema(description = "描述信息", example = "操作成功", requiredMode = Schema.RequiredMode.REQUIRED)
    private final String message;
    /**
     * 业务执行是否成功标志
     */
    @Schema(description = "执行结果", example = "true", requiredMode = Schema.RequiredMode.REQUIRED)
    private final boolean success;
    /**
     * 响应负载数据
     */
    @Schema(description = "负载数据")
    private final T data;
    /**
     * 响应状态码
     */
    @Schema(description = "状态码", example = "200", requiredMode = Schema.RequiredMode.REQUIRED)
    private String code;

    /**
     * 私有构造函数，强制通过静态工厂方法创建对象
     *
     * @param code    状态码
     * @param message 消息
     * @param success 成功标志
     * @param data    负载数据
     */
    @JsonCreator
    private R(@JsonProperty("code") String code,
              @JsonProperty("message") String message,
              @JsonProperty("success") boolean success,
              @JsonProperty("data") T data) {
        this.code = code;
        this.message = message;
        this.success = success;
        this.data = data;
    }

    /**
     * 构建成功响应结果（不带数据负载）
     * <p>默认状态码：200，默认消息：操作成功</p>
     *
     * @param <T> 数据泛型类型
     * @return 成功响应对象
     */
    public static <T> R<T> ok() {
        return ok(null);
    }

    /**
     * 构建成功响应结果（带数据负载）
     * <p>默认状态码：200，默认消息：操作成功</p>
     *
     * @param data 响应给前端的数据对象
     * @param <T>  数据泛型类型
     * @return 成功响应对象
     */
    public static <T> R<T> ok(T data) {
        return new R<>(ResponseStatusEnum.SUCCESS.getCode(), ResponseStatusEnum.SUCCESS.getMessage(), true, data);
    }

    /**
     * 根据预定义状态码构建失败响应
     *
     * @param status 基础响应状态接口实现（通常为枚举）
     * @param <T>    数据泛型类型
     * @return 失败响应对象
     */
    public static <T> R<T> fail(BaseResponseStatus status) {
        return fail(status.getCode(), status.getMessage(), null);
    }

    /**
     * 根据预定义状态码构建失败响应，并自定义错误描述信息
     * <p>常用于表单校验提示或业务规则冲突提示，覆盖枚举中的默认消息</p>
     *
     * @param status  基础响应状态接口实现
     * @param message 自定义错误描述信息
     * @param <T>     数据泛型类型
     * @return 失败响应对象
     */
    public static <T> R<T> fail(BaseResponseStatus status, String message) {
        return fail(status.getCode(), message, null);
    }

    /**
     * 根据预定义状态码构建失败响应，并携带详细的错误上下文数据
     *
     * @param status 基础响应状态接口实现
     * @param data   错误的详细上下文数据（如具体字段校验失败列表）
     * @param <T>    数据泛型类型
     * @return 失败响应对象
     */
    public static <T> R<T> fail(BaseResponseStatus status, T data) {
        return fail(status.getCode(), status.getMessage(), data);
    }

    /**
     * 基础失败构建方法（全参数版）
     * <p>通常用于全局异常处理器手动构建特定的错误响应</p>
     *
     * @param code    自定义响应状态码
     * @param message 自定义响应描述信息
     * @param data    错误详细上下文数据
     * @param <T>     数据泛型类型
     * @return 失败响应对象
     */
    public static <T> R<T> fail(String code, String message, T data) {
        return new R<>(code, message, false, data);
    }

    /**
     * 更新message
     *
     * @param newMessage message
     * @return R
     */
    public R<T> updateMessage(String newMessage) {
        return new R<>(this.code, newMessage, this.success, this.data);
    }

}
