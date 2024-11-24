package br.edu.fateczl.agendamentobanda.persistence;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;
import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import br.edu.fateczl.agendamentobanda.model.Banda;
import br.edu.fateczl.agendamentobanda.model.Ensaio;
import br.edu.fateczl.agendamentobanda.model.Local;

public class EnsaioDao implements IEnsaioDao, ICRUDDao<Ensaio> {

    private final Context context;
    private GenericDao gDao;
    private SQLiteDatabase database;

    public EnsaioDao(Context context) {
        this.context = context;
    }

    @Override
    public EnsaioDao open() throws SQLException {
        gDao = new GenericDao(context);
        database = gDao.getWritableDatabase();
        return this;
    }

    @Override
    public void close() throws SQLException {
        gDao.close();
    }

    @Override
    public void insert(Ensaio ensaio) throws SQLException {
        ContentValues contentValues = getContentValues(ensaio);
        database.insertOrThrow("agendamento", null, contentValues);
    }

    @Override
    public int update(Ensaio ensaio) throws SQLException {
        ContentValues contentValues = getContentValues(ensaio);
        int res = database.update("agendamento", contentValues, "id = " +
                ensaio.getId(), null);

        return res;
    }

    @Override
    public int delete(Ensaio ensaio) throws SQLException {
        int ret = database.delete("agendamento", "id = " +
                ensaio.getId(), null);
        return ret;
    }

    @SuppressLint("Range")
    @Override
    public Ensaio findOne(Ensaio ensaio) throws SQLException {
        String sql = "SELECT " +
                "agendamento.id as idAgendamento, " +
                "agendamento.nome as nomeAgendamento, " +
                "agendamento.tipo as tipoAgendamento, " +
                "agendamento.data as dataAgendamento, " +
                "agendamento.hora as horaAgendamento, " +
                "agendamento.cor as corAgendamento, " +
                "local.id as idLocal, " +
                "local.nome as nomeLocal, " +
                "local.endereco as enderecoLocal, " +
                "local.descricao as descricaoLocal, " +
                "banda.Codigo as CodigoBanda, " +
                "banda.nome as nomeBanda " +
                "FROM agendamento " +
                "   INNER JOIN local " +
                "       ON local.id = agendamento.local_id " +
                "   LEFT JOIN banda " +
                "       ON banda.Codigo = agendamento.banda_id " +
                "WHERE agendamento.data = " + ensaio.getData() + " AND agendamento.id = " + ensaio.getId();
        Cursor cursor = database.rawQuery(sql, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        if (!cursor.isAfterLast()) {
            Banda banda = new Banda();
            banda.setCodigo(cursor.getInt(cursor.getColumnIndex("CodigoBanda")));
            banda.setNome(cursor.getString(cursor.getColumnIndex("nomeBanda")));


            Local local = new Local();
            local.setId(cursor.getInt(cursor.getColumnIndex("idLocal")));
            local.setNome(cursor.getString(cursor.getColumnIndex("nomeLocal")));
            local.setEndereco(cursor.getString(cursor.getColumnIndex("enderecoLocal")));
            local.setDescricao(cursor.getString(cursor.getColumnIndex("descricaoLocal")));

            ensaio.setId(cursor.getInt(cursor.getColumnIndex("idAgendamento")));
            ensaio.setNome(cursor.getString(cursor.getColumnIndex("nomeAgendamento")));
            ensaio.setCor(cursor.getString(cursor.getColumnIndex("corAgendamento")));
            ensaio.setData(cursor.getLong(cursor.getColumnIndex("dataAgendamento")));
            ensaio.setHora(Time.valueOf(cursor.getString(cursor.getColumnIndex("horaAgendamento"))));
            ensaio.setBanda(banda);
            ensaio.setLocal(local);
        }
        cursor.close();
        return ensaio;
    }

    @SuppressLint("Range")
    @Override
    public List<Ensaio> findAll() throws SQLException {
        List<Ensaio> ensaios = new ArrayList<>();

        String sql = "SELECT " +
                "agendamento.id as idAgendamento, " +
                "agendamento.nome as nomeAgendamento, " +
                "agendamento.tipo as tipoAgendamento, " +
                "agendamento.data as dataAgendamento, " +
                "agendamento.hora as horaAgendamento, " +
                "agendamento.cor as corAgendamento, " +
                "local.id as idLocal, " +
                "local.nome as nomeLocal, " +
                "local.endereco as enderecoLocal, " +
                "local.descricao as descricaoLocal, " +
                "banda.Codigo as CodigoBanda, " +
                "banda.nome as nomeBanda " +
                "FROM agendamento " +
                "   INNER JOIN local " +
                "       ON local.id = agendamento.local_id " +
                "   LEFT JOIN banda " +
                "       ON banda.Codigo = agendamento.banda_id " +
                "ORDER BY agendamento.id ASC;";
        Cursor cursor = database.rawQuery(sql, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        while (!cursor.isAfterLast()) {
            Ensaio ensaio = new Ensaio();

            Banda banda = new Banda();
            banda.setCodigo(cursor.getInt(cursor.getColumnIndex("CodigoBanda")));
            banda.setNome(cursor.getString(cursor.getColumnIndex("nomeBanda")));

            Local local = new Local();
            local.setId(cursor.getInt(cursor.getColumnIndex("idLocal")));
            local.setNome(cursor.getString(cursor.getColumnIndex("nomeLocal")));
            local.setEndereco(cursor.getString(cursor.getColumnIndex("enderecoLocal")));
            local.setDescricao(cursor.getString(cursor.getColumnIndex("descricaoLocal")));

            ensaio.setId(cursor.getInt(cursor.getColumnIndex("idAgendamento")));
            ensaio.setNome(cursor.getString(cursor.getColumnIndex("nomeAgendamento")));
            ensaio.setCor(cursor.getString(cursor.getColumnIndex("corAgendamento")));
            ensaio.setData(cursor.getLong(cursor.getColumnIndex("dataAgendamento")));
            ensaio.setHora(Time.valueOf(cursor.getString(cursor.getColumnIndex("horaAgendamento"))));
            ensaio.setBanda(banda);
            ensaio.setLocal(local);

            ensaios.add(ensaio);
            cursor.moveToNext();
        }
        cursor.close();
        return ensaios;
    }

    private ContentValues getContentValues(Ensaio ensaio) {
        ContentValues contentValues = new ContentValues();

        DateFormat format = new SimpleDateFormat("HH:mm:ss");
        String hora = format.format(ensaio.getHora());

        contentValues.put("id", ensaio.getId());
        contentValues.put("tipo", "ensaio");
        contentValues.put("nome", ensaio.getNome());
        contentValues.put("cor", ensaio.getCor());
        contentValues.put("data", ensaio.getData());
        contentValues.put("hora", hora);
        contentValues.put("local_id", ensaio.getLocal().getId());
        contentValues.put("banda_id", ensaio.getBanda().getCodigo());

        return contentValues;
    }

}
