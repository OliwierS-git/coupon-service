CREATE SCHEMA IF NOT EXISTS coupons;

CREATE TABLE coupons.coupon (
                                id              UUID PRIMARY KEY,
                                code            VARCHAR(64) NOT NULL,
                                country         VARCHAR(2)  NOT NULL,
                                max_usages      INTEGER     NOT NULL,
                                current_usages  INTEGER     NOT NULL DEFAULT 0,
                                created_at      TIMESTAMP   NOT NULL,
                                version         BIGINT      NOT NULL DEFAULT 0,
                                CONSTRAINT uk_coupon_code UNIQUE (code)
);

CREATE TABLE coupons.coupon_usage (
                                      id                UUID PRIMARY KEY,
                                      coupon_id         UUID        NOT NULL REFERENCES coupons.coupon(id),
                                      user_id           UUID        NOT NULL,
                                      used_at           TIMESTAMP   NOT NULL,
                                      country_at_usage  VARCHAR(2)  NOT NULL,
                                      CONSTRAINT uk_coupon_user UNIQUE (coupon_id, user_id)
);