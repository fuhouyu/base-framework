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

package com.fuhouyu.framework.resource.enums;

/**
 * <p>
 * 文件资源上传的类型
 * </p>
 *
 * @author fuhouyu
 * @since 2024/8/16 18:16
 */
public enum ResourceUploadTypeEnum {
    /**
     * 本地
     */
    LOCAL,

    /**
     * 阿里云oss
     */
    ALI,

    /**
     * 华为云oss
     */
    HUAWEI,

    /**
     * 七牛oss
     */
    QI_NIU,


}
