CREATE TABLE tb_users (
    id BIGSERIAL PRIMARY KEY,
    full_name VARCHAR(255) NOT NULL ,
    document  VARCHAR(14) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL  UNIQUE,
    password VARCHAR(255) NOT NULL,
    user_type VARCHAR(20) NOT NULL
);

CREATE TABLE tb_wallets (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL UNIQUE,
    balance NUMERIC(19, 2) NOT NULL DEFAULT 0.00,
    CONSTRAINT fk_wallets_user FOREIGN KEY (user_id) REFERENCES tb_users(id),
    CONSTRAINT chk_balance_non_negative CHECK (balance >= 0)
);

CREATE TABLE tb_transactions (
    id BIGSERIAL PRIMARY KEY,
    payer_id BIGINT NOT NULL,
    payee_id BIGINT NOT NULL,
    value NUMERIC(19, 2) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_transactions_payer FOREIGN KEY (payer_id) REFERENCES tb_users(id),
    CONSTRAINT fk_transactions_payee FOREIGN KEY (payee_id) REFERENCES tb_users(id)
);