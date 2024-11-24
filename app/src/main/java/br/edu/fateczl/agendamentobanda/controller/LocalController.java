package br.edu.fateczl.agendamentobanda.controller;

import java.sql.SQLException;
import java.util.List;

import br.edu.fateczl.agendamentobanda.model.Local;
import br.edu.fateczl.agendamentobanda.persistence.LocalDao;

public class LocalController implements IController<Local>{

    private final LocalDao lDao;

    public LocalController(LocalDao lDao) {
        this.lDao = lDao;
    }

    @Override
    public void inserir(Local local) throws SQLException {
        if (lDao.open() == null) {
            lDao.open();
        }
        lDao.insert(local);
    }

    @Override
    public void modificar(Local local) throws SQLException {
        if (lDao.open() == null) {
            lDao.open();
        }
        lDao.update(local);
    }

    @Override
    public int deletar(Local local) throws SQLException {
        if (lDao.open() == null) {
            lDao.open();
        }
        return lDao.delete(local);
    }

    @Override
    public Local buscar(Local local) throws SQLException {
        if (lDao.open() == null) {
            lDao.open();
        }
        return lDao.findOne(local);
    }

    @Override
    public List<Local> listar() throws SQLException {
        if (lDao.open() == null) {
            lDao.open();
        }
        return lDao.findAll();
    }
}
