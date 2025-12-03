package infrastructure.persistence;

import domain.entities.Veiculo;
import domain.repositories.IVeiculoRepository;
import infrastructure.database.ConexaoBanco;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class VeiculoRepositoryPostgres implements IVeiculoRepository {

    @Override
    public Veiculo salvar(Veiculo veiculo, int motoristaId) {
        String sql = "INSERT INTO veiculos (motorista_id, marca, modelo, placa, ano, cor) VALUES (?, ?, ?, ?, ?, ?) RETURNING id";
        try (Connection conn = ConexaoBanco.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, motoristaId);
            stmt.setString(2, veiculo.getMarca());
            stmt.setString(3, veiculo.getModelo());
            stmt.setString(4, veiculo.getPlaca());
            stmt.setInt(5, veiculo.getAno());
            stmt.setString(6, veiculo.getCor());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                veiculo.setId(rs.getInt("id"));
            }
            return veiculo;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar veiculo: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Veiculo> buscarPorId(int id) {
        String sql = "SELECT * FROM veiculos WHERE id = ?";
        try (Connection conn = ConexaoBanco.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(construirVeiculo(rs));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar veiculo: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Veiculo> buscarPorPlaca(String placa) {
        String sql = "SELECT * FROM veiculos WHERE placa = ?";
        try (Connection conn = ConexaoBanco.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, placa);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(construirVeiculo(rs));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar veiculo: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Veiculo> buscarPorMotoristaId(int motoristaId) {
        String sql = "SELECT * FROM veiculos WHERE motorista_id = ? ORDER BY marca, modelo";
        List<Veiculo> veiculos = new ArrayList<>();
        try (Connection conn = ConexaoBanco.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, motoristaId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                veiculos.add(construirVeiculo(rs));
            }
            return veiculos;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar veiculos: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean remover(int id) {
        String sql = "DELETE FROM veiculos WHERE id = ?";
        try (Connection conn = ConexaoBanco.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao remover veiculo: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean existePorPlaca(String placa) {
        String sql = "SELECT COUNT(*) FROM veiculos WHERE placa = ?";
        try (Connection conn = ConexaoBanco.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, placa);
            ResultSet rs = stmt.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao verificar placa: " + e.getMessage(), e);
        }
    }

    private Veiculo construirVeiculo(ResultSet rs) throws SQLException {
        Veiculo veiculo = new Veiculo();
        veiculo.setId(rs.getInt("id"));
        veiculo.setMarca(rs.getString("marca"));
        veiculo.setModelo(rs.getString("modelo"));
        veiculo.setPlaca(rs.getString("placa"));
        veiculo.setAno(rs.getInt("ano"));
        veiculo.setCor(rs.getString("cor"));
        return veiculo;
    }
}
