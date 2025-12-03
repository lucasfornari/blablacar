package domain.repositories;

import domain.entities.Usuario;
import java.util.List;
import java.util.Optional;

public interface IUsuarioRepository {
    Usuario salvar(Usuario usuario);
    Usuario atualizar(Usuario usuario);
    Optional<Usuario> buscarPorId(int id);
    Optional<Usuario> buscarPorEmail(String email);
    List<Usuario> buscarTodos();
    boolean remover(int id);
    boolean existePorEmail(String email);
    Optional<Usuario> login(String email, String senha);
}
