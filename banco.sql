
CREATE TABLE IF NOT EXISTS cliente (
    cliente_id SERIAL PRIMARY KEY,
    cliente_nome VARCHAR(120) NOT NULL,
    cliente_email VARCHAR(160) NOT NULL,
    cliente_telefone VARCHAR(20),
    cliente_ativo BOOLEAN NOT NULL DEFAULT TRUE,
    cliente_data_cadastro TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT uq_cliente_email
        UNIQUE (cliente_email),

    CONSTRAINT ck_cliente_nome
        CHECK (LENGTH(BTRIM(cliente_nome)) >= 3),

    CONSTRAINT ck_cliente_email
        CHECK (POSITION('@' IN cliente_email) > 1)
);

CREATE UNIQUE INDEX IF NOT EXISTS uq_cliente_email_lower
    ON cliente (LOWER(cliente_email));


CREATE TABLE IF NOT EXISTS jogo (
    jogo_id SERIAL PRIMARY KEY,
    jogo_titulo VARCHAR(150) NOT NULL,
    jogo_plataforma VARCHAR(60) NOT NULL,
    jogo_genero VARCHAR(60) NOT NULL,
    jogo_ano_lancamento INTEGER,
    jogo_formato VARCHAR(20) NOT NULL,
    jogo_valor_diaria NUMERIC(10, 2) NOT NULL,
    jogo_ativo BOOLEAN NOT NULL DEFAULT TRUE,

    CONSTRAINT ck_jogo_titulo
        CHECK (LENGTH(BTRIM(jogo_titulo)) > 0),

    CONSTRAINT ck_jogo_formato
        CHECK (jogo_formato IN ('DISCO', 'CARTUCHO')),

    CONSTRAINT ck_jogo_valor
        CHECK (jogo_valor_diaria > 0),

    CONSTRAINT ck_jogo_ano
        CHECK (
            jogo_ano_lancamento IS NULL
            OR jogo_ano_lancamento BETWEEN 1970 AND 2100
        )
);


CREATE TABLE IF NOT EXISTS exemplar (
    exemplar_id SERIAL PRIMARY KEY,
    exemplar_codigo VARCHAR(40) NOT NULL UNIQUE,
    exemplar_jogo_id INTEGER NOT NULL
        REFERENCES jogo(jogo_id),
    exemplar_estado VARCHAR(20) NOT NULL DEFAULT 'BOM',
    exemplar_observacao VARCHAR(255),
    exemplar_ativo BOOLEAN NOT NULL DEFAULT TRUE,

    CONSTRAINT ck_exemplar_estado
        CHECK (
            exemplar_estado IN (
                'NOVO',
                'BOM',
                'DESGASTADO'
            )
        )
);


CREATE TABLE IF NOT EXISTS aluguel (
    aluguel_id SERIAL PRIMARY KEY,
    aluguel_cliente_id INTEGER NOT NULL
        REFERENCES cliente(cliente_id),
    aluguel_exemplar_id INTEGER NOT NULL
        REFERENCES exemplar(exemplar_id),
    aluguel_data_retirada DATE NOT NULL,
    aluguel_data_prevista_devolucao DATE NOT NULL,
    aluguel_data_devolucao_real DATE,
    aluguel_valor_total NUMERIC(10, 2) NOT NULL,
    aluguel_percentual_multa_dia NUMERIC(5, 4)
        NOT NULL DEFAULT 0.0200,
    aluguel_valor_multa NUMERIC(10, 2)
        NOT NULL DEFAULT 0,
    aluguel_observacao VARCHAR(255),

    CONSTRAINT ck_aluguel_prazo
        CHECK (
            aluguel_data_prevista_devolucao =
            aluguel_data_retirada + 7
        ),

    CONSTRAINT ck_aluguel_valores
        CHECK (
            aluguel_valor_total > 0
            AND aluguel_percentual_multa_dia = 0.0200
            AND aluguel_valor_multa >= 0
        ),

    CONSTRAINT ck_aluguel_devolucao
        CHECK (
            aluguel_data_devolucao_real IS NULL
            OR aluguel_data_devolucao_real >=
               aluguel_data_retirada
        )
);


CREATE UNIQUE INDEX IF NOT EXISTS uq_aluguel_exemplar_aberto
    ON aluguel (aluguel_exemplar_id)
    WHERE aluguel_data_devolucao_real IS NULL;


CREATE INDEX IF NOT EXISTS idx_aluguel_cliente
    ON aluguel (aluguel_cliente_id);


CREATE INDEX IF NOT EXISTS idx_exemplar_jogo
    ON exemplar (exemplar_jogo_id);
