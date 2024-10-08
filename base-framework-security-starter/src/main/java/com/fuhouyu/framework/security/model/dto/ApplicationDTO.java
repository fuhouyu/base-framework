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

package com.fuhouyu.framework.security.model.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <p>
 * 应用dto传输对象
 * </p>
 *
 * @author fuhouyu
 * @since 2024/8/15 17:16
 */
@ToString
@Getter
@Setter
public class ApplicationDTO {

    private String clientId;

    private String clientSecret;

    private Integer accessTokenExpireTime;

    private Integer refreshTokenExpireTime;
}
