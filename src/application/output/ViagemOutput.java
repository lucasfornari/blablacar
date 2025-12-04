package application.output;

public class ViagemOutput {
    public int id;
    public String nomeMotorista;
    public String cidadeOrigem;
    public String cidadeDestino;
    public double preco;
    public String dataViagem;
    public int vagas;
    public int vagasDisponiveis;
    public String status;
    public int minhaAvaliacao;

    public ViagemOutput(int id, String nomeMotorista, String cidadeOrigem, String cidadeDestino,
                        double preco, String dataViagem, int vagas, int vagasDisponiveis, String status) {
        this.id = id;
        this.nomeMotorista = nomeMotorista;
        this.cidadeOrigem = cidadeOrigem;
        this.cidadeDestino = cidadeDestino;
        this.preco = preco;
        this.dataViagem = dataViagem;
        this.vagas = vagas;
        this.vagasDisponiveis = vagasDisponiveis;
        this.status = status;
        this.minhaAvaliacao = 0;
    }
}
