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

package com.fuhouyu.framework.http.request;

import java.util.*;

/**
 * <p>
 * http请求的header头
 * </p>
 *
 * @author fuhouyu
 * @since 2024/8/15 15:05
 */
public class HttpRequestHeader {

    private final Map<String, List<String>> headers;

    public HttpRequestHeader(Map<String, List<String>> headers) {
        this.headers = headers;
    }

    public static HttpRequestHeader createDefault() {
        HttpRequestHeader httpRequestHeader = new HttpRequestHeader(new HashMap<>());
        // 设置默认的请求头
        httpRequestHeader.addHeader("Content-Type", "application/json");
        return httpRequestHeader;
    }


    /**
     * 添加请求头
     *
     * @param name  请求头名称
     * @param value 请求头值
     * @return 当前对象
     */
    public HttpRequestHeader addHeader(String name, String value) {
        if (Objects.isNull(name)) {
            return this;
        }
        this.headers.getOrDefault(name, new LinkedList<>()).add(value);
        return this;
    }

    /**
     * 添加请求头
     *
     * @param addHeader 需要添加的请求头对象
     * @return 当前对象
     */
    public HttpRequestHeader addHeaderAll(Map<String, List<String>> addHeader) {
        if (Objects.isNull(addHeader)) {
            return this;
        }
        this.headers.putAll(addHeader);
        return this;
    }

    public Map<String, List<String>> getHeaders() {
        return headers;
    }
}
