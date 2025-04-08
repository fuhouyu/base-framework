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

package com.fuhouyu.framework.kms.properties;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.PathUtil;
import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import cn.hutool.crypto.SmUtil;
import cn.hutool.crypto.asymmetric.SM2;
import com.fuhouyu.framework.common.constants.ConfigPropertiesConstant;
import com.fuhouyu.framework.common.utils.LoggerUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.SystemUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.util.Objects;

/**
 * <p>
 * kms默认的配置
 * </p>
 *
 * @author fuhouyu
 * @since 2024/8/17 17:30
 */
@ConfigurationProperties(prefix = KmsDefaultProperties.PREFIX)
@ToString
@Getter
@Setter
@Slf4j
public class KmsDefaultProperties {

    /**
     * 国密算法配置
     */
    public static final String PREFIX = ConfigPropertiesConstant.PROPERTIES_PREFIX + "kms-national";

    /**
     * sm2 配置，优先字符串读取，当字符串不存在时，读取密钥文件，当都不存在时，进行生成
     */
    private Sm2Properties sm2 = new Sm2Properties();

    /**
     * sm4配置
     */
    private Sm3Properties sm3 = new Sm3Properties();

    /**
     * sm4配置
     */
    private Sm4Properties sm4 = new Sm4Properties();


    /**
     * sm2配置
     */
    @ToString
    @Getter
    @Setter
    public static class Sm2Properties {

        /**
         * 公钥key
         */
        private String publicKey;

        /**
         * 公钥key路径
         */
        private String publicKeyPath;

        /**
         * 私钥key
         */
        private String privateKey;

        /**
         * 私钥key 路径
         */
        private String privateKeyPath;


        /**
         * 生成一个sm2
         *
         * @return sm2
         */
        public SM2 generateSm2() {
            Path parentPath = Paths.get(SystemUtils.getUserHome().getAbsolutePath(), "sm2");
            LoggerUtil.info(log, "自动生成的sm2密钥的文件证书路径:{}", parentPath);
            Path publicPath = parentPath.resolve("publicKey");
            Path privatePath = parentPath.resolve("privateKey");
            if (Files.exists(publicPath) && Files.exists(privatePath)) {
                return SmUtil.sm2(PathUtil.readBytes(privatePath), PathUtil.readBytes(publicPath));
            }
            return SmUtil.sm2();
        }

        /**
         * 读取公钥
         *
         * @return 公钥字节
         */
        public byte[] getPublicKeyBytes() {
            if (Objects.nonNull(publicKeyPath)) {
                return FileUtil.readBytes(publicKeyPath);
            }
            if (Objects.nonNull(publicKey)) {
                return publicKey.getBytes(StandardCharsets.UTF_8);
            }
            return new byte[0];
        }

        /**
         * 读取私钥
         *
         * @return 私钥字节
         */
        public byte[] getPrivateKeyBytes() {
            if (Objects.nonNull(privateKeyPath)) {
                return FileUtil.readBytes(privateKeyPath);
            }
            if (Objects.nonNull(privateKey)) {
                return privateKey.getBytes(StandardCharsets.UTF_8);
            }
            return new byte[0];
        }
    }


    /**
     * sm3配置
     */
    @ToString
    @Getter
    @Setter
    public static class Sm3Properties {

        /**
         * 盐值
         */
        private String salt;

        /**
         * 盐值路径
         */
        private String saltFilePath;


        /**
         * 获取盐值
         *
         * @return 盐值字节数组
         */
        public byte[] getSaltBytes() {
            if (Objects.nonNull(saltFilePath)) {
                return FileUtil.readBytes(saltFilePath);
            }
            return new byte[0];
        }


    }


    /**
     * sm4配置
     */
    @ToString
    @Getter
    @Setter
    public static class Sm4Properties {

        /**
         * Sm4密钥字符串 128位
         */
        private String secretKey;

        /**
         * key 文件路径，优先取该值
         */
        private String keyFilePath;

        /**
         * 模式
         */
        private Mode mode;

        /**
         * 填充
         */
        private Padding padding;


        /**
         * 获取secretKey 字节
         *
         * @return 密钥字节数组
         */
        public byte[] getSecretKeyBytes() {
            if (Objects.isNull(keyFilePath)) {
                if (Objects.isNull(secretKey)) {
                    LoggerUtil.warn(log, "未设置secret Key，生成默认值");
                    return generateKey();
                }
                return secretKey.getBytes(StandardCharsets.UTF_8);
            }
            return FileUtil.readBytes(keyFilePath);
        }

        /**
         * 生成key
         *
         * @return secretKey
         */
        private byte[] generateKey() {
            SecureRandom secureRandom = new SecureRandom();
            byte[] bytes = new byte[16];
            secureRandom.nextBytes(bytes);
            return bytes;
        }
    }

}
