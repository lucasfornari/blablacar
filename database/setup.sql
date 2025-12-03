DROP TABLE IF EXISTS passageiros_viagens CASCADE;
DROP TABLE IF EXISTS viagens CASCADE;
DROP TABLE IF EXISTS veiculos CASCADE;
DROP TABLE IF EXISTS passageiros CASCADE;
DROP TABLE IF EXISTS motoristas CASCADE;
DROP TABLE IF EXISTS usuarios CASCADE;

CREATE TABLE usuarios (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL,
    telefone VARCHAR(20),
    endereco TEXT,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE motoristas (
    id SERIAL PRIMARY KEY,
    usuario_id INTEGER NOT NULL UNIQUE,
    cnh VARCHAR(20) NOT NULL UNIQUE,
    numero_viagens INTEGER DEFAULT 0,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE
);

CREATE TABLE passageiros (
    id SERIAL PRIMARY KEY,
    usuario_id INTEGER NOT NULL UNIQUE,
    numero_viagens INTEGER DEFAULT 0,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE
);

CREATE TABLE veiculos (
    id SERIAL PRIMARY KEY,
    motorista_id INTEGER NOT NULL,
    marca VARCHAR(100) NOT NULL,
    modelo VARCHAR(100) NOT NULL,
    placa VARCHAR(10) NOT NULL UNIQUE,
    ano INTEGER NOT NULL,
    cor VARCHAR(50),
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (motorista_id) REFERENCES motoristas(id) ON DELETE CASCADE
);

CREATE TABLE viagens (
    id SERIAL PRIMARY KEY,
    veiculo_id INTEGER NOT NULL,
    motorista_id INTEGER NOT NULL,
    cidade_origem VARCHAR(255) NOT NULL,
    cidade_destino VARCHAR(255) NOT NULL,
    preco DECIMAL(10, 2) NOT NULL,
    data_viagem VARCHAR(20) NOT NULL,
    vagas INTEGER NOT NULL,
    status VARCHAR(20) DEFAULT 'PENDENTE',
    avaliacao DECIMAL(3, 2) DEFAULT 0,
    soma_notas DECIMAL(10, 2) DEFAULT 0,
    total_avaliacoes INTEGER DEFAULT 0,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (veiculo_id) REFERENCES veiculos(id) ON DELETE CASCADE,
    FOREIGN KEY (motorista_id) REFERENCES motoristas(id) ON DELETE CASCADE
);

CREATE TABLE passageiros_viagens (
    id SERIAL PRIMARY KEY,
    viagem_id INTEGER NOT NULL,
    passageiro_id INTEGER NOT NULL,
    numero_lugares INTEGER NOT NULL,
    status VARCHAR(20) DEFAULT 'RESERVADO',
    data_reserva TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (viagem_id) REFERENCES viagens(id) ON DELETE CASCADE,
    FOREIGN KEY (passageiro_id) REFERENCES passageiros(id) ON DELETE CASCADE
);

CREATE INDEX idx_usuarios_email ON usuarios(email);
CREATE INDEX idx_motoristas_usuario ON motoristas(usuario_id);
CREATE INDEX idx_passageiros_usuario ON passageiros(usuario_id);
CREATE INDEX idx_veiculos_motorista ON veiculos(motorista_id);
CREATE INDEX idx_viagens_motorista ON viagens(motorista_id);
CREATE INDEX idx_viagens_origem_destino ON viagens(cidade_origem, cidade_destino);
CREATE INDEX idx_passageiros_viagens_viagem ON passageiros_viagens(viagem_id);
CREATE INDEX idx_passageiros_viagens_passageiro ON passageiros_viagens(passageiro_id);
