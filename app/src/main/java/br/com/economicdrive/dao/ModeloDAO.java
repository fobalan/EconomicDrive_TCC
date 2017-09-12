package br.com.economicdrive.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Flavio on 12/09/2017.
 */

public class ModeloDAO extends SQLiteOpenHelper{
    private static final int DATABASE_VERSION = 1;

    public ModeloDAO(Context context) {
        super(context, "EconomicDrive.sqlite", null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE modelo(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nome VARCHAR(100), "+
                "idMarca INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS modelo";
        db.execSQL(sql);
        onCreate(db);
    }
}
