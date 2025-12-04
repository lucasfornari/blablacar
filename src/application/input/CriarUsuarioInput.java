package application.input;

public class CriarUsuarioInput {

    public String nome;
    public String email;
    public String senha;
    public String telefone;
    public String endereco;

    public CriarUsuarioInput(String nome, String email, String senha, String telefone, String endereco) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.telefone = telefone;
        this.endereco = endereco;
    }
}
