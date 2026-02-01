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
package com.fuhouyu.framework.s3.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

import java.util.Collection;

/**
 * <p>
 * sts policy
 * </p>
 *
 * @author fuhouyu
 * @since 2025/3/18 21:38
 */
@Builder
@Getter
public class StsPolicy {

    @JsonProperty("Version")
    private final String version;

    @JsonProperty("Statement")
    private final Collection<StsStatement> statements;
}

