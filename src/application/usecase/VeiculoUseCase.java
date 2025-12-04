package application.usecase;

import java.util.List;

import application.input.AdicionarVeiculoInput;
import application.output.VeiculoOutput;
import application.repository.VeiculoRepository;

public class VeiculoUseCase {

    private VeiculoRepository veiculoRepo = new VeiculoRepository();

    public int adicionar(AdicionarVeiculoInput input) {
        if (input.marca == null || input.modelo == null || input.placa == null) {
            return 0;
        }
        if (veiculoRepo.placaExiste(input.placa)) {
            return 0;
        }
        return veiculoRepo.salvar(input.motoristaId, input.marca, input.modelo, input.placa, input.ano, input.cor);
    }

    public List<VeiculoOutput> listar(int motoristaId) {
        return veiculoRepo.listarPorMotorista(motoristaId);
    }
}
