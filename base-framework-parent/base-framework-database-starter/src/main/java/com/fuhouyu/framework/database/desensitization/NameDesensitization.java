/*
 * Copyright 2024-present fuhouyu.
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
package com.fuhouyu.framework.database.desensitization;

import org.babyfish.jimmer.jackson.Converter;
import org.jspecify.annotations.NonNull;
import org.springframework.util.StringUtils;

/**
 * <p>
 * 姓名脱敏
 * </p>
 *
 * @author fuhouyu
 * @since 2026/3/3 21:08
 */
public class NameDesensitization implements Converter<String, String> {

    @Override
    public @NonNull String output(@NonNull String value) {
        if (!StringUtils.hasText(value) || value.length() < 2) {
            return value;
        }

        int length = value.length();
        StringBuilder sb = new StringBuilder(length);

        if (length == 2) {
            // 二字姓名：保留第一个，脱敏最后一个
            sb.append(value.charAt(0)).append("*");
        } else if (length == 3) {
            // 三字姓名：保留首尾，脱敏中间
            sb.append(value.charAt(0)).append("*").append(value.charAt(2));
        } else {
            // 四字及以上：保留前二后一，中间全部脱敏
            sb.append(value, 0, 2);
            sb.repeat("*", length - 3);
            sb.append(value.charAt(length - 1));
        }

        return sb.toString();
    }
}
