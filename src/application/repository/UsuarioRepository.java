package application.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import application.output.UsuarioOutput;
import infrastructure.BancoDeDados;

public class UsuarioRepository {

    public int salvar(String nome, String email, String senha, String telefone, String endereco) {
        String sql = "INSERT INTO usuarios (nome, email, senha, telefone, endereco) VALUES (?, ?, ?, ?, ?) RETURNING id";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = BancoDeDados.conectar();
            if (conn == null) {
                return 0;
            }
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, nome);
            stmt.setString(2, email);
            stmt.setString(3, senha);
            stmt.setString(4, telefone);
            stmt.setString(5, endereco);
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

    public UsuarioOutput buscarPorEmailSenha(String email, String senha) {
        String sql = """
            SELECT u.*, m.id as motorista_id, p.id as passageiro_id
            FROM usuarios u
            LEFT JOIN motoristas m ON u.id = m.usuario_id
            LEFT JOIN passageiros p ON u.id = p.usuario_id
            WHERE u.email = ? AND u.senha = ?
        """;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = BancoDeDados.conectar();
            if (conn == null) {
                return null;
            }
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, email);
            stmt.setString(2, senha);
            rs = stmt.executeQuery();
            if (rs.next()) {
                return new UsuarioOutput(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("email"),
                        rs.getString("telefone"),
                        rs.getString("endereco"),
                        rs.getInt("motorista_id") > 0,
                        rs.getInt("passageiro_id") > 0,
                        rs.getInt("motorista_id"),
                        rs.getInt("passageiro_id")
                );
            }
        } catch (SQLException e) {
            System.out.println("Erro: " + e.getMessage());
        } finally {
            fechar(rs, stmt, conn);
        }
        return null;
    }

    public boolean emailExiste(String email) {
        String sql = "SELECT 1 FROM usuarios WHERE email = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = BancoDeDados.conectar();
            if (conn == null) {
                return false;
            }
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, email);
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
