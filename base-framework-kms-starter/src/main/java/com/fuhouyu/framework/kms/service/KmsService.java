/*
 * Copyright 2012-2020 the original author or authors.
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

package com.fuhouyu.framework.kms.service;

/**
 * <p>
 * kms相关接口
 * </p>
 *
 * @author fuhouyu
 * @since 2024/8/17 15:52
 */
public interface KmsService {

    /**
     * 获取非对称的公钥
     *
     * @return 公钥
     */
    String getAsymmetricPublicKey();

    /**
     * 获取非对称的公钥，十六进制
     *
     * @return 十六进制公钥
     */
    String getAsymmetricPublicKeyHex();

    /**
     * 非对称加密使用公钥加密
     *
     * @param originDataByte 原始数据
     * @return 加密后的字节数组
     */
    byte[] asymmetricEncrypt(byte[] originDataByte);

    /**
     * 非对称解密使用私钥解密
     *
     * @param encryptData 加密的数据
     * @return 原始数据
     */
    byte[] asymmetricDecrypt(byte[] encryptData);

    /**
     * 对数据进行签名，返回签名后的数据
     *
     * @param originData 原始数据
     * @return 签名后的字节数组
     */
    byte[] signature(byte[] originData);

    /**
     * 对数据进行签名，返回签名后的数
     *
     * @param originData 原始数据
     * @param salt       盐值
     * @return 签名后的字节数组
     */
    byte[] signature(byte[] originData,
                     String salt);

    /**
     * 签名验证
     *
     * @param signatureData 签名数据
     * @param originData    原始数据
     * @return true/false
     */
    boolean verifyDigest(byte[] signatureData,
                         byte[] originData);

    /**
     * 签名验证
     *
     * @param signatureData 签名数据
     * @param originData    原始数据
     * @param salt          盐值
     * @return true/false
     */
    boolean verifyDigest(byte[] signatureData,
                         byte[] originData,
                         String salt);


    /**
     * 对称加密
     *
     * @param originData 原始数据
     * @return 字节数组
     */
    byte[] symmetryEncrypt(byte[] originData);


    /**
     * 对称加密
     *
     * @param originData 原始数据
     * @param secretKey  密钥
     * @return 加密后的字节数组
     */
    byte[] symmetryEncrypt(byte[] originData,
                           String secretKey);


    /**
     * 对称加密
     *
     * @param originData 原始数据
     * @param iv         向量
     * @return 加密后的字节数组
     */
    byte[] symmetryEncrypt(byte[] originData,
                           byte[] iv);

    /**
     * 对称加密
     *
     * @param originData 原始数据
     * @param secretKey  密钥
     * @param iv         向量
     * @return 加密后的字节数组
     */
    byte[] symmetryEncrypt(byte[] originData,
                           String secretKey,
                           byte[] iv);

    /**
     * 对称解密
     *
     * @param encryptData 加密的字节数组
     * @return 解密后的字节数组
     */
    byte[] symmetryDecrypt(byte[] encryptData);

    /**
     * 对称解密
     *
     * @param encryptData 加密的字节数组
     * @param secretKey   密钥
     * @return 解密后的字节数组
     */
    byte[] symmetryDecrypt(byte[] encryptData,
                           String secretKey);


    /**
     * 对称解密
     *
     * @param encryptData 加密的字节数组
     * @param iv          向量
     * @return 加密后的字节数组
     */
    byte[] symmetryDecrypt(byte[] encryptData,
                           byte[] iv);

    /**
     * 对称解密
     *
     * @param encryptData 加密的字节数组
     * @param secretKey   密钥
     * @param iv          向量
     * @return 加密后的字节数组
     */
    byte[] symmetryDecrypt(byte[] encryptData,
                           String secretKey,
                           byte[] iv);

    /**
     * 对称解密
     *
     * @param encryptData 加密的字节数组
     * @param secretKey   密钥
     * @param iv          向量
     * @return 加密后的字节数组
     */
    byte[] symmetryDecrypt(byte[] encryptData,
                           byte[] secretKey,
                           byte[] iv);

    /**
     * 根据传入的filed值，计算mac值
     *
     * @param fields field列
     * @return mac值
     */
    String calculateMac(String... fields);

    /**
     * 验证mac值
     *
     * @param mac    mac值
     * @param fields field列
     * @return true 验证成功
     */
    boolean verifyMac(String mac, String... fields);

}
