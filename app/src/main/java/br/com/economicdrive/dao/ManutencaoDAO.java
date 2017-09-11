package br.com.economicdrive.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by ITST on 11/09/2017.
 */

public class ManutencaoDAO extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    public ManutencaoDAO(Context context) {
        super(context, "EconomicDrive.sqlite", null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE manutencao("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "idCarro INTEGER,"
                + "valor NUMERIC(18,2),"
                + "local INT,"
                + "dataG DATE,"
                + "tipo VARCHAR(100),"
                + "descricao VARCHAR(100),"
                + "FOREIGN KEY(idCarro) REFERENCES carro (idCarro))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        String sql = "DROP TABLE IF EXISTS manutencao";
        db.execSQL(sql);
        onCreate(db);
    }
}
