package domain.entities;

public class Usuario {
    private int id;
    private String nome;
    private String email;
    private String senha;
    private String telefone;
    private String endereco;
    private Motorista motorista;
    private Passageiro passageiro;

    public Usuario() {}

    public Usuario(String nome, String email, String senha, String telefone, String endereco) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.telefone = telefone;
        this.endereco = endereco;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public Motorista getMotorista() {
        return motorista;
    }

    public void setMotorista(Motorista motorista) {
        this.motorista = motorista;
    }

    public Passageiro getPassageiro() {
        return passageiro;
    }

    public void setPassageiro(Passageiro passageiro) {
        this.passageiro = passageiro;
    }

    public boolean ehMotorista() {
        return motorista != null;
    }

    public boolean ehPassageiro() {
        return passageiro != null;
    }

    public void tornarMotorista(String cnh) {
        if (this.motorista == null) {
            this.motorista = new Motorista(cnh);
        }
    }

    public void tornarPassageiro() {
        if (this.passageiro == null) {
            this.passageiro = new Passageiro();
        }
    }
}
