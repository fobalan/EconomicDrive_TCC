package br.com.economicdrive.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import br.com.economicdrive.constantes.Constantes;
import br.com.economicdrive.model.Marca;
import br.com.economicdrive.model.Modelo;

/**
 * Created by Flavio on 12/09/2017.
 */

public class MarcaDAO extends SQLiteOpenHelper {

    public MarcaDAO(Context context) {
        super(context, "EconomicDrive.sqlite", null, Constantes.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE marca(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nome VARCHAR(100))";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS marca";
        db.execSQL(sql);
        onCreate(db);
    }

    public List <Marca> getList(){
        SQLiteDatabase db = getWritableDatabase();
        String sql = "SELECT * FROM marca";
        Cursor cursor = db.rawQuery(sql, null);
        List <Marca> marcaList = new ArrayList <>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            Marca marca = new Marca(
                    cursor.getInt(cursor.getColumnIndex("id")),
                    cursor.getString(cursor.getColumnIndex("nome")));
            marcaList.add(marca);
            cursor.moveToNext();
        }
        cursor.close();
        return marcaList;
    }


    public List<Modelo> getModelos(Marca marca){
        SQLiteDatabase db = getWritableDatabase();
        String sql = "SELECT * FROM modelo WHERE idMarca = ?";
        String[] args = {String.valueOf(marca.getCodigo())};
        Cursor cursor = db.rawQuery(sql, args);
        List <Modelo> modeloList = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            Modelo modelo = new Modelo(
                    cursor.getInt(cursor.getColumnIndex("id")),
                    cursor.getString(cursor.getColumnIndex("nome")),
                    cursor.getInt(cursor.getColumnIndex("idMarca")));
            modeloList.add(modelo);
            cursor.moveToNext();
        }
        cursor.close();
        return modeloList;
    }


}
