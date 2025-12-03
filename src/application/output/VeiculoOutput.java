package application.output;

public class VeiculoOutput {
    private int id;
    private String marca;
    private String modelo;
    private String placa;
    private int ano;
    private String cor;

    public VeiculoOutput(int id, String marca, String modelo, String placa, int ano, String cor) {
        this.id = id;
        this.marca = marca;
        this.modelo = modelo;
        this.placa = placa;
        this.ano = ano;
        this.cor = cor;
    }

    public int getId() { return id; }
    public String getMarca() { return marca; }
    public String getModelo() { return modelo; }
    public String getPlaca() { return placa; }
    public int getAno() { return ano; }
    public String getCor() { return cor; }

    public String getDescricao() {
        return marca + " " + modelo + " (" + ano + ") - " + placa;
    }
}
