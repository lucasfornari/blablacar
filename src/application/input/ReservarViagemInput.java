package application.input;

public class ReservarViagemInput {
    private int viagemId;
    private int passageiroId;
    private int numeroLugares;

    public ReservarViagemInput(int viagemId, int passageiroId, int numeroLugares) {
        this.viagemId = viagemId;
        this.passageiroId = passageiroId;
        this.numeroLugares = numeroLugares;
    }

    public int getViagemId() { return viagemId; }
    public int getPassageiroId() { return passageiroId; }
    public int getNumeroLugares() { return numeroLugares; }

    public boolean isValido() {
        return viagemId > 0 && passageiroId > 0 && numeroLugares > 0;
    }
}
