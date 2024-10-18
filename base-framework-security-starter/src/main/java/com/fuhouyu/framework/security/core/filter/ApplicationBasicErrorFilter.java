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

package com.fuhouyu.framework.security.core.filter;

import com.fuhouyu.framework.response.BaseResponse;
import com.fuhouyu.framework.utils.JacksonUtil;
import jakarta.servlet.ServletOutputStream;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.nio.charset.StandardCharsets;

/**
 * <p>
 * 应用基本认证错误的异常过滤器
 * </p>
 *
 * @author fuhouyu
 * @since 2024/8/15 17:30
 */
public class ApplicationBasicErrorFilter extends BasicAuthenticationFilter {

    /**
     * 初始化基本认证过滤器
     * Basic Base64(clientId:clientSecret).
     * 主要为了处理异常错误的友好返回
     * @param authenticationManager 认证管理器
     */
    public ApplicationBasicErrorFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager, (request, response, authException) -> {
            BaseResponse<Void> baseResponse = new BaseResponse<>() {
                @Override
                public Integer getCode() {
                    return 401;
                }

                @Override
                public String getMessage() {
                    return authException.getMessage();
                }

                @Override
                public Boolean getIsSuccess() {
                    return false;
                }

                @Override
                public Void getData() {
                    return null;
                }
            };
            String body = JacksonUtil.writeValueAsString(baseResponse);
            try (ServletOutputStream outputStream = response.getOutputStream()) {
                outputStream.write(body.getBytes(StandardCharsets.UTF_8));
                outputStream.flush();
            }
        });
    }

}
