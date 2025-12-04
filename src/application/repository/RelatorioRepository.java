package application.repository;

import application.output.*;
import infrastructure.BancoDeDados;
import java.sql.*;

public class RelatorioRepository {

    public RelatorioMotoristaOutput relatorioMotorista(int motoristaId, String nomeMotorista) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        int totalViagens = 0;
        int finalizadas = 0;
        int canceladas = 0;
        double mediaAvaliacao = 0;
        double totalFaturado = 0;

        try {
            conn = BancoDeDados.conectar();
            if (conn == null) return null;

            // Total de viagens
            stmt = conn.prepareStatement("SELECT COUNT(*) as total FROM viagens WHERE motorista_id = ?");
            stmt.setInt(1, motoristaId);
            rs = stmt.executeQuery();
            if (rs.next()) totalViagens = rs.getInt("total");
            rs.close(); stmt.close();

            // Finalizadas
            stmt = conn.prepareStatement("SELECT COUNT(*) as total FROM viagens WHERE motorista_id = ? AND status = 'FINALIZADA'");
            stmt.setInt(1, motoristaId);
            rs = stmt.executeQuery();
            if (rs.next()) finalizadas = rs.getInt("total");
            rs.close(); stmt.close();

            // Canceladas
            stmt = conn.prepareStatement("SELECT COUNT(*) as total FROM viagens WHERE motorista_id = ? AND status = 'CANCELADA'");
            stmt.setInt(1, motoristaId);
            rs = stmt.executeQuery();
            if (rs.next()) canceladas = rs.getInt("total");
            rs.close(); stmt.close();

            // Media avaliacao
            stmt = conn.prepareStatement("SELECT AVG(avaliacao) as media FROM viagens WHERE motorista_id = ? AND avaliacao > 0");
            stmt.setInt(1, motoristaId);
            rs = stmt.executeQuery();
            if (rs.next()) mediaAvaliacao = rs.getDouble("media");
            rs.close(); stmt.close();

            // Total faturado
            stmt = conn.prepareStatement("""
                SELECT COALESCE(SUM(v.preco * pv.numero_lugares), 0) as total
                FROM viagens v
                JOIN passageiros_viagens pv ON v.id = pv.viagem_id
                WHERE v.motorista_id = ? AND v.status = 'FINALIZADA'
            """);
            stmt.setInt(1, motoristaId);
            rs = stmt.executeQuery();
            if (rs.next()) totalFaturado = rs.getDouble("total");

        } catch (SQLException e) {
            System.out.println("Erro: " + e.getMessage());
        } finally {
            fechar(rs, stmt, conn);
        }

        return new RelatorioMotoristaOutput(nomeMotorista, totalViagens, finalizadas, canceladas, mediaAvaliacao, totalFaturado);
    }

    public RelatorioPassageiroOutput relatorioPassageiro(int passageiroId, String nomePassageiro) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        int totalViagens = 0;
        int avaliacoesFeitas = 0;
        double totalGasto = 0;

        try {
            conn = BancoDeDados.conectar();
            if (conn == null) return null;

            // Total de viagens
            stmt = conn.prepareStatement("SELECT COUNT(*) as total FROM passageiros_viagens WHERE passageiro_id = ?");
            stmt.setInt(1, passageiroId);
            rs = stmt.executeQuery();
            if (rs.next()) totalViagens = rs.getInt("total");
            rs.close(); stmt.close();

            // Avaliacoes feitas
            stmt = conn.prepareStatement("SELECT COUNT(*) as total FROM passageiros_viagens WHERE passageiro_id = ? AND avaliacao > 0");
            stmt.setInt(1, passageiroId);
            rs = stmt.executeQuery();
            if (rs.next()) avaliacoesFeitas = rs.getInt("total");
            rs.close(); stmt.close();

            // Total gasto
            stmt = conn.prepareStatement("""
                SELECT COALESCE(SUM(v.preco * pv.numero_lugares), 0) as total
                FROM viagens v
                JOIN passageiros_viagens pv ON v.id = pv.viagem_id
                WHERE pv.passageiro_id = ? AND v.status = 'FINALIZADA'
            """);
            stmt.setInt(1, passageiroId);
            rs = stmt.executeQuery();
            if (rs.next()) totalGasto = rs.getDouble("total");

        } catch (SQLException e) {
            System.out.println("Erro: " + e.getMessage());
        } finally {
            fechar(rs, stmt, conn);
        }

        return new RelatorioPassageiroOutput(nomePassageiro, totalViagens, avaliacoesFeitas, totalGasto);
    }

    public RelatorioGeralOutput relatorioGeral() {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        
        int totalUsuarios = 0;
        int totalMotoristas = 0;
        int totalPassageiros = 0;
        int totalViagens = 0;
        int viagensFinalizadas = 0;
        int viagensPendentes = 0;
        double faturamentoTotal = 0;

        try {
            conn = BancoDeDados.conectar();
            if (conn == null) return null;
            stmt = conn.createStatement();

            rs = stmt.executeQuery("SELECT COUNT(*) as total FROM usuarios");
            if (rs.next()) totalUsuarios = rs.getInt("total");
            rs.close();

            rs = stmt.executeQuery("SELECT COUNT(*) as total FROM motoristas");
            if (rs.next()) totalMotoristas = rs.getInt("total");
            rs.close();

            rs = stmt.executeQuery("SELECT COUNT(*) as total FROM passageiros");
            if (rs.next()) totalPassageiros = rs.getInt("total");
            rs.close();

            rs = stmt.executeQuery("SELECT COUNT(*) as total FROM viagens");
            if (rs.next()) totalViagens = rs.getInt("total");
            rs.close();

            rs = stmt.executeQuery("SELECT COUNT(*) as total FROM viagens WHERE status = 'FINALIZADA'");
            if (rs.next()) viagensFinalizadas = rs.getInt("total");
            rs.close();

            rs = stmt.executeQuery("SELECT COUNT(*) as total FROM viagens WHERE status = 'PENDENTE'");
            if (rs.next()) viagensPendentes = rs.getInt("total");
            rs.close();

            rs = stmt.executeQuery("""
                SELECT COALESCE(SUM(v.preco * pv.numero_lugares), 0) as total
                FROM viagens v
                JOIN passageiros_viagens pv ON v.id = pv.viagem_id
                WHERE v.status = 'FINALIZADA'
            """);
            if (rs.next()) faturamentoTotal = rs.getDouble("total");

        } catch (SQLException e) {
            System.out.println("Erro: " + e.getMessage());
        } finally {
            try { if (rs != null) rs.close(); } catch (SQLException e) {}
            try { if (stmt != null) stmt.close(); } catch (SQLException e) {}
            BancoDeDados.fechar(conn);
        }

        return new RelatorioGeralOutput(totalUsuarios, totalMotoristas, totalPassageiros, 
                                         totalViagens, viagensFinalizadas, viagensPendentes, faturamentoTotal);
    }

    private void fechar(ResultSet rs, PreparedStatement stmt, Connection conn) {
        try { if (rs != null) rs.close(); } catch (SQLException e) {}
        try { if (stmt != null) stmt.close(); } catch (SQLException e) {}
        BancoDeDados.fechar(conn);
    }
}
