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

/**
 * <p>
 * 响应状态码
 * </p>
 *
 * @author fuhouyu
 * @since 2024/8/13 17:39
 */
public interface BaseResponseCode {

    /**
     * 获取响应码
     *
     * @return 响应码
     */
    int getCode();

    /**
     * 获取具体的响应信息
     *
     * @return 响应信息
     */
    String getMessage();
}
