package application.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import infrastructure.BancoDeDados;

public class MotoristaRepository {

    public int salvar(int usuarioId, String cnh) {
        String sql = "INSERT INTO motoristas (usuario_id, cnh) VALUES (?, ?) RETURNING id";
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
            stmt.setString(2, cnh);
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
        String sql = "SELECT id FROM motoristas WHERE usuario_id = ?";
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

    public boolean cnhExiste(String cnh) {
        String sql = "SELECT 1 FROM motoristas WHERE cnh = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = BancoDeDados.conectar();
            if (conn == null) {
                return false;
            }
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, cnh);
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

            }
        } catch (SQLException e) {
        }
        try {
            if (stmt != null) {
                stmt.close();

            }
        } catch (SQLException e) {
        }
        BancoDeDados.fechar(conn);
    }
}
