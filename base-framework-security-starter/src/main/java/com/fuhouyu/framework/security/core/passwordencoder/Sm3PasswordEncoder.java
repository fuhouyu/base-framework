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

package com.fuhouyu.framework.security.core.passwordencoder;

import cn.hutool.crypto.SmUtil;
import com.fuhouyu.framework.utils.LoggerUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.utils.Base64;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Objects;

/**
 * <p>
 * sm3
 * </p>
 *
 * @author fuhouyu
 * @since 2024/9/7 22:25
 */
@Slf4j
public class Sm3PasswordEncoder implements PasswordEncoder {

    private static final char[] RANDOM_CHAR = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789./".toCharArray();

    private static final String DEFAULT_SM3_PREFIX = "$3m";

    private final String prefix;

    private final SecureRandom random;

    /**
     * 构造函数
     */
    public Sm3PasswordEncoder() {
        this(DEFAULT_SM3_PREFIX, null);
    }

    /**
     * 构造函数
     *
     * @param prefix 前缀
     */
    public Sm3PasswordEncoder(String prefix) {
        this(prefix, null);
    }

    /**
     * 构造函数
     * @param random 安全随机数
     */
    public Sm3PasswordEncoder(SecureRandom random) {
        this(DEFAULT_SM3_PREFIX, random);
    }

    /**
     * 构造函数
     * @param prefix 密码前缀
     * @param random 安全随机数
     */
    public Sm3PasswordEncoder(String prefix, SecureRandom random) {
        this.prefix = prefix;
        this.random = random;
    }

    @Override
    public String encode(CharSequence rawPassword) {
        if (Objects.isNull(rawPassword)) {
            throw new IllegalArgumentException("rawPassword cannot be null");
        }
        String salt = this.getSalt();
        return salt + this.hashPw(salt, rawPassword.toString());
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        if (!encodedPassword.startsWith(this.prefix)) {
            LoggerUtil.error(log, "encodedPassword is {}, 非sm3国密编码", rawPassword, encodedPassword);
            return false;
        }
        String salt = encodedPassword.substring(0, 25);
        return MessageDigest.isEqual(
                (salt + this.hashPw(salt, rawPassword.toString())).getBytes(StandardCharsets.UTF_8),
                encodedPassword.getBytes(StandardCharsets.UTF_8));
    }


    /**
     * 对输入的密码进行hash
     *
     * @param salt        盐值
     * @param rawPassword 原始密码
     * @return hash后的密码
     */
    private String hashPw(String salt, String rawPassword) {
        return Base64.encodeBase64String(SmUtil.sm3WithSalt(salt.getBytes(StandardCharsets.UTF_8))
                .digest(rawPassword.getBytes(StandardCharsets.UTF_8))).replace("=", "");
    }

    /**
     * 获取盐值
     *
     * @return 盐值
     */
    private String getSalt() {
        StringBuilder sb = new StringBuilder();
        byte[] rnd = new byte[16];
        SecureRandom secureRandom = Objects.isNull(random) ? new SecureRandom() : this.random;
        for (int i = 0; i < 16; i++) {
            int randomIndex = secureRandom.nextInt(RANDOM_CHAR.length);
            rnd[i] = (byte) RANDOM_CHAR[randomIndex];
        }
        sb.append("$3")
                .append(this.prefix.charAt(2));
        sb.append(Base64.encodeBase64String(rnd).replace("=", ""));
        return sb.toString();
    }


}
