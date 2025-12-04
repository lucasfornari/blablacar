package application.output;

public class RelatorioMotoristaOutput {
    public String nome;
    public int totalViagens;
    public int viagensFinalizadas;
    public int viagensCanceladas;
    public double mediaAvaliacao;
    public double totalFaturado;

    public RelatorioMotoristaOutput(String nome, int totalViagens, int viagensFinalizadas, 
                                     int viagensCanceladas, double mediaAvaliacao, double totalFaturado) {
        this.nome = nome;
        this.totalViagens = totalViagens;
        this.viagensFinalizadas = viagensFinalizadas;
        this.viagensCanceladas = viagensCanceladas;
        this.mediaAvaliacao = mediaAvaliacao;
        this.totalFaturado = totalFaturado;
    }
}
