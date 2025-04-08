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
package com.fuhouyu.framework.kms;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.PathUtil;
import cn.hutool.crypto.SmUtil;
import cn.hutool.crypto.digest.SM3;
import com.fuhouyu.framework.common.utils.LoggerUtil;
import com.fuhouyu.framework.kms.properties.SM3Properties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.SystemUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.util.Objects;

/**
 * <p>
 * sm3配置
 * </p>
 *
 * @author fuhouyu
 * @since 2025/4/8 19:07
 */
@Configuration(proxyBeanMethods = false)
@Slf4j
@RequiredArgsConstructor
@EnableConfigurationProperties({SM3Properties.class})
public class SM3Configuration {

    private static final Path SM3_SALT_PATH = Paths.get(SystemUtils.getUserHome().getAbsolutePath(), "sm3", "salt.key");

    private final SM3Properties sm3Properties;

    /**
     * sm3配置
     *
     * @return sm3
     */
    @Bean
    @ConditionalOnMissingBean(SM3.class)
    public SM3 sm3() {
        byte[] saltBytes = this.getSaltBytes();
        return SmUtil.sm3WithSalt(saltBytes);
    }

    /**
     * 获取盐值
     *
     * @return 盐值字节数组
     */
    private byte[] getSaltBytes() {
        String saltFilePath = this.sm3Properties.getSaltFilePath();
        if (Objects.nonNull(saltFilePath)) {
            return FileUtil.readBytes(saltFilePath);
        }
        String salt = this.sm3Properties.getSalt();
        if (Objects.nonNull(salt)) {
            return salt.getBytes(StandardCharsets.UTF_8);
        }
        return this.generateSaltBytes();
    }

    /**
     * 生成sm3
     * 如果已经生成sm3，则进行读取
     *
     * @return sm3
     */
    private byte[] generateSaltBytes() {
        if (SM3_SALT_PATH.toFile().exists()) {
            LoggerUtil.warn(log, "已存在生成的盐值，盐值路径:{}", SM3_SALT_PATH);
            return PathUtil.readBytes(SM3_SALT_PATH);
        }
        SecureRandom secureRandom = new SecureRandom();
        byte[] bytes = new byte[16];
        secureRandom.nextBytes(bytes);
        FileUtil.writeBytes(bytes, SM3_SALT_PATH.toFile());
        LoggerUtil.warn(log, "自动生成sm3配置文件的路径: {}", SM3_SALT_PATH);
        return bytes;
    }
}
