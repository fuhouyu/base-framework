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
 * id 身份证脱敏
 * </p>
 *
 * @author fuhouyu
 * @since 2025/3/30 19:05
 */
public class IdCardDesensitization implements DefaultDesensitization {

    private static final String DEFAULT_PATTERN = "^[1-9]\\d{5}(18|19|20)\\d{2}(0[1-9]|1[0-2])(0[1-9]|[12]\\d|3[01])\\d{3}[\\dXx]$";

    @Override
    public String desensitize(String idCard) {
        if (idCard == null || !idCard.matches(DEFAULT_PATTERN)) {
            return idCard;
        }
        return idCard.replaceAll("(\\d)\\d{16}(\\d)", "$1****************$2");

    }
}
