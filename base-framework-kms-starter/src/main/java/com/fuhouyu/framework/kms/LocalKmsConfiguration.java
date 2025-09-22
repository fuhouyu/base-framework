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

import cn.hutool.crypto.SmUtil;
import cn.hutool.crypto.asymmetric.SM2;
import cn.hutool.crypto.digest.SM3;
import cn.hutool.crypto.symmetric.SM4;
import com.fuhouyu.framework.kms.properties.KmsProviderProperties;
import com.fuhouyu.framework.kms.properties.LocalKmsProviderProperties;
import com.fuhouyu.framework.kms.service.KmsService;
import com.fuhouyu.framework.kms.service.impl.DefaultKmsServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Assert;

import java.util.Base64;

/**
 * <p>
 * db kms 配置
 * </p>
 *
 * @author fuhouyu
 * @since 2025/7/27 10:30
 */
@Slf4j
@ConditionalOnProperty(prefix = KmsProviderProperties.PREFIX,
        name = "provider", havingValue = "local")
@Configuration(proxyBeanMethods = false)
@RequiredArgsConstructor
@EnableConfigurationProperties(LocalKmsProviderProperties.class)
public class LocalKmsConfiguration implements InitializingBean {

    private final LocalKmsProviderProperties localProperties;

    /**
     * kms服务
     *
     * @return kms服务
     */
    @Bean
    public KmsService kmsService() {
        SM2 sm2 = SmUtil.sm2(localProperties.getSm2().getPrivateKey(), localProperties.getSm2().getPublicKey());
        SM3 sm3 = SmUtil.sm3WithSalt(Base64.getDecoder().decode(localProperties.getSm3().getSecret()));
        SM4 sm4 = SmUtil.sm4(Base64.getDecoder().decode(localProperties.getSm4().getKey()));
        return new DefaultKmsServiceImpl(sm2, sm3, sm4);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.hasText(localProperties.getSm2().getPrivateKey(), "SM2 私钥未配置");
        Assert.hasText(localProperties.getSm2().getPublicKey(), "SM2 公钥未配置");
        Assert.hasText(localProperties.getSm3().getSecret(), "SM3 密钥未配置");
        Assert.hasText(localProperties.getSm4().getKey(), "SM4 密钥未配置");
    }
}
