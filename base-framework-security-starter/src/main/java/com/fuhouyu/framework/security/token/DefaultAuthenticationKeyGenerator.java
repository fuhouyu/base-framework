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

package com.fuhouyu.framework.security.token;

import com.fuhouyu.framework.context.ContextHolderStrategy;
import com.fuhouyu.framework.context.Request;
import org.springframework.security.core.Authentication;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * <p>
 * 默认key信息提取类
 * </p>
 *
 * @author fuhouyu
 * @since 2024/8/14 22:10
 */
public class DefaultAuthenticationKeyGenerator implements AuthenticationKeyGenerator {


    @Override
    public String extractKey(Authentication authentication) {
        Object principal = authentication.getPrincipal();
        Map<String, Object> map = new HashMap<>();
        map.put("principal", principal);

        if (Objects.nonNull(ContextHolderStrategy.getContext())
                && Objects.nonNull(ContextHolderStrategy.getContext().getRequest())) {
            Request request = ContextHolderStrategy.getContext().getRequest();
            map.put("ip", request.getRequestIp());
        }
        return this.generateKey(map);
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
