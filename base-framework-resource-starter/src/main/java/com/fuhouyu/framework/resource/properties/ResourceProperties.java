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

import com.fuhouyu.framework.constants.ConfigPropertiesConstant;
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
    private AliYunOssProperties aliOssConfig;

}
