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

import com.fasterxml.jackson.core.type.TypeReference;
import com.fuhouyu.framework.exception.HttpRequestException;
import com.fuhouyu.framework.http.request.HttpQueryParams;
import com.fuhouyu.framework.http.request.HttpRequestEntity;
import com.fuhouyu.framework.http.request.HttpRequestHeader;
import com.fuhouyu.framework.utils.JacksonUtil;
import com.fuhouyu.framework.utils.LoggerUtil;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.classic.methods.HttpUriRequestBase;
import org.apache.hc.client5.http.entity.UrlEncodedFormEntity;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.function.Callback;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.io.HttpClientResponseHandler;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.message.BasicNameValuePair;
import org.apache.hc.core5.net.URIBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * <p>
 * apache http client实现
 * </p>
 *
 * @author fuhouyu
 * @since 2024/8/15 15:34
 */
public class DefaultHttpClientRequest implements AsyncHttpClientRequest {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultHttpClientRequest.class);

    private final HttpClientResponseHandler<?> responseHandler;

    private DefaultHttpClientRequest() {
        this.responseHandler = createDefaultResponseHandler();
    }

    public static DefaultHttpClientRequest createDefaultHttpClientRequest() {
        return new DefaultHttpClientRequest();
    }

    private HttpClientResponseHandler<?> createDefaultResponseHandler() {
        return response -> {
            int code = response.getCode();
            if (!Objects.equals(code, 200)) {
                throw new HttpRequestException(code, response.getReasonPhrase());
            }
            if (Objects.isNull(response.getEntity())) {
                return null;
            }
            return JacksonUtil.readValue(EntityUtils.toString(response.getEntity()), new TypeReference<>() {
            });
        };
    }

    @Override
    public <T> void httpGet(String url, HttpRequestEntity httpRequestEntity, Callback<T> callback) throws Exception {
        URI uri = this.buildUri(url, httpRequestEntity.getHttpQueryParams());
        HttpGet httpGet = new HttpGet(uri);
        this.setHeader(httpRequestEntity.getHttpRequestHeader(), httpGet);
        this.execute(httpGet, callback);
    }

    @Override
    public <T> void httpPost(String url, HttpRequestEntity httpRequestEntity, Callback<T> callback) throws Exception {
        URI uri = this.buildUri(url, httpRequestEntity.getHttpQueryParams());
        HttpPost httpPost = new HttpPost(uri);
        this.setBody(httpRequestEntity.getBody(), httpPost);
        this.setHeader(httpRequestEntity.getHttpRequestHeader(), httpPost);
        this.execute(httpPost, callback);
    }

    /**
     * 构建url参数
     *
     * @param url             url
     * @param httpQueryParams 请求参数
     * @return url
     */
    private URI buildUri(String url,
                         HttpQueryParams httpQueryParams) {
        Map<String, String> queryParams = httpQueryParams.getQueryParams();
        List<NameValuePair> nameValuePairList = new ArrayList<>(queryParams.size());
        for (Map.Entry<String, String> entry : queryParams.entrySet()) {
            nameValuePairList.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }
        try {
            return new URIBuilder(new URI(url))
                    .addParameters(nameValuePairList)
                    .build();
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException(String.format("%s url参数错误", url));
        }
    }

    /**
     * 设置请求头
     *
     * @param httpRequestHeader 请求头设置
     * @param requestBase       请求基类
     */
    private void setHeader(HttpRequestHeader httpRequestHeader,
                           HttpUriRequestBase requestBase) {
        Set<Map.Entry<String, List<String>>> entries = httpRequestHeader.getHeaders().entrySet();
        for (Map.Entry<String, List<String>> entry : entries) {
            StringBuilder sb = new StringBuilder();
            for (String value : entry.getValue()) {
                sb.append(value).append(";");
            }
            requestBase.addHeader(entry.getKey(), sb.toString());
        }
    }

    /**
     * 设置body体
     *
     * @param body     body
     * @param httpPost post请求
     */
    private void setBody(Object body,
                         HttpPost httpPost) {
        if (Objects.isNull(body)) {
            return;
        }
        ArrayList<BasicNameValuePair> bodyLists =
                JacksonUtil.tryParse(() -> JacksonUtil.getObjectMapper().convertValue(body, new TypeReference<>() {
                }));
        httpPost.setEntity(new UrlEncodedFormEntity(bodyLists));
    }


    @SuppressWarnings("unchecked")
    private <T> void execute(HttpUriRequestBase httpUriRequestBase, Callback<T> callback) {
        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            callback.execute((T) httpclient.execute(httpUriRequestBase, responseHandler));
        } catch (IOException e) {
            LoggerUtil.error(LOGGER, "http 请求执行失败，method:{}, url: {}, errorMessage: {}",
                    httpUriRequestBase.getMethod(),
                    httpUriRequestBase.getRequestUri(), e.getMessage(), e);
            throw new HttpRequestException(500, e.getMessage());
        }
    }


}
