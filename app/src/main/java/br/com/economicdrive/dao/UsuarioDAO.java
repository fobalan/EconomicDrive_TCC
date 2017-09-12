package br.com.economicdrive.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import br.com.economicdrive.constantes.Constantes;
import br.com.economicdrive.model.Usuario;

/**
 * Created by ITST on 12/09/2017.
 */

public class UsuarioDAO extends SQLiteOpenHelper{

    public UsuarioDAO(Context context) {
        super(context, "EconomicDrive.sqlite", null, Constantes.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE usuario(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "senha VARCHAR(100))";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        String sql = "DROP TABLE IF EXISTS usuario";
        db.execSQL(sql);
        onCreate(db);
    }

    public void insert(Usuario usuario){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = getContentValues(usuario);
        db.insert("usuario",null, values);
    }

    public void update(Usuario usuario){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = getContentValues(usuario);
        String[] args = {String.valueOf(usuario.getId())};
        db.update("usuario", values, "WHERE id = ?", args);
    }

    public void delete(Usuario usuario){
        SQLiteDatabase db = getWritableDatabase();
        String[] args = {String.valueOf(usuario.getId())};
        db.delete("usuario","WHERE id = ?", args);
    }

    private ContentValues getContentValues(Usuario usuario) {
        ContentValues values = new ContentValues();
        values.put("senha", usuario.getSenha());
        return values;
    }

    public List<Usuario> getList(){
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT * FROM usuario";
        List<Usuario> usuarioList = new ArrayList<>();
        Cursor cursor = db.rawQuery(sql,null);
        while (cursor.isAfterLast()){
            Usuario usuario = new Usuario(
                    cursor.getInt(cursor.getColumnIndex("id")),
                    cursor.getString(cursor.getColumnIndex("senha"))
            );
            usuarioList.add(usuario);
        }
        cursor.close();
        return usuarioList;
    }
}
