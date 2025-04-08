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
import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import cn.hutool.crypto.SmUtil;
import cn.hutool.crypto.symmetric.SM4;
import com.fuhouyu.framework.common.utils.LoggerUtil;
import com.fuhouyu.framework.kms.properties.SM4Properties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.SystemUtils;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.engines.SM4Engine;
import org.bouncycastle.crypto.macs.CMac;
import org.bouncycastle.crypto.params.KeyParameter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Assert;

import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.util.Objects;

/**
 * <p>
 * sm4配置
 * </p>
 *
 * @author fuhouyu
 * @since 2025/4/8 19:20
 */
@Configuration(proxyBeanMethods = false)
@Slf4j
@RequiredArgsConstructor
@EnableConfigurationProperties({SM4Properties.class})
public class SM4Configuration {

    private static final Path SM4_SECRET_KEY_PATH = Paths.get(SystemUtils.getUserHome().getAbsolutePath(), "sm4", "secret.key");

    private final SM4Properties sm4Properties;


    @Bean
    @ConditionalOnMissingBean(SM4.class)
    public SM4 sm4() {
        byte[] secretKeyBytes = this.getSecretKeyBytes();
        Assert.isTrue(secretKeyBytes.length == 16, "sm4密码位数不正确，必须为128位");
        Mode mode = this.sm4Properties.getMode();
        Padding padding = this.sm4Properties.getPadding();
        return Objects.nonNull(mode) && Objects.nonNull(padding) ?
                new SM4(mode.name(), padding.name(), secretKeyBytes) :
                SmUtil.sm4(secretKeyBytes);
    }


    /**
     * CMac 签名
     *
     * @return CMac bean
     */
    @Bean
    @ConditionalOnMissingBean(CMac.class)
    public CMac mac() {
        SM4Engine sm4Engine = new SM4Engine();
        CMac cmac = new CMac(sm4Engine);
        // 密钥，注意密钥长度必须符合SM4算法规范
        CipherParameters key = new KeyParameter(this.getSecretKeyBytes());
        cmac.init(key);
        return cmac;
    }

    /**
     * 获取secretKey 字节
     *
     * @return 密钥字节数组
     */
    private byte[] getSecretKeyBytes() {
        String keyFilePath = this.sm4Properties.getKeyFilePath();
        String secretKey = this.sm4Properties.getSecretKey();
        if (Objects.isNull(keyFilePath)) {
            if (Objects.isNull(secretKey)) {
                return this.getDefaultSecretKey();
            }
            return secretKey.getBytes(StandardCharsets.UTF_8);
        }
        return FileUtil.readBytes(keyFilePath);
    }

    /**
     * 获取默认key，如果不存在，生成一个默认值
     *
     * @return secretKey
     */
    private byte[] getDefaultSecretKey() {
        if (SM4_SECRET_KEY_PATH.toFile().exists()) {
            LoggerUtil.warn(log, "已存在生成的sm4密钥文件:{}", SM4_SECRET_KEY_PATH);
            return PathUtil.readBytes(SM4_SECRET_KEY_PATH);
        }
        SecureRandom secureRandom = new SecureRandom();
        byte[] bytes = new byte[16];
        secureRandom.nextBytes(bytes);
        LoggerUtil.warn(log, "未设置secret Key，生成默认值，存储至默认路径: {}", SM4_SECRET_KEY_PATH);
        FileUtil.writeBytes(bytes, SM4_SECRET_KEY_PATH.toFile());
        return bytes;
    }
}
