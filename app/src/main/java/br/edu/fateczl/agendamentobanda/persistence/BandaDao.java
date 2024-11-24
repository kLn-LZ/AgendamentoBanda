package br.edu.fateczl.agendamentobanda.persistence;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import java.util.ArrayList;
import java.util.List;

import br.edu.fateczl.agendamentobanda.model.Banda;

public class BandaDao implements IBandaDao, ICRUDDao<Banda> {

    private final Context context;
    private GenericDao gDao;
    private SQLiteDatabase database;

    public BandaDao (Context context) {
        this.context = context;
    }

    @Override
    public BandaDao open() throws SQLiteException {
        gDao = new GenericDao(context);
        database = gDao.getWritableDatabase();
        return this;
    }

    @Override
    public void close() throws SQLiteException {
        gDao.close();
    }

    @Override
    public void insert(Banda banda) throws SQLiteException {
        ContentValues contentValues = getContentValues(banda);
        database.insertOrThrow("banda", null, contentValues);
    }

    @Override
    public int update(Banda banda) throws SQLiteException {
        ContentValues contentValues = getContentValues(banda);
        int res = database.update("banda", contentValues, "Codigo = " +
                banda.getCodigo(), null);

        return res;
    }

    @Override
    public int delete(Banda banda) throws SQLiteException {
       int ret = database.delete("banda", "Codigo = " +
                banda.getCodigo(), null);
        return ret;
    }

    @SuppressLint("Range")
    @Override
    public Banda findOne(Banda banda) throws SQLiteException {
        String sql = "SELECT " +
                     " Codigo, " +
                     " nome " +
                     "FROM banda " +
                     "WHERE codigo = " + banda.getCodigo();
        Cursor cursor = database.rawQuery(sql, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        if (!cursor.isAfterLast()) {
            banda.setCodigo(cursor.getInt(cursor.getColumnIndex("Codigo")));
            banda.setNome(cursor.getString(cursor.getColumnIndex("nome")));
        }
        cursor.close();
        return banda;
    }

    @SuppressLint("Range")
    @Override
    public List<Banda> findAll() throws SQLiteException {
        List<Banda> bandas = new ArrayList<>();

        String sql = "SELECT " +
                " Codigo, " +
                " nome " +
                "FROM banda " +
                "ORDER BY Codigo ASC";
        Cursor cursor = database.rawQuery(sql, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        while (!cursor.isAfterLast()) {
            Banda banda = new Banda();

            banda.setCodigo(cursor.getInt(cursor.getColumnIndex("Codigo")));
            banda.setNome(cursor.getString(cursor.getColumnIndex("nome")));

            bandas.add(banda);
            cursor.moveToNext();
        }
        cursor.close();
        return bandas;
    }

    private ContentValues getContentValues(Banda banda) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("Codigo", banda.getCodigo());
        contentValues.put("nome", banda.getNome());

        return contentValues;
    }
}
