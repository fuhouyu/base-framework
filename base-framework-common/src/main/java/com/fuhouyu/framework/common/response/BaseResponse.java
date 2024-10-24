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
package com.fuhouyu.framework.common.response;

import java.io.Serializable;

/**
 * <p>
 * 基类响应
 * </p>
 *
 * @author fuhouyu
 * @since 2024/10/16 20:11
 */
public interface BaseResponse<T> extends Serializable {

    /**
     * 获取响应
     *
     * @return 响应编码
     */
    Integer getCode();

    /**
     * 获取响应信息
     *
     * @return 响应信息
     */
    String getMessage();

    /**
     * 获取成功/错误响应
     *
     * @return 成功/错误
     */
    Boolean getIsSuccess();

    /**
     * 获取响应data信息
     *
     * @return data信息
     */
    T getData();
}
