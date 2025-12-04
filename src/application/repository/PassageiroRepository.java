package application.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import infrastructure.BancoDeDados;

public class PassageiroRepository {

    public int salvar(int usuarioId) {
        String sql = "INSERT INTO passageiros (usuario_id) VALUES (?) RETURNING id";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = BancoDeDados.conectar();
            if (conn == null) {
                return 0;
            }
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, usuarioId);
            rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        } catch (SQLException e) {
            System.out.println("erro: " + e.getMessage());
        } finally {
            fechar(rs, stmt, conn);
        }
        return 0;
    }

    public int buscarPorUsuarioId(int usuarioId) {
        String sql = "SELECT id FROM passageiros WHERE usuario_id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = BancoDeDados.conectar();
            if (conn == null) {
                return 0;
            }
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, usuarioId);
            rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        } catch (SQLException e) {
            System.out.println("erro: " + e.getMessage());
        } finally {
            fechar(rs, stmt, conn);
        }
        return 0;
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
