package application.input;

public class LoginInput {
    private String email;
    private String senha;

    public LoginInput(String email, String senha) {
        this.email = email;
        this.senha = senha;
    }

    public String getEmail() { return email; }
    public String getSenha() { return senha; }

    public boolean isValido() {
        return email != null && !email.trim().isEmpty() &&
               senha != null && !senha.trim().isEmpty();
    }
}
