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

package com.fuhouyu.framework.web;

import com.fuhouyu.framework.cache.service.CacheService;
import com.fuhouyu.framework.web.aspectj.NoRepeatSubmitAspectj;
import org.springframework.context.annotation.Bean;

/**
 * <p>
 * form表单自动装配类
 * </p>
 *
 * @author fuhouyu
 * @since 2024/8/17 23:07
 */
public class FormAutoConfigure {

    private final CacheService<String, Object> cacheService;

    public FormAutoConfigure(CacheService<String, Object> cacheService) {
        this.cacheService = cacheService;
    }

    @Bean
    public NoRepeatSubmitAspectj noDuplicateSubmitAspect() {
        return new NoRepeatSubmitAspectj(cacheService);
    }
}
