package infrastructure.persistence;

import domain.entities.Passageiro;
import domain.repositories.IPassageiroRepository;
import infrastructure.database.ConexaoBanco;
import java.sql.*;
import java.util.Optional;

public class PassageiroRepositoryPostgres implements IPassageiroRepository {

    @Override
    public Passageiro salvar(Passageiro passageiro, int usuarioId) {
        String sql = "INSERT INTO passageiros (usuario_id) VALUES (?) RETURNING id";
        try (Connection conn = ConexaoBanco.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, usuarioId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                passageiro.setId(rs.getInt("id"));
            }
            return passageiro;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar passageiro: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Passageiro> buscarPorId(int id) {
        String sql = "SELECT * FROM passageiros WHERE id = ?";
        try (Connection conn = ConexaoBanco.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(construirPassageiro(rs));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar passageiro: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Passageiro> buscarPorUsuarioId(int usuarioId) {
        String sql = "SELECT * FROM passageiros WHERE usuario_id = ?";
        try (Connection conn = ConexaoBanco.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, usuarioId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(construirPassageiro(rs));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar passageiro: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean remover(int id) {
        String sql = "DELETE FROM passageiros WHERE id = ?";
        try (Connection conn = ConexaoBanco.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao remover passageiro: " + e.getMessage(), e);
        }
    }

    @Override
    public void incrementarViagens(int passageiroId) {
        String sql = "UPDATE passageiros SET numero_viagens = numero_viagens + 1 WHERE id = ?";
        try (Connection conn = ConexaoBanco.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, passageiroId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao incrementar viagens: " + e.getMessage(), e);
        }
    }

    private Passageiro construirPassageiro(ResultSet rs) throws SQLException {
        Passageiro passageiro = new Passageiro();
        passageiro.setId(rs.getInt("id"));
        passageiro.setNumeroDeViagens(rs.getInt("numero_viagens"));
        return passageiro;
    }
}
