package application;

import application.input.*;
import application.output.*;
import domain.entities.*;
import domain.enums.StatusViagem;
import domain.repositories.*;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ViagemUseCase {
    private final IViagemRepository viagemRepo;
    private final IVeiculoRepository veiculoRepo;
    private final IPassageiroViagemRepository pvRepo;

    public ViagemUseCase(IViagemRepository viagemRepo, IVeiculoRepository veiculoRepo, IPassageiroViagemRepository pvRepo) {
        this.viagemRepo = viagemRepo;
        this.veiculoRepo = veiculoRepo;
        this.pvRepo = pvRepo;
    }

    public Resposta<ViagemOutput> criarViagem(CriarViagemInput input) {
        if (!input.isValido()) {
            return Resposta.erro("Dados invalidos");
        }

        Optional<Veiculo> veiculo = veiculoRepo.buscarPorId(input.getVeiculoId());
        if (veiculo.isEmpty()) {
            return Resposta.erro("Veiculo nao encontrado");
        }

        Viagem viagem = new Viagem(
            veiculo.get(),
            input.getMotoristaId(),
            input.getCidadeOrigem(),
            input.getCidadeDestino(),
            input.getPreco(),
            input.getDataViagem(),
            input.getVagas()
        );
        Viagem salva = viagemRepo.salvar(viagem);
        return Resposta.sucesso("Viagem criada", toViagemOutput(salva));
    }

    public List<ViagemOutput> buscarViagens(BuscarViagensInput input) {
        List<Viagem> viagens;
        if (input.temData()) {
            viagens = viagemRepo.buscarPorOrigemDestinoData(
                input.getCidadeOrigem(), 
                input.getCidadeDestino(), 
                input.getDataViagem()
            );
        } else {
            viagens = viagemRepo.buscarPorOrigemDestino(
                input.getCidadeOrigem(), 
                input.getCidadeDestino()
            );
        }
        return viagens.stream().map(this::toViagemOutput).collect(Collectors.toList());
    }

    public Resposta<Void> reservarViagem(ReservarViagemInput input) {
        if (!input.isValido()) {
            return Resposta.erro("Dados invalidos");
        }

        Optional<Viagem> viagemOpt = viagemRepo.buscarPorId(input.getViagemId());
        if (viagemOpt.isEmpty()) {
            return Resposta.erro("Viagem nao encontrada");
        }

        Viagem viagem = viagemOpt.get();
        int ocupados = pvRepo.contarLugaresReservados(input.getViagemId());
        int disponiveis = viagem.getVagas() - ocupados;

        if (disponiveis < input.getNumeroLugares()) {
            return Resposta.erro("Nao ha vagas suficientes");
        }

        PassageiroViagem pv = new PassageiroViagem(input.getPassageiroId(), input.getNumeroLugares());
        pvRepo.salvar(pv, input.getViagemId());

        if (disponiveis - input.getNumeroLugares() == 0) {
            viagemRepo.atualizarStatus(input.getViagemId(), StatusViagem.CHEIA);
        }

        return Resposta.sucesso("Reserva realizada");
    }

    public Resposta<Void> avaliarViagem(AvaliarViagemInput input) {
        if (!input.isValido()) {
            return Resposta.erro("Nota deve ser entre 0 e 5");
        }

        Optional<Viagem> viagem = viagemRepo.buscarPorId(input.getViagemId());
        if (viagem.isEmpty()) {
            return Resposta.erro("Viagem nao encontrada");
        }

        viagemRepo.atualizarAvaliacao(input.getViagemId(), input.getNota());
        return Resposta.sucesso("Avaliacao registrada");
    }

    public List<ViagemOutput> listarPorMotorista(int motoristaId) {
        return viagemRepo.buscarPorMotoristaId(motoristaId)
            .stream()
            .map(this::toViagemOutput)
            .collect(Collectors.toList());
    }

    private ViagemOutput toViagemOutput(Viagem v) {
        int ocupados = pvRepo.contarLugaresReservados(v.getId());
        return new ViagemOutput(
            v.getId(),
            v.getVeiculo() != null ? v.getVeiculo().getId() : 0,
            v.getNomeMotorista() != null ? v.getNomeMotorista() : "",
            v.getCidadeOrigem(),
            v.getCidadeDestino(),
            v.getPreco(),
            v.getDataViagem(),
            v.getVagas(),
            v.getVagas() - ocupados,
            v.getStatus().name(),
            v.getAvaliacao()
        );
    }
}
