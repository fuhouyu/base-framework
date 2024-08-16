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

package com.fuhouyu.framework.http.client;

import com.fuhouyu.framework.http.request.HttpRequestEntity;
import org.apache.hc.core5.function.Callback;

/**
 * <p>
 * 异常请求接口
 * </p>
 *
 * @author fuhouyu
 * @since 2024/8/15 15:11
 */
public interface AsyncHttpClientRequest {

    /**
     * get请求
     *
     * @param url               请求的url地址
     * @param httpRequestEntity http请求实体
     * @param callback          回调地址
     * @param <T>               返回的具体类型
     * @throws Exception 可能会抛出的异常
     */
    <T> void httpGet(String url, HttpRequestEntity httpRequestEntity, Callback<T> callback) throws Exception;

    /**
     * get请求
     *
     * @param url               请求的url地址
     * @param httpRequestEntity http请求实体
     * @param callback          回调地址
     * @param <T>               返回的具体类型
     * @throws Exception 可能会抛出的异常
     */
    <T> void httpPost(String url, HttpRequestEntity httpRequestEntity, Callback<T> callback) throws Exception;
}
