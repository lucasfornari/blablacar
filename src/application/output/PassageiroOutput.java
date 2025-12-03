package application.output;

public class PassageiroOutput {
    private int id;
    private int numeroViagens;

    public PassageiroOutput(int id, int numeroViagens) {
        this.id = id;
        this.numeroViagens = numeroViagens;
    }

    public int getId() { return id; }
    public int getNumeroViagens() { return numeroViagens; }
}
