package br.edu.fateczl.agendamentobanda.controller;

import java.sql.SQLException;
import java.util.List;

public interface IController<T> {
    public void inserir(T t) throws SQLException;
    public void modificar(T t) throws SQLException;
    public int deletar(T t) throws SQLException;
    public T buscar(T t) throws SQLException;
    public List<T> listar() throws SQLException;
}
