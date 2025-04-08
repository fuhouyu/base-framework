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
import com.fuhouyu.framework.common.utils.LoggerUtil;
import com.fuhouyu.framework.kms.properties.KmsDefaultProperties;
import com.fuhouyu.framework.kms.service.KmsService;
import com.fuhouyu.framework.kms.service.impl.DefaultKmsServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.engines.SM4Engine;
import org.bouncycastle.crypto.macs.CMac;
import org.bouncycastle.crypto.params.KeyParameter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.util.Assert;

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
@Configuration(proxyBeanMethods = false)
@Slf4j
public class DefaultKmsConfiguration {

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
        byte[] publicKeyBytes = sm2Properties.getPublicKeyBytes();
        byte[] privateKeyBytes = sm2Properties.getPrivateKeyBytes();
        if (publicKeyBytes.length > 0 && privateKeyBytes.length > 0) {
            return SmUtil.sm2(privateKeyBytes, publicKeyBytes);
        }
        // 生成sm2配置
        LoggerUtil.warn(log, "sm2公私钥未设置，生成公私钥");
        return sm2Properties.generateSm2();
    }


    /**
     * 初始化sm3配置
     *
     * @return sm3
     */
    private SM3 initSm3() {
        KmsDefaultProperties.Sm3Properties sm3Properties = this.properties.getSm3();
        byte[] saltBytes = sm3Properties.getSaltBytes();
        if (saltBytes.length == 0) {
            LoggerUtil.warn(log, "sm3盐值未设置，使用默认方法：SmUtil.sm3()");
            return SmUtil.sm3();
        }
        return SmUtil.sm3WithSalt(saltBytes);
    }

    /**
     * 初始化sm4配置
     *
     * @return sm4
     */
    private SM4 initSm4() {
        KmsDefaultProperties.Sm4Properties sm4 = properties.getSm4();
        byte[] secretKeyFileBytes = sm4.getSecretKeyBytes();
        Assert.isTrue(secretKeyFileBytes.length == 16, "sm4密码位数不正确，必须为128位");
        Mode mode = sm4.getMode();
        Padding padding = sm4.getPadding();
        return Objects.nonNull(mode) && Objects.nonNull(padding) ?
                new SM4(mode.name(), padding.name(), secretKeyFileBytes) :
                SmUtil.sm4(secretKeyFileBytes);
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

}
