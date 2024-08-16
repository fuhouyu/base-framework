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

package com.fuhouyu.framework.http;

import com.fuhouyu.framework.http.client.AsyncHttpClientRequest;
import com.fuhouyu.framework.http.client.DefaultHttpClientRequest;
import com.fuhouyu.framework.http.request.HttpRequestEntity;
import org.apache.hc.core5.function.Callback;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import org.springframework.util.Assert;

import java.util.HashMap;

/**
 * <p>
 * http客户端请求测试
 * </p>
 *
 * @author fuhouyu
 * @since 2024/8/15 16:12
 */
@EnabledIfSystemProperty(named = "run.tests", matches = "true")
class ApacheHttpClientTest {

    private static final String GET_URL = "https://api.oioweb.cn/api/common/yiyan";

    private AsyncHttpClientRequest asyncHttpRequest;

    @BeforeEach
    void setUp() {
        asyncHttpRequest = DefaultHttpClientRequest.createDefaultHttpClientRequest();
    }


    @Test
    void testRequestUrl() throws Exception {
        HttpRequestEntity httpRequestEntity = new HttpRequestEntity();
        asyncHttpRequest.httpGet(GET_URL, httpRequestEntity, (Callback<HashMap<String, Object>>) response -> {
            Assert.notNull(response, "返回的格式不正确。");
        });
    }
}
