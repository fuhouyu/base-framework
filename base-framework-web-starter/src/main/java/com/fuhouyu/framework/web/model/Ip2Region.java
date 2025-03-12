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
package com.fuhouyu.framework.web.model;

import lombok.Data;

import java.util.Objects;

/**
 * <p>
 * ip2Region实体
 * </p>
 *
 * @author fuhouyu
 * @since 2025/3/12 21:38
 */
@Data
public class Ip2Region {

    /**
     * 国家
     */
    private String country;

    /**
     * 地区
     */
    private String region;

    /**
     * 省
     */
    private String province;

    /**
     * 市
     */
    private String city;

    /**
     * 运营商
     */
    private String isp;

    public Ip2Region(String location) {
        if (Objects.isNull(location)) {
            return;
        }
        String[] split = location.split("\\|");
        this.country = split[0];
        this.region = split[1];
        this.province = split[2];
        this.city = split[3];
        this.isp = split[4];
    }
}
