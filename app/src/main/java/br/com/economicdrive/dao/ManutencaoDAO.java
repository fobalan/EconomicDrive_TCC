package br.com.economicdrive.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import br.com.economicdrive.constantes.Constantes;
import br.com.economicdrive.model.Manutencao;

/**
 * Created by ITST on 11/09/2017.
 */

public class ManutencaoDAO extends SQLiteOpenHelper {

    private ContentValues contentValues;

    public ManutencaoDAO(Context context) {
        super(context, "EconomicDrive.sqlite", null, Constantes.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE manutencao("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "idCarro INTEGER,"
                + "valor NUMERIC(18,2),"
                + "local INT,"
                + "data DATE,"
                + "tipo VARCHAR(100),"
                + "descricao VARCHAR(100),"
                + "FOREIGN KEY(idCarro) REFERENCES carro (idCarro))";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        String sql = "DROP TABLE IF EXISTS manutencao";
        db.execSQL(sql);
        onCreate(db);
    }

    public void insere(Manutencao manutencao) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = getContentValues(manutencao);
        db.insert("manutencao", null, values);
    }

    public void update (Manutencao manutencao){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = getContentValues(manutencao);
        String[] args = {String.valueOf(manutencao.getCodigoGasto())};
        db.update("manutencao", values, "WHERE id = ?", args);
    }

    public void delete(Manutencao manutencao){
        SQLiteDatabase db = getWritableDatabase();
        String[] args = {String.valueOf(manutencao.getCodigoGasto())};
        db.delete("manutencao", "WHERE id = ?", args);
    }

    private ContentValues getContentValues(Manutencao manutencao) {
        ContentValues values = new ContentValues();

        values.put("idCarro", manutencao.getIdCarro());
        values.put("valor", manutencao.getValorGasto());
        values.put("local", manutencao.getLocalGasto());
        values.put("data", manutencao.getDataGasto());
        values.put("tipo", manutencao.getTipoManutencao());
        values.put("descricao", manutencao.getDescricaoManutencao());

        return values;
    }
}
