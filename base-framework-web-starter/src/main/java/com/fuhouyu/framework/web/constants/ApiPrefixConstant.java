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

package com.fuhouyu.framework.web.constants;

/**
 * <p>
 * api前缀常量
 * </p>
 *
 * @author fuhouyu
 * @since 2024/8/17 18:46
 */
public class ApiPrefixConstant {

    private static final String VERSION = "/v1";
    private static final String BASE_PREFIX = "/base";
    public static final String FORM_CONTROLLER_PREFIX =
            BASE_PREFIX + VERSION + "/form";

    private ApiPrefixConstant() {
    }
}
