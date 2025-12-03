package application;

public class Resposta<T> {
    private boolean sucesso;
    private String mensagem;
    private T dados;

    private Resposta(boolean sucesso, String mensagem, T dados) {
        this.sucesso = sucesso;
        this.mensagem = mensagem;
        this.dados = dados;
    }

    public static <T> Resposta<T> sucesso(String mensagem, T dados) {
        return new Resposta<>(true, mensagem, dados);
    }

    public static <T> Resposta<T> sucesso(String mensagem) {
        return new Resposta<>(true, mensagem, null);
    }

    public static <T> Resposta<T> erro(String mensagem) {
        return new Resposta<>(false, mensagem, null);
    }

    public boolean ok() { return sucesso; }
    public String getMensagem() { return mensagem; }
    public T getDados() { return dados; }
}
