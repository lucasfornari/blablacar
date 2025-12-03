package domain.repositories;

import domain.entities.PassageiroViagem;
import domain.enums.StatusParticipacao;
import java.util.List;
import java.util.Optional;

public interface IPassageiroViagemRepository {
    PassageiroViagem salvar(PassageiroViagem passageiroViagem, int viagemId);
    Optional<PassageiroViagem> buscarPorId(int id);
    List<PassageiroViagem> buscarPorViagemId(int viagemId);
    List<PassageiroViagem> buscarPorPassageiroId(int passageiroId);
    boolean remover(int id);
    void atualizarStatus(int id, StatusParticipacao status);
    int contarLugaresReservados(int viagemId);
}
