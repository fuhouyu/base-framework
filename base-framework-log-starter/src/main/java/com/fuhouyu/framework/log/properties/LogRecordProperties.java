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

package com.fuhouyu.framework.log.properties;

import com.fuhouyu.framework.constants.ConfigPropertiesConstant;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * <p>
 * 日志配置
 * </p>
 *
 * @author fuhouyu
 * @since 2024/8/14 12:17
 */
@ConfigurationProperties(prefix = LogRecordProperties.LOG_RECORD_PREFIX)
@ToString
@Getter
@Setter
public class LogRecordProperties {

    public static final String LOG_RECORD_PREFIX = ConfigPropertiesConstant.PROPERTIES_PREFIX + "log-record";

    /**
     * 是否启用日志记录
     */
    private Boolean enabled;

    /**
     * 日志记录的当前系统名称
     */
    private String systemName;

}
