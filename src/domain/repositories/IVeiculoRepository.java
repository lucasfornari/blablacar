package domain.repositories;

import domain.entities.Veiculo;
import java.util.List;
import java.util.Optional;

public interface IVeiculoRepository {
    Veiculo salvar(Veiculo veiculo, int motoristaId);
    Optional<Veiculo> buscarPorId(int id);
    Optional<Veiculo> buscarPorPlaca(String placa);
    List<Veiculo> buscarPorMotoristaId(int motoristaId);
    boolean remover(int id);
    boolean existePorPlaca(String placa);
}
