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