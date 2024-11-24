package br.edu.fateczl.agendamentobanda.model;

import androidx.annotation.NonNull;

public class Local {
    private int id;
    private String nome;
    private String endereco;
    private String descricao;

    public Local() {
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

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @NonNull
    @Override
    public String toString() {
        return "Código do Local: " + id + " - " + "Nome do Local: " + nome + " - " + "Endereço do Local:" +
                endereco + " - " + "Descrição do Local: " + descricao;
    }
}
