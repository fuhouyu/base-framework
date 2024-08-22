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

package com.fuhouyu.framework.kms.properties;

import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import com.fuhouyu.framework.constants.ConfigPropertiesConstant;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * <p>
 * kms默认的配置
 * </p>
 *
 * @author fuhouyu
 * @since 2024/8/17 17:30
 */
@ConfigurationProperties(prefix = KmsDefaultProperties.PREFIX)
public class KmsDefaultProperties {

    public static final String PREFIX = ConfigPropertiesConstant.PROPERTIES_PREFIX + "kms.default";

    /**
     * sm2 配置，优先字符串读取，当字符串不存在时，读取密钥文件，当都不存在时，进行生成
     */
    private Sm2Properties sm2 = new Sm2Properties();

    /**
     * sm4配置
     */
    private Sm3Properties sm3;

    /**
     * sm4配置
     */
    private Sm4Properties sm4;

    public Sm2Properties getSm2() {
        return sm2;
    }

    public void setSm2(Sm2Properties sm2) {
        this.sm2 = sm2;
    }

    public Sm3Properties getSm3() {
        return sm3;
    }

    public void setSm3(Sm3Properties sm3) {
        this.sm3 = sm3;
    }

    public Sm4Properties getSm4() {
        return sm4;
    }

    public void setSm4(Sm4Properties sm4) {
        this.sm4 = sm4;
    }

    @Override
    public String toString() {
        return "KmsDefaultProperties{" +
                "sm2=" + sm2 +
                ", sm3=" + sm3 +
                ", sm4=" + sm4 +
                '}';
    }

    /**
     * sm2配置
     */
    public static class Sm2Properties {

        /**
         * 未设置时，是否自动进行生成
         */
        private boolean autoGenerate;

        /**
         * 自动生成后存储的路径
         */
        private String autoGenerateLocalPath;

        /**
         * 公钥key
         */
        private String publicKey;

        /**
         * 私钥key
         */
        private String privateKey;

        public String getPublicKey() {
            return publicKey;
        }

        public void setPublicKey(String publicKey) {
            this.publicKey = publicKey;
        }

        public String getPrivateKey() {
            return privateKey;
        }

        public void setPrivateKey(String privateKey) {
            this.privateKey = privateKey;
        }

        public boolean getAutoGenerate() {
            return autoGenerate;
        }

        public void setAutoGenerate(boolean autoGenerate) {
            this.autoGenerate = autoGenerate;
        }

        public String getAutoGenerateLocalPath() {
            return autoGenerateLocalPath;
        }

        public void setAutoGenerateLocalPath(String autoGenerateLocalPath) {
            this.autoGenerateLocalPath = autoGenerateLocalPath;
        }

        @Override
        public String toString() {
            return "Sm2Properties{" +
                    "isAutoGenerate=" + autoGenerate +
                    ", autoGenerateLocalPath='" + autoGenerateLocalPath + '\'' +
                    ", publicKey='" + publicKey + '\'' +
                    ", privateKey='" + privateKey + '\'' +
                    '}';
        }
    }


    /**
     * sm3配置
     */
    public static class Sm3Properties {

        /**
         * 盐值
         */
        private String salt;

        public String getSalt() {
            return salt;
        }

        public void setSalt(String salt) {
            this.salt = salt;
        }


        @Override
        public String toString() {
            return "Sm3Properties{" +
                    "salt='" + salt + '\'' +
                    '}';
        }
    }


    /**
     * sm4配置
     */
    public static class Sm4Properties {

        /**
         * Sm4密钥，应用为16字符， 128位
         */
        private String secretKey;

        /**
         * 模式
         */
        private Mode mode;

        /**
         * 填充
         */
        private Padding padding;

        public String getSecretKey() {
            return secretKey;
        }

        public void setSecretKey(String secretKey) {
            this.secretKey = secretKey;
        }

        public Mode getMode() {
            return mode;
        }

        public void setMode(Mode mode) {
            this.mode = mode;
        }

        public Padding getPadding() {
            return padding;
        }

        public void setPadding(Padding padding) {
            this.padding = padding;
        }

        @Override
        public String toString() {
            return "Sm4Properties{" +
                    "secretKey='" + secretKey + '\'' +
                    ", mode=" + mode +
                    ", padding=" + padding +
                    '}';
        }
    }
}
