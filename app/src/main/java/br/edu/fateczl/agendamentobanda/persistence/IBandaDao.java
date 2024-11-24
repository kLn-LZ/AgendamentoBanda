package br.edu.fateczl.agendamentobanda.persistence;

import java.sql.SQLException;

public interface IBandaDao {
    public BandaDao open() throws SQLException;
    public void close() throws SQLException;
}
