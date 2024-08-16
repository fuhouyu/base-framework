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

package com.fuhouyu.framework.security.token;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fuhouyu.framework.utils.JacksonUtil;
import org.springframework.security.core.Authentication;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * 默认key信息提取类
 * </p>
 *
 * @author fuhouyu
 * @since 2024/8/14 22:10
 */
public class DefaultAuthenticationKeyGenerator implements AuthenticationKeyGenerator {


    private static final String[] FILTER_ATTR = new String[] {
            "password", "secret", "authorities", "principal"
    };

    @Override
    public String extractKey(Authentication authentication) {
        Object principal = authentication.getPrincipal();
        Map<String, Object> valuesMap =
                JacksonUtil.tryParse(() -> JacksonUtil.getObjectMapper().convertValue(principal, new TypeReference<HashMap<String, Object>>() {
                }));
        this.filterMapSecret(valuesMap);
        return this.generateKey(valuesMap);
    }

    private void filterMapSecret(Map<String, Object> map) {
        Set<Map.Entry<String, Object>> entries = map.entrySet();
        entries.removeIf(entry -> {
            for (String filter : FILTER_ATTR) {
                if (entry.getKey().toLowerCase(Locale.ROOT).contains(filter.toLowerCase(Locale.ROOT))) {
                    return true;
                }
            }
            return false;
        });

    }

    private String generateKey(Map<String, Object> values) {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("MD5");
            byte[] bytes = digest.digest(values.toString().getBytes(StandardCharsets.UTF_8));
            return String.format("%032x", new BigInteger(1, bytes));
        } catch (NoSuchAlgorithmException nsae) {
            throw new IllegalStateException(
                    "MD5 algorithm not available.  Fatal (should be in the JDK).", nsae);
        }
    }
}
