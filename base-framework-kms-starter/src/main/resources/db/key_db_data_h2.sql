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

-- 插入示例：对称密钥
MERGE INTO SYMMETRIC_KEY
USING (VALUES ('90EC4F0D567D4A83879678E437DD581A')) AS vals(key_id)
ON SYMMETRIC_KEY.key_id = vals.key_id
WHEN MATCHED THEN
    UPDATE
    SET key_name   = 'AES128-CBC-Key',
        secret     = X'25f30885b204032c0feff3c26d705591',
        mode       = 'CBC',
        padding    = 'PKCS5Padding',
        key_length = 128,
        updated_at = now(),
        updated_by = 'admin'
WHEN NOT MATCHED THEN
    INSERT (key_id, key_name, secret, mode, padding, key_length, created_at, updated_at, created_by, updated_by)
    VALUES ('90EC4F0D567D4A83879678E437DD581A',
            'AES128-CBC-Key',
            X'25f30885b204032c0feff3c26d705591',
            'CBC',
            'PKCS5Padding',
            128,
            now(),
            now(),
            'admin',
            'admin');

-- 非对称密钥
MERGE INTO asymmetric_key
USING (VALUES ('70784B1AB6D54DA599FC5D90E48CF929')) AS vals(key_id)
ON asymmetric_key.key_id = vals.key_id
WHEN MATCHED THEN
    UPDATE
    SET key_name    = 'RSA2048-Key',
        public_key  = X'3059301306072a8648ce3d020106082a811ccf5501822d0342000467b9830c4c50b216952275e897e4d50e61bfd196040e00e1981c953b4aea93b1b2836214ec7c248f67570d9976899d882bae2e751946bdc41d47fc6a91f2b197',
        private_key = X'308193020100301306072a8648ce3d020106082a811ccf5501822d0479307702010104206717586057f9b91c3c10c60f71a820909aa0bc6d50f8979fcc1c2114b4e47ae0a00a06082a811ccf5501822da1440342000467b9830c4c50b216952275e897e4d50e61bfd196040e00e1981c953b4aea93b1b2836214ec7c248f67570d9976899d882bae2e751946bdc41d47fc6a91f2b197',
        key_length  = 2048,
        updated_at  = now(),
        updated_by  = 'admin'
WHEN NOT MATCHED THEN
    INSERT (key_id, key_name, public_key, private_key, key_length, created_at, updated_at, created_by, updated_by)
    VALUES ('70784B1AB6D54DA599FC5D90E48CF929',
            'RSA2048-Key',
            X'3059301306072a8648ce3d020106082a811ccf5501822d0342000467b9830c4c50b216952275e897e4d50e61bfd196040e00e1981c953b4aea93b1b2836214ec7c248f67570d9976899d882bae2e751946bdc41d47fc6a91f2b197',
            X'308193020100301306072a8648ce3d020106082a811ccf5501822d0479307702010104206717586057f9b91c3c10c60f71a820909aa0bc6d50f8979fcc1c2114b4e47ae0a00a06082a811ccf5501822da1440342000467b9830c4c50b216952275e897e4d50e61bfd196040e00e1981c953b4aea93b1b2836214ec7c248f67570d9976899d882bae2e751946bdc41d47fc6a91f2b197',
            2048,
            now(),
            now(),
            'admin',
            'admin');

-- 摘要密钥
MERGE INTO digest_key
USING (VALUES ('E72BCFAA4A114A16BE2F2A3F0D77B1D5')) AS vals(key_id)
ON digest_key.key_id = vals.key_id
WHEN MATCHED THEN
    UPDATE
    SET key_name   = 'SM3-Salt',
        salt       = X'3c0a99d03c65197c3b4659fe400cb5c9',
        updated_at = now(),
        updated_by = 'admin'
WHEN NOT MATCHED THEN
    INSERT (key_id, key_name, salt, created_at, updated_at, created_by, updated_by)
    VALUES ('E72BCFAA4A114A16BE2F2A3F0D77B1D5',
            'SM3-Salt',
            X'3c0a99d03c65197c3b4659fe400cb5c9',
            now(),
            now(),
            'admin',
            'admin');
