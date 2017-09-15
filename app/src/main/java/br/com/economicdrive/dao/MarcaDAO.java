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
        insertData(db);
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

    private void insertData(SQLiteDatabase db) {
        //INSERE OS REGISTROS PADRÕES
        //MARCAS
        db.execSQL("INSERT INTO marca (nome) SELECT 'Agrale'");
        db.execSQL("INSERT INTO marca (nome) SELECT 'Aston Martin'");
        db.execSQL("INSERT INTO marca (nome) SELECT 'Audi'");
        db.execSQL("INSERT INTO marca (nome) SELECT 'Bentley'");
        db.execSQL("INSERT INTO marca (nome) SELECT 'BMW'");
        db.execSQL("INSERT INTO marca (nome) SELECT 'Changan'");
        db.execSQL("INSERT INTO marca (nome) SELECT 'Chery'");
        db.execSQL("INSERT INTO marca (nome) SELECT 'GM/Chevrolet'");
        db.execSQL("INSERT INTO marca (nome) SELECT 'Chrysler'");
        db.execSQL("INSERT INTO marca (nome) SELECT 'Citroën'");
        db.execSQL("INSERT INTO marca (nome) SELECT 'Dodge'");
        db.execSQL("INSERT INTO marca (nome) SELECT 'Effa'");
        db.execSQL("INSERT INTO marca (nome) SELECT 'Ferrari'");
        db.execSQL("INSERT INTO marca (nome) SELECT 'Fiat'");
        db.execSQL("INSERT INTO marca (nome) SELECT 'Ford'");
        db.execSQL("INSERT INTO marca (nome) SELECT 'Geely'");
        db.execSQL("INSERT INTO marca (nome) SELECT 'Hafei'");
        db.execSQL("INSERT INTO marca (nome) SELECT 'Honda'");
        db.execSQL("INSERT INTO marca (nome) SELECT 'Hyundai'");
        db.execSQL("INSERT INTO marca (nome) SELECT 'Iveco'");
        db.execSQL("INSERT INTO marca (nome) SELECT 'Jac Motors'");
        db.execSQL("INSERT INTO marca (nome) SELECT 'Jaguar'");
        db.execSQL("INSERT INTO marca (nome) SELECT 'Jeep'");
        db.execSQL("INSERT INTO marca (nome) SELECT 'Jinbei'");
        db.execSQL("INSERT INTO marca (nome) SELECT 'Kia'");
        db.execSQL("INSERT INTO marca (nome) SELECT 'Lamborghini'");
        db.execSQL("INSERT INTO marca (nome) SELECT 'Land Rover'");
        db.execSQL("INSERT INTO marca (nome) SELECT 'Lifan'");
        db.execSQL("INSERT INTO marca (nome) SELECT 'Mahindra'");
        db.execSQL("INSERT INTO marca (nome) SELECT 'Maserati'");
        db.execSQL("INSERT INTO marca (nome) SELECT 'Mercedes-Benz'");
        db.execSQL("INSERT INTO marca (nome) SELECT 'MG Motors'");
        db.execSQL("INSERT INTO marca (nome) SELECT 'Mini'");
        db.execSQL("INSERT INTO marca (nome) SELECT 'Mitsubishi'");
        db.execSQL("INSERT INTO marca (nome) SELECT 'Nissan'");
        db.execSQL("INSERT INTO marca (nome) SELECT 'Peugeot'");
        db.execSQL("INSERT INTO marca (nome) SELECT 'Porsche'");
        db.execSQL("INSERT INTO marca (nome) SELECT 'Ram'");
        db.execSQL("INSERT INTO marca (nome) SELECT 'Renault'");
        db.execSQL("INSERT INTO marca (nome) SELECT 'Smart'");
        db.execSQL("INSERT INTO marca (nome) SELECT 'Ssangyong'");
        db.execSQL("INSERT INTO marca (nome) SELECT 'Subaru'");
        db.execSQL("INSERT INTO marca (nome) SELECT 'Suzuki'");
        db.execSQL("INSERT INTO marca (nome) SELECT 'Toyota'");
        db.execSQL("INSERT INTO marca (nome) SELECT 'Troller'");
        db.execSQL("INSERT INTO marca (nome) SELECT 'Volkswagen'");
        db.execSQL("INSERT INTO marca (nome) SELECT 'Volvo'");
        db.execSQL("INSERT INTO marca (nome) SELECT 'Outra'");
    }
}
