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

package com.fuhouyu.framework.web.filter;

import com.fuhouyu.framework.web.annotaions.PrepareHttpBody;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.Objects;

/**
 * <p>
 * http请求处理
 * </p>
 *
 * @author fuhouyu
 * @since 2024/8/21 22:53
 */
@RestControllerAdvice
@RequiredArgsConstructor
public class HttpRequestBodyAdvice extends RequestBodyAdviceAdapter {

    private final HttpBodyFilter httpBodyFilter;


    @Override
    public boolean supports(@NonNull MethodParameter methodParameter,
                            @NonNull Type targetType,
                            @NonNull Class<? extends HttpMessageConverter<?>> converterType) {
        return Objects.nonNull(httpBodyFilter)
                && methodParameter.hasMethodAnnotation(PrepareHttpBody.class)
                && methodParameter.hasMethodAnnotation(PostMapping.class);
    }


    @Override
    @NonNull
    public HttpInputMessage beforeBodyRead(@NonNull HttpInputMessage inputMessage,
                                           @NonNull MethodParameter parameter,
                                           @NonNull Type targetType,
                                           @NonNull Class<? extends HttpMessageConverter<?>> converterType) throws IOException {
        if (Objects.isNull(httpBodyFilter)) {
            return inputMessage;
        }
        return new HttpInputMessage() {
            private final InputStream body = decryptionBody();

            private InputStream decryptionBody() {
                byte[] bytes;
                try (InputStream inputStream = inputMessage.getBody()) {
                    bytes = inputStream.readAllBytes();
                } catch (IOException e) {
                    throw new IllegalArgumentException("read body failed " + e.getMessage(), e);
                }
                return new ByteArrayInputStream(httpBodyFilter.decryptionBody(bytes));
            }

            @Override
            @NonNull
            public HttpHeaders getHeaders() {
                return inputMessage.getHeaders();
            }

            @Override
            @NonNull
            public InputStream getBody() {
                return body;
            }
        };
    }

}



