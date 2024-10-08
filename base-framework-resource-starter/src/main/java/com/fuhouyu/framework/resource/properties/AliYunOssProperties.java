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

package com.fuhouyu.framework.resource.properties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <p>
 * 阿里云配置项
 * </p>
 *
 * @author fuhouyu
 * @since 2024/8/16 18:33
 */
@ToString
@Getter
@Setter
public class AliYunOssProperties extends BaseOssResourceProperties {

    /**
     * 区域.
     */
    private String region;

    /**
     * 是否启用sts，默认为false
     */
    private boolean enableSts;

    /**
     * sts相关配置，获取webToken必须要有该值.
     */
    private StsConfig sts;

    /**
     * stsConfig.
     */
    @ToString
    @Getter
    @Setter
    public static class StsConfig {

        private String endpoint;

        private String roleArn;

        private Integer expire;

    }


}
