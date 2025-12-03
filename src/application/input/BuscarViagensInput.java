package application.input;

public class BuscarViagensInput {
    private String cidadeOrigem;
    private String cidadeDestino;
    private String dataViagem;

    public BuscarViagensInput(String cidadeOrigem, String cidadeDestino, String dataViagem) {
        this.cidadeOrigem = cidadeOrigem;
        this.cidadeDestino = cidadeDestino;
        this.dataViagem = dataViagem;
    }

    public String getCidadeOrigem() { return cidadeOrigem; }
    public String getCidadeDestino() { return cidadeDestino; }
    public String getDataViagem() { return dataViagem; }

    public boolean isValido() {
        return cidadeOrigem != null && !cidadeOrigem.trim().isEmpty() &&
               cidadeDestino != null && !cidadeDestino.trim().isEmpty();
    }

    public boolean temData() {
        return dataViagem != null && !dataViagem.trim().isEmpty();
    }
}
