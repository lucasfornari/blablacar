package application.input;

public class AvaliarViagemInput {
    private int viagemId;
    private double nota;

    public AvaliarViagemInput(int viagemId, double nota) {
        this.viagemId = viagemId;
        this.nota = nota;
    }

    public int getViagemId() { return viagemId; }
    public double getNota() { return nota; }

    public boolean isValido() {
        return viagemId > 0 && nota >= 0 && nota <= 5;
    }
}
