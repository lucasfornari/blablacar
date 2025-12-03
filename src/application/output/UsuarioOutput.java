package application.output;

public class UsuarioOutput {
    private int id;
    private String nome;
    private String email;
    private String telefone;
    private String endereco;
    private boolean ehMotorista;
    private boolean ehPassageiro;
    private int motoristaId;
    private int passageiroId;

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

    public int getId() { return id; }
    public String getNome() { return nome; }
    public String getEmail() { return email; }
    public String getTelefone() { return telefone; }
    public String getEndereco() { return endereco; }
    public boolean isEhMotorista() { return ehMotorista; }
    public boolean isEhPassageiro() { return ehPassageiro; }
    public int getMotoristaId() { return motoristaId; }
    public int getPassageiroId() { return passageiroId; }

    public void setEhMotorista(boolean ehMotorista) { this.ehMotorista = ehMotorista; }
    public void setEhPassageiro(boolean ehPassageiro) { this.ehPassageiro = ehPassageiro; }
    public void setMotoristaId(int motoristaId) { this.motoristaId = motoristaId; }
    public void setPassageiroId(int passageiroId) { this.passageiroId = passageiroId; }
}
