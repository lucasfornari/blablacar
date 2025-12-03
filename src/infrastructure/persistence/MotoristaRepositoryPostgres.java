package infrastructure.persistence;

import domain.entities.Motorista;
import domain.repositories.IMotoristaRepository;
import infrastructure.database.ConexaoBanco;
import java.sql.*;
import java.util.Optional;

public class MotoristaRepositoryPostgres implements IMotoristaRepository {

    @Override
    public Motorista salvar(Motorista motorista, int usuarioId) {
        String sql = "INSERT INTO motoristas (usuario_id, cnh) VALUES (?, ?) RETURNING id";
        try (Connection conn = ConexaoBanco.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, usuarioId);
            stmt.setString(2, motorista.getCnh());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                motorista.setId(rs.getInt("id"));
            }
            return motorista;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar motorista: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Motorista> buscarPorId(int id) {
        String sql = "SELECT * FROM motoristas WHERE id = ?";
        try (Connection conn = ConexaoBanco.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(construirMotorista(rs));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar motorista: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Motorista> buscarPorUsuarioId(int usuarioId) {
        String sql = "SELECT * FROM motoristas WHERE usuario_id = ?";
        try (Connection conn = ConexaoBanco.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, usuarioId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(construirMotorista(rs));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar motorista: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Motorista> buscarPorCnh(String cnh) {
        String sql = "SELECT * FROM motoristas WHERE cnh = ?";
        try (Connection conn = ConexaoBanco.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, cnh);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(construirMotorista(rs));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar motorista: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean remover(int id) {
        String sql = "DELETE FROM motoristas WHERE id = ?";
        try (Connection conn = ConexaoBanco.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao remover motorista: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean existePorCnh(String cnh) {
        String sql = "SELECT COUNT(*) FROM motoristas WHERE cnh = ?";
        try (Connection conn = ConexaoBanco.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, cnh);
            ResultSet rs = stmt.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao verificar CNH: " + e.getMessage(), e);
        }
    }

    @Override
    public void incrementarViagens(int motoristaId) {
        String sql = "UPDATE motoristas SET numero_viagens = numero_viagens + 1 WHERE id = ?";
        try (Connection conn = ConexaoBanco.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, motoristaId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao incrementar viagens: " + e.getMessage(), e);
        }
    }

    private Motorista construirMotorista(ResultSet rs) throws SQLException {
        Motorista motorista = new Motorista(rs.getString("cnh"));
        motorista.setId(rs.getInt("id"));
        motorista.setNumeroDeViagens(rs.getInt("numero_viagens"));
        return motorista;
    }
}
