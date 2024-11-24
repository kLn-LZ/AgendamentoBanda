package br.edu.fateczl.agendamentobanda.model;

import androidx.annotation.NonNull;

public class Ensaio extends Agendamento{

    private Banda banda;

    public Ensaio() {
        super();
    }

    public Banda getBanda() {
        return banda;
    }

    public void setBanda(Banda banda) {
        this.banda = banda;
    }

    @NonNull
    @Override
    public String toString() {
        return super.toString() + " - " + "banda: " + banda.getNome();
    }
}
