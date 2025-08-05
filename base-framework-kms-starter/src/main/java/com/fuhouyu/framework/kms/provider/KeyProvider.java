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
package com.fuhouyu.framework.kms.provider;

import com.fuhouyu.framework.kms.entity.AsymmetricKey;
import com.fuhouyu.framework.kms.entity.DigestKey;
import com.fuhouyu.framework.kms.entity.SymmetricKey;
import com.fuhouyu.framework.kms.exception.KeyNotFoundException;

/**
 * <p>
 * 密钥提供者接口
 * 支持获取对称、非对称、签名密钥等。
 * </p>
 *
 * @author fuhouyu
 * @since 2025/7/26 20:31
 */
public interface KeyProvider {

    /**
     * 获取对称密钥（如 AES、SM4）
     *
     * @param keyId 密钥 ID
     * @return 对称密钥实体
     */
    SymmetricKey getSymmetricKey(String keyId) throws KeyNotFoundException;

    /**
     * 获取非对称密钥对（如 RSA、SM2）
     *
     * @param keyId 密钥 ID
     * @return 非对称密钥对
     */
    AsymmetricKey getAsymmetricKey(String keyId) throws KeyNotFoundException;

    /**
     * 获取签名密钥（如 SM3、HMAC）
     *
     * @param keyId 密钥 ID
     * @return 签名密钥
     */
    DigestKey getDigestKey(String keyId) throws KeyNotFoundException;
}
