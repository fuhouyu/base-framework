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

package com.fuhouyu.framework.log.model;


import com.fuhouyu.framework.context.ContextHolder;
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

    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

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
     * 请求ip
     */
    private String requestIp;

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

    /**
     * 错误信息
     */
    private String errorMessage;

    /**
     * 消耗时间
     */
    private Long costTime;


    public LogRecordEntity() {
        this.isSuccess = true;
        this.operationTime = LocalDateTime.now().format(DATE_TIME_FORMATTER);
        this.initRequest();
    }

    /**
     * 初始化请求
     */
    private void initRequest() {
        Request request = ContextHolder.getContext().getRequest();
        if (Objects.nonNull(request)) {
            HttpServletRequest httpServletRequest = request.getHttpServletRequest();
            this.requestUri = httpServletRequest.getRequestURI();
            this.requestMethod = httpServletRequest.getMethod();
            this.requestIp = request.getRequestIp();
        }
    }
}
