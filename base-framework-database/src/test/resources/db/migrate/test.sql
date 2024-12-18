DROP TABLE IF EXISTS test_users;

CREATE TABLE test_users
(
    id              BIGINT PRIMARY KEY NOT NULL,
    username        VARCHAR(255)       NOT NULL,
    password        VARCHAR(255)       NOT NULL,
    owner_tenant_id BIGINT             NOT NULL,
    create_at       TIMESTAMP          NOT NULL,
    create_by       VARCHAR(255)       NOT NULL,
    update_at       TIMESTAMP          NOT NULL,
    update_by       VARCHAR(255)       NOT NULL
);
