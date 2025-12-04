package application.input;

public class AdicionarVeiculoInput {

    public int motoristaId;
    public String marca;
    public String modelo;
    public String placa;
    public int ano;
    public String cor;

    public AdicionarVeiculoInput(int motoristaId, String marca, String modelo, String placa, int ano, String cor) {
        this.motoristaId = motoristaId;
        this.marca = marca;
        this.modelo = modelo;
        this.placa = placa;
        this.ano = ano;
        this.cor = cor;
    }
}
