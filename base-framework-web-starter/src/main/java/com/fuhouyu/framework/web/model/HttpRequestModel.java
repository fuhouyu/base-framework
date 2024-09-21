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

package com.fuhouyu.framework.web.model;

import com.fuhouyu.framework.context.request.Request;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Map;

/**
 * <p>
 * http请求详情类
 * </p>
 *
 * @author fuhouyu
 * @since 2024/8/18 16:44
 */
@ToString
@Getter
@Setter
public class HttpRequestModel implements Request {

    private String requestIp;

    private String requestHost;

    private String requestTarget;

    private String userAgent;

    private Map<String, Object> additionalInformation;

}
