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

package com.fuhouyu.framework.web.controller;

import com.fuhouyu.framework.cache.service.CacheService;
import com.fuhouyu.framework.common.response.BaseResponse;
import com.fuhouyu.framework.web.annotaions.PrepareHttpBody;
import com.fuhouyu.framework.web.constants.FormTokenConstant;
import com.fuhouyu.framework.web.response.ResponseHelper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
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
@RequestMapping("/v1/base/form")
@Tag(name = "base form表单 前端控制器")
@RequiredArgsConstructor
public class BaseFormController {

    @Value("${spring.application.name}")
    private String applicationName;

    private final CacheService<String, Object> cacheService;


    /**
     * 生成表单唯一token并缓存
     * 配合注解{@link com.fuhouyu.framework.web.annotaions.NoRepeatSubmit} 使用
     * 会在表单提交前进行验证
     *
     * @return 表单临时token
     */
    @GetMapping("/token")
    @Operation(summary = "生成表单唯一的token，默认十分钟")
    @PrepareHttpBody
    public BaseResponse<String> formToken() {
        String token = String.format("%s_%s", applicationName, UUID.randomUUID().toString().replace("-", ""));
        cacheService.set(FormTokenConstant.TOKEN_PREFIX + token,
                true, FormTokenConstant.EXPIRE_TIME, TimeUnit.SECONDS);
        return ResponseHelper.success(token);
    }
}
