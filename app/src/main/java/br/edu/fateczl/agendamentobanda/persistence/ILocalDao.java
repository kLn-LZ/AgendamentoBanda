package br.edu.fateczl.agendamentobanda.persistence;

import java.sql.SQLException;

public interface ILocalDao {
        public LocalDao open() throws SQLException;
        public void close() throws SQLException;
}
