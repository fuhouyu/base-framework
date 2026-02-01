/*
 * Copyright 2024-2025 fuhouyu.
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
package com.fuhouyu.framework.common.desensitize;

/**
 * <p>
 * 手机号脱敏器
 * </p>
 *
 * @author fuhouyu
 * @since 2025/3/30 18:56
 */
public class PhoneDesensitization implements DefaultDesensitization {

    private static final String DEFAULT_PATTERN = "^(\\+?86)?1[3-9]\\d{9}$";

    @Override
    public String desensitize(String phone) {
        if (phone == null || !phone.matches(DEFAULT_PATTERN)) {
            return phone;
        }
        return phone.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
    }
}
