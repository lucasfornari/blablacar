package application.input;

public class BuscarViagensInput {

    public String cidadeOrigem;
    public String cidadeDestino;
    public String dataViagem;

    public BuscarViagensInput(String cidadeOrigem, String cidadeDestino, String dataViagem) {
        this.cidadeOrigem = cidadeOrigem;
        this.cidadeDestino = cidadeDestino;
        this.dataViagem = dataViagem;
    }
}
