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

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * ip2Region实体
 * </p>
 *
 * @author fuhouyu
 * @since 2025/3/12 21:38
 */
public class Ip2Region {

    private static final String DEFAULT_LOCATION_STR = "0";

    /**
     * location
     * country | region | province | city | isp
     */
    @Getter
    private final String location;

    /**
     * locations
     */
    private final String[] locations;


    public Ip2Region(String location) {
        this.location = location;
        this.locations = location.split("\\|");
    }

    public Ip2Region() {
        this.location = null;
        this.locations = null;
    }

    @Override
    public String toString() {
        return location;
    }

    public String getCountry() {
        if (this.location == null) {
            return null;
        }
        return this.locations[0];
    }

    public String getRegion() {
        if (this.location == null) {
            return null;
        }
        return this.locations[1];
    }

    public String getProvince() {
        if (this.location == null) {
            return null;
        }
        return this.locations[2];
    }

    public String getCity() {
        if (this.location == null) {
            return null;
        }
        return this.locations[3];
    }

    public String getIsp() {
        if (this.location == null) {
            return null;
        }
        return this.locations[4];
    }

    /**
     * 将非空的字段拼接成字符串
     *
     * @return 拼接后的字符串
     */
    public String toNotNullString() {
        return this.toNotNullString("/");
    }

    /**
     * 将非空的字段拼接成字符串
     *
     * @param delimiter 分隔符
     * @return 拼接后的字符串
     */
    public String toNotNullString(String delimiter) {
        List<String> parts = new ArrayList<>();
        if (!Objects.equals(this.getCountry(), DEFAULT_LOCATION_STR)) {
            parts.add(this.getCountry());
        }
        if (!Objects.equals(this.getRegion(), DEFAULT_LOCATION_STR)) {
            parts.add(this.getRegion());
        }
        if (!Objects.equals(this.getProvince(), DEFAULT_LOCATION_STR)) {
            parts.add(this.getProvince());
        }
        if (!Objects.equals(this.getCity(), DEFAULT_LOCATION_STR)) {
            parts.add(this.getCity());
        }
        if (!Objects.equals(this.getIsp(), DEFAULT_LOCATION_STR)) {
            parts.add(this.getIsp());
        }
        return String.join(delimiter, parts);
    }

}
