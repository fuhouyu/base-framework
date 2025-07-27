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

import com.fuhouyu.framework.kms.enums.KeyTypeEnum;
import com.fuhouyu.framework.kms.exception.KmsException;
import com.fuhouyu.framework.kms.properties.KeyProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

/**
 * <p>
 * kms自动装配类
 * </p>
 *
 * @author fuhouyu
 * @since 2024/8/17 17:27
 */
@Import({
        DbKmsProviderConfiguration.class,
        DefaultKmsConfiguration.class
})
@EnableConfigurationProperties(KeyProperties.class)
@RequiredArgsConstructor
public class KmsAutoConfiguration implements InitializingBean {

    private final KeyProperties keyProperties;


    @Override
    public void afterPropertiesSet() throws Exception {
        Map<KeyTypeEnum, String> keyIds = keyProperties.getKeyIds();
        for (KeyTypeEnum value : KeyTypeEnum.values()) {
            if (!keyIds.containsKey(value)) {
                throw new KmsException(String.format("%s 的keyId未配置", value));
            }
        }
    }
}
