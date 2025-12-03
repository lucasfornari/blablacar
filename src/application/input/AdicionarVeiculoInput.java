package application.input;

public class AdicionarVeiculoInput {
    private int motoristaId;
    private String marca;
    private String modelo;
    private String placa;
    private int ano;
    private String cor;

    public AdicionarVeiculoInput(int motoristaId, String marca, String modelo, String placa, int ano, String cor) {
        this.motoristaId = motoristaId;
        this.marca = marca;
        this.modelo = modelo;
        this.placa = placa;
        this.ano = ano;
        this.cor = cor;
    }

    public int getMotoristaId() { return motoristaId; }
    public String getMarca() { return marca; }
    public String getModelo() { return modelo; }
    public String getPlaca() { return placa; }
    public int getAno() { return ano; }
    public String getCor() { return cor; }

    public boolean isValido() {
        return motoristaId > 0 &&
               marca != null && !marca.trim().isEmpty() &&
               modelo != null && !modelo.trim().isEmpty() &&
               placa != null && !placa.trim().isEmpty() &&
               ano >= 1900;
    }
}
