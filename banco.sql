-- GameLocadora | PostgreSQL
-- Execute este arquivo em um banco vazio chamado gamelocadora.
-- Todos os atributos seguem o padrão: nome_tabela_nome_atributo.

CREATE TABLE cliente (
    cliente_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    cliente_nome VARCHAR(120) NOT NULL,
    cliente_email VARCHAR(160) NOT NULL UNIQUE,
    cliente_telefone VARCHAR(20),
    cliente_ativo BOOLEAN NOT NULL DEFAULT TRUE,
    cliente_data_cadastro TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT ck_cliente_nome CHECK (LENGTH(TRIM(cliente_nome)) >= 3)
);

CREATE TABLE jogo (
    jogo_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    jogo_titulo VARCHAR(150) NOT NULL,
    jogo_plataforma VARCHAR(60) NOT NULL,
    jogo_genero VARCHAR(60) NOT NULL,
    jogo_ano_lancamento INTEGER,
    jogo_formato VARCHAR(20) NOT NULL,
    jogo_valor_diaria NUMERIC(10,2) NOT NULL,
    jogo_ativo BOOLEAN NOT NULL DEFAULT TRUE,
    CONSTRAINT ck_jogo_formato CHECK (jogo_formato IN ('DISCO', 'CARTUCHO')),
    CONSTRAINT ck_jogo_valor CHECK (jogo_valor_diaria > 0),
    CONSTRAINT ck_jogo_ano CHECK (jogo_ano_lancamento IS NULL OR jogo_ano_lancamento BETWEEN 1970 AND 2100)
);

CREATE TABLE exemplar (
    exemplar_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    exemplar_codigo VARCHAR(40) NOT NULL UNIQUE,
    exemplar_jogo_id BIGINT NOT NULL,
    exemplar_estado VARCHAR(20) NOT NULL DEFAULT 'BOM',
    exemplar_ativo BOOLEAN NOT NULL DEFAULT TRUE,
    CONSTRAINT fk_exemplar_jogo FOREIGN KEY (exemplar_jogo_id) REFERENCES jogo(jogo_id),
    CONSTRAINT ck_exemplar_estado CHECK (exemplar_estado IN ('NOVO', 'BOM', 'DESGASTADO'))
);

CREATE TABLE aluguel (
    aluguel_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    aluguel_cliente_id BIGINT NOT NULL,
    aluguel_exemplar_id BIGINT NOT NULL,
    aluguel_data_retirada DATE NOT NULL,
    aluguel_data_prevista_devolucao DATE NOT NULL,
    aluguel_data_devolucao_real DATE,
    aluguel_valor_total NUMERIC(10,2) NOT NULL,
    aluguel_percentual_multa_dia NUMERIC(5,4) NOT NULL DEFAULT 0.0200,
    aluguel_valor_multa NUMERIC(10,2) NOT NULL DEFAULT 0,
    CONSTRAINT fk_aluguel_cliente FOREIGN KEY (aluguel_cliente_id) REFERENCES cliente(cliente_id),
    CONSTRAINT fk_aluguel_exemplar FOREIGN KEY (aluguel_exemplar_id) REFERENCES exemplar(exemplar_id),
    CONSTRAINT ck_aluguel_periodo CHECK (aluguel_data_prevista_devolucao = aluguel_data_retirada + 7),
    CONSTRAINT ck_aluguel_valores CHECK (
        aluguel_valor_total > 0 AND
        aluguel_percentual_multa_dia = 0.0200 AND
        aluguel_valor_multa >= 0
    ),
    CONSTRAINT ck_aluguel_devolucao CHECK (
        aluguel_data_devolucao_real IS NULL OR
        aluguel_data_devolucao_real >= aluguel_data_retirada
    )
);

-- Impede dois aluguéis em aberto para a mesma cópia física.
-- Cópias diferentes do mesmo jogo podem ser alugadas ao mesmo tempo.
CREATE UNIQUE INDEX uq_aluguel_exemplar_em_aberto
    ON aluguel (aluguel_exemplar_id)
    WHERE aluguel_data_devolucao_real IS NULL;

CREATE INDEX idx_aluguel_cliente ON aluguel(aluguel_cliente_id);
CREATE INDEX idx_exemplar_jogo ON exemplar(exemplar_jogo_id);

-- Dados iniciais para teste da aplicação.
INSERT INTO cliente (cliente_nome, cliente_email, cliente_telefone)
VALUES
    ('Ana Souza', 'ana@email.com', '(61) 99999-1111'),
    ('Bruno Lima', 'bruno@email.com', '(61) 99999-2222');

INSERT INTO jogo (jogo_titulo, jogo_plataforma, jogo_genero, jogo_ano_lancamento, jogo_formato, jogo_valor_diaria)
VALUES
    ('The Last of Us Part II', 'PlayStation 4', 'Ação/Aventura', 2020, 'DISCO', 8.00),
    ('Mario Kart 8 Deluxe', 'Nintendo Switch', 'Corrida', 2017, 'CARTUCHO', 7.00);

INSERT INTO exemplar (exemplar_codigo, exemplar_jogo_id, exemplar_estado)
SELECT 'PS4-TLOU-001', jogo_id, 'BOM' FROM jogo WHERE jogo_titulo = 'The Last of Us Part II';

INSERT INTO exemplar (exemplar_codigo, exemplar_jogo_id, exemplar_estado)
SELECT 'PS4-TLOU-002', jogo_id, 'NOVO' FROM jogo WHERE jogo_titulo = 'The Last of Us Part II';

INSERT INTO exemplar (exemplar_codigo, exemplar_jogo_id, exemplar_estado)
SELECT 'SW-MK8-001', jogo_id, 'BOM' FROM jogo WHERE jogo_titulo = 'Mario Kart 8 Deluxe';
