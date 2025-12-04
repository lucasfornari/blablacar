package application.usecase;

import java.util.List;

import application.input.AvaliarViagemInput;
import application.input.BuscarViagensInput;
import application.input.CriarViagemInput;
import application.input.FinalizarViagemInput;
import application.input.ReservarViagemInput;
import application.output.ViagemOutput;
import application.repository.ViagemRepository;

public class ViagemUseCase {

    private ViagemRepository viagemRepo = new ViagemRepository();

    public int criar(CriarViagemInput input) {
        if (input.cidadeOrigem == null || input.cidadeDestino == null) {
            return 0;
        }
        if (input.vagas <= 0) {
            return 0;
        }
        return viagemRepo.salvar(input.veiculoId, input.motoristaId, input.cidadeOrigem,
                input.cidadeDestino, input.preco, input.dataViagem, input.vagas);
    }

    public List<ViagemOutput> buscar(BuscarViagensInput input) {
        return viagemRepo.buscar(input.cidadeOrigem, input.cidadeDestino, input.dataViagem);
    }

    public List<ViagemOutput> listarPorMotorista(int motoristaId) {
        return viagemRepo.listarPorMotorista(motoristaId);
    }

    public List<ViagemOutput> listarPorPassageiro(int passageiroId) {
        return viagemRepo.listarPorPassageiro(passageiroId);
    }

    public String reservar(ReservarViagemInput input) {
        if (input.numeroLugares <= 0) {
            return "informe um valor maior que zero";
        }

        int vagas = viagemRepo.buscarVagas(input.viagemId);
        int ocupadas = viagemRepo.contarVagasOcupadas(input.viagemId);
        int disponiveis = vagas - ocupadas;

        if (disponiveis < input.numeroLugares) {
            return "não tem mais vagas";
        }

        viagemRepo.salvarReserva(input.viagemId, input.passageiroId, input.numeroLugares);

        if (disponiveis - input.numeroLugares == 0) {
            viagemRepo.atualizarStatus(input.viagemId, "CHEIA");
        }

        return "reservado";
    }

    public String finalizar(FinalizarViagemInput input) {

        int motoristaDaViagem = viagemRepo.buscarMotoristaIdDaViagem(input.viagemId);
        if (motoristaDaViagem != input.motoristaId) {
            return "somente o motorista que criou a viagem pode finalizar";
        }

        String statusAtual = viagemRepo.buscarStatusViagem(input.viagemId);
        if (statusAtual == null) {
            return "viagem nao encontrada";
        }
        if (statusAtual.equals("FINALIZADA")) {
            return "viagem ja foi finalizada";
        }
        if (statusAtual.equals("CANCELADA")) {
            return "viagem foi cancelada";
        }

        viagemRepo.atualizarStatus(input.viagemId, "FINALIZADA");
        return "viagem finalizada com sucesso";
    }

    public String avaliar(AvaliarViagemInput input) {
        if (input.nota < 1 || input.nota > 5) {
            return "a nota deve ser de 1 a 5";
        }

        String status = viagemRepo.buscarStatusViagem(input.viagemId);
        if (status == null) {
            return "viagem nao encontrada";
        }
        if (!status.equals("FINALIZADA")) {
            return "somente viagens finalizadas podem ser avaliadas";
        }

        if (!viagemRepo.passageiroNaViagem(input.viagemId, input.passageiroId)) {
            return "voce nao participou desta viagem";
        }

        if (viagemRepo.jaAvaliou(input.viagemId, input.passageiroId)) {
            return "voce ja avaliou esta viagem";
        }

        viagemRepo.salvarAvaliacao(input.viagemId, input.passageiroId, input.nota);
        return "avaliacao registrada";
    }
}
