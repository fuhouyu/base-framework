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

package com.fuhouyu.framework.web.filter;

import com.fuhouyu.framework.kms.service.KmsService;
import com.fuhouyu.framework.utils.HexUtil;
import com.fuhouyu.framework.utils.JacksonUtil;
import com.fuhouyu.framework.utils.LoggerUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

/**
 * <p>
 * 默认的http body 加解密过滤器
 * </p>
 *
 * @author fuhouyu
 * @since 2024/8/22 12:16
 */
public class DefaultHttpBodyFilter implements HttpBodyFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultHttpBodyFilter.class);

    private final KmsService kmsService;

    public DefaultHttpBodyFilter(KmsService kmsService) {
        this.kmsService = kmsService;
    }

    @Override
    public byte[] decryptionBody(byte[] encryptBodyBytes) {
        HttpBodyEncryptionModel httpBodyEncryptionModel = JacksonUtil.readValue(encryptBodyBytes, HttpBodyEncryptionModel.class);
        if (Objects.isNull(httpBodyEncryptionModel)) {
            LoggerUtil.error(LOGGER,
                    "需要解密转换后的body对象为空，直接返回");
            return encryptBodyBytes;
        }
        return kmsService.asymmetricDecrypt(HexUtil.decodeHex(httpBodyEncryptionModel.getBody()));
    }

    @Override
    public byte[] encryptionBody(byte[] originBody) {
        return kmsService.asymmetricEncrypt(originBody);
    }
}

/**
 * http body 加密的实体
 */
@ToString
@Getter
@Setter
class HttpBodyEncryptionModel {
    /**
     * 加密的body体
     */
    private String body;

}