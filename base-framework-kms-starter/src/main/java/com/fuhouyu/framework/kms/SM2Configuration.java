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
import cn.hutool.crypto.asymmetric.SM2;
import com.fuhouyu.framework.common.utils.LoggerUtil;
import com.fuhouyu.framework.kms.properties.SM2Properties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.SystemUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

/**
 * <p>
 * sm2配置项
 * </p>
 *
 * @author fuhouyu
 * @since 2025/4/8 18:59
 */
@Configuration(proxyBeanMethods = false)
@Slf4j
@RequiredArgsConstructor
@EnableConfigurationProperties({SM2Properties.class})
public class SM2Configuration {

    private static final Path DEFAULT_PUBLIC_KEY_PATH = Paths.get(SystemUtils.getUserHome().getAbsolutePath(), "sm2", "public.key");
    private static final Path DEFAULT_PRIVATE_KEY_PATH = Paths.get(SystemUtils.getUserHome().getAbsolutePath(), "sm2", "private.key");
    private final SM2Properties sm2Properties;

    /**
     * sm2配置
     *
     * @return sm2
     */
    @Bean
    @ConditionalOnMissingBean(SM2.class)
    public SM2 sm2() {
        byte[] publicKeyBytes = this.getPublicKeyBytes();
        byte[] privateKeyBytes = this.getPrivateKeyBytes();
        if (publicKeyBytes.length > 0 && privateKeyBytes.length > 0) {
            return SmUtil.sm2(privateKeyBytes, publicKeyBytes);
        }
        // 生成sm2配置
        LoggerUtil.warn(log, "sm2公私钥未设置，生成公私钥");
        return this.generateSm2();
    }


    /**
     * 读取公钥
     *
     * @return 公钥字节
     */
    public byte[] getPublicKeyBytes() {
        String publicKeyPath = this.sm2Properties.getPublicKeyPath();
        String publicKey = this.sm2Properties.getPublicKey();
        if (Objects.nonNull(publicKeyPath)) {
            return FileUtil.readBytes(publicKeyPath);
        }
        if (Objects.nonNull(publicKey)) {
            return publicKey.getBytes(StandardCharsets.UTF_8);
        }
        return new byte[0];
    }

    /**
     * 读取私钥
     *
     * @return 私钥字节
     */
    public byte[] getPrivateKeyBytes() {
        String privateKey = this.sm2Properties.getPrivateKey();
        String privateKeyPath = this.sm2Properties.getPrivateKeyPath();
        if (Objects.nonNull(privateKeyPath)) {
            return FileUtil.readBytes(privateKeyPath);
        }
        if (Objects.nonNull(privateKey)) {
            return privateKey.getBytes(StandardCharsets.UTF_8);
        }
        return new byte[0];
    }

    /**
     * 生成一个sm2
     *
     * @return sm2
     */
    public SM2 generateSm2() {
        if (Files.exists(DEFAULT_PUBLIC_KEY_PATH) && Files.exists(DEFAULT_PRIVATE_KEY_PATH)) {
            LoggerUtil.warn(log, "已存在生成的公私钥文件，直接读取，公钥路径:{}, 私钥路径:{}",
                    DEFAULT_PUBLIC_KEY_PATH, DEFAULT_PRIVATE_KEY_PATH);
            return SmUtil.sm2(PathUtil.readBytes(DEFAULT_PRIVATE_KEY_PATH), PathUtil.readBytes(DEFAULT_PUBLIC_KEY_PATH));
        }
        SM2 sm2 = SmUtil.sm2();
        FileUtil.writeBytes(sm2.getPrivateKey().getEncoded(), DEFAULT_PRIVATE_KEY_PATH.toFile());
        FileUtil.writeBytes(sm2.getPublicKey().getEncoded(), DEFAULT_PUBLIC_KEY_PATH.toFile());
        LoggerUtil.warn(log, "公私钥不存在，进行生成，公钥路径:{}, 私钥路径:{}",
                DEFAULT_PUBLIC_KEY_PATH, DEFAULT_PRIVATE_KEY_PATH);
        return sm2;
    }
}
