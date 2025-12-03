package infrastructure.persistence;

import domain.entities.PassageiroViagem;
import domain.enums.StatusParticipacao;
import domain.repositories.IPassageiroViagemRepository;
import infrastructure.database.ConexaoBanco;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PassageiroViagemRepositoryPostgres implements IPassageiroViagemRepository {

    @Override
    public PassageiroViagem salvar(PassageiroViagem pv, int viagemId) {
        String sql = "INSERT INTO passageiros_viagens (viagem_id, passageiro_id, numero_lugares, status) VALUES (?, ?, ?, ?) RETURNING id";
        try (Connection conn = ConexaoBanco.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, viagemId);
            stmt.setInt(2, pv.getPassageiroId());
            stmt.setInt(3, pv.getNumeroDeLugares());
            stmt.setString(4, pv.getStatus().name());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                pv.setId(rs.getInt("id"));
            }
            return pv;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar reserva: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<PassageiroViagem> buscarPorId(int id) {
        String sql = """
            SELECT pv.*, u.nome as nome_passageiro
            FROM passageiros_viagens pv
            JOIN passageiros p ON pv.passageiro_id = p.id
            JOIN usuarios u ON p.usuario_id = u.id
            WHERE pv.id = ?
        """;
        try (Connection conn = ConexaoBanco.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(construirPassageiroViagem(rs));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar reserva: " + e.getMessage(), e);
        }
    }

    @Override
    public List<PassageiroViagem> buscarPorViagemId(int viagemId) {
        String sql = """
            SELECT pv.*, u.nome as nome_passageiro
            FROM passageiros_viagens pv
            JOIN passageiros p ON pv.passageiro_id = p.id
            JOIN usuarios u ON p.usuario_id = u.id
            WHERE pv.viagem_id = ?
        """;
        List<PassageiroViagem> lista = new ArrayList<>();
        try (Connection conn = ConexaoBanco.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, viagemId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                lista.add(construirPassageiroViagem(rs));
            }
            return lista;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar reservas: " + e.getMessage(), e);
        }
    }

    @Override
    public List<PassageiroViagem> buscarPorPassageiroId(int passageiroId) {
        String sql = """
            SELECT pv.*, u.nome as nome_passageiro
            FROM passageiros_viagens pv
            JOIN passageiros p ON pv.passageiro_id = p.id
            JOIN usuarios u ON p.usuario_id = u.id
            WHERE pv.passageiro_id = ?
        """;
        List<PassageiroViagem> lista = new ArrayList<>();
        try (Connection conn = ConexaoBanco.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, passageiroId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                lista.add(construirPassageiroViagem(rs));
            }
            return lista;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar reservas: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean remover(int id) {
        String sql = "DELETE FROM passageiros_viagens WHERE id = ?";
        try (Connection conn = ConexaoBanco.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao remover reserva: " + e.getMessage(), e);
        }
    }

    @Override
    public void atualizarStatus(int id, StatusParticipacao status) {
        String sql = "UPDATE passageiros_viagens SET status = ? WHERE id = ?";
        try (Connection conn = ConexaoBanco.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status.name());
            stmt.setInt(2, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar status: " + e.getMessage(), e);
        }
    }

    @Override
    public int contarLugaresReservados(int viagemId) {
        String sql = "SELECT COALESCE(SUM(numero_lugares), 0) as total FROM passageiros_viagens WHERE viagem_id = ? AND status != 'CANCELADO'";
        try (Connection conn = ConexaoBanco.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, viagemId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("total");
            }
            return 0;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao contar lugares: " + e.getMessage(), e);
        }
    }

    private PassageiroViagem construirPassageiroViagem(ResultSet rs) throws SQLException {
        PassageiroViagem pv = new PassageiroViagem();
        pv.setId(rs.getInt("id"));
        pv.setPassageiroId(rs.getInt("passageiro_id"));
        pv.setNomePassageiro(rs.getString("nome_passageiro"));
        pv.setNumeroDeLugares(rs.getInt("numero_lugares"));
        pv.setStatus(StatusParticipacao.valueOf(rs.getString("status")));
        return pv;
    }
}
