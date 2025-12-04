package application.input;

public class CriarViagemInput {

    public int veiculoId;
    public int motoristaId;
    public String cidadeOrigem;
    public String cidadeDestino;
    public double preco;
    public String dataViagem;
    public int vagas;

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
}
