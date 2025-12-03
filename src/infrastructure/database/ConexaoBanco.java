package infrastructure.database;

import java.sql.Connection;
import java.sql.SQLException;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class ConexaoBanco {
    private static HikariDataSource dataSource;
    private static final String URL = System.getenv().getOrDefault("DB_URL", "jdbc:postgresql://localhost:5432/blablacar");
    private static final String USUARIO = System.getenv().getOrDefault("DB_USER", "postgres");
    private static final String SENHA = System.getenv().getOrDefault("DB_PASSWORD", "123123");

    static {
        inicializarPool();
    }

    private static void inicializarPool() {
        try {
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(URL);
            config.setUsername(USUARIO);
            config.setPassword(SENHA);
            config.setMaximumPoolSize(10);
            config.setMinimumIdle(2);
            config.setConnectionTimeout(30000);
            config.setIdleTimeout(600000);
            config.setMaxLifetime(1800000);
            config.setAutoCommit(true);
            config.setConnectionTestQuery("SELECT 1");
            dataSource = new HikariDataSource(config);
            System.out.println("[DB] Pool de conexoes inicializado");
        } catch (Exception e) {
            System.err.println("[DB] Erro ao inicializar pool: " + e.getMessage());
        }
    }

    public static Connection obterConexao() throws SQLException {
        if (dataSource == null) {
            inicializarPool();
        }
        return dataSource.getConnection();
    }

    public static void fecharPool() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
            System.out.println("[DB] Pool fechado");
        }
    }

    public static boolean testarConexao() {
        try (Connection conn = obterConexao()) {
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            System.err.println("[DB] Erro: " + e.getMessage());
            return false;
        }
    }
}
