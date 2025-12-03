package domain.repositories;

import domain.entities.Passageiro;
import java.util.Optional;

public interface IPassageiroRepository {
    Passageiro salvar(Passageiro passageiro, int usuarioId);
    Optional<Passageiro> buscarPorId(int id);
    Optional<Passageiro> buscarPorUsuarioId(int usuarioId);
    boolean remover(int id);
    void incrementarViagens(int passageiroId);
}
