package application.input;

public class ReservarViagemInput {

    public int viagemId;
    public int passageiroId;
    public int numeroLugares;

    public ReservarViagemInput(int viagemId, int passageiroId, int numeroLugares) {
        this.viagemId = viagemId;
        this.passageiroId = passageiroId;
        this.numeroLugares = numeroLugares;
    }
}
