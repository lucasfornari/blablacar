package application.input;

public class CriarViagemInput {
    private int veiculoId;
    private int motoristaId;
    private String cidadeOrigem;
    private String cidadeDestino;
    private double preco;
    private String dataViagem;
    private int vagas;

    public CriarViagemInput(int veiculoId, int motoristaId, String cidadeOrigem, String cidadeDestino, 
                            double preco, String dataViagem, int vagas) {
        this.veiculoId = veiculoId;
        this.motoristaId = motoristaId;
        this.cidadeOrigem = cidadeOrigem;
        this.cidadeDestino = cidadeDestino;
        this.preco = preco;
        this.dataViagem = dataViagem;
        this.vagas = vagas;
    }

    public int getVeiculoId() { return veiculoId; }
    public int getMotoristaId() { return motoristaId; }
    public String getCidadeOrigem() { return cidadeOrigem; }
    public String getCidadeDestino() { return cidadeDestino; }
    public double getPreco() { return preco; }
    public String getDataViagem() { return dataViagem; }
    public int getVagas() { return vagas; }

    public boolean isValido() {
        return veiculoId > 0 && motoristaId > 0 &&
               cidadeOrigem != null && !cidadeOrigem.trim().isEmpty() &&
               cidadeDestino != null && !cidadeDestino.trim().isEmpty() &&
               dataViagem != null && !dataViagem.trim().isEmpty() &&
               preco >= 0 && vagas > 0;
    }
}
