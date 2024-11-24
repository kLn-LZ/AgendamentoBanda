package br.edu.fateczl.agendamentobanda.persistence;

import java.sql.SQLException;

public interface IEnsaioDao {
    public EnsaioDao open() throws SQLException;
    public void close() throws SQLException;
}
