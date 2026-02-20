/*
 * Copyright 2024-present fuhouyu.
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
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.expression.ParseException;
import org.springframework.util.StringUtils;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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

    private static final ParameterNameDiscoverer DISCOVERER = new DefaultParameterNameDiscoverer();
    private final LogEvaluator evaluator = new LogEvaluator();
    private final List<LogRecordStoreService> logRecordStoreServiceList;
    private final String systemName;
    private final BeanFactoryResolver beanFactoryResolver;

    public LogRecordAspectj(List<LogRecordStoreService> logRecordStoreServiceList,
                            String systemName,
                            BeanFactoryResolver beanFactoryResolver) {
        this.logRecordStoreServiceList = logRecordStoreServiceList;
        this.systemName = systemName;
        this.beanFactoryResolver = beanFactoryResolver;
    }

    @Around("@annotation(logRecord)")
    public Object logAround(ProceedingJoinPoint joinPoint, LogRecord logRecord) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object result = null;
        Throwable throwable = null;
        try {
            result = joinPoint.proceed();
            return result;
        } catch (Throwable e) {
            throwable = e;
            throw e;
        } finally {
            long costTime = System.currentTimeMillis() - startTime;
            // 使用try-catch包裹日志逻辑，确保日志记录失败不影响主业务响应
            try {
                this.handleLog(joinPoint, logRecord, throwable, result, costTime);
            } catch (Exception ex) {
                LoggerUtil.error(log, "Log collection failed", ex);
            }
        }
    }

    protected void handleLog(final JoinPoint joinPoint, LogRecord logRecord, final Throwable e, Object result, long cost) {
        LogRecordEntity entity = this.buildLogRecordEntity(e, joinPoint, logRecord, result);
        entity.setCostTime(cost);
        for (LogRecordStoreService storeService : logRecordStoreServiceList) {
            storeService.saveLogRecord(entity);
        }
    }

    private MethodBasedEvaluationContext getMethodBasedEvaluationContext(JoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        MethodBasedEvaluationContext context = new MethodBasedEvaluationContext(
                joinPoint.getTarget(),
                methodSignature.getMethod(),
                joinPoint.getArgs(),
                DISCOVERER);
        context.setBeanResolver(this.beanFactoryResolver);
        return context;
    }

    private LogRecordEntity buildLogRecordEntity(Throwable exception, JoinPoint joinPoint, LogRecord logRecord, Object result) {
        LogModule module = joinPoint.getTarget().getClass().getAnnotation(LogModule.class);
        LogRecordEntity entity = LogRecordEntity.createDefault();
        entity.setModuleName(Objects.isNull(module) ? "" : module.value());
        entity.setSystemName(systemName);
        entity.setOperationType(logRecord.operationType().name());
        entity.setRiskType(logRecord.riskType().name());

        MethodBasedEvaluationContext context = getMethodBasedEvaluationContext(joinPoint);

        // 处理返回值变量
        if (Objects.nonNull(result)) {
            context.setVariable("result", result);
            entity.setResponseData(JacksonUtil.toJsonString(result));
        }

        // 处理异常信息
        if (Objects.nonNull(exception)) {
            entity.setIsSuccess(false);
            entity.setErrorMessage(exception.getMessage());
        }

        // 解析SpEL内容
        entity.setContent(this.parseContent(logRecord.content(), context));
        entity.setContentEn(this.parseContent(logRecord.contentEn(), context));
        entity.setOperationUser(this.parseContent(logRecord.operationUser(), context));

        // 处理参数序列化
        this.processMethodParameters(joinPoint, entity);

        return entity;
    }

    private String parseContent(String content, MethodBasedEvaluationContext context) {
        if (!StringUtils.hasText(content)) {
            return "";
        }
        try {
            return String.valueOf(evaluator.parse(content, context));
        } catch (ParseException ex) {
            LoggerUtil.error(log, "Log SpEL content parse failed: {}", content, ex);
            throw new LogException(ex);
        } catch (Exception ex) {
            LoggerUtil.error(log, "Log parse unknown error: {}", content, ex);
            throw new LogException(ex);
        }
    }

    private void processMethodParameters(JoinPoint joinPoint, LogRecordEntity entity) {
        Object[] args = joinPoint.getArgs();
        if (args == null || args.length == 0) {
            entity.setRequestParam("");
            return;
        }

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String[] parameterNames = signature.getParameterNames();
        Class<?>[] parameterTypes = signature.getParameterTypes();
        Map<String, Object> paramMap = new LinkedHashMap<>(args.length);

        for (int i = 0; i < args.length; i++) {
            String paramName = (parameterNames != null && i < parameterNames.length) ? parameterNames[i] : "arg" + i;
            Object paramValue = args[i];

            if (Objects.isNull(paramValue)) {
                paramMap.put(paramName, "null");
            } else if (isSimpleType(parameterTypes[i])) {
                paramMap.put(paramName, paramValue);
            } else {
                // 复杂对象序列化
                paramMap.put(paramName, JacksonUtil.toJsonString(paramValue));
            }
        }
        entity.setRequestParam(JacksonUtil.toJsonString(paramMap));
    }

    private boolean isSimpleType(Class<?> clazz) {
        return clazz.isPrimitive() ||
                clazz.equals(String.class) ||
                Number.class.isAssignableFrom(clazz) ||
                clazz.equals(Boolean.class) ||
                clazz.equals(Character.class) ||
                java.time.temporal.Temporal.class.isAssignableFrom(clazz) ||
                java.util.Date.class.isAssignableFrom(clazz);
    }
}
