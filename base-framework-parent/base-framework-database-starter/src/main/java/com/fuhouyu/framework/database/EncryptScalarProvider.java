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
package com.fuhouyu.framework.database;

import com.fuhouyu.framework.common.utils.LoggerUtil;
import com.fuhouyu.framework.kms.service.KmsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.babyfish.jimmer.sql.runtime.ScalarProvider;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * <p>
 *
 * </p>
 *
 * @author fuhouyu
 * @since 2026/2/4 21:31
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class EncryptScalarProvider implements ScalarProvider<CipherText, String> {

    private final KmsService kmsService;

    @Override
    public CipherText toScalar(@NonNull String sqlValue) throws Exception {
        String result = sqlValue;
        try {
            result = new String(this.kmsService.symmetryDecrypt(Base64.getDecoder().decode(sqlValue)));
        } catch (Exception e) {
            LoggerUtil.error(log, "kms 解密失败,原始数据:{}, 错误原因:{}",
                    sqlValue, e.getMessage());
        }
        return new CipherText(result);
    }

    @Override
    public String toSql(@NonNull CipherText scalarValue) throws Exception {
        return Base64.getEncoder().encodeToString(this.kmsService.symmetryEncrypt(scalarValue.value().getBytes(StandardCharsets.UTF_8)));
    }
}
