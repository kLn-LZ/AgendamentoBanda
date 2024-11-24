package br.edu.fateczl.agendamentobanda.controller;

import java.sql.SQLException;
import java.util.List;

import br.edu.fateczl.agendamentobanda.model.Ensaio;
import br.edu.fateczl.agendamentobanda.persistence.EnsaioDao;

public class EnsaioController implements IController<Ensaio>{

    private final EnsaioDao eDao;

    public EnsaioController(EnsaioDao eDao) {
        this.eDao = eDao;
    }

    @Override
    public void inserir(Ensaio ensaio) throws SQLException {
        if (eDao.open() == null) {
            eDao.open();
        }
        eDao.insert(ensaio);
    }

    @Override
    public void modificar(Ensaio ensaio) throws SQLException {
        if (eDao.open() == null) {
            eDao.open();
        }
        eDao.update(ensaio);
    }

    @Override
    public int deletar(Ensaio ensaio) throws SQLException {
        if (eDao.open() == null) {
            eDao.open();
        }
        return eDao.delete(ensaio);
    }

    @Override
    public Ensaio buscar(Ensaio ensaio) throws SQLException {
        if (eDao.open() == null) {
            eDao.open();
        }
        return eDao.findOne(ensaio);
    }

    @Override
    public List<Ensaio> listar() throws SQLException {
        if (eDao.open() == null) {
            eDao.open();
        }
        return eDao.findAll();
    }

}
