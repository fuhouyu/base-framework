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
import com.fuhouyu.framework.kms.entity.AsymmetricKey;
import com.fuhouyu.framework.kms.entity.DigestKey;
import com.fuhouyu.framework.kms.entity.SymmetricKey;
import com.fuhouyu.framework.kms.enums.KeyTypeEnum;
import com.fuhouyu.framework.kms.properties.KeyProperties;
import com.fuhouyu.framework.kms.provider.KeyProvider;
import com.fuhouyu.framework.kms.service.KmsService;
import com.fuhouyu.framework.kms.service.impl.DefaultKmsServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.engines.SM4Engine;
import org.bouncycastle.crypto.macs.CMac;
import org.bouncycastle.crypto.params.KeyParameter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

/**
 * <p>
 * 默认的配置项，当不存在kms的实现时，启用默认的配置
 * </p>
 *
 * @author fuhouyu
 * @since 2024/8/17 17:33
 */
@ConditionalOnMissingBean(KmsService.class)
@RequiredArgsConstructor
@Configuration(proxyBeanMethods = false)
@Slf4j
public class DefaultKmsConfiguration {

    private final KeyProvider keyProvider;

    private final KeyProperties keyProperties;


    /**
     * 返回默认的bean
     *
     * @return 默认的kms实现
     */
    @Bean
    @Primary
    @ConditionalOnMissingBean(KmsService.class)
    public KmsService kmsService() {
        return new DefaultKmsServiceImpl(this.sm2(), this.sm3(), this.sm4());
    }

    private SM2 sm2() {
        Map<KeyTypeEnum, String> keyIds = keyProperties.getKeyIds();
        String asymmetricKeyId = keyIds.get(KeyTypeEnum.ASYMMETRIC);
        AsymmetricKey asymmetricKey = this.keyProvider.getAsymmetricKey(asymmetricKeyId);
        return SmUtil.sm2(asymmetricKey.getPrivateKey(), asymmetricKey.getPublicKey());
    }

    private SM3 sm3() {
        Map<KeyTypeEnum, String> keyIds = keyProperties.getKeyIds();
        String digestKeyId = keyIds.get(KeyTypeEnum.DIGEST);
        DigestKey digestKey = this.keyProvider.getDigestKey(digestKeyId);
        return SmUtil.sm3WithSalt(digestKey.getSalt());
    }


    private SM4 sm4() {
        Map<KeyTypeEnum, String> keyIds = keyProperties.getKeyIds();
        String symmetricKeyId = keyIds.get(KeyTypeEnum.SYMMETRIC);
        SymmetricKey symmetricKey = this.keyProvider.getSymmetricKey(symmetricKeyId);
        return SmUtil.sm4(symmetricKey.getSecret());
    }

}
