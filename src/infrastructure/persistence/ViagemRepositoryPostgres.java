package infrastructure.persistence;

import domain.entities.Viagem;
import domain.entities.Veiculo;
import domain.enums.StatusViagem;
import domain.repositories.IViagemRepository;
import infrastructure.database.ConexaoBanco;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ViagemRepositoryPostgres implements IViagemRepository {

    @Override
    public Viagem salvar(Viagem viagem) {
        String sql = "INSERT INTO viagens (veiculo_id, motorista_id, cidade_origem, cidade_destino, preco, data_viagem, vagas, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?) RETURNING id";
        try (Connection conn = ConexaoBanco.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, viagem.getVeiculo().getId());
            stmt.setInt(2, viagem.getMotoristaId());
            stmt.setString(3, viagem.getCidadeOrigem());
            stmt.setString(4, viagem.getCidadeDestino());
            stmt.setDouble(5, viagem.getPreco());
            stmt.setString(6, viagem.getDataViagem());
            stmt.setInt(7, viagem.getVagas());
            stmt.setString(8, viagem.getStatus().name());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                viagem.setId(rs.getInt("id"));
            }
            return viagem;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar viagem: " + e.getMessage(), e);
        }
    }

    @Override
    public Viagem atualizar(Viagem viagem) {
        String sql = "UPDATE viagens SET status = ?, avaliacao = ? WHERE id = ?";
        try (Connection conn = ConexaoBanco.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, viagem.getStatus().name());
            stmt.setDouble(2, viagem.getAvaliacao());
            stmt.setInt(3, viagem.getId());
            stmt.executeUpdate();
            return viagem;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar viagem: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Viagem> buscarPorId(int id) {
        String sql = """
            SELECT v.*, u.nome as nome_motorista,
                   ve.id as veiculo_id, ve.marca, ve.modelo, ve.placa, ve.ano, ve.cor
            FROM viagens v
            JOIN motoristas m ON v.motorista_id = m.id
            JOIN usuarios u ON m.usuario_id = u.id
            JOIN veiculos ve ON v.veiculo_id = ve.id
            WHERE v.id = ?
        """;
        try (Connection conn = ConexaoBanco.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(construirViagem(rs));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar viagem: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Viagem> buscarTodas() {
        String sql = """
            SELECT v.*, u.nome as nome_motorista,
                   ve.id as veiculo_id, ve.marca, ve.modelo, ve.placa, ve.ano, ve.cor
            FROM viagens v
            JOIN motoristas m ON v.motorista_id = m.id
            JOIN usuarios u ON m.usuario_id = u.id
            JOIN veiculos ve ON v.veiculo_id = ve.id
            ORDER BY v.data_viagem
        """;
        return executarBusca(sql);
    }

    @Override
    public List<Viagem> buscarPorOrigemDestino(String origem, String destino) {
        String sql = """
            SELECT v.*, u.nome as nome_motorista,
                   ve.id as veiculo_id, ve.marca, ve.modelo, ve.placa, ve.ano, ve.cor
            FROM viagens v
            JOIN motoristas m ON v.motorista_id = m.id
            JOIN usuarios u ON m.usuario_id = u.id
            JOIN veiculos ve ON v.veiculo_id = ve.id
            WHERE LOWER(v.cidade_origem) LIKE LOWER(?) AND LOWER(v.cidade_destino) LIKE LOWER(?)
            AND v.status IN ('PENDENTE', 'INICIADA')
            ORDER BY v.data_viagem
        """;
        List<Viagem> viagens = new ArrayList<>();
        try (Connection conn = ConexaoBanco.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + origem + "%");
            stmt.setString(2, "%" + destino + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                viagens.add(construirViagem(rs));
            }
            return viagens;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar viagens: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Viagem> buscarPorOrigemDestinoData(String origem, String destino, String data) {
        String sql = """
            SELECT v.*, u.nome as nome_motorista,
                   ve.id as veiculo_id, ve.marca, ve.modelo, ve.placa, ve.ano, ve.cor
            FROM viagens v
            JOIN motoristas m ON v.motorista_id = m.id
            JOIN usuarios u ON m.usuario_id = u.id
            JOIN veiculos ve ON v.veiculo_id = ve.id
            WHERE LOWER(v.cidade_origem) LIKE LOWER(?) AND LOWER(v.cidade_destino) LIKE LOWER(?) AND v.data_viagem = ?
            AND v.status IN ('PENDENTE', 'INICIADA')
            ORDER BY v.preco
        """;
        List<Viagem> viagens = new ArrayList<>();
        try (Connection conn = ConexaoBanco.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + origem + "%");
            stmt.setString(2, "%" + destino + "%");
            stmt.setString(3, data);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                viagens.add(construirViagem(rs));
            }
            return viagens;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar viagens: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Viagem> buscarPorMotoristaId(int motoristaId) {
        String sql = """
            SELECT v.*, u.nome as nome_motorista,
                   ve.id as veiculo_id, ve.marca, ve.modelo, ve.placa, ve.ano, ve.cor
            FROM viagens v
            JOIN motoristas m ON v.motorista_id = m.id
            JOIN usuarios u ON m.usuario_id = u.id
            JOIN veiculos ve ON v.veiculo_id = ve.id
            WHERE v.motorista_id = ?
            ORDER BY v.data_viagem DESC
        """;
        List<Viagem> viagens = new ArrayList<>();
        try (Connection conn = ConexaoBanco.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, motoristaId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                viagens.add(construirViagem(rs));
            }
            return viagens;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar viagens: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean remover(int id) {
        String sql = "DELETE FROM viagens WHERE id = ?";
        try (Connection conn = ConexaoBanco.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao remover viagem: " + e.getMessage(), e);
        }
    }

    @Override
    public void atualizarStatus(int viagemId, StatusViagem status) {
        String sql = "UPDATE viagens SET status = ? WHERE id = ?";
        try (Connection conn = ConexaoBanco.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status.name());
            stmt.setInt(2, viagemId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar status: " + e.getMessage(), e);
        }
    }

    @Override
    public void atualizarAvaliacao(int viagemId, double nota) {
        String sql = "UPDATE viagens SET soma_notas = soma_notas + ?, total_avaliacoes = total_avaliacoes + 1, avaliacao = (soma_notas + ?) / (total_avaliacoes + 1) WHERE id = ?";
        try (Connection conn = ConexaoBanco.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDouble(1, nota);
            stmt.setDouble(2, nota);
            stmt.setInt(3, viagemId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar avaliacao: " + e.getMessage(), e);
        }
    }

    private List<Viagem> executarBusca(String sql) {
        List<Viagem> viagens = new ArrayList<>();
        try (Connection conn = ConexaoBanco.obterConexao();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                viagens.add(construirViagem(rs));
            }
            return viagens;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar viagens: " + e.getMessage(), e);
        }
    }

    private Viagem construirViagem(ResultSet rs) throws SQLException {
        Veiculo veiculo = new Veiculo();
        veiculo.setId(rs.getInt("veiculo_id"));
        veiculo.setMarca(rs.getString("marca"));
        veiculo.setModelo(rs.getString("modelo"));
        veiculo.setPlaca(rs.getString("placa"));
        veiculo.setAno(rs.getInt("ano"));
        veiculo.setCor(rs.getString("cor"));

        Viagem viagem = new Viagem();
        viagem.setId(rs.getInt("id"));
        viagem.setVeiculo(veiculo);
        viagem.setMotoristaId(rs.getInt("motorista_id"));
        viagem.setNomeMotorista(rs.getString("nome_motorista"));
        viagem.setCidadeOrigem(rs.getString("cidade_origem"));
        viagem.setCidadeDestino(rs.getString("cidade_destino"));
        viagem.setPreco(rs.getDouble("preco"));
        viagem.setDataViagem(rs.getString("data_viagem"));
        viagem.setVagas(rs.getInt("vagas"));
        viagem.setStatus(StatusViagem.valueOf(rs.getString("status")));
        viagem.setAvaliacao(rs.getDouble("avaliacao"));
        viagem.setSomaNotas(rs.getDouble("soma_notas"));
        viagem.setTotalAvaliacoes(rs.getInt("total_avaliacoes"));
        return viagem;
    }
}
