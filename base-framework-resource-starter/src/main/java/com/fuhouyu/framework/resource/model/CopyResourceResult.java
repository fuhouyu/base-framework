/*
 * Copyright 2012-2020 the original author or authors.
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

import java.util.Date;

/**
 * <p>
 * 文件资源复制实体
 * </p>
 *
 * @author fuhouyu
 * @since 2024/8/16 18:13
 */
public class CopyResourceResult extends BaseResourceResult {

    private String etag;

    private Date lastModified;

    public CopyResourceResult(String bucketName, String objectKey) {
        super(bucketName, objectKey);
    }

    public String getEtag() {
        return etag;
    }

    public void setEtag(String etag) {
        this.etag = etag;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    @Override
    public String toString() {
        return "CopyFileResourceResult{" +
                "etag='" + etag + '\'' +
                ", lastModified=" + lastModified +
                '}';
    }
}
