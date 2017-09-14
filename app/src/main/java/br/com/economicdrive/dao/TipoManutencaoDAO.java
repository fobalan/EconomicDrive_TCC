package br.com.economicdrive.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import br.com.economicdrive.constantes.Constantes;
import br.com.economicdrive.model.TipoManutencao;

/**
 * Created by ITST on 14/09/2017.
 */

public class TipoManutencaoDAO extends SQLiteOpenHelper {
    public TipoManutencaoDAO(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, "EconomicDrive.sqlite", null, Constantes.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE tipo_manutencao("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "tipo VARCHAR(100))";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        String sql = "DROP TABLE IF EXISTS tipo_manutencao";
        db.execSQL(sql);
        onCreate(db);
    }

    public List<TipoManutencao> getList() {
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT * FROM tipo_manutencao";
        Cursor cursor = db.rawQuery(sql,null);
        List<TipoManutencao> tipoManutencaoList = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            TipoManutencao newManutencao = new TipoManutencao(
                    cursor.getInt(cursor.getColumnIndex("id")),
                    cursor.getString(cursor.getColumnIndex("tipo")));
            tipoManutencaoList.add(newManutencao);
            cursor.moveToNext();
        }
        cursor.close();
        return tipoManutencaoList;
    }
}
