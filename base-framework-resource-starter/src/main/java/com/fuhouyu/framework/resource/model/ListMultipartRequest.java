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

package com.fuhouyu.framework.resource.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <p>
 *
 * </p>
 *
 * @author fuhouyu
 * @since 2024/8/16 17:53
 */
@ToString
@Getter
@Setter
public class ListMultipartRequest extends BaseResourceRequest {

    /**
     * 指定List的起始位置。只有分片号大于此参数值的分片会被列举.
     */
    private int partNumberMaker;

    /**
     * 最大列举的分片数，默认1000.
     */
    private Integer maxParts = 1000;

    /**
     * 上传的id.
     */
    private String uploadId;
}
