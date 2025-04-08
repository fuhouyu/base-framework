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

import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import com.fuhouyu.framework.common.constants.ConfigPropertiesConstant;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * <p>
 * sm4配置
 * </p>
 *
 * @author fuhouyu
 * @since 2025/4/8 19:09
 */
@ToString
@Getter
@Setter
@ConfigurationProperties(prefix = SM4Properties.PREFIX)
public class SM4Properties {

    public static final String PREFIX = ConfigPropertiesConstant.PROPERTIES_PREFIX + "kms.sm4";

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
}
