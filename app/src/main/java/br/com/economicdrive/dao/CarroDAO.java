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
import br.com.economicdrive.model.Carro;
import br.com.economicdrive.model.Despesas;

/**
 * Created by ITST on 11/09/2017.
 */

public class CarroDAO extends SQLiteOpenHelper {

    public CarroDAO(Context context) {
        super(context, "EconomicDrive.sqlite", null, Constantes.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE carro(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nome VARCHAR(100)," +
                "placa VARCHAR(100)," +
                "kilometros INT," +
                "marca int," +
                "modelo int," +
                "combustivel INT," +
                "ativo VARCHAR(3))";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        String sql = "DROP TABLE IF EXISTS carro";
        db.execSQL(sql);
        onCreate(db);
    }

    public void insert (Carro carro){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = getContentValues(carro);
        db.insert("carro",null,contentValues);
    }

    public void update(Carro carro){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = getContentValues(carro);
        String[] args = {String.valueOf(carro.getCodigo())};
        db.update("carro",contentValues, "WHERE id = ?", args);
    }

    public void delete(Carro carro){
        SQLiteDatabase db = getWritableDatabase();
        String[] args = {String.valueOf(carro.getCodigo())};
        db.delete("carro", "WHERE id = ?", args);
    }

    public List<Information> getCarByPlacaByName(Carro carro){
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT * FROM carro WHERE placa = ? AND nome = ?";
        String[] args = { carro.getPlaca(), carro.getNome()};
        Cursor cursor = db.rawQuery(sql, args);
        List <Information> carroList = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            Carro newCarro = new Carro(
                    cursor.getInt(cursor.getColumnIndex("id")),
                    cursor.getString(cursor.getColumnIndex("nome")),
                    cursor.getString(cursor.getColumnIndex("placa")),
                    cursor.getInt(cursor.getColumnIndex("kilometros")),
                    cursor.getInt(cursor.getColumnIndex("marca")),
                    cursor.getInt(cursor.getColumnIndex("modelo")),
                    cursor.getInt(cursor.getColumnIndex("combustivel")),
                    cursor.getString(cursor.getColumnIndex("ativo")));
            carroList.add(newCarro);
            cursor.moveToNext();
        }
        cursor.close();
        return carroList;
    }

    public void changeToActive(Carro carro){
        SQLiteDatabase db = getWritableDatabase();
        String sql01 = 	"UPDATE carro SET ativo = 'nao';";
        db.rawQuery(sql01,null);
        String sql02 = "UPDATE carro SET ativo = 'sim' WHERE idCARRO = ?";
        String[] args = {String.valueOf(carro.getCodigo())};
        db.rawQuery(sql02, args);
    }

    public Carro getCarActive(){
        Carro carro = new Carro();
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT * FROM carro WHERE ativo = 'sim';";
        Cursor cursor = db.rawQuery(sql,null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            carro = new Carro(
                    cursor.getInt(cursor.getColumnIndex("id")),
                    cursor.getString(cursor.getColumnIndex("nome")),
                    cursor.getString(cursor.getColumnIndex("placa")),
                    cursor.getInt(cursor.getColumnIndex("kilometros")),
                    cursor.getInt(cursor.getColumnIndex("marca")),
                    cursor.getInt(cursor.getColumnIndex("modelo")),
                    cursor.getInt(cursor.getColumnIndex("combustivel")),
                    cursor.getString(cursor.getColumnIndex("ativo")));
            cursor.moveToNext();
        }
        cursor.close();
        return carro;
    }

    public List<Information> getDespesas(int idCarro){
        SQLiteDatabase db = getWritableDatabase();
        String sql = "SELECT * FROM despesas WHERE idCarro = ?";
        String[] args = {String.valueOf(idCarro)};
        Cursor cursor = db.rawQuery(sql, args);
        List <Information> despesas = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            Despesas newDespesa = new Despesas();
            newDespesa.setCodigoGasto(cursor.getInt(0));
            newDespesa.setIdCarro(cursor.getInt(1));
            newDespesa.setValorGasto(cursor.getFloat(2));
            newDespesa.setLocalGasto(cursor.getInt(3));
            newDespesa.setDataGasto(cursor.getString(4));
            newDespesa.setDescricaoDespesa(cursor.getString(5));
            despesas.add(newDespesa);
            cursor.moveToNext();
        }
        cursor.close();
        return despesas;
    }

    public int countDespesas(Carro carro){
        SQLiteDatabase db = getWritableDatabase();
        String sql = "SELECT COUNT (*) FROM despesas WHERE idCarro = ?";
        String[] args = {String.valueOf(carro.getCodigo())};
        Cursor cursor = db.rawQuery(sql, args);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();
        return count;
    }

    public int countManutencao(Carro carro){
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT COUNT (*) FROM manutencao WHERE idCarro = ?";
        String[] args = {String.valueOf(carro.getCodigo())};
        Cursor cursor = db.rawQuery(sql, args);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();
        return count;
    }

    public int countAbastecimento(Carro carro){
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT COUNT (*) FROM abastecimento WHERE idCarro = ?";
        String[] where = {String.valueOf(carro.getCodigo())};
        Cursor cursor = db.rawQuery(sql, where);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();
        return count;
    }

    private ContentValues getContentValues(Carro carro) {
        ContentValues contentValues = new ContentValues();

        contentValues.put("nome", carro.getNome());
        contentValues.put("placa", carro.getPlaca());
        contentValues.put("kilometros", carro.getKm());
        contentValues.put("marca", carro.getMarca());
        contentValues.put("modelo", carro.getModelo());
        contentValues.put("combustivel", carro.getComb());
        contentValues.put("ativo", carro.getAtivo());

        return contentValues;
    }
}
