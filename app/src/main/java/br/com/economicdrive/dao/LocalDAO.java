package br.com.economicdrive.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import br.com.economicdrive.Information;
import br.com.economicdrive.constantes.Constantes;
import br.com.economicdrive.model.Local;

/**
 * Created by ITST on 11/09/2017.
 */

public class LocalDAO extends SQLiteOpenHelper {

    public LocalDAO(Context context) {
        super(context, "EconomicDrive.sqlite", null, Constantes.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE local(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nome VARCHAR(100)," +
                "endereco VARCHAR(100))";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        String sql = "DROP TABLE IF EXISTS local";
        db.execSQL(sql);
        onCreate(db);
    }

    public void insert(Local local) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = getContentValues(local);
        db.insert("local", null, contentValues);
    }

    public void update(Local local) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = getContentValues(local);
        String[] args = {String.valueOf(local.getCodigo())};
        db.update("local", contentValues, "WHERE id = ?", args);
    }

    public void delete(Local local) {
        SQLiteDatabase db = getWritableDatabase();
        String[] args = {String.valueOf(local.getCodigo())};
        db.delete("local", "WHERE id = ?", args);
    }

    public List<Information> getLocaisByEndereco(Local local) {
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT * FROM local WHERE endereco =?";
        String[] args = {local.getEndereco()};
        List<Information> localList = new ArrayList<>();
        Cursor cursor = db.rawQuery(sql, args);
        cursor.moveToFirst();
        while (cursor.isAfterLast()) {
            Local newLocal = new Local(
                    cursor.getInt(cursor.getColumnIndex("id")),
                    cursor.getString(cursor.getColumnIndex("endereco")),
                    cursor.getString(cursor.getColumnIndex("nome")));
            localList.add(newLocal);
            cursor.moveToNext();
        }
        cursor.close();
        return localList;
    }

    private ContentValues getContentValues(Local local) {

        ContentValues contentValues = new ContentValues();
        contentValues.put("nome", local.getNome());
        contentValues.put("endereco", local.getEndereco());

        return contentValues;
    }
}
