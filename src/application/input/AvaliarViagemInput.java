package application.input;

public class AvaliarViagemInput {
    public int viagemId;
    public int passageiroId;
    public int nota;

    public AvaliarViagemInput(int viagemId, int passageiroId, int nota) {
        this.viagemId = viagemId;
        this.passageiroId = passageiroId;
        this.nota = nota;
    }
}
