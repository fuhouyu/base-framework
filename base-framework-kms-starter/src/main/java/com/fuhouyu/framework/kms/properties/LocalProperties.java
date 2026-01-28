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
package com.fuhouyu.framework.kms.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * <p>
 * kms 本地配置
 * </p>
 *
 * @author fuhouyu
 * @since 2026/1/28 19:52
 */
@Data
@ConfigurationProperties(prefix = KmsProviderProperties.PREFIX + ".local")
public class LocalProperties {

    /**
     * SM2 公钥/私钥
     */
    private Sm2 sm2 = new Sm2();

    /**
     * SM3 摘要/签名密钥
     */
    private Sm3 sm3 = new Sm3();

    /**
     * SM4 对称加密密钥
     */
    private Sm4 sm4 = new Sm4();

    @Data
    public static class Sm2 {
        /**
         * 公钥
         */
        private String publicKey;
        /**
         * 私钥
         */
        private String privateKey;
    }

    @Data
    public static class Sm3 {
        /**
         * 签名/摘要密钥
         */
        private String secret;
    }

    @Data
    public static class Sm4 {
        /**
         * 对称加密密钥
         */
        private String key;
    }
}
