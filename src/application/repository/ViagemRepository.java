package application.repository;

import application.output.ViagemOutput;
import infrastructure.BancoDeDados;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ViagemRepository {

    public int salvar(int veiculoId, int motoristaId, String origem, String destino, double preco, String data, int vagas) {
        String sql = "INSERT INTO viagens (veiculo_id, motorista_id, cidade_origem, cidade_destino, preco, data_viagem, vagas, status) VALUES (?, ?, ?, ?, ?, ?, ?, 'PENDENTE') RETURNING id";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = BancoDeDados.conectar();
            if (conn == null) return 0;
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, veiculoId);
            stmt.setInt(2, motoristaId);
            stmt.setString(3, origem);
            stmt.setString(4, destino);
            stmt.setDouble(5, preco);
            stmt.setString(6, data);
            stmt.setInt(7, vagas);
            rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt("id");
        } catch (SQLException e) {
            System.out.println("Erro: " + e.getMessage());
        } finally {
            fechar(rs, stmt, conn);
        }
        return 0;
    }

    public List<ViagemOutput> buscar(String origem, String destino, String data) {
        String sql = """
            SELECT v.*, u.nome as nome_motorista
            FROM viagens v
            JOIN motoristas m ON v.motorista_id = m.id
            JOIN usuarios u ON m.usuario_id = u.id
            WHERE LOWER(v.cidade_origem) LIKE LOWER(?) 
            AND LOWER(v.cidade_destino) LIKE LOWER(?)
            AND v.status IN ('PENDENTE', 'INICIADA')
        """;
        if (data != null && !data.isEmpty()) sql += " AND v.data_viagem = ?";
        sql += " ORDER BY v.data_viagem";

        List<ViagemOutput> lista = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = BancoDeDados.conectar();
            if (conn == null) return lista;
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, "%" + origem + "%");
            stmt.setString(2, "%" + destino + "%");
            if (data != null && !data.isEmpty()) stmt.setString(3, data);
            rs = stmt.executeQuery();
            while (rs.next()) {
                int vagasOcupadas = contarVagasOcupadas(rs.getInt("id"));
                lista.add(construirViagem(rs, vagasOcupadas));
            }
        } catch (SQLException e) {
            System.out.println("Erro: " + e.getMessage());
        } finally {
            fechar(rs, stmt, conn);
        }
        return lista;
    }

    public List<ViagemOutput> listarPorMotorista(int motoristaId) {
        String sql = """
            SELECT v.*, u.nome as nome_motorista
            FROM viagens v
            JOIN motoristas m ON v.motorista_id = m.id
            JOIN usuarios u ON m.usuario_id = u.id
            WHERE v.motorista_id = ?
            ORDER BY v.data_viagem DESC
        """;
        List<ViagemOutput> lista = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = BancoDeDados.conectar();
            if (conn == null) return lista;
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, motoristaId);
            rs = stmt.executeQuery();
            while (rs.next()) {
                int vagasOcupadas = contarVagasOcupadas(rs.getInt("id"));
                lista.add(construirViagem(rs, vagasOcupadas));
            }
        } catch (SQLException e) {
            System.out.println("Erro: " + e.getMessage());
        } finally {
            fechar(rs, stmt, conn);
        }
        return lista;
    }

    public List<ViagemOutput> listarPorPassageiro(int passageiroId) {
        String sql = """
            SELECT v.*, u.nome as nome_motorista, pv.avaliacao as minha_avaliacao
            FROM viagens v
            JOIN motoristas m ON v.motorista_id = m.id
            JOIN usuarios u ON m.usuario_id = u.id
            JOIN passageiros_viagens pv ON v.id = pv.viagem_id
            WHERE pv.passageiro_id = ?
            ORDER BY v.data_viagem DESC
        """;
        List<ViagemOutput> lista = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = BancoDeDados.conectar();
            if (conn == null) return lista;
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, passageiroId);
            rs = stmt.executeQuery();
            while (rs.next()) {
                int vagasOcupadas = contarVagasOcupadas(rs.getInt("id"));
                ViagemOutput viagem = construirViagem(rs, vagasOcupadas);
                viagem.minhaAvaliacao = rs.getInt("minha_avaliacao");
                lista.add(viagem);
            }
        } catch (SQLException e) {
            System.out.println("Erro: " + e.getMessage());
        } finally {
            fechar(rs, stmt, conn);
        }
        return lista;
    }

    public ViagemOutput buscarPorId(int viagemId) {
        String sql = """
            SELECT v.*, u.nome as nome_motorista
            FROM viagens v
            JOIN motoristas m ON v.motorista_id = m.id
            JOIN usuarios u ON m.usuario_id = u.id
            WHERE v.id = ?
        """;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = BancoDeDados.conectar();
            if (conn == null) return null;
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, viagemId);
            rs = stmt.executeQuery();
            if (rs.next()) {
                int vagasOcupadas = contarVagasOcupadas(rs.getInt("id"));
                return construirViagem(rs, vagasOcupadas);
            }
        } catch (SQLException e) {
            System.out.println("Erro: " + e.getMessage());
        } finally {
            fechar(rs, stmt, conn);
        }
        return null;
    }

    public int buscarMotoristaIdDaViagem(int viagemId) {
        String sql = "SELECT motorista_id FROM viagens WHERE id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = BancoDeDados.conectar();
            if (conn == null) return 0;
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, viagemId);
            rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt("motorista_id");
        } catch (SQLException e) {
        } finally {
            fechar(rs, stmt, conn);
        }
        return 0;
    }

    public int buscarVagas(int viagemId) {
        String sql = "SELECT vagas FROM viagens WHERE id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = BancoDeDados.conectar();
            if (conn == null) return 0;
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, viagemId);
            rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt("vagas");
        } catch (SQLException e) {
        } finally {
            fechar(rs, stmt, conn);
        }
        return 0;
    }

    public int contarVagasOcupadas(int viagemId) {
        String sql = "SELECT COALESCE(SUM(numero_lugares), 0) as total FROM passageiros_viagens WHERE viagem_id = ? AND status != 'CANCELADO'";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = BancoDeDados.conectar();
            if (conn == null) return 0;
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, viagemId);
            rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt("total");
        } catch (SQLException e) {
        } finally {
            fechar(rs, stmt, conn);
        }
        return 0;
    }

    public void atualizarStatus(int viagemId, String status) {
        String sql = "UPDATE viagens SET status = ? WHERE id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = BancoDeDados.conectar();
            if (conn == null) return;
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, status);
            stmt.setInt(2, viagemId);
            stmt.executeUpdate();
        } catch (SQLException e) {
        } finally {
            fechar(null, stmt, conn);
        }
    }

    public void salvarReserva(int viagemId, int passageiroId, int lugares) {
        String sql = "INSERT INTO passageiros_viagens (viagem_id, passageiro_id, numero_lugares) VALUES (?, ?, ?)";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = BancoDeDados.conectar();
            if (conn == null) return;
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, viagemId);
            stmt.setInt(2, passageiroId);
            stmt.setInt(3, lugares);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erro: " + e.getMessage());
        } finally {
            fechar(null, stmt, conn);
        }
    }

    public boolean passageiroNaViagem(int viagemId, int passageiroId) {
        String sql = "SELECT 1 FROM passageiros_viagens WHERE viagem_id = ? AND passageiro_id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = BancoDeDados.conectar();
            if (conn == null) return false;
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, viagemId);
            stmt.setInt(2, passageiroId);
            rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            return false;
        } finally {
            fechar(rs, stmt, conn);
        }
    }

    public boolean jaAvaliou(int viagemId, int passageiroId) {
        String sql = "SELECT avaliacao FROM passageiros_viagens WHERE viagem_id = ? AND passageiro_id = ? AND avaliacao > 0";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = BancoDeDados.conectar();
            if (conn == null) return false;
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, viagemId);
            stmt.setInt(2, passageiroId);
            rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            return false;
        } finally {
            fechar(rs, stmt, conn);
        }
    }

    public void salvarAvaliacao(int viagemId, int passageiroId, int nota) {
        String sql = "UPDATE passageiros_viagens SET avaliacao = ? WHERE viagem_id = ? AND passageiro_id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = BancoDeDados.conectar();
            if (conn == null) return;
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, nota);
            stmt.setInt(2, viagemId);
            stmt.setInt(3, passageiroId);
            stmt.executeUpdate();
            atualizarMediaAvaliacao(viagemId);
        } catch (SQLException e) {
            System.out.println("Erro: " + e.getMessage());
        } finally {
            fechar(null, stmt, conn);
        }
    }

    private void atualizarMediaAvaliacao(int viagemId) {
        String sql = "UPDATE viagens SET avaliacao = (SELECT AVG(avaliacao) FROM passageiros_viagens WHERE viagem_id = ? AND avaliacao > 0) WHERE id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = BancoDeDados.conectar();
            if (conn == null) return;
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, viagemId);
            stmt.setInt(2, viagemId);
            stmt.executeUpdate();
        } catch (SQLException e) {
        } finally {
            fechar(null, stmt, conn);
        }
    }

    public String buscarStatusViagem(int viagemId) {
        String sql = "SELECT status FROM viagens WHERE id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = BancoDeDados.conectar();
            if (conn == null) return null;
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, viagemId);
            rs = stmt.executeQuery();
            if (rs.next()) return rs.getString("status");
        } catch (SQLException e) {
        } finally {
            fechar(rs, stmt, conn);
        }
        return null;
    }

    private ViagemOutput construirViagem(ResultSet rs, int vagasOcupadas) throws SQLException {
        return new ViagemOutput(
            rs.getInt("id"),
            rs.getString("nome_motorista"),
            rs.getString("cidade_origem"),
            rs.getString("cidade_destino"),
            rs.getDouble("preco"),
            rs.getString("data_viagem"),
            rs.getInt("vagas"),
            rs.getInt("vagas") - vagasOcupadas,
            rs.getString("status")
        );
    }

    private void fechar(ResultSet rs, PreparedStatement stmt, Connection conn) {
        try { if (rs != null) rs.close(); } catch (SQLException e) {}
        try { if (stmt != null) stmt.close(); } catch (SQLException e) {}
        BancoDeDados.fechar(conn);
    }
}
