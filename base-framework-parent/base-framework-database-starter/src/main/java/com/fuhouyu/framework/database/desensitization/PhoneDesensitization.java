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

import com.fuhouyu.framework.database.CipherText;
import org.babyfish.jimmer.jackson.Converter;
import org.jspecify.annotations.NonNull;

/**
 * <p>
 * 手机号脱敏
 * </p>
 *
 * @author fuhouyu
 * @since 2026/3/3 21:01
 */
public class PhoneDesensitization implements Converter<CipherText, String> {
    @Override
    public @NonNull String output(@NonNull CipherText cipherText) {
        String value = cipherText.value();
        if (value.length() != 11) {
            // 长度不对，原样返回
            return value;
        }
        return value.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
    }
}
