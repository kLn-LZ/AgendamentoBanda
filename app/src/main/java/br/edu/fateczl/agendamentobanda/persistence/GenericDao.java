package br.edu.fateczl.agendamentobanda.persistence;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class GenericDao extends SQLiteOpenHelper {

    private static final String DATABASE = "MEETING_SCHEDULER.DB";
    private static final int DATABASE_VER = 4;
    private static final String CREATE_TABLE_BANDA =
                    "CREATE TABLE banda (" +
                    "Codigo INT NOT NULL PRIMARY KEY, " +
                    "nome VARCHAR(50) NOT NULL);";

    private static final String CREATE_TABLE_LOCAL =
                    "CREATE TABLE local (" +
                    "id INT NOT NULL PRIMARY KEY, " +
                    "nome VARCHAR(50) NOT NULL," +
                    "endereco VARCHAR(512) NOT NULL," +
                    "descricao VARCHAR(512));";

    private static final String CREATE_TABLE_AGENDAMENTO =
                    "CREATE TABLE agendamento (" +
                    "id INT NOT NULL PRIMARY KEY, " +
                    "tipo VARCHAR(50) NOT NULL, " +
                    "nome VARCHAR(250) NOT NULL, " +
                    "data INTEGER , " +
                    "hora varchar(9) , " +
                    "cor varchar(7) , " +
                    "local_id INTEGER, " +
                    "banda_id INTEGER, " +
                    "FOREIGN KEY(locaL_id) REFERENCES local(id), " +
                    "FOREIGN KEY(banda_id) REFERENCES banda(Codigo)); ";
    public GenericDao (Context context) {
        super(context, DATABASE, null, DATABASE_VER);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE_BANDA);
        sqLiteDatabase.execSQL(CREATE_TABLE_LOCAL);
        sqLiteDatabase.execSQL(CREATE_TABLE_AGENDAMENTO);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int antigaVersao, int novaVersao) {
        if(novaVersao != antigaVersao) {
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS banda");
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS local");
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS agendamento");
            onCreate(sqLiteDatabase);
        }
    }
}
