/*
 * Copyright 2012-2020 the original author or authors.
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

package com.fuhouyu.framework.log.core;


/**
 * <p>
 * 日志记录实体类
 * </p>
 *
 * @author fuhouyu
 * @since 2024/8/14 11:57
 */
public class LogRecordEntity {

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

    public String getSystemName() {
        return systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getOperationUser() {
        return operationUser;
    }

    public void setOperationUser(String operationUser) {
        this.operationUser = operationUser;
    }

    public String getOperationTime() {
        return operationTime;
    }

    public void setOperationTime(String operationTime) {
        this.operationTime = operationTime;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Boolean getIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(Boolean success) {
        isSuccess = success;
    }

    @Override
    public String toString() {
        return "LogRecordEntity{" +
                "systemName='" + systemName + '\'' +
                ", moduleName='" + moduleName + '\'' +
                ", operationType='" + operationType + '\'' +
                ", content='" + content + '\'' +
                ", operationUser='" + operationUser + '\'' +
                ", operationTime='" + operationTime + '\'' +
                ", requestId='" + requestId + '\'' +
                ", category='" + category + '\'' +
                ", isSuccess=" + isSuccess +
                '}';
    }
}
