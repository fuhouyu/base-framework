/*
 * Copyright 2024-present fuhouyu.
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

package com.fuhouyu.framework.kms.service.impl;


import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.SM2;
import cn.hutool.crypto.digest.SM3;
import cn.hutool.crypto.symmetric.SM4;
import com.fuhouyu.framework.kms.service.KmsService;
import lombok.RequiredArgsConstructor;
import org.bouncycastle.util.encoders.Hex;

import java.util.Arrays;

/**
 * <p>
 * 默认实现
 * </p>
 *
 * @author fuhouyu
 * @since 2024/8/17 16:14
 */
@RequiredArgsConstructor
public class SmKmsServiceImpl implements KmsService {

    /**
     * 非对称加密
     */
    private final SM2 sm2;

    /**
     * 摘要加密算法
     */
    private final SM3 sm3;

    /**
     * 对称加密
     */
    private final SM4 sm4;

    @Override
    public String getAsymmetricPublicKey() {
        return this.sm2.getPublicKeyBase64();
    }

    @Override
    public String getAsymmetricPublicKeyHex() {
        return Hex.toHexString(this.sm2.getQ(false));
    }

    @Override
    public byte[] asymmetricEncrypt(byte[] originDataByte) {
        return this.sm2.encrypt(originDataByte, KeyType.PublicKey);
    }

    @Override
    public byte[] asymmetricDecrypt(byte[] encryptData) {
        return this.sm2.decrypt(encryptData, KeyType.PrivateKey);
    }

    @Override
    public byte[] signature(byte[] originData) {
        return this.sm3.digest(originData);
    }

    @Override
    public boolean verifySignature(byte[] signatureData, byte[] originData) {
        return Arrays.equals(this.sm3.digest(originData), signatureData);
    }


    @Override
    public byte[] symmetryEncrypt(byte[] originData) {
        return this.sm4.encrypt(originData);
    }


    @Override
    public byte[] symmetryDecrypt(byte[] encryptData) {
        return this.sm4.decrypt(encryptData);
    }

}



