package application.output;

public class UsuarioOutput {

    public int id;
    public String nome;
    public String email;
    public String telefone;
    public String endereco;
    public boolean ehMotorista;
    public boolean ehPassageiro;
    public int motoristaId;
    public int passageiroId;

    public UsuarioOutput(int id, String nome, String email, String telefone, String endereco,
            boolean ehMotorista, boolean ehPassageiro, int motoristaId, int passageiroId) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
        this.endereco = endereco;
        this.ehMotorista = ehMotorista;
        this.ehPassageiro = ehPassageiro;
        this.motoristaId = motoristaId;
        this.passageiroId = passageiroId;
    }
}
