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

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * <p>
 * sts配置
 * </p>
 *
 * @author fuhouyu
 * @since 2025/2/22 19:00
 */
@ConfigurationProperties(prefix = StsProperties.PREFIX)
@Data
public class StsProperties {

    public static final String PREFIX = S3Properties.PREFIX + "s3.sts";

    /**
     * 角色ARN
     */
    private String roleArn;

    /**
     * sts token 过期时间，默认3600
     */
    private Integer durationSeconds;

    /**
     * 策略操作前缀
     */
    private String policyActionPrefix;

    /**
     * 策略资源前缀
     */
    private String policyResourcePrefix;

    public StsProperties() {
        this.policyActionPrefix = "s3:";
        this.policyResourcePrefix = "arn:aws:s3:::";
    }
}
