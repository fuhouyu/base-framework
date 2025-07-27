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
import com.fuhouyu.framework.kms.mapper.AsymmetricKeyMapper;
import com.fuhouyu.framework.kms.mapper.DigestKeyMapper;
import com.fuhouyu.framework.kms.mapper.SymmetricKeyMapper;
import lombok.RequiredArgsConstructor;

import java.util.Objects;

/**
 * <p>
 * db密钥实现
 * </p>
 *
 * @author fuhouyu
 * @since 2025/7/26 21:29
 */
@RequiredArgsConstructor
public class DbKeyProvider implements KeyProvider {

    private final SymmetricKeyMapper symmetricKeyMapper;

    private final AsymmetricKeyMapper asymmetricKeyMapper;

    private final DigestKeyMapper digestKeyMapper;


    @Override
    public SymmetricKey getSymmetricKey(String keyId) {
        SymmetricKey key = symmetricKeyMapper.selectById(keyId);
        if (Objects.isNull(key)) {
            throw new KeyNotFoundException("对称密钥不存在，keyId: " + keyId);
        }
        return key;
    }

    @Override
    public AsymmetricKey getAsymmetricKey(String keyId) {
        AsymmetricKey key = asymmetricKeyMapper.selectById(keyId);
        if (Objects.isNull(key)) {
            throw new KeyNotFoundException("非对称密钥不存在，keyId: " + keyId);
        }
        return key;
    }

    @Override
    public DigestKey getDigestKey(String keyId) {
        DigestKey key = digestKeyMapper.selectById(keyId);
        if (Objects.isNull(key)) {
            throw new KeyNotFoundException("签名密钥不存在，keyId: " + keyId);
        }
        return key;
    }

}
