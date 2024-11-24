package br.edu.fateczl.agendamentobanda.persistence;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import java.util.ArrayList;
import java.util.List;

import br.edu.fateczl.agendamentobanda.model.Local;

public class LocalDao implements ILocalDao, ICRUDDao<Local> {

    private final Context context;
    private GenericDao gDao;
    private SQLiteDatabase database;

    public LocalDao (Context context) {
        this.context = context;
    }

    @Override
    public LocalDao open() throws SQLiteException {
        gDao = new GenericDao(context);
        database = gDao.getWritableDatabase();
        return this;
    }

    @Override
    public void close() throws SQLiteException {
        gDao.close();
    }

    @Override
    public void insert(Local local) throws SQLiteException {
        ContentValues contentValues = getContentValues(local);
        database.insertOrThrow("local", null, contentValues);
    }

    @Override
    public int update(Local Local) throws SQLiteException {
        ContentValues contentValues = getContentValues(Local);
        int res = database.update("local", contentValues, "id = " +
                Local.getId(), null);

        return res;
    }

    @Override
    public int delete(Local Local) throws SQLiteException {
        int ret = database.delete("local", "id = " +
                Local.getId(), null);
        return ret;
    }

    @SuppressLint("Range")
    @Override
    public Local findOne(Local Local) throws SQLiteException {
        String sql = "SELECT " +
                " id, " +
                " nome, " +
                " endereco, " +
                " descricao " +
                "FROM local " +
                "WHERE id = " + Local.getId();
        Cursor cursor = database.rawQuery(sql, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        if (!cursor.isAfterLast()) {
            Local.setId(cursor.getInt(cursor.getColumnIndex("id")));
            Local.setNome(cursor.getString(cursor.getColumnIndex("nome")));
            Local.setEndereco(cursor.getString(cursor.getColumnIndex("endereco")));
            Local.setDescricao(cursor.getString(cursor.getColumnIndex("descricao")));
        }
        cursor.close();
        return Local;
    }

    @SuppressLint("Range")
    @Override
    public List<Local> findAll() throws SQLiteException {
        List<Local> Locals = new ArrayList<>();

        String sql = "SELECT " +
                " id, " +
                " nome, " +
                " endereco, " +
                " descricao " +
                "FROM local " +
                "ORDER BY id ASC";
        Cursor cursor = database.rawQuery(sql, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        while (!cursor.isAfterLast()) {
            Local Local = new Local();

            Local.setId(cursor.getInt(cursor.getColumnIndex("id")));
            Local.setNome(cursor.getString(cursor.getColumnIndex("nome")));
            Local.setEndereco(cursor.getString(cursor.getColumnIndex("endereco")));
            Local.setDescricao(cursor.getString(cursor.getColumnIndex("descricao")));

            Locals.add(Local);
            cursor.moveToNext();
        }
        cursor.close();
        return Locals;
    }

    private ContentValues getContentValues(Local Local) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("id", Local.getId());
        contentValues.put("nome", Local.getNome());
        contentValues.put("endereco", Local.getEndereco());
        contentValues.put("descricao", Local.getDescricao());

        return contentValues;
    }
}
