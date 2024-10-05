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

package com.fuhouyu.framework.web.config;

import com.fuhouyu.framework.constants.HttpRequestHeaderConstant;
import com.fuhouyu.framework.context.user.DefaultUserDetail;
import com.fuhouyu.framework.context.user.User;
import com.fuhouyu.framework.utils.ClassUtils;
import com.fuhouyu.framework.utils.JacksonUtil;
import com.fuhouyu.framework.web.handler.HttpRequestContextHandler;
import com.fuhouyu.framework.web.handler.HttpRequestUserHandler;
import com.fuhouyu.framework.web.handler.UserExtensionHandler;
import com.fuhouyu.framework.web.handler.UserParseHandler;
import com.fuhouyu.framework.web.properties.WebProperties;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.core.OrderComparator;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.web.servlet.AsyncHandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * webmvc配置
 * </p>
 *
 * @author fuhouyu
 * @since 2024/8/15 19:49
 */
public class WebMvcAutoConfigure implements WebMvcConfigurer {

    private final ApplicationContext applicationContext;

    private final WebProperties webProperties;

    public WebMvcAutoConfigure(ApplicationContext applicationContext, WebProperties webProperties) {
        this.applicationContext = applicationContext;
        this.webProperties = webProperties;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new HttpRequestContextHandler()).addPathPatterns("/**");
        registry.addInterceptor(this.getHttpHandlerInterceptor()).addPathPatterns("/**");
    }

    /**
     * 设置拦截请求头
     *
     * @return 拦截器
     */
    private AsyncHandlerInterceptor getHttpHandlerInterceptor() {
        return new HttpRequestUserHandler(this.getUserExtensionHandlerInterceptors(),
                this.userParseHandler(),
                this.getUserClassName());
    }

    /**
     * 获取可能存在的user扩展处理器
     *
     * @return 用户扩展处理器
     */
    private List<UserExtensionHandler> getUserExtensionHandlerInterceptors() {
        String[] beanNamesForType = applicationContext.getBeanNamesForType(
                UserExtensionHandler.class);
        if (beanNamesForType.length == 0) {
            return Collections.emptyList();
        }
        // 如果存在用户扩展项，进行设置
        List<UserExtensionHandler> list = new ArrayList<>(
                beanNamesForType.length);
        for (String beanName : beanNamesForType) {
            list.add(applicationContext.getBean(beanName,
                    UserExtensionHandler.class));
        }
        OrderComparator.sort(list);
        AnnotationAwareOrderComparator.sort(list);
        return list;
    }

    /**
     * 解析user子类型
     *
     * @param <T> 用户泛型
     * @return user子类型
     */
    @SuppressWarnings("unchecked")
    private <T extends User> Class<T> getUserClassName() {
        String userClassName = webProperties.getUserClassName();
        return Objects.isNull(userClassName) ? (Class<T>) DefaultUserDetail.class :
                ClassUtils.loadClass(userClassName);
    }

    /**
     * 用户解析的处理器
     *
     * @return 解析用户信息
     */
    private UserParseHandler userParseHandler() {
        try {
            return applicationContext.getBean(UserParseHandler.class);
        } catch (BeansException e) {
            // 返回一个默认
            return new UserParseHandler() {
                @Override
                public <T extends User> T parseUser(@NonNull HttpServletRequest request,
                                                    @NonNull HttpServletResponse response,
                                                    @NonNull Class<T> userSubClass) {
                    String userInfo = request.getHeader(HttpRequestHeaderConstant.USERINFO_HEADER);
                    if (Objects.isNull(userInfo)) {
                        return null;
                    }
                    userInfo = URLDecoder.decode(userInfo, Charset.defaultCharset());
                    return JacksonUtil.readValue(userInfo, userSubClass);
                }
            };
        }
    }
}
