package domain.entities;

import domain.enums.StatusParticipacao;

public class PassageiroViagem {
    private int id;
    private int passageiroId;
    private String nomePassageiro;
    private int numeroDeLugares;
    private StatusParticipacao status;

    public PassageiroViagem() {
        this.status = StatusParticipacao.RESERVADO;
    }

    public PassageiroViagem(int passageiroId, int numeroDeLugares) {
        this();
        this.passageiroId = passageiroId;
        this.numeroDeLugares = numeroDeLugares;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPassageiroId() {
        return passageiroId;
    }

    public void setPassageiroId(int passageiroId) {
        this.passageiroId = passageiroId;
    }

    public String getNomePassageiro() {
        return nomePassageiro;
    }

    public void setNomePassageiro(String nomePassageiro) {
        this.nomePassageiro = nomePassageiro;
    }

    public int getNumeroDeLugares() {
        return numeroDeLugares;
    }

    public void setNumeroDeLugares(int numeroDeLugares) {
        if (numeroDeLugares <= 0) {
            throw new IllegalArgumentException("O numero de lugares deve ser maior que zero");
        }
        this.numeroDeLugares = numeroDeLugares;
    }

    public StatusParticipacao getStatus() {
        return status;
    }

    public void setStatus(StatusParticipacao status) {
        this.status = status;
    }

    public void cancelar() {
        this.status = StatusParticipacao.CANCELADO;
    }

    public void concluir() {
        this.status = StatusParticipacao.CONCLUIDO;
    }
}
