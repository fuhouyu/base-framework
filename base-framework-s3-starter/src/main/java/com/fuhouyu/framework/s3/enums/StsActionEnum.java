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
package com.fuhouyu.framework.s3.enums;

/**
 * <p>
 * sts 操作类型枚举类
 * </p>
 *
 * @author fuhouyu
 * @since 2025/2/22 19:05
 */
public enum StsActionEnum {


    /**
     * 上传对象
     */
    PutObject,

    /**
     * 获取对象
     */
    GetObject,

    /**
     * 删除对象
     */
    DeleteObject,

    /**
     * 列出对象
     */
    ListObjects,
}
