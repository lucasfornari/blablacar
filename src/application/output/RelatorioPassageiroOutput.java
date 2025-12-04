package application.output;

public class RelatorioPassageiroOutput {
    public String nome;
    public int totalViagens;
    public int avaliacoesFeitas;
    public double totalGasto;

    public RelatorioPassageiroOutput(String nome, int totalViagens, int avaliacoesFeitas, double totalGasto) {
        this.nome = nome;
        this.totalViagens = totalViagens;
        this.avaliacoesFeitas = avaliacoesFeitas;
        this.totalGasto = totalGasto;
    }
}
