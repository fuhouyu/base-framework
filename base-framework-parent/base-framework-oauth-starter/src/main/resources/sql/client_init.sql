DROP TABLE IF EXISTS oauth2_registered_client;
CREATE TABLE oauth2_registered_client
(
    id                            varchar(100)                            NOT NULL,
    client_id                     varchar(100)                            NOT NULL,
    client_id_issued_at           timestamptz   DEFAULT CURRENT_TIMESTAMP NOT NULL,
    client_secret                 varchar(200)  DEFAULT NULL,
    client_secret_expires_at      timestamptz   DEFAULT NULL,
    client_name                   varchar(200)                            NOT NULL,
    client_authentication_methods varchar(1000)                           NOT NULL,
    authorization_grant_types     varchar(1000)                           NOT NULL,
    redirect_uris                 varchar(1000) DEFAULT NULL,
    post_logout_redirect_uris     varchar(1000) DEFAULT NULL,
    scopes                        varchar(1000)                           NOT NULL,
    client_settings               varchar(2000)                           NOT NULL,
    token_settings                varchar(2000)                           NOT NULL,
    PRIMARY KEY (id)
);

COMMENT ON TABLE oauth2_registered_client IS 'OAuth2 客户端注册信息表';
COMMENT ON COLUMN oauth2_registered_client.id IS '记录唯一标识（通常为 UUID）';
COMMENT ON COLUMN oauth2_registered_client.client_id IS '客户端标识 ID';
COMMENT ON COLUMN oauth2_registered_client.client_id_issued_at IS '客户端创建/签发时间';
COMMENT ON COLUMN oauth2_registered_client.client_secret IS '客户端密钥（加密存储）';
COMMENT ON COLUMN oauth2_registered_client.client_secret_expires_at IS '密钥过期时间';
COMMENT ON COLUMN oauth2_registered_client.client_name IS '显示名称';
COMMENT ON COLUMN oauth2_registered_client.client_authentication_methods IS '客户端认证方式（如 client_secret_basic, none）';
COMMENT ON COLUMN oauth2_registered_client.authorization_grant_types IS '支持的授权模式（如 authorization_code, refresh_token）';
COMMENT ON COLUMN oauth2_registered_client.redirect_uris IS '允许的回调重定向地址（逗号分隔）';
COMMENT ON COLUMN oauth2_registered_client.post_logout_redirect_uris IS '登出后的重定向地址';
COMMENT ON COLUMN oauth2_registered_client.scopes IS '授权范围（如 openid, profile）';
COMMENT ON COLUMN oauth2_registered_client.client_settings IS '客户端设置（JSON 格式，包含 PKCE 强制校验等）';
COMMENT ON COLUMN oauth2_registered_client.token_settings IS '令牌设置（JSON 格式，包含 Token 有效期等）';