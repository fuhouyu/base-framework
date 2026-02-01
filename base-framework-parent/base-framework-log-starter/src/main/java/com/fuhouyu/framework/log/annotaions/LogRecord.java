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

package com.fuhouyu.framework.log.annotaions;

import com.fuhouyu.framework.log.enums.OperationTypeEnum;
import com.fuhouyu.framework.log.enums.RiskTypeEnum;

import java.lang.annotation.*;

/**
 * <p>
 * 日志记录
 * </p>
 *
 * @author fuhouyu
 * @since 2024/8/14 09:07
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface LogRecord {

    /**
     * 日志记录的内容，可编写SpEL表达式
     *
     * @return 日志记录的内容
     */
    String content() default "";


    /**
     * 日志记录英文
     *
     * @return 日志记录
     */
    String contentEn() default "";

    /**
     * 操作类型
     *
     * @return 操作类型
     * @see OperationTypeEnum
     */
    OperationTypeEnum operationType();

    /**
     * 风险等级
     *
     * @return 风险等级
     * @see RiskTypeEnum
     */
    RiskTypeEnum riskType() default RiskTypeEnum.LOW_LEVEL;

    /**
     * 获取操作人
     *
     * @return 操作人
     */
    String operationUser() default "#{T(com.fuhouyu.framework.context.ContextHolder).user.username}";
}
