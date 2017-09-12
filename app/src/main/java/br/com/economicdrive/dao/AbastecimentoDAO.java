package br.com.economicdrive.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import br.com.economicdrive.Information;
import br.com.economicdrive.model.Abastecimento;
import br.com.economicdrive.model.Carro;
import br.com.economicdrive.model.Local;

/**
 * Created by Flavio on 10/09/2017.
 */

public class AbastecimentoDAO extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    public AbastecimentoDAO(Context context) {
        super(context, "EconomicDrive.sqlite", null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE abastecimento("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "idCarro INTEGER,"
                + "idCombustivel INTEGER,"
                + "idLocal INT,"
                + "valorGasto NUMERIC(18,2),"
                + "valorLitro NUMERIC(9,2),"
                + "dataGasto DATE,"
                + "kilometros INT,"
                + "tanqueCheio VARCHAR(3),"
                + "kilometrosRodados INT,"
                + "litrosGastos NUMERIC(9,2),"
                + "FOREIGN KEY(idCarro) REFERENCES Carro (id),"
                + "FOREIGN KEY(idCombustivel) REFERENCES Combustivel (id),"
                + "FOREIGN KEY(idLocal) REFERENCES tb_LOVSL (codigoLOCAL))";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS abastecimento";
        db.execSQL(sql);
        onCreate(db);
    }

    public void insert(Abastecimento abastecimento) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = getContentValues(abastecimento);
        db.insert("abastecimento", null, values);
    }

    public void update(Abastecimento abastecimento){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = getContentValues(abastecimento);
        String[] where = {String.valueOf(abastecimento.getCodigoGasto())};
        db.update("abastecimento", values, "id = ?", where);
    }

    public void delete(Abastecimento abastecimento){
        SQLiteDatabase db = getWritableDatabase();
        String[] where = {String.valueOf(abastecimento.getCodigoGasto())};
        db.delete("abastecimento", "id = ?", where);
    }

    public List<Information> getList(){
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT * FROM abastecimento";
        Cursor cursor = db.rawQuery(sql, null);
        List <Information> abastecimentoList = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            Abastecimento newAbastecimento = new Abastecimento();
            newAbastecimento.setCodigoGasto(cursor.getInt(cursor.getColumnIndex("id")));
            newAbastecimento.setIdCarro(cursor.getInt(cursor.getColumnIndex("idCarro")));
            newAbastecimento.setCombustivel(cursor.getInt(cursor.getColumnIndex("idCombustivel")));
            newAbastecimento.setLocalGasto(cursor.getInt(cursor.getColumnIndex("idLocal")));
            newAbastecimento.setValorGasto(cursor.getFloat(cursor.getColumnIndex("valorGasto")));
            newAbastecimento.setValorLitro(cursor.getFloat(cursor.getColumnIndex("valorLitro")));
            newAbastecimento.setDataGasto(cursor.getString(cursor.getColumnIndex("data")));
            newAbastecimento.setKilometros(cursor.getInt(cursor.getColumnIndex("kilometros")));
            newAbastecimento.setTanqueCheio(cursor.getString(cursor.getColumnIndex("tanqueCheio")));
            newAbastecimento.setKmdif(cursor.getInt(cursor.getColumnIndex("kilometrosRodados")));
            newAbastecimento.setLitros(cursor.getFloat(cursor.getColumnIndex("litrosGastos")));
            abastecimentoList.add(newAbastecimento);
            cursor.moveToNext();
        }
        return abastecimentoList;
    }

    public List<Information> getListByIdCar( int idCarro){
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT * FROM abastecimento WHERE idCarro = ? ORDER BY data, id";
        String[] where = {String.valueOf(idCarro)};
        Cursor cursor = db.rawQuery(sql, where);
        List <Information> abastecimentoList = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            Abastecimento newAbastecimento = new Abastecimento();
            newAbastecimento.setCodigoGasto(cursor.getInt(cursor.getColumnIndex("id")));
            newAbastecimento.setIdCarro(cursor.getInt(cursor.getColumnIndex("idCarro")));
            newAbastecimento.setCombustivel(cursor.getInt(cursor.getColumnIndex("idCombustivel")));
            newAbastecimento.setLocalGasto(cursor.getInt(cursor.getColumnIndex("idLocal")));
            newAbastecimento.setValorGasto(cursor.getFloat(cursor.getColumnIndex("valorGasto")));
            newAbastecimento.setValorLitro(cursor.getFloat(cursor.getColumnIndex("valorLitro")));
            newAbastecimento.setDataGasto(cursor.getString(cursor.getColumnIndex("data")));
            newAbastecimento.setKilometros(cursor.getInt(cursor.getColumnIndex("kilometros")));
            newAbastecimento.setTanqueCheio(cursor.getString(cursor.getColumnIndex("tanqueCheio")));
            newAbastecimento.setKmdif(cursor.getInt(cursor.getColumnIndex("kilometrosRodados")));
            newAbastecimento.setLitros(cursor.getFloat(cursor.getColumnIndex("litrosGastos")));
            abastecimentoList.add(newAbastecimento);
            cursor.moveToNext();
        }
        cursor.close();
        return abastecimentoList;
    }

    public Local getLocal (Abastecimento abastecimento){
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT * FROM local WHERE id = ?";
        String [] where = {String.valueOf(abastecimento.getLocalGasto())};
        Cursor cursor = db.rawQuery(sql, where);
        cursor.moveToFirst();
        Local local = new Local();
        while (!cursor.isAfterLast()){
            local = new Local(cursor.getInt(cursor.getColumnIndex("id")),
                    cursor.getString(cursor.getColumnIndex("endereco")),
                    cursor.getString(cursor.getColumnIndex("nome")));
            cursor.moveToNext();
        }
        cursor.close();
        return local;
    }
    public void updateKmRodado (int idCarro){
        List<Information> abastecimentoList = getListByIdCar(idCarro);
        Abastecimento[] itens = abastecimentoList.toArray(new Abastecimento[0]);
        if (itens.length > 0 ){
            int i = itens.length - 1;
            for(int t = 0; t < itens.length; t++){
                if (t < i){
                    itens[t].setKmdif(itens[t + 1].getKilometros() - itens[t].getKilometros());
                    itens[t].setLitros(itens[t + 1].getValorGasto()/itens[t + 1].getValorLitro());
                    if (itens[t+1].getTanqueCheio().equals("sim")){
                        update(itens[t]);
                    }
                }
            }
            //limpa o ultima abastecimento
            if (itens[itens.length - 1].getKmdif() > 0 || itens[itens.length - 1].getLitros() > 0){
                itens[itens.length - 1].setKmdif(0);
                itens[itens.length - 1].setLitros(0);
                update(itens[itens.length - 1]);
            }
        }
    }

    public int getMaxHodometro(Carro carro) {
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT MAX(kilometros) FROM abastecimento WHERE idCarro = ?";
        String[] where = {String.valueOf(carro.getCodigo())};
        Cursor cursor = db.rawQuery(sql, where);
        cursor.moveToFirst();
        int idCarro = cursor.getInt(cursor.getColumnIndex("idCarro"));
        cursor.close();
        return idCarro;
    }

    public ContentValues getContentValues(Abastecimento abastecimento){
        ContentValues contentValues = new ContentValues();
        contentValues.put("idCarro", abastecimento.getIdCarro());
        contentValues.put("idCombustivel", abastecimento.getCombustivel());
        contentValues.put("idLocal", abastecimento.getLocalGasto());
        contentValues.put("valorGasto", abastecimento.getValorLitro());
        contentValues.put("data", abastecimento.getDataGasto());
        contentValues.put("valorLitro", abastecimento.getValorLitro());
        contentValues.put("kilometros", abastecimento.getKilometros());
        contentValues.put("tanqueCheio", abastecimento.getTanqueCheio());
        contentValues.put("kilometrosRodados", abastecimento.getKmdif());

        return contentValues;
    }
}
