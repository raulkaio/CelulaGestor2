package br.com.raulkaio.celulagestor.Classes;

/**
 * Created by raulk on 08/05/2018.
 */

public class Pessoa {
    protected String email, nome, genero, classificacao, encontro, batismo, dataDeNascimento, observacoes, keyEmailEncontro, keyEmailBatismo;

    public Pessoa() {
    }

    public Pessoa(String email, String nome, String genero, String classificacao, String encontro, String batismo, String dataDeNascimento, String observacoes, String keyEmailEncontro, String keyEmailBatismo) {
        this.email = email;
        this.nome = nome;
        this.genero = genero;
        this.classificacao = classificacao;
        this.encontro = encontro;
        this.batismo = batismo;
        this.dataDeNascimento = dataDeNascimento;
        this.observacoes = observacoes;
        this.keyEmailEncontro = keyEmailEncontro;
        this.keyEmailBatismo = keyEmailBatismo;
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

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getClassificacao() {
        return classificacao;
    }

    public void setClassificacao(String classificacao) {
        this.classificacao = classificacao;
    }

    public String getEncontro() {
        return encontro;
    }

    public void setEncontro(String encontro) {
        this.encontro = encontro;
    }

    public String getBatismo() {
        return batismo;
    }

    public void setBatismo(String batismo) {
        this.batismo = batismo;
    }

    public String getDataDeNascimento() {
        return dataDeNascimento;
    }

    public void setDataDeNascimento(String dataDeNascimento) {
        this.dataDeNascimento = dataDeNascimento;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public String getKeyEmailEncontro() {
        return keyEmailEncontro;
    }

    public void setKeyEmailEncontro(String keyEmailEncontro) {
        this.keyEmailEncontro = keyEmailEncontro;
    }

    public String getKeyEmailBatismo() {
        return keyEmailBatismo;
    }

    public void setKeyEmailBatismo(String keyEmailBatismo) {
        this.keyEmailBatismo = keyEmailBatismo;
    }
}
