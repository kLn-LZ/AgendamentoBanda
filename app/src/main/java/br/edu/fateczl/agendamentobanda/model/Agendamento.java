package br.edu.fateczl.agendamentobanda.model;

import androidx.annotation.NonNull;

import java.sql.Time;

public abstract class Agendamento {

    protected int id;
    protected String nome;
    protected String cor;
    protected long data;
    protected Time hora;
    protected Local local;

    public Agendamento(){
        super();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getData() {
        return data;
    }

    public void setData(long data) {
        this.data = data;
    }

    public Local getLocal() {
        return local;
    }

    public void setLocal(Local local) {
        this.local = local;
    }

    public Time getHora() {
        return hora;
    }

    public void setHora(Time hora) {
        this.hora = hora;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCor() {
        return cor;
    }

    public void setCor(String cor) {
        this.cor = cor;
    }

    @NonNull
    @Override
    public String toString() {
        return "Código: " + id + " - " + "Nome: " + nome + " - " + " Data: " + String.valueOf(new java.util.Date(data)) +
                " - " + " Hora: " + String.valueOf(hora) + " - " + "Local: " + local.getNome() + " - " + "Cor: " + cor;
    }
}
