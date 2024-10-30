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

import com.fuhouyu.framework.common.constants.ConfigPropertiesConstant;
import com.fuhouyu.framework.resource.enums.ResourceUploadTypeEnum;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * <p>
 * 文件资源类型
 * </p>
 *
 * @author fuhouyu
 * @since 2024/8/16 18:19
 */
@ConfigurationProperties(prefix = ResourceProperties.PREFIX)
@ToString
@Getter
@Setter
public class ResourceProperties {

    /**
     * 资源配置的前缀
     */
    public static final String PREFIX = ConfigPropertiesConstant.PROPERTIES_PREFIX + "resource";

    /**
     * 文件上传的类型
     */
    private ResourceUploadTypeEnum uploadType;

    /**
     * 文件资源配置基类
     */
    private AliYunOssProperties aliyunOss;

    /**
     * 本地配置
     */
    private LocalResourceProperties localResource;

    @Getter
    @Setter
    @ToString
    public static class LocalResourceProperties {
        /**
         * 根路径
         */
        private String basePath;
    }

    @Getter
    @Setter
    @ToString
    public static class CloudResourceProperties {
        /**
         * endpoint
         */
        private String endpoint;

        /**
         * ak
         */
        private String accessKey;

        /**
         * sk
         */
        private String secretKey;
    }


    @Getter
    @Setter
    @ToString(callSuper = true)
    public static class AliYunOssProperties extends CloudResourceProperties {
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
}
