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
CREATE TABLE IF NOT EXISTS SYMMETRIC_KEY
(
    key_id     VARCHAR(100)   NOT NULL PRIMARY KEY,
    key_name   VARCHAR(100)   NOT NULL,
    secret     VARBINARY(256) NOT NULL,
    mode       VARCHAR(50),
    padding    VARCHAR(50),
    key_length INTEGER DEFAULT 128,
    created_at TIMESTAMP      NOT NULL,
    updated_at TIMESTAMP      NOT NULL,
    created_by VARCHAR(64)    NOT NULL,
    updated_by VARCHAR(64)    NOT NULL
);

CREATE TABLE IF NOT EXISTS ASYMMETRIC_KEY
(
    key_id      VARCHAR(100)    NOT NULL PRIMARY KEY,
    key_name    VARCHAR(100)    NOT NULL,
    public_key  VARBINARY(2048) NOT NULL,
    private_key VARBINARY(2048) NOT NULL,
    key_length  INTEGER,
    created_at  TIMESTAMP       NOT NULL,
    updated_at  TIMESTAMP       NOT NULL,
    created_by  VARCHAR(64)     NOT NULL,
    updated_by  VARCHAR(64)     NOT NULL
);

CREATE TABLE IF NOT EXISTS DIGEST_KEY
(
    key_id     VARCHAR(100)   NOT NULL PRIMARY KEY,
    key_name   VARCHAR(100)   NOT NULL,
    salt       VARBINARY(256) NOT NULL,
    created_at TIMESTAMP      NOT NULL,
    updated_at TIMESTAMP      NOT NULL,
    created_by VARCHAR(64)    NOT NULL,
    updated_by VARCHAR(64)    NOT NULL
);