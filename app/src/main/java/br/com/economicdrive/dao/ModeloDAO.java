package br.com.economicdrive.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import br.com.economicdrive.constantes.Constantes;

/**
 * Created by Flavio on 12/09/2017.
 */

public class ModeloDAO extends SQLiteOpenHelper{

    public ModeloDAO(Context context) {
        super(context, "EconomicDrive.sqlite", null, Constantes.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE modelo(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nome VARCHAR(100), "+
                "idMarca INTEGER)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS modelo";
        db.execSQL(sql);
        onCreate(db);
    }
}
