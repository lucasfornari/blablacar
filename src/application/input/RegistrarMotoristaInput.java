package application.input;

public class RegistrarMotoristaInput {
    private int usuarioId;
    private String cnh;

    public RegistrarMotoristaInput(int usuarioId, String cnh) {
        this.usuarioId = usuarioId;
        this.cnh = cnh;
    }

    public int getUsuarioId() { return usuarioId; }
    public String getCnh() { return cnh; }

    public boolean isValido() {
        return usuarioId > 0 && cnh != null && !cnh.trim().isEmpty();
    }
}
