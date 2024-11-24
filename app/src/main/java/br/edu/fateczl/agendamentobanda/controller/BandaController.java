package br.edu.fateczl.agendamentobanda.controller;

import android.database.SQLException;
import android.database.sqlite.SQLiteException;

import java.util.List;

import br.edu.fateczl.agendamentobanda.model.Banda;
import br.edu.fateczl.agendamentobanda.persistence.BandaDao;

public class BandaController implements IController<Banda>{
    private final BandaDao bDao;

    public BandaController(BandaDao bDao) {
        this.bDao = bDao;
    }

    @Override
    public void inserir(Banda banda) throws SQLiteException {
        if (bDao.open() == null) {
            bDao.open();
        }
        bDao.insert(banda);
    }

    @Override
    public void modificar(Banda banda) throws SQLiteException {
        if (bDao.open() == null) {
            bDao.open();
        }
        bDao.update(banda);
    }

    @Override
    public int deletar(Banda banda) throws SQLiteException {
        if (bDao.open() == null) {
            bDao.open();
        }
        return bDao.delete(banda);
    }

    @Override
    public Banda buscar(Banda banda) throws SQLiteException {
        if (bDao.open() == null) {
            bDao.open();
        }
        return bDao.findOne(banda);
    }

    @Override
    public List<Banda> listar() throws SQLiteException {
        if (bDao.open() == null) {
            bDao.open();
        }
        return bDao.findAll();
    }
}
