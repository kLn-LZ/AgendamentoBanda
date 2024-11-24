package br.edu.fateczl.agendamentobanda.model;

import androidx.annotation.NonNull;

public class Banda {
    private int codigo;
    private String nome;

    public Banda() {
        super();
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @NonNull
    @Override
    public String toString() {
        return "Código da Banda: " + codigo + " - " + "Nome da Banda: " + nome;
    }
}
