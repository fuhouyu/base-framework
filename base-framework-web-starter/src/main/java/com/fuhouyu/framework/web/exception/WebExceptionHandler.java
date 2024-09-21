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

package com.fuhouyu.framework.web.exception;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fuhouyu.framework.response.ResponseCodeEnum;
import com.fuhouyu.framework.response.ResponseHelper;
import com.fuhouyu.framework.response.RestResult;
import com.fuhouyu.framework.utils.JacksonUtil;
import com.fuhouyu.framework.utils.LoggerUtil;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestValueException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * <p>
 * web异常拦截处理器
 * </p>
 *
 * @author fuhouyu
 * @since 2024/8/15 20:22
 */
@RestControllerAdvice
public class WebExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebExceptionHandler.class);

    /**
     * <p style="color:red">
     * 未定义的异常在抛出时，将会在这里进行拦截。 如需自定义异常，新增handler方法，进行拦截处理
     * </p>
     *
     * @param request 请求
     * @param e       自定义的服务异常处理
     * @return 包装后的异常信息
     */
    @ExceptionHandler(Exception.class)
    public RestResult<Void> exceptionHandle(ServletWebRequest request, Exception e) {
        this.printExceptionLog(e, Exception.class.getSimpleName(), request);
        return ResponseHelper.failed(ResponseCodeEnum.SERVER_ERROR);
    }

    /**
     * spring 升级后，访问无可用的资源时，会在这里抛出异常
     *
     * @param request 请求
     * @param e       找不到资源异常
     * @return 包装后的异常信息
     */
    @ExceptionHandler(NoResourceFoundException.class)
    public RestResult<Void> noResourceFoundException(ServletWebRequest request, NoResourceFoundException e) {
        this.printExceptionLog(e, Exception.class.getSimpleName(), request);
        return ResponseHelper.failed(ResponseCodeEnum.NOT_FOUND,
                String.format("当前访问地址：%s 不存在", e.getMessage().replace("No static resource ", "").trim()));
    }

    /**
     * 不支持的方法异常
     *
     * @param request 请求
     * @param e       异常
     * @return 包装后的异常信息
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public RestResult<Void> methodNotSupportException(ServletWebRequest request, HttpRequestMethodNotSupportedException e) {
        this.printExceptionLog(e, e.getClass().getName(), request);
        return ResponseHelper.failed(ResponseCodeEnum.METHOD_NOT_ALLOWED);
    }

    /**
     * 响应状态异常
     *
     * @param request 请求
     * @param e       异常
     * @return 包装后的异常信息
     */
    @ExceptionHandler(ResponseStatusException.class)
    public RestResult<Void> notFoundException(ServletWebRequest request, ResponseStatusException e) {
        this.printExceptionLog(e, e.getClass().getName(), request);
        return ResponseHelper.failed(ResponseCodeEnum.SERVER_ERROR, e.getMessage());
    }

    /**
     * 自定义异常拦截
     *
     * @param serviceException 自定义的服务异常处理
     * @return 包装后的异常信息
     */
    @ExceptionHandler(WebServiceException.class)
    public RestResult<Void> serviceExceptionHandler(WebServiceException serviceException) {
        return ResponseHelper.failed(serviceException.getResponseStatus(), serviceException.getMessage());
    }

    /**
     * valid controller 入参验证时的异常拦截器
     *
     * @param request 请求
     * @param e       请求参数异常
     * @return 包装后的异常信息
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public RestResult<Void> methodArgumentNotValidExceptionHandler(ServletWebRequest request, MethodArgumentNotValidException e) {
        Map<String, String> errorMessageMap = new TreeMap<>(String::compareToIgnoreCase);
        for (FieldError fieldError : e.getBindingResult().getFieldErrors()) {
            errorMessageMap.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        this.printExceptionLog(e, e.getClass().getSimpleName(), request);
        return ResponseHelper.failed(ResponseCodeEnum.INVALID_PARAM, JacksonUtil.writeValueAsString(errorMessageMap));
    }


    /**
     * 参数异常都会在这里进行统一抛出。
     *
     * @param request 请求
     * @param e       http缺少参数异常
     * @return 包装后的响应
     */
    @ExceptionHandler(value = {
            MissingServletRequestParameterException.class,
            IllegalArgumentException.class,
            HttpMessageNotReadableException.class,
            MissingRequestValueException.class,
            NumberFormatException.class,
            HttpMessageConversionException.class
    })
    public RestResult<String> handleHttpMediaTypeException(ServletWebRequest request,
                                                           Exception e) {
        this.printExceptionLog(e, e.getClass().getSimpleName(), request);
        return ResponseHelper.failed(ResponseCodeEnum.INVALID_PARAM, e.getMessage());
    }

    /**
     * 媒体类型错误
     *
     * @param request 请求
     * @param e       媒体请求异常
     * @return 包装后的响应
     */
    @ExceptionHandler(HttpMediaTypeException.class)
    public RestResult<String> handleHttpMediaTypeException(ServletWebRequest request, HttpMediaTypeException e) {
        this.printExceptionLog(e, e.getClass().getSimpleName(), request);
        return ResponseHelper.failed(ResponseCodeEnum.NOT_SUPPORT_MEDIA_TYPE, e.getMessage());
    }


    /**
     * 入参的body体中的参数异常拦截器 如 @RequestBody @Size(min = 1, message= "xxx") List<Integer> list
     *
     * @param request 请求
     * @param e       请求入参转换异常
     * @return 包装后的异常信息
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public RestResult<Void> constraintViolationException(ServletWebRequest request, ConstraintViolationException e) {
        this.printExceptionLog(e, e.getClass().getSimpleName(), request);
        Set<ConstraintViolation<?>> constraintViolations = e.getConstraintViolations();
        StringBuilder sb = new StringBuilder();
        for (ConstraintViolation<?> next : constraintViolations) {
            String message = next.getMessage();
            sb.append(message).append("\n");
        }
        // 直接返回参数异常
        return ResponseHelper.failed(ResponseCodeEnum.INVALID_PARAM, sb.toString());
    }

    /**
     * 打印日志
     *
     * @param e             异常
     * @param exceptionName 异常的名称
     * @param request       请求
     */
    public void printExceptionLog(Exception e, String exceptionName, ServletWebRequest request) {
        String name = request.getHttpMethod().name();
        String queryParams = request.getRequest().getQueryString();
        String requestUrl = request.getRequest().getRequestURI();
        LoggerUtil.error(LOGGER,
                """
                        异常名称: {}
                        请求方式: {}
                        请求路径: {}
                        请求头参数: {}
                        请求Url参数: {}
                        异常原因: {}
                        """,
                exceptionName, name, requestUrl,
                this.parseHttpServletHeaders(request), queryParams, e.getMessage(), e);
    }

    /**
     * 解析请求头参数
     *
     * @param request 请求头
     * @return 字符串
     */
    private String parseHttpServletHeaders(ServletWebRequest request) {
        ObjectNode objectNode = JacksonUtil.getObjectMapper().createObjectNode();
        Iterator<String> headerNames = request.getHeaderNames();
        while (headerNames.hasNext()) {
            String headName = headerNames.next();
            String headerValue = request.getHeader(headName);
            objectNode.put(headName, headerValue);
        }
        return objectNode.toString();
    }
}
