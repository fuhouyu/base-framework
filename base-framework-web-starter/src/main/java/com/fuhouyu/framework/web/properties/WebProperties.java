/*
 * Copyright 2024-2034 the original author or authors.
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

import com.fuhouyu.framework.constants.ConfigPropertiesConstant;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * <p>
 * web配置
 * </p>
 *
 * @author fuhouyu
 * @since 2024/8/17 11:07
 */
@ConfigurationProperties(prefix = WebProperties.PREFIX)
public class WebProperties {

    public static final String PREFIX = ConfigPropertiesConstant.PROPERTIES_PREFIX + "web";

    /**
     * 用户类型
     */
    private String userClassName;

    public String getUserClassName() {
        return userClassName;
    }

    public void setUserClassName(String userClassName) {
        this.userClassName = userClassName;
    }

    @Override
    public String toString() {
        return "WebProperties{" +
                "userClassName='" + userClassName + '\'' +
                '}';
    }
}
