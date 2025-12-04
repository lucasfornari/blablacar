package application.output;

public class VeiculoOutput {

    public int id;
    public String marca;
    public String modelo;
    public String placa;
    public int ano;
    public String cor;

    public VeiculoOutput(int id, String marca, String modelo, String placa, int ano, String cor) {
        this.id = id;
        this.marca = marca;
        this.modelo = modelo;
        this.placa = placa;
        this.ano = ano;
        this.cor = cor;
    }

    public String getDescricao() {
        return marca + " " + modelo + " (" + ano + ") - " + placa;
    }
}
