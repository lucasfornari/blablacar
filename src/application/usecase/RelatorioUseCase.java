package application.usecase;

import application.output.RelatorioGeralOutput;
import application.output.RelatorioMotoristaOutput;
import application.output.RelatorioPassageiroOutput;
import application.repository.RelatorioRepository;

public class RelatorioUseCase {
    private RelatorioRepository relatorioRepo = new RelatorioRepository();

    public RelatorioMotoristaOutput relatorioMotorista(int motoristaId, String nome) {
        return relatorioRepo.relatorioMotorista(motoristaId, nome);
    }

    public RelatorioPassageiroOutput relatorioPassageiro(int passageiroId, String nome) {
        return relatorioRepo.relatorioPassageiro(passageiroId, nome);
    }

    public RelatorioGeralOutput relatorioGeral() {
        return relatorioRepo.relatorioGeral();
    }
}
