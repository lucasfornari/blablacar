package infrastructure;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class BancoDeDados {

    private static final String URL = "jdbc:postgresql://localhost:5432/blablacar";
    private static final String USUARIO = "postgres";
    private static final String SENHA = "123123";

    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Driver PostgreSQL nao encontrado!");
        }
    }

    public static Connection conectar() {
        try {
            return DriverManager.getConnection(URL, USUARIO, SENHA);
        } catch (SQLException e) {
            System.out.println("Erro ao conectar: " + e.getMessage());
            return null;
        }
    }

    public static boolean testar() {
        Connection conn = null;
        try {
            conn = conectar();
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            return false;
        } finally {
            fechar(conn);
        }
    }

    public static void fechar(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
            }
        }
    }

    public static void criarTabelas() {
        String[] sqls = {
            """
            CREATE TABLE IF NOT EXISTS usuarios (
                id SERIAL PRIMARY KEY,
                nome VARCHAR(100) NOT NULL,
                email VARCHAR(100) UNIQUE NOT NULL,
                senha VARCHAR(100) NOT NULL,
                telefone VARCHAR(20),
                endereco VARCHAR(200)
            )
            """,
            """
            CREATE TABLE IF NOT EXISTS motoristas (
                id SERIAL PRIMARY KEY,
                usuario_id INTEGER UNIQUE REFERENCES usuarios(id),
                cnh VARCHAR(20) UNIQUE NOT NULL,
                numero_viagens INTEGER DEFAULT 0
            )
            """,
            """
            CREATE TABLE IF NOT EXISTS passageiros (
                id SERIAL PRIMARY KEY,
                usuario_id INTEGER UNIQUE REFERENCES usuarios(id),
                numero_viagens INTEGER DEFAULT 0
            )
            """,
            """
            CREATE TABLE IF NOT EXISTS veiculos (
                id SERIAL PRIMARY KEY,
                motorista_id INTEGER REFERENCES motoristas(id),
                marca VARCHAR(50) NOT NULL,
                modelo VARCHAR(50) NOT NULL,
                placa VARCHAR(10) UNIQUE NOT NULL,
                ano INTEGER,
                cor VARCHAR(30)
            )
            """,
            """
            CREATE TABLE IF NOT EXISTS viagens (
                id SERIAL PRIMARY KEY,
                veiculo_id INTEGER REFERENCES veiculos(id),
                motorista_id INTEGER REFERENCES motoristas(id),
                cidade_origem VARCHAR(100) NOT NULL,
                cidade_destino VARCHAR(100) NOT NULL,
                preco DECIMAL(10,2),
                data_viagem VARCHAR(20),
                vagas INTEGER,
                status VARCHAR(20) DEFAULT 'PENDENTE',
                avaliacao DECIMAL(3,2) DEFAULT 0
            )
            """,
            """
            CREATE TABLE IF NOT EXISTS passageiros_viagens (
                id SERIAL PRIMARY KEY,
                viagem_id INTEGER REFERENCES viagens(id),
                passageiro_id INTEGER REFERENCES passageiros(id),
                numero_lugares INTEGER DEFAULT 1,
                status VARCHAR(20) DEFAULT 'RESERVADO',
                avaliacao INTEGER DEFAULT 0
            )
            """,
            """
            DO $$ 
            BEGIN 
                IF NOT EXISTS (SELECT 1 FROM information_schema.columns 
                               WHERE table_name='passageiros_viagens' AND column_name='avaliacao') 
                THEN ALTER TABLE passageiros_viagens ADD COLUMN avaliacao INTEGER DEFAULT 0;
                END IF;
            END $$
            """
        };

        Connection conn = null;
        Statement stmt = null;
        try {
            conn = conectar();
            if (conn == null) {
                System.out.println("Erro: Sem conexao para criar tabelas!");
                return;
            }
            stmt = conn.createStatement();
            for (String sql : sqls) {
                stmt.execute(sql);
            }
            System.out.println("Tabelas OK!");
        } catch (SQLException e) {
            System.out.println("Erro tabelas: " + e.getMessage());
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();

                }
            } catch (SQLException e) {
            }
            fechar(conn);
        }
    }
}
