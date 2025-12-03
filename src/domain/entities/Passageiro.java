package domain.entities;

public class Passageiro {
    private int id;
    private int numeroDeViagens;

    public Passageiro() {
        this.numeroDeViagens = 0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
}
