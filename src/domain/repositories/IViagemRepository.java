package domain.repositories;

import domain.entities.Viagem;
import domain.enums.StatusViagem;
import java.util.List;
import java.util.Optional;

public interface IViagemRepository {
    Viagem salvar(Viagem viagem);
    Viagem atualizar(Viagem viagem);
    Optional<Viagem> buscarPorId(int id);
    List<Viagem> buscarTodas();
    List<Viagem> buscarPorOrigemDestino(String origem, String destino);
    List<Viagem> buscarPorOrigemDestinoData(String origem, String destino, String data);
    List<Viagem> buscarPorMotoristaId(int motoristaId);
    boolean remover(int id);
    void atualizarStatus(int viagemId, StatusViagem status);
    void atualizarAvaliacao(int viagemId, double nota);
}
