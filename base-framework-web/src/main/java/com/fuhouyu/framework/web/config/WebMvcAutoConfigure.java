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

package com.fuhouyu.framework.web.config;

import com.fuhouyu.framework.context.user.DefaultUserDetail;
import com.fuhouyu.framework.web.handler.HttpRequestUserHandler;
import com.fuhouyu.framework.web.handler.UserExtensionHandlerInterceptor;
import org.springframework.context.ApplicationContext;
import org.springframework.core.OrderComparator;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.web.servlet.AsyncHandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

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

    public WebMvcAutoConfigure(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(this.getHttpHandlerInterceptor()).addPathPatterns("/**");
    }

    /**
     * 设置拦截请求头
     *
     * @return 拦截器
     */
    private AsyncHandlerInterceptor getHttpHandlerInterceptor() {

        String[] beanNamesForType = applicationContext.getBeanNamesForType(
                UserExtensionHandlerInterceptor.class);
        if (beanNamesForType.length == 0) {
            return new HttpRequestUserHandler(null, DefaultUserDetail.class);
        }
        // 如果存在用户扩展项，进行设置
        List<UserExtensionHandlerInterceptor> list = new ArrayList<>(
                beanNamesForType.length);
        for (String beanName : beanNamesForType) {
            list.add(applicationContext.getBean(beanName,
                    UserExtensionHandlerInterceptor.class));
        }
        OrderComparator.sort(list);
        AnnotationAwareOrderComparator.sort(list);
        return new HttpRequestUserHandler(null, DefaultUserDetail.class);
    }
}
