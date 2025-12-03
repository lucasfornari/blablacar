package domain.repositories;

import domain.entities.Motorista;
import java.util.Optional;

public interface IMotoristaRepository {
    Motorista salvar(Motorista motorista, int usuarioId);
    Optional<Motorista> buscarPorId(int id);
    Optional<Motorista> buscarPorUsuarioId(int usuarioId);
    Optional<Motorista> buscarPorCnh(String cnh);
    boolean remover(int id);
    boolean existePorCnh(String cnh);
    void incrementarViagens(int motoristaId);
}
