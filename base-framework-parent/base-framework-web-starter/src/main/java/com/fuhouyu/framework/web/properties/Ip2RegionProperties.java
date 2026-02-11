/*
 * Copyright 2024-present fuhouyu.
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
package com.fuhouyu.framework.web.properties;

import com.fuhouyu.framework.common.constants.ConfigPropertiesConstant;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * <p>
 * ip2region 配置文件
 * </p>
 *
 * @author fuhouyu
 * @since 2025/3/11 22:13
 */
@ConfigurationProperties(Ip2RegionProperties.PREFIX)
@Data
public class Ip2RegionProperties {

    public static final String PREFIX = ConfigPropertiesConstant.PROPERTIES_PREFIX + "ip2region";

    private Boolean enabled;

    private String dbPath;

}
