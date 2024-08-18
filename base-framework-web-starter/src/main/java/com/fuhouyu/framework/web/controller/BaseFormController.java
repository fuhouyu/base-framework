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

package com.fuhouyu.framework.web.controller;

import com.fuhouyu.framework.cache.service.CacheService;
import com.fuhouyu.framework.model.response.ResponseHelper;
import com.fuhouyu.framework.model.response.RestResult;
import com.fuhouyu.framework.web.constants.ApiPrefixConstant;
import com.fuhouyu.framework.web.constants.FormTokenConstant;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 表单controller
 * </p>
 *
 * @author fuhouyu
 * @since 2024/8/17 18:45
 */
@RestController
@RequestMapping(ApiPrefixConstant.FORM_CONTROLLER_PREFIX)
@Tag(name = "base form表单 前端控制器")
public class BaseFormController {

    private final String applicationName;

    private final CacheService<String, Object> cacheService;

    public BaseFormController(@Value("${spring.application.name}") String applicationName,
                              CacheService<String, Object> cacheService) {
        this.applicationName = applicationName;
        this.cacheService = cacheService;
    }

    /**
     * 生成表单唯一token并缓存
     * 配合注解{@link com.fuhouyu.framework.web.annotaions.NoRepeatSubmit} 使用
     * 会在表单提交前进行验证
     *
     * @return 表单临时token
     */
    @GetMapping("/token")
    @Operation(summary = "生成表单唯一的token，默认十分钟")
    public RestResult<String> formToken() {
        String token = String.format("%s_%s", applicationName, UUID.randomUUID().toString().replace("-", ""));
        cacheService.set(FormTokenConstant.TOKEN_PREFIX + token,
                true, FormTokenConstant.EXPIRE_TIME, TimeUnit.SECONDS);
        return ResponseHelper.success(token);
    }
}
