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

import com.fuhouyu.framework.context.user.UserContextHolder;
import com.fuhouyu.framework.log.annotaions.LogRecord;
import com.fuhouyu.framework.utils.JacksonUtil;
import com.fuhouyu.framework.utils.LoggerUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.expression.ParseException;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * <p>
 * 日志记录切面
 * </p>
 *
 * @author fuhouyu
 * @since 2024/8/14 11:39
 */

@Aspect
public class LogRecordAspectj {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private static final Logger LOGGER = LoggerFactory.getLogger(LogRecordAspectj.class);

    /**
     * 日志SpEL解析器
     */
    private final LogEvaluator evaluator = new LogEvaluator();

    /**
     * 日志存储接口
     */
    private final List<LogRecordStoreService> logRecordStoreServiceList;

    /**
     * 系统名称
     */
    private final String systemName;

    /**
     * bean工厂解析器
     */
    private final BeanFactoryResolver beanFactoryResolver;

    public LogRecordAspectj(List<LogRecordStoreService> logRecordStoreServiceList, String systemName, BeanFactoryResolver beanFactoryResolver) {
        this.logRecordStoreServiceList = logRecordStoreServiceList;
        this.systemName = systemName;
        this.beanFactoryResolver = beanFactoryResolver;
    }

    /**
     * 处理完请求后执行
     *
     * @param joinPoint  切点
     * @param logRecord  log注解
     * @param jsonResult 返回参数
     */
    @AfterReturning(pointcut = "@annotation(logRecord)", returning = "jsonResult")
    public void doAfterReturning(JoinPoint joinPoint, LogRecord logRecord, Object jsonResult) {
        handleLog(joinPoint, logRecord, null, jsonResult);
    }

    /**
     * 拦截异常操作
     *
     * @param joinPoint 切点
     * @param logRecord log注解
     * @param e         异常
     */
    @AfterThrowing(value = "@annotation(logRecord)", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, LogRecord logRecord, Exception e) {
        handleLog(joinPoint, logRecord, e, null);
    }

    protected void handleLog(final JoinPoint joinPoint, LogRecord logRecord, final Exception e, Object objectResult) {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();
        Object[] args = joinPoint.getArgs();
        Class<?> targetClass = AopProxyUtils.ultimateTargetClass(joinPoint.getTarget());
        LogEvaluationContextRootObject rootObject = new LogEvaluationContextRootObject(method, args, targetClass);
        MethodBasedEvaluationContext context = new MethodBasedEvaluationContext(rootObject,
                rootObject.method(),
                rootObject.args(),
                evaluator.getDiscoverer());
        context.setBeanResolver(this.beanFactoryResolver);
        // 如果返回值存在，则设置返回值
        // 使SpEL表达式可以获取到结果中的值
        Optional.ofNullable(objectResult)
                .ifPresent(o -> context.setVariable("result", o));
        Object content = null;
        String result;
        try {
            content = evaluator.parse(logRecord.content(), context);
            result = JacksonUtil.writeValueAsString(content);
        } catch (ParseException ex) {
            LoggerUtil.error(LOGGER, "log content: {} parse failed", logRecord.content(), ex);
            return;
        } catch (Exception ex) {
            LoggerUtil.error(LOGGER, "log other error: {} ", content, ex);
            return;
        }
        LogRecordEntity logRecordEntity = new LogRecordEntity();
        logRecordEntity.setSystemName(systemName);
        logRecordEntity.setModuleName(logRecord.moduleName());
        logRecordEntity.setOperationType(logRecord.operationType().name());

        logRecordEntity.setContent(Objects.isNull(e) ? result : String.format("%s method execute error, message: %s",
                methodSignature.getMethod().getName(), e.getMessage()));
        logRecordEntity.setOperationUser(UserContextHolder.getContext().getUser().getUsername());
        logRecordEntity.setOperationTime(LocalDateTime.now().format(DATE_TIME_FORMATTER));
        logRecordEntity.setCategory(logRecordEntity.getCategory());
        logRecordEntity.setIsSuccess(e == null);
        for (LogRecordStoreService logRecordStoreService : logRecordStoreServiceList) {
            logRecordStoreService.saveLogRecord(logRecordEntity);
        }

    }
}
