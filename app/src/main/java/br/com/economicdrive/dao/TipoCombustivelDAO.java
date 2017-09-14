package br.com.economicdrive.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.webkit.ConsoleMessage;

import java.util.ArrayList;
import java.util.List;

import br.com.economicdrive.constantes.Constantes;
import br.com.economicdrive.model.Combustivel;
import br.com.economicdrive.model.TipoCombustivel;

/**
 * Created by ITST on 12/09/2017.
 */

public class TipoCombustivelDAO extends SQLiteOpenHelper{

    public TipoCombustivelDAO(Context context) {
        super(context, "EconomicDrive.sqlite", null, Constantes.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE tipo_combustivel(" +
                "id INTEGER PRIMARY KEY," +
                "nome VARCHAR(50))";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        String sql = "DROP TABLE IF EXISTS tipo_combustivel";
        db.execSQL(sql);
        onCreate(db);
    }

    public List <TipoCombustivel> getList(){
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT * FROM tipo_combustivel";
        Cursor cursor = db.rawQuery(sql, null);
        List <TipoCombustivel> tipoComvustivelList = new ArrayList <>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            TipoCombustivel newTipoCombustivel = new TipoCombustivel(
                    cursor.getInt(cursor.getColumnIndex("id")),
                    cursor.getString(cursor.getColumnIndex("nome")));
            tipoComvustivelList.add(newTipoCombustivel);
            cursor.moveToNext();
        }
        cursor.close();
        return tipoComvustivelList;
    }

}
