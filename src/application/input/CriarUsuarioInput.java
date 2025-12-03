package application.input;

public class CriarUsuarioInput {
    private String nome;
    private String email;
    private String senha;
    private String telefone;
    private String endereco;

    public CriarUsuarioInput(String nome, String email, String senha, String telefone, String endereco) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.telefone = telefone;
        this.endereco = endereco;
    }

    public String getNome() { return nome; }
    public String getEmail() { return email; }
    public String getSenha() { return senha; }
    public String getTelefone() { return telefone; }
    public String getEndereco() { return endereco; }

    public boolean isValido() {
        return nome != null && !nome.trim().isEmpty() &&
               email != null && !email.trim().isEmpty() &&
               senha != null && senha.length() >= 6;
    }
}
