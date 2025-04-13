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

import cn.hutool.crypto.asymmetric.SM2;
import cn.hutool.crypto.digest.SM3;
import cn.hutool.crypto.symmetric.SM4;
import com.fuhouyu.framework.kms.service.KmsService;
import com.fuhouyu.framework.kms.service.impl.DefaultKmsServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.crypto.macs.CMac;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;

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
@Import({
        SM2Configuration.class,
        SM3Configuration.class,
        SM4Configuration.class
})
@Configuration(proxyBeanMethods = false)
@Slf4j
public class DefaultKmsConfiguration {

    /**
     * 返回默认的bean
     *
     * @return 默认的kms实现
     */
    @Bean
    @Primary
    @ConditionalOnMissingBean(KmsService.class)
    public KmsService kmsService(SM2 sm2,
                                 SM3 sm3,
                                 SM4 sm4,
                                 CMac cMac) {
        return new DefaultKmsServiceImpl(sm2, sm3, sm4, cMac);
    }


}
