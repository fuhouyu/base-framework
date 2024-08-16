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

/**
 * <p>
 * http请求的实体
 * </p>
 *
 * @author fuhouyu
 * @since 2024/8/15 15:05
 */
public class HttpRequestEntity {

    private final HttpQueryParams httpQueryParams;

    private final HttpRequestHeader httpRequestHeader;

    private final Object body;

    public HttpRequestEntity() {
        this(HttpQueryParams.build(), HttpRequestHeader.createDefault(), null);
    }

    public HttpRequestEntity(HttpQueryParams httpQueryParams) {
        this(httpQueryParams, HttpRequestHeader.createDefault(), null);
    }

    public HttpRequestEntity(HttpQueryParams httpQueryParams, HttpRequestHeader httpRequestHeader) {
        this(httpQueryParams, httpRequestHeader, null);
    }

    public HttpRequestEntity(HttpQueryParams httpQueryParams, HttpRequestHeader httpRequestHeader, Object body) {
        this.httpQueryParams = httpQueryParams;
        this.httpRequestHeader = httpRequestHeader;
        this.body = body;
    }

    public HttpQueryParams getHttpQueryParams() {
        return httpQueryParams;
    }

    public HttpRequestHeader getHttpRequestHeader() {
        return httpRequestHeader;
    }

    public Object getBody() {
        return body;
    }
}
