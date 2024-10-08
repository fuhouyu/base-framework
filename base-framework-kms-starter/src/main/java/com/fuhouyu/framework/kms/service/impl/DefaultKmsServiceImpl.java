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

package com.fuhouyu.framework.kms.service.impl;


import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.SM2;
import cn.hutool.crypto.digest.SM3;
import cn.hutool.crypto.symmetric.SM4;
import com.fuhouyu.framework.kms.exception.KmsException;
import com.fuhouyu.framework.kms.service.KmsService;
import org.bouncycastle.crypto.macs.CMac;
import org.bouncycastle.util.encoders.Hex;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;
import java.util.Objects;

/**
 * <p>
 * 默认实现
 * </p>
 *
 * @author fuhouyu
 * @since 2024/8/17 16:14
 */
public class DefaultKmsServiceImpl implements KmsService {

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

    /**
     * 用于计算完整性
     */
    private final CMac cmac;

    public DefaultKmsServiceImpl(SM2 sm2, SM3 sm3, SM4 sm4, CMac cmac) {
        this.sm2 = sm2;
        this.sm3 = sm3;
        this.sm4 = sm4;
        this.cmac = cmac;
    }

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
    public boolean verifyDigest(byte[] signatureData, byte[] originData) {
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

    @Override
    public String calculateMac(String... fields) {
        return this.doCmac(fields);
    }

    @Override
    public boolean verifyMac(String mac, String... fields) {
        return Objects.equals(this.doCmac(fields), mac);
    }

    /**
     * 计算mac值
     *
     * @param fields 列名
     * @return base64加密后的字符串
     */
    private String doCmac(String... fields) {
        byte[] byteArrays = this.combineByteArrays(fields);
        cmac.update(byteArrays, 0, byteArrays.length);
        // 计算最终的MAC值
        byte[] mac = new byte[cmac.getMacSize()];
        this.cmac.doFinal(mac, 0);
        return Base64.getEncoder().encodeToString(mac);
    }


    /**
     * 合并数组
     *
     * @param fields 列
     * @return 合并后的数组
     */
    private byte[] combineByteArrays(String... fields) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            for (String field : fields) {
                outputStream.write(field.getBytes(StandardCharsets.UTF_8));
            }
            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new KmsException(e);
        }
    }
}



