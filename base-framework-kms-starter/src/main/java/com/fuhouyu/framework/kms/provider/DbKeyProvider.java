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
package com.fuhouyu.framework.kms.provider;

import com.fuhouyu.framework.kms.entity.AsymmetricKey;
import com.fuhouyu.framework.kms.entity.DigestKey;
import com.fuhouyu.framework.kms.entity.SymmetricKey;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * <p>
 * db密钥实现
 * </p>
 *
 * @author fuhouyu
 * @since 2025/7/26 21:29
 */
@RequiredArgsConstructor
public class DbKeyProvider implements KeyProvider {

    private final JdbcTemplate jdbcTemplate;

    private static final String SYMMETRIC_KEY_SQL = "SELECT * FROM symmetric_key WHERE key_id = ?";

    private static final String ASYMMETRIC_KEY_SQL = "SELECT * FROM asymmetric_key WHERE key_id = ?";

    private static final String DIGEST_KEY_SQL = "SELECT * FROM digest_key WHERE key_id = ?";


    @Override
    public SymmetricKey getSymmetricKey(String keyId) {
        return jdbcTemplate.queryForObject(SYMMETRIC_KEY_SQL,  new BeanPropertyRowMapper<>(SymmetricKey.class), keyId);
    }

    @Override
    public AsymmetricKey getAsymmetricKey(String keyId) {
        return jdbcTemplate.queryForObject(ASYMMETRIC_KEY_SQL,  new BeanPropertyRowMapper<>(AsymmetricKey.class), keyId);
    }

    @Override
    public DigestKey getDigestKey(String keyId) {
        return jdbcTemplate.queryForObject(DIGEST_KEY_SQL,  new BeanPropertyRowMapper<>(DigestKey.class), keyId);
    }

}
