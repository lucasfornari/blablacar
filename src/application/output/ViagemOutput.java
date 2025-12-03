package application.output;

public class ViagemOutput {
    private int id;
    private int veiculoId;
    private String nomeMotorista;
    private String cidadeOrigem;
    private String cidadeDestino;
    private double preco;
    private String dataViagem;
    private int vagas;
    private int vagasDisponiveis;
    private String status;
    private double avaliacao;

    public ViagemOutput(int id, int veiculoId, String nomeMotorista, String cidadeOrigem, String cidadeDestino,
                        double preco, String dataViagem, int vagas, int vagasDisponiveis, String status, double avaliacao) {
        this.id = id;
        this.veiculoId = veiculoId;
        this.nomeMotorista = nomeMotorista;
        this.cidadeOrigem = cidadeOrigem;
        this.cidadeDestino = cidadeDestino;
        this.preco = preco;
        this.dataViagem = dataViagem;
        this.vagas = vagas;
        this.vagasDisponiveis = vagasDisponiveis;
        this.status = status;
        this.avaliacao = avaliacao;
    }

    public int getId() { return id; }
    public int getVeiculoId() { return veiculoId; }
    public String getNomeMotorista() { return nomeMotorista; }
    public String getCidadeOrigem() { return cidadeOrigem; }
    public String getCidadeDestino() { return cidadeDestino; }
    public double getPreco() { return preco; }
    public String getDataViagem() { return dataViagem; }
    public int getVagas() { return vagas; }
    public int getVagasDisponiveis() { return vagasDisponiveis; }
    public String getStatus() { return status; }
    public double getAvaliacao() { return avaliacao; }
}
