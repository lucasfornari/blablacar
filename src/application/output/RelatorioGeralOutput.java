package application.output;

public class RelatorioGeralOutput {
    public int totalUsuarios;
    public int totalMotoristas;
    public int totalPassageiros;
    public int totalViagens;
    public int viagensFinalizadas;
    public int viagensPendentes;
    public double faturamentoTotal;

    public RelatorioGeralOutput(int totalUsuarios, int totalMotoristas, int totalPassageiros,
                                 int totalViagens, int viagensFinalizadas, int viagensPendentes,
                                 double faturamentoTotal) {
        this.totalUsuarios = totalUsuarios;
        this.totalMotoristas = totalMotoristas;
        this.totalPassageiros = totalPassageiros;
        this.totalViagens = totalViagens;
        this.viagensFinalizadas = viagensFinalizadas;
        this.viagensPendentes = viagensPendentes;
        this.faturamentoTotal = faturamentoTotal;
    }
}
