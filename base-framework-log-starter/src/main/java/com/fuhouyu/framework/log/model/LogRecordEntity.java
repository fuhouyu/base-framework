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

package com.fuhouyu.framework.log.model;


import com.fuhouyu.framework.context.ContextHolderStrategy;
import com.fuhouyu.framework.context.request.Request;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

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
public class LogRecordEntity {

    /**
     * 模块名称
     */
    private String moduleName;

    /**
     * 请求地址
     */
    private String requestUri;

    /**
     * 请求方法
     */
    private String requestMethod;

    /**
     * 操作类型
     */
    private String operationType;

    /**
     * 日志内容
     */
    private String content;

    /**
     * 日志内容英文
     */
    private String contentEn;

    /**
     * 操作人
     */
    private String operationUser;

    /**
     * 操作时间
     */
    private String operationTime;

    /**
     * 操作状态 true/false
     */
    private Boolean isSuccess;

    /**
     * 操作风险类型
     */
    private String riskType;

    /**
     * 请求参数
     */
    private String requestParam;

    /**
     * 响应
     */
    private String responseData;

    /**
     * 系统名称
     */
    private String systemName;


    public LogRecordEntity() {
        this.isSuccess = true;
        this.operationTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        this.initRequest();
    }

    /**
     * 初始化请求
     */
    private void initRequest() {
        Request request = ContextHolderStrategy.getContext().getRequest();
        if (Objects.nonNull(request)) {
            HttpServletRequest httpServletRequest = request.getHttpServletRequest();
            this.requestUri = httpServletRequest.getRequestURI();
            this.requestMethod = httpServletRequest.getMethod();
        }
    }
}
