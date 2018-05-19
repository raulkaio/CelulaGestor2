package br.com.raulkaio.celulagestor.Classes;

/**
 * Created by raulk on 08/05/2018.
 */

public class Celula {
    protected String email, nome, frequencia, horario, observacoes;
    protected Pessoa[] pessoa;

    public Celula() {
    }

    public Celula(String email, String nome, String frequencia, String horario, String observacoes) {
        this.email = email;
        this.nome = nome;
        this.frequencia = frequencia;
        this.horario = horario;
        this.observacoes = observacoes;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getFrequencia() {
        return frequencia;
    }

    public void setFrequencia(String frequencia) {
        this.frequencia = frequencia;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }
}
