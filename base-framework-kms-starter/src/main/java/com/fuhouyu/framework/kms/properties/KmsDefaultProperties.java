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

import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import com.fuhouyu.framework.constants.ConfigPropertiesConstant;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
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
@ToString
@Getter
@Setter
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
    private Sm3Properties sm3;

    /**
     * sm4配置
     */
    private Sm4Properties sm4;


    /**
     * sm2配置
     */
    @ToString
    @Getter
    @Setter
    public static class Sm2Properties {

        /**
         * 未设置时，是否自动进行生成
         */
        private Boolean autoGenerate;

        /**
         * 自动生成后存储的路径
         */
        private String autoGenerateLocalPath = "/tmp/keypair";

        /**
         * 公钥key
         */
        private String publicKey;

        /**
         * 私钥key
         */
        private String privateKey;

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

    }


    /**
     * sm4配置
     */
    @ToString
    @Getter
    @Setter
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

    }
}
