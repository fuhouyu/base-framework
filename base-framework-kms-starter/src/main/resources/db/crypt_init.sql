CREATE TABLE  IF NOT EXISTS symmetric_key
(
    key_id     VARCHAR(100) NOT NULL PRIMARY KEY,
    key_name VARCHAR(100) NOT NULL,
    secret  BYTEA        NOT NULL,
    mode       VARCHAR(50),
    padding    VARCHAR(50),
    key_length INTEGER DEFAULT 128,

    created_at TIMESTAMP    NOT NULL,
    updated_at TIMESTAMP    NOT NULL,
    created_by VARCHAR(64)  NOT NULL,
    updated_by VARCHAR(64)  NOT NULL
);

COMMENT ON TABLE symmetric_key IS '对称加密密钥表';
COMMENT ON COLUMN symmetric_key.key_id IS '密钥唯一标识';
COMMENT ON COLUMN symmetric_key.key_name IS '密钥名称';
COMMENT ON COLUMN symmetric_key.secret IS '密钥';
COMMENT ON COLUMN symmetric_key.mode IS '加密工作模式，例如 CBC、GCM';
COMMENT ON COLUMN symmetric_key.padding IS '填充模式，例如 PKCS5Padding、NoPadding';
COMMENT ON COLUMN symmetric_key.key_length IS '密钥长度（单位：bit）';
COMMENT ON COLUMN symmetric_key.created_at IS '创建时间';
COMMENT ON COLUMN symmetric_key.created_by IS '创建人';
COMMENT ON COLUMN symmetric_key.updated_at IS '操作时间';
COMMENT ON COLUMN symmetric_key.updated_by IS '操作人';


CREATE TABLE  IF NOT EXISTS asymmetric_key
(
    key_id      VARCHAR(100) NOT NULL PRIMARY KEY,
    key_name VARCHAR(100) NOT NULL ,
    public_key  BYTEA        NOT NULL,
    private_key BYTEA NOT NULL,
    key_length  INTEGER,
    created_at  TIMESTAMP    NOT NULL,
    updated_at  TIMESTAMP    NOT NULL,
    created_by  VARCHAR(64)  NOT NULL,
    updated_by  VARCHAR(64)  NOT NULL
);

COMMENT ON TABLE asymmetric_key IS '非对称加密密钥表';
COMMENT ON COLUMN asymmetric_key.key_id IS '密钥唯一标识';
COMMENT ON COLUMN asymmetric_key.key_name IS '密钥名称';
COMMENT ON COLUMN asymmetric_key.public_key IS '公钥内容';
COMMENT ON COLUMN asymmetric_key.private_key IS '私钥内容（可为空）';
COMMENT ON COLUMN asymmetric_key.key_length IS '密钥长度（单位：bit）';
COMMENT ON COLUMN asymmetric_key.created_at IS '创建时间';
COMMENT ON COLUMN asymmetric_key.created_by IS '创建人';
COMMENT ON COLUMN asymmetric_key.updated_at IS '操作时间';
COMMENT ON COLUMN asymmetric_key.updated_by IS '操作人';

CREATE TABLE  IF NOT EXISTS digest_key
(
    key_id      VARCHAR(100) NOT NULL PRIMARY KEY,
    key_name VARCHAR(100) NOT NULL ,
    salt BYTEA NOT NULL ,
    created_at  TIMESTAMP    NOT NULL,
    updated_at  TIMESTAMP    NOT NULL,
    created_by  VARCHAR(64)  NOT NULL,
    updated_by  VARCHAR(64)  NOT NULL
);

COMMENT ON TABLE digest_key IS '摘要密钥表';
COMMENT ON COLUMN digest_key.key_id IS '密钥唯一标识';
COMMENT ON COLUMN digest_key.key_name IS '密钥名称';
COMMENT ON COLUMN digest_key.salt IS '盐值';
COMMENT ON COLUMN digest_key.created_at IS '创建时间';
COMMENT ON COLUMN digest_key.created_by IS '创建人';
COMMENT ON COLUMN digest_key.updated_at IS '操作时间';
COMMENT ON COLUMN digest_key.updated_by IS '操作人';


