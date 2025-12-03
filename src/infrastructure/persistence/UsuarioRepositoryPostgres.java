package infrastructure.persistence;

import domain.entities.Usuario;
import domain.entities.Motorista;
import domain.entities.Passageiro;
import domain.repositories.IUsuarioRepository;
import infrastructure.database.ConexaoBanco;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UsuarioRepositoryPostgres implements IUsuarioRepository {

    @Override
    public Usuario salvar(Usuario usuario) {
        String sql = "INSERT INTO usuarios (nome, email, senha, telefone, endereco) VALUES (?, ?, ?, ?, ?) RETURNING id";
        try (Connection conn = ConexaoBanco.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getEmail());
            stmt.setString(3, usuario.getSenha());
            stmt.setString(4, usuario.getTelefone());
            stmt.setString(5, usuario.getEndereco());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                usuario.setId(rs.getInt("id"));
            }
            return usuario;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar usuario: " + e.getMessage(), e);
        }
    }

    @Override
    public Usuario atualizar(Usuario usuario) {
        String sql = "UPDATE usuarios SET nome = ?, telefone = ?, endereco = ? WHERE id = ?";
        try (Connection conn = ConexaoBanco.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getTelefone());
            stmt.setString(3, usuario.getEndereco());
            stmt.setInt(4, usuario.getId());
            stmt.executeUpdate();
            return usuario;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar usuario: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Usuario> buscarPorId(int id) {
        String sql = """
            SELECT u.*, m.id as motorista_id, m.cnh, m.numero_viagens as motorista_viagens,
                   p.id as passageiro_id, p.numero_viagens as passageiro_viagens
            FROM usuarios u
            LEFT JOIN motoristas m ON m.usuario_id = u.id
            LEFT JOIN passageiros p ON p.usuario_id = u.id
            WHERE u.id = ?
        """;
        try (Connection conn = ConexaoBanco.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(construirUsuario(rs));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar usuario: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Usuario> buscarPorEmail(String email) {
        String sql = """
            SELECT u.*, m.id as motorista_id, m.cnh, m.numero_viagens as motorista_viagens,
                   p.id as passageiro_id, p.numero_viagens as passageiro_viagens
            FROM usuarios u
            LEFT JOIN motoristas m ON m.usuario_id = u.id
            LEFT JOIN passageiros p ON p.usuario_id = u.id
            WHERE u.email = ?
        """;
        try (Connection conn = ConexaoBanco.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(construirUsuario(rs));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar usuario: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Usuario> buscarTodos() {
        String sql = """
            SELECT u.*, m.id as motorista_id, m.cnh, m.numero_viagens as motorista_viagens,
                   p.id as passageiro_id, p.numero_viagens as passageiro_viagens
            FROM usuarios u
            LEFT JOIN motoristas m ON m.usuario_id = u.id
            LEFT JOIN passageiros p ON p.usuario_id = u.id
            ORDER BY u.nome
        """;
        List<Usuario> usuarios = new ArrayList<>();
        try (Connection conn = ConexaoBanco.obterConexao();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                usuarios.add(construirUsuario(rs));
            }
            return usuarios;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar usuarios: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean remover(int id) {
        String sql = "DELETE FROM usuarios WHERE id = ?";
        try (Connection conn = ConexaoBanco.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao remover usuario: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean existePorEmail(String email) {
        String sql = "SELECT COUNT(*) FROM usuarios WHERE email = ?";
        try (Connection conn = ConexaoBanco.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao verificar email: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Usuario> login(String email, String senha) {
        String sql = """
            SELECT u.*, m.id as motorista_id, m.cnh, m.numero_viagens as motorista_viagens,
                   p.id as passageiro_id, p.numero_viagens as passageiro_viagens
            FROM usuarios u
            LEFT JOIN motoristas m ON m.usuario_id = u.id
            LEFT JOIN passageiros p ON p.usuario_id = u.id
            WHERE u.email = ? AND u.senha = ?
        """;
        try (Connection conn = ConexaoBanco.obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            stmt.setString(2, senha);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(construirUsuario(rs));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao realizar login: " + e.getMessage(), e);
        }
    }

    private Usuario construirUsuario(ResultSet rs) throws SQLException {
        Usuario usuario = new Usuario();
        usuario.setId(rs.getInt("id"));
        usuario.setNome(rs.getString("nome"));
        usuario.setEmail(rs.getString("email"));
        usuario.setSenha(rs.getString("senha"));
        usuario.setTelefone(rs.getString("telefone"));
        usuario.setEndereco(rs.getString("endereco"));

        String cnh = rs.getString("cnh");
        if (cnh != null) {
            Motorista motorista = new Motorista(cnh);
            motorista.setId(rs.getInt("motorista_id"));
            motorista.setNumeroDeViagens(rs.getInt("motorista_viagens"));
            usuario.setMotorista(motorista);
        }

        int passageiroId = rs.getInt("passageiro_id");
        if (!rs.wasNull()) {
            Passageiro passageiro = new Passageiro();
            passageiro.setId(passageiroId);
            passageiro.setNumeroDeViagens(rs.getInt("passageiro_viagens"));
            usuario.setPassageiro(passageiro);
        }

        return usuario;
    }
}
