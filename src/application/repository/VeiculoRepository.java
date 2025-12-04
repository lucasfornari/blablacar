package application.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import application.output.VeiculoOutput;
import infrastructure.BancoDeDados;

public class VeiculoRepository {

    public int salvar(int motoristaId, String marca, String modelo, String placa, int ano, String cor) {
        String sql = "INSERT INTO veiculos (motorista_id, marca, modelo, placa, ano, cor) VALUES (?, ?, ?, ?, ?, ?) RETURNING id";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = BancoDeDados.conectar();
            if (conn == null) {
                return 0;
            }
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, motoristaId);
            stmt.setString(2, marca);
            stmt.setString(3, modelo);
            stmt.setString(4, placa);
            stmt.setInt(5, ano);
            stmt.setString(6, cor);
            rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        } catch (SQLException e) {
            System.out.println("Erro: " + e.getMessage());
        } finally {
            fechar(rs, stmt, conn);
        }
        return 0;
    }

    public List<VeiculoOutput> listarPorMotorista(int motoristaId) {
        String sql = "SELECT * FROM veiculos WHERE motorista_id = ?";
        List<VeiculoOutput> lista = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = BancoDeDados.conectar();
            if (conn == null) {
                return lista;
            }
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, motoristaId);
            rs = stmt.executeQuery();
            while (rs.next()) {
                lista.add(new VeiculoOutput(
                        rs.getInt("id"),
                        rs.getString("marca"),
                        rs.getString("modelo"),
                        rs.getString("placa"),
                        rs.getInt("ano"),
                        rs.getString("cor")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Erro: " + e.getMessage());
        } finally {
            fechar(rs, stmt, conn);
        }
        return lista;
    }

    public boolean placaExiste(String placa) {
        String sql = "SELECT 1 FROM veiculos WHERE placa = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = BancoDeDados.conectar();
            if (conn == null) {
                return false;
            }
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, placa);
            rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            return false;
        } finally {
            fechar(rs, stmt, conn);
        }
    }

    private void fechar(ResultSet rs, PreparedStatement stmt, Connection conn) {
        try {
            if (rs != null) {
                rs.close();
        
            }} catch (SQLException e) {
        }
        try {
            if (stmt != null) {
                stmt.close();
        
            }} catch (SQLException e) {
        }
        BancoDeDados.fechar(conn);
    }
}
