package application.output;

public class MotoristaOutput {
    private int id;
    private String cnh;
    private int numeroViagens;

    public MotoristaOutput(int id, String cnh, int numeroViagens) {
        this.id = id;
        this.cnh = cnh;
        this.numeroViagens = numeroViagens;
    }

    public int getId() { return id; }
    public String getCnh() { return cnh; }
    public int getNumeroViagens() { return numeroViagens; }
}
