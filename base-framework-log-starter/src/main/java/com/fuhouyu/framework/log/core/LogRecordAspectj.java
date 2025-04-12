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

package com.fuhouyu.framework.log.core;

import com.fuhouyu.framework.common.utils.JacksonUtil;
import com.fuhouyu.framework.common.utils.LoggerUtil;
import com.fuhouyu.framework.log.annotaions.LogModule;
import com.fuhouyu.framework.log.annotaions.LogRecord;
import com.fuhouyu.framework.log.exception.LogException;
import com.fuhouyu.framework.log.model.LogRecordEntity;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.expression.ParseException;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.*;

/**
 * <p>
 * 日志记录切面
 * </p>
 *
 * @author fuhouyu
 * @since 2024/8/14 11:39
 */

@Aspect
@Slf4j
public class LogRecordAspectj {


    private final ParameterNameDiscoverer discoverer = new DefaultParameterNameDiscoverer();

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
    public void doAfterReturning(JoinPoint joinPoint, com.fuhouyu.framework.log.annotaions.LogRecord logRecord, Object jsonResult) {
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

    /**
     * 处理日志信息
     *
     * @param joinPoint    切入点
     * @param logRecord    日志注解
     * @param e            异常信息
     * @param objectResult 返回结果
     */
    protected void handleLog(final JoinPoint joinPoint, LogRecord logRecord, final Exception e, Object objectResult) {
        LogRecordEntity logRecordEntity = this.buildLogRecordEntity(e,
                joinPoint, logRecord, objectResult);
        for (LogRecordStoreService logRecordStoreService : logRecordStoreServiceList) {
            logRecordStoreService.saveLogRecord(logRecordEntity);
        }
    }

    /**
     * 获取方法评估上下文
     *
     * @param joinPoint 切入点
     * @return 方法评估上下文
     */
    private MethodBasedEvaluationContext getMethodBasedEvaluationContext(JoinPoint joinPoint) {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();
        Object[] args = joinPoint.getArgs();
        MethodBasedEvaluationContext context = new MethodBasedEvaluationContext(joinPoint.getTarget(),
                method,
                args,
                discoverer);
        context.setBeanResolver(this.beanFactoryResolver);
        return context;
    }

    /**
     * 构建日志实体
     *
     * @param exception 异常
     * @param joinPoint 切入点
     * @return 日志记录实体
     */
    private LogRecordEntity buildLogRecordEntity(Exception exception,
                                                 JoinPoint joinPoint,
                                                 LogRecord logRecord,
                                                 Object objectResult) {
        LogModule module = joinPoint.getTarget().getClass().getAnnotation(LogModule.class);
        LogRecordEntity logRecordEntity = new LogRecordEntity();
        logRecordEntity.setModuleName(Objects.isNull(module) ? "" : module.value());

        MethodBasedEvaluationContext context = getMethodBasedEvaluationContext(joinPoint);
        // 如果返回值存在，则设置返回值
        // 使SpEL表达式可以获取到结果中的值
        Optional.ofNullable(objectResult)
                .ifPresent(o -> {
                    context.setVariable("result", o);
                    logRecordEntity.setResponseData(JacksonUtil.writeValueAsString(o));
                });
        Optional.ofNullable(exception)
                .ifPresent(e -> {
                    logRecordEntity.setIsSuccess(false);
                    logRecordEntity.setErrorMessage(exception.getMessage());
                });
        String logContent = this.parseContent(logRecord.content(), context);
        String logContentEn = this.parseContent(logRecord.contentEn(), context);
        String operationUser = this.parseContent(logRecord.operationUser(), context);
        logRecordEntity.setOperationType(logRecord.operationType().name());
        logRecordEntity.setContent(logContent);
        logRecordEntity.setContentEn(logContentEn);
        logRecordEntity.setRiskType(logRecord.riskType().name());
        logRecordEntity.setSystemName(systemName);
        logRecordEntity.setOperationUser(operationUser);
        this.processMethodParameters(joinPoint, logRecordEntity);

        return logRecordEntity;
    }

    /**
     * 解析日志具体内容
     *
     * @param content 日志内容
     * @param context 上下文
     * @return 解析后的信息
     */
    private String parseContent(String content,
                                MethodBasedEvaluationContext context) {
        if (!StringUtils.hasText(content)) {
            return "";
        }
        try {
            return String.valueOf(evaluator.parse(content, context));
        } catch (ParseException ex) {
            LoggerUtil.error(log, "log content: {} parse failed", content, ex);
            throw new LogException(ex);
        } catch (Exception ex) {
            LoggerUtil.error(log, "log other error: {} ", content, ex);
            throw new LogException(ex);
        }
    }


    /**
     * 处理方法参数
     *
     * @param joinPoint       切入点
     * @param logRecordEntity 日志记录
     */
    private void processMethodParameters(JoinPoint joinPoint, LogRecordEntity logRecordEntity) {
        Object[] args = joinPoint.getArgs();
        if (args.length == 0) {
            logRecordEntity.setRequestParam("");
            return;
        }
        // 获取方法签名
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String[] parameterNames = signature.getParameterNames();
        Class<?>[] parameterTypes = signature.getParameterTypes();

        // 创建参数名-值映射
        Map<String, Object> paramMap = new LinkedHashMap<>();

        for (int i = 0; i < args.length; i++) {
            String paramName = parameterNames != null && i < parameterNames.length
                    ? parameterNames[i]
                    : "arg" + i;

            // 处理参数值为null的情况
            Object paramValue = args[i] != null ? args[i] : "null";

            // 对于简单类型直接存储，复杂类型转换为JSON字符串
            if (isSimpleType(parameterTypes[i])) {
                paramMap.put(paramName, paramValue);
            } else {
                paramMap.put(paramName, JacksonUtil.writeValueAsString(paramValue));
            }
        }
        // 将参数Map转换为JSON字符串
        logRecordEntity.setRequestParam(JacksonUtil.writeValueAsString(paramMap));
    }

    /**
     * 判断是否为简单类型
     *
     * @param clazz class
     * @return true / false
     */
    private boolean isSimpleType(Class<?> clazz) {
        return clazz.isPrimitive() ||
                clazz.equals(String.class) ||
                Number.class.isAssignableFrom(clazz) ||
                clazz.equals(Boolean.class) ||
                clazz.equals(Character.class) ||
                clazz.equals(java.util.Date.class) ||
                clazz.equals(java.time.temporal.Temporal.class);
    }
}
