package br.com.economicdrive.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import br.com.economicdrive.constantes.Constantes;


/**
 * Created by ITST on 12/09/2017.
 */

public class CombustivelDAO extends SQLiteOpenHelper {

    public CombustivelDAO(Context context) {
        super(context, "EconomicDrive.sqlite", null, Constantes.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE combustivel(" +
                "id INTEGER PRIMARY KEY," +
                "idTipo INTEGER" +
                "nome VARCHAR(50)," +
                "FOREIGN KEY (idTipo) REFERENCES tipo_combustivel (id))";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        String sql = "DROP TABLE IF EXISTS combustivel";
        db.execSQL(sql);
        onCreate(db);
    }
}