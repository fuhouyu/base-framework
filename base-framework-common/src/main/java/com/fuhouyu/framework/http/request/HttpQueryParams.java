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

import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * <p>
 * http请求的query参数
 * </p>
 *
 * @author fuhouyu
 * @since 2024/8/15 14:55
 */
public class HttpQueryParams {

    private final Map<String, String> queryParams;

    private HttpQueryParams(Map<String, String> queryParams) {
        this.queryParams = queryParams;
    }

    public static HttpQueryParams build() {
        return new HttpQueryParams(new LinkedHashMap<>());
    }

    public static HttpQueryParams build(Map<String, String> queryParams) {
        return new HttpQueryParams(queryParams);
    }


    /**
     * 添加查询参数
     *
     * @param key   key
     * @param value value
     * @return 当前对象
     */
    public HttpQueryParams put(String key, String value) {
        this.queryParams.put(key, value);
        return this;
    }

    /**
     * 添加查询参数
     *
     * @param map 需要保存的键值对
     * @return 当前对象
     */
    public HttpQueryParams putAll(Map<String, String> map) {
        if (Objects.isNull(map) || map.isEmpty()) {
            return this;
        }
        queryParams.putAll(map);
        return this;
    }

    /**
     * 拼接为具体的参数
     * 如a=1&b=2
     *
     * @return 拼接后的字符串
     */
    public String buildQueryParams() {
        if (queryParams.isEmpty()) {
            return "";
        }
        Set<Map.Entry<String, String>> entries = queryParams.entrySet();
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : entries) {
            sb.append(entry.getKey())
                    .append("=")
                    .append(Base64.getEncoder().encodeToString(entry.getValue().getBytes(StandardCharsets.UTF_8)))
                    .append("&");
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    public Map<String, String> getQueryParams() {
        return queryParams;
    }


}
