package br.com.economicdrive.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import br.com.economicdrive.Information;
import br.com.economicdrive.model.Despesas;
import br.com.economicdrive.model.Local;

/**
 * Created by Flavio on 11/09/2017.
 */

public class DespesasDAO extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    public DespesasDAO(Context context) {
        super(context, "EconomicDrive.sqlite", null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE despesas("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "idCarro INTEGER,"
                + "idLocal INT,"
                + "valor NUMERIC(18,2),"
                + "data DATE,"
                + "descricao VARCHAR(100),"
                + "FOREIGN KEY(idCarro) REFERENCES carro (id),"
                + "FOREIGN KEY(idLocal) REFERENCES local (id))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS despesas";
        db.execSQL(sql);
        onCreate(db);
    }

    public void insert (Despesas despesas){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = getContentValues(despesas);
        db.insert("despesas", null, values);
    }

    public void update (Despesas despesas){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = getContentValues(despesas);
        String[] args = {String.valueOf(despesas.getCodigoGasto())};
        db.update("despesas", values, "WHERE id = ?", args);
    }

    public void delete (Despesas despesas){
        SQLiteDatabase db = getWritableDatabase();
        String[] args = {String.valueOf(despesas.getCodigoGasto())};
        db.delete("despesas","WHERE id = ?", args);
    }

    public Local getLocal(Despesas despesas){
        Local local = new Local();
        SQLiteDatabase db = getWritableDatabase();
        String sql = "SELECT * FROM local WHERE idLocal = ?";
        String[] args = { String.valueOf(despesas.getLocalGasto())};
        Cursor cursor = db.rawQuery(sql, args);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            local = new Local(cursor.getInt(0), cursor.getString(2), cursor.getString(1));
            cursor.moveToNext();
        }
        cursor.close();
        return local;
    }

    private ContentValues getContentValues(Despesas despesas) {
        ContentValues values = new ContentValues();
        values.put("idCarro", despesas.getIdCarro());
        values.put("idLocal", despesas.getLocalGasto());
        values.put("valor", despesas.getValorGasto());
        values.put("data", despesas.getDataGasto());
        values.put("descricao", despesas.getDescricaoDespesa());
        return values;
    }
}
