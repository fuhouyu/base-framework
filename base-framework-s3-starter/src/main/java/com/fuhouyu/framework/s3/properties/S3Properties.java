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
package com.fuhouyu.framework.s3.properties;

import com.fuhouyu.framework.common.constants.ConfigPropertiesConstant;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import software.amazon.awssdk.regions.Region;

/**
 * <p>
 * s3协议属性配置
 * </p>
 *
 * @author fuhouyu
 * @since 2025/2/9 12:37
 */
@ConfigurationProperties(prefix = S3Properties.PREFIX)
@ToString
@Getter
@Setter
public class S3Properties {

    /**
     * 资源配置的前缀
     */
    public static final String PREFIX = ConfigPropertiesConstant.PROPERTIES_PREFIX + "s3";

    /**
     * endpoint
     */
    private String endpoint;

    /**
     * ak
     */
    private String accessKeyId;

    /**
     * sk
     */
    private String secretKey;

    /**
     * 使用路径风格
     * 如： https://oss.s3.com?bucketName=xxx
     */
    private boolean pathStyleEnabled;

    /**
     * 分块编码启禁用
     */
    private boolean chunkedEncodingEnabled;

    /**
     * stsToken
     */
    private String stsToken;


    /**
     * 区域
     */
    private Region region;

    public S3Properties() {
        this.pathStyleEnabled = false;
        this.chunkedEncodingEnabled = false;
        this.region = Region.AWS_GLOBAL;
    }
}
