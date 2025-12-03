package domain.entities;

import domain.enums.StatusViagem;

public class Viagem {
    private int id;
    private Veiculo veiculo;
    private int motoristaId;
    private String nomeMotorista;
    private String cidadeOrigem;
    private String cidadeDestino;
    private double preco;
    private String dataViagem;
    private int vagas;
    private StatusViagem status;
    private double avaliacao;
    private double somaNotas;
    private int totalAvaliacoes;

    public Viagem() {
        this.status = StatusViagem.PENDENTE;
        this.avaliacao = 0;
        this.somaNotas = 0;
        this.totalAvaliacoes = 0;
    }

    public Viagem(Veiculo veiculo, int motoristaId, String cidadeOrigem, String cidadeDestino, 
                  double preco, String dataViagem, int vagas) {
        this();
        this.veiculo = veiculo;
        this.motoristaId = motoristaId;
        this.cidadeOrigem = cidadeOrigem;
        this.cidadeDestino = cidadeDestino;
        this.preco = preco;
        this.dataViagem = dataViagem;
        this.vagas = vagas;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Veiculo getVeiculo() {
        return veiculo;
    }

    public void setVeiculo(Veiculo veiculo) {
        this.veiculo = veiculo;
    }

    public int getMotoristaId() {
        return motoristaId;
    }

    public void setMotoristaId(int motoristaId) {
        this.motoristaId = motoristaId;
    }

    public String getNomeMotorista() {
        return nomeMotorista;
    }

    public void setNomeMotorista(String nomeMotorista) {
        this.nomeMotorista = nomeMotorista;
    }

    public String getCidadeOrigem() {
        return cidadeOrigem;
    }

    public void setCidadeOrigem(String cidadeOrigem) {
        this.cidadeOrigem = cidadeOrigem;
    }

    public String getCidadeDestino() {
        return cidadeDestino;
    }

    public void setCidadeDestino(String cidadeDestino) {
        this.cidadeDestino = cidadeDestino;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public String getDataViagem() {
        return dataViagem;
    }

    public void setDataViagem(String dataViagem) {
        this.dataViagem = dataViagem;
    }

    public int getVagas() {
        return vagas;
    }

    public void setVagas(int vagas) {
        this.vagas = vagas;
    }

    public StatusViagem getStatus() {
        return status;
    }

    public void setStatus(StatusViagem status) {
        this.status = status;
    }

    public double getAvaliacao() {
        return avaliacao;
    }

    public void setAvaliacao(double avaliacao) {
        this.avaliacao = avaliacao;
    }

    public double getSomaNotas() {
        return somaNotas;
    }

    public void setSomaNotas(double somaNotas) {
        this.somaNotas = somaNotas;
    }

    public int getTotalAvaliacoes() {
        return totalAvaliacoes;
    }

    public void setTotalAvaliacoes(int totalAvaliacoes) {
        this.totalAvaliacoes = totalAvaliacoes;
    }

    public void avaliar(double nota) {
        if (nota < 0 || nota > 5) {
            throw new IllegalArgumentException("Avaliacao deve ser entre 0 e 5");
        }
        this.somaNotas += nota;
        this.totalAvaliacoes++;
        this.avaliacao = this.somaNotas / this.totalAvaliacoes;
    }

    public void marcarComoCheia() {
        this.status = StatusViagem.CHEIA;
    }

    public void cancelar() {
        this.status = StatusViagem.CANCELADA;
    }

    public void iniciar() {
        this.status = StatusViagem.INICIADA;
    }

    public void concluir() {
        this.status = StatusViagem.CONCLUIDA;
    }
}
