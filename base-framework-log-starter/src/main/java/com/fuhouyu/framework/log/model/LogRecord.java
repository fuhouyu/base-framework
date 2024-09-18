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

package com.fuhouyu.framework.log.model;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <p>
 * 日志记录实体类
 * </p>
 *
 * @author fuhouyu
 * @since 2024/8/14 11:57
 */
@ToString
@Getter
@Setter
public class LogRecord {

    /**
     * 系统名称
     */
    private String systemName;

    /**
     * 模块名称
     */
    private String moduleName;

    /**
     * 操作类型
     */
    private String operationType;

    /**
     * 日志内容
     */
    private String content;

    /**
     * 错误信息，如果存在
     */
    private String errorMessage;

    /**
     * 操作人
     */
    private String operationUser;

    /**
     * 操作时间
     */
    private String operationTime;

    /**
     * 请求id
     */
    private String requestId;

    /**
     * 分类
     */
    private String category;

    /**
     * 操作状态 true/false
     */
    private Boolean isSuccess;

}
