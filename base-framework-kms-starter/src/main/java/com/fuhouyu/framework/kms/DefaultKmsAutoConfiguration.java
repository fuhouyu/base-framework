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

package com.fuhouyu.framework.kms;

import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import cn.hutool.crypto.SmUtil;
import cn.hutool.crypto.asymmetric.SM2;
import cn.hutool.crypto.digest.SM3;
import cn.hutool.crypto.symmetric.SM4;
import com.fuhouyu.framework.kms.exception.KmsException;
import com.fuhouyu.framework.kms.properties.KmsDefaultProperties;
import com.fuhouyu.framework.kms.service.KmsService;
import com.fuhouyu.framework.kms.service.impl.DefaultKmsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.engines.SM4Engine;
import org.bouncycastle.crypto.macs.CMac;
import org.bouncycastle.crypto.params.KeyParameter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Objects;

/**
 * <p>
 * 默认的配置项，当不存在kms的实现时，启用默认的配置
 * </p>
 *
 * @author fuhouyu
 * @since 2024/8/17 17:33
 */
@ConditionalOnMissingBean(KmsService.class)
@EnableConfigurationProperties(KmsDefaultProperties.class)
@RequiredArgsConstructor
public class DefaultKmsAutoConfiguration {

    private final KmsDefaultProperties properties;

    /**
     * 返回默认的bean
     *
     * @return 默认的kms实现
     */
    @Bean
    @Primary
    @ConditionalOnMissingBean(KmsService.class)
    public KmsService kmsService() {

        SM2 sm2 = this.initSm2();
        SM3 sm3 = this.initSm3();
        SM4 sm4 = this.initSm4();
        return new DefaultKmsServiceImpl(sm2, sm3, sm4, this.initCmac(sm4));
    }

    /**
     * 初始化sm2配置
     *
     * @return sm2配置
     */
    private SM2 initSm2() {
        KmsDefaultProperties.Sm2Properties sm2Properties = properties.getSm2();
        String privateKey = sm2Properties.getPrivateKey();
        String publicKey = sm2Properties.getPublicKey();
        if (StringUtils.hasText(privateKey)
                && StringUtils.hasText(publicKey)) {
            return SmUtil.sm2(privateKey, publicKey);
        }
        return this.generatorSm2();
    }


    /**
     * 初始化sm3配置
     *
     * @return sm3
     */
    private SM3 initSm3() {
        if (Objects.isNull(properties.getSm3())) {
            return SmUtil.sm3();
        }
        KmsDefaultProperties.Sm3Properties sm3Properties = this.properties.getSm3();
        String salt = sm3Properties.getSalt();
        if (StringUtils.hasText(salt)) {
            return SmUtil.sm3WithSalt(salt.getBytes(StandardCharsets.UTF_8));
        }
        return SmUtil.sm3();
    }

    /**
     * 初始化sm4配置
     *
     * @return sm4
     */
    private SM4 initSm4() {
        KmsDefaultProperties.Sm4Properties sm4 = properties.getSm4();
        if (Objects.isNull(sm4)) {
            return SmUtil.sm4();
        }
        String secretKey = sm4.getSecretKey();
        Mode mode = sm4.getMode();
        Padding padding = sm4.getPadding();
        if (Objects.nonNull(mode) && Objects.nonNull(padding)) {
            if (StringUtils.hasText(secretKey)) {
                Assert.isTrue(secretKey.length() == 16, "sm4密码位数不正确，必须为16长度的字符串");
                return new SM4(mode.name(), padding.name(), secretKey.getBytes(StandardCharsets.UTF_8));
            } else {
                return new SM4(mode.name(), padding.name());
            }
        }
        return StringUtils.hasText(secretKey) ? SmUtil.sm4(secretKey.getBytes(StandardCharsets.UTF_8)) : SmUtil.sm4();
    }


    /**
     * cMac配置，用于计算完整性数据
     *
     * @param sm4 sm4算法，这里用的密钥和其保持一致
     * @return CMac
     */
    private CMac initCmac(SM4 sm4) {
        SM4Engine sm4Engine = new SM4Engine();
        CMac cmac = new CMac(sm4Engine);
        // 密钥，注意密钥长度必须符合SM4算法规范
        CipherParameters key = new KeyParameter(sm4.getSecretKey().getEncoded());
        cmac.init(key);
        return cmac;
    }


    /**
     * 生成一个sm2配置
     *
     * @return sm2
     */
    private SM2 generatorSm2() {
        KmsDefaultProperties.Sm2Properties sm2Properties = this.properties.getSm2();
        if (Boolean.FALSE.equals(sm2Properties.getAutoGenerate())) {
            throw new KmsException("sm2 公私钥未设置，且未启用自动生成");
        }

        String parentPath = sm2Properties.getAutoGenerateLocalPath();
        Path publicKeyPath = Path.of(parentPath, "publicKey");
        Path privateKeyPath = Path.of(parentPath, "privateKey");
        if (Files.exists(publicKeyPath) && Files.exists(privateKeyPath)) {
            return SmUtil.sm2(this.readFileAllString(privateKeyPath.toString()), this.readFileAllString(publicKeyPath.toString()));
        }
        try {
            Path path = Path.of(parentPath);
            Files.deleteIfExists(path);
            Files.createDirectory(path);

            SM2 sm2 = SmUtil.sm2();
            this.writeStrToPath(publicKeyPath, sm2.getPublicKeyBase64());
            this.writeStrToPath(privateKeyPath, sm2.getPrivateKeyBase64());
            return sm2;
        } catch (IOException e) {
            throw new KmsException(e);
        }
    }

    /**
     * 读取文件中的所有字符串
     *
     * @param filePath 文件路径
     * @return 字符串信息
     */
    private String readFileAllString(String filePath) {
        try {
            return Files.readString(Paths.get(filePath));
        } catch (IOException e) {
            throw new KmsException(e);
        }
    }

    /**
     * 写入文本数据
     *
     * @param path 路径
     * @param data 需要写入的数据
     */
    private void writeStrToPath(Path path, String data) {
        try {
            Files.writeString(path, data, StandardCharsets.UTF_8, StandardOpenOption.WRITE,
                    StandardOpenOption.CREATE);
        } catch (IOException e) {
            throw new KmsException(e);
        }
    }

}
