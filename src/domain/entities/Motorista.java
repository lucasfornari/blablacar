package domain.entities;

import java.util.ArrayList;
import java.util.List;

public class Motorista {
    private int id;
    private String cnh;
    private int numeroDeViagens;
    private List<Veiculo> veiculos;

    public Motorista() {
        this.veiculos = new ArrayList<>();
        this.numeroDeViagens = 0;
    }

    public Motorista(String cnh) {
        this();
        this.cnh = cnh;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCnh() {
        return cnh;
    }

    public void setCnh(String cnh) {
        this.cnh = cnh;
    }

    public int getNumeroDeViagens() {
        return numeroDeViagens;
    }

    public void setNumeroDeViagens(int numeroDeViagens) {
        this.numeroDeViagens = numeroDeViagens;
    }

    public void incrementarViagens() {
        this.numeroDeViagens++;
    }

    public List<Veiculo> getVeiculos() {
        return veiculos;
    }

    public void setVeiculos(List<Veiculo> veiculos) {
        this.veiculos = veiculos;
    }

    public void adicionarVeiculo(Veiculo veiculo) {
        this.veiculos.add(veiculo);
    }
}
