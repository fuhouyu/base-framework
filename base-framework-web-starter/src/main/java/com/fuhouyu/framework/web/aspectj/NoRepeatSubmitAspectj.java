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

package com.fuhouyu.framework.web.aspectj;

import com.fuhouyu.framework.cache.service.CacheService;
import com.fuhouyu.framework.response.ResponseCodeEnum;
import com.fuhouyu.framework.web.annotaions.NoRepeatSubmit;
import com.fuhouyu.framework.web.constants.FormTokenConstant;
import com.fuhouyu.framework.web.exception.WebServiceException;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Arrays;
import java.util.Locale;
import java.util.Objects;

/**
 * <p>
 * 表单token切面
 * </p>
 *
 * @author fuhouyu
 * @since 2024/8/17 22:52
 */
@Aspect
public class NoRepeatSubmitAspectj {

    private final CacheService<String, Object> cacheService;

    public NoRepeatSubmitAspectj(CacheService<String, Object> cacheService) {
        this.cacheService = cacheService;
    }

    /**
     * 切面，当在使用{@link com.fuhouyu.framework.web.annotaions.NoRepeatSubmit}
     * 注解后，会在这里进行拦截，判断当前表单是否已经提交
     *
     * @param joinPoint 连接点
     * @return obj
     */
    @Around(value = "@annotation(noRepeatSubmit)")
    public Object doAround(ProceedingJoinPoint joinPoint, NoRepeatSubmit noRepeatSubmit) throws Throwable {
        ServletRequestAttributes attributes = Objects.requireNonNull((ServletRequestAttributes) RequestContextHolder.getRequestAttributes());
        HttpServletRequest request = attributes.getRequest();
        String[] httpMethods = noRepeatSubmit.httpMethods();
        boolean containsMethod = Arrays.stream(httpMethods).anyMatch(httpMethod -> Objects.equals(httpMethod.toLowerCase(Locale.ROOT),
                request.getMethod().toLowerCase(Locale.ROOT)));
        if (!containsMethod) {
            return joinPoint.proceed();
        }
        String formTokenHeader = request.getHeader(noRepeatSubmit.headerToken());
        if (!StringUtils.hasText(formTokenHeader)) {
            throw new WebServiceException(
                    ResponseCodeEnum.INVALID_PARAM,
                    noRepeatSubmit.message());
        }
        Object formToken = cacheService.get(FormTokenConstant.TOKEN_PREFIX
                + formTokenHeader);
        if (Objects.isNull(formToken)) {
            throw new WebServiceException(
                    ResponseCodeEnum.INVALID_PARAM,
                    noRepeatSubmit.message());
        }
        cacheService.delete(FormTokenConstant.TOKEN_PREFIX + formTokenHeader);
        return joinPoint.proceed();
    }
}
