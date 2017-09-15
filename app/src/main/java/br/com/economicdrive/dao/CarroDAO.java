package br.com.economicdrive.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import br.com.economicdrive.Information;
import br.com.economicdrive.constantes.Constantes;
import br.com.economicdrive.database.Sqlite;
import br.com.economicdrive.model.Abastecimento;
import br.com.economicdrive.model.Carro;
import br.com.economicdrive.model.Combustivel;
import br.com.economicdrive.model.Despesas;
import br.com.economicdrive.model.Manutencao;
import br.com.economicdrive.model.TipoCombustivel;

/**
 * Created by ITST on 11/09/2017.
 */

public class CarroDAO extends SQLiteOpenHelper {

    private DespesasDAO despesasDAO;
    private ManutencaoDAO manutencaoDAO;
    private AbastecimentoDAO abastecimentoDAO;
    private CombustivelDAO combustivelDAO;
    private TipoCombustivelDAO tipoCombustivelDAO;

    public CarroDAO(Context context) {
        super(context, "EconomicDrive.sqlite", null, Constantes.DATABASE_VERSION);
        despesasDAO = new DespesasDAO(context);
        manutencaoDAO = new ManutencaoDAO(context);
        abastecimentoDAO = new AbastecimentoDAO(context);
        combustivelDAO = new CombustivelDAO(context);
        tipoCombustivelDAO = new TipoCombustivelDAO(context);
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

    public List<Information> getDespesas(Carro carro){
        SQLiteDatabase db = getWritableDatabase();
        String sql = "SELECT * FROM despesas WHERE idCarro = ?";
        String[] args = {String.valueOf(carro.getCodigo())};
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

    public List<Information> getAbastecimento(Carro carro){
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT * FROM abastecimento WHERE idCarro = ? ORDER BY data, id";
        String[] where = {String.valueOf(carro.getCodigo())};
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

    public List <Information> getManutencao (Carro carro){
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT * FROM manutencao WHERE idCarro = ?";
        String[] args = {String.valueOf(carro.getCodigo())};
        Cursor cursor = db.rawQuery(sql, args);
        List <Information> manutencaoList = new ArrayList <>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            Manutencao newManutencao = new Manutencao();
            newManutencao.setCodigoGasto(cursor.getInt(cursor.getColumnIndex("id")));
            newManutencao.setIdCarro(cursor.getInt(cursor.getColumnIndex("idCarro")));
            newManutencao.setValorGasto(cursor.getFloat(cursor.getColumnIndex("valor")));
            newManutencao.setLocalGasto(cursor.getInt(cursor.getColumnIndex("local")));
            newManutencao.setDataGasto(cursor.getString(cursor.getColumnIndex("data")));
            newManutencao.setTipoManutencao(cursor.getInt(cursor.getColumnIndex("tipo")));
            newManutencao.setDescricaoManutencao(cursor.getString(cursor.getColumnIndex("descricao")));
            manutencaoList.add(newManutencao);
            cursor.moveToNext();
        }
        cursor.close();
        return manutencaoList;
    }

    public List<Combustivel> getCombustivel(Carro carro) {
        SQLiteDatabase db = getReadableDatabase();
        String sql = null;
        Cursor cursor = null;
        if (carro.getComb() == Constantes.FLEX) {
            sql = "SELECT *"
                    + " FROM combustivel"
                    + " WHERE idTipo = ?"
                    + " AND idTipo = ?";
            String[] args = {"1", "2"};
            cursor = db.rawQuery(sql, args);
        } else {
            sql = "SELECT *"
                    + " FROM combustivel"
                    + " WHERE idTipo = ?";
            String[] args = {String.valueOf(carro.getComb())};
            cursor = db.rawQuery(sql, args);
        }
        List<Combustivel> gasolineList = new ArrayList<>();
        while (cursor.isAfterLast()) {
            Combustivel combustivel = new Combustivel();
            combustivel.setCodigo(cursor.getInt(cursor.getColumnIndex("id")));
            combustivel.setNome(cursor.getString(cursor.getColumnIndex("nome")));
            gasolineList.add(combustivel);
            cursor.moveToFirst();
        }
        cursor.close();
        return gasolineList;
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

    public void updateKmRodado (Context context, Carro carro){
        AbastecimentoDAO dao = new AbastecimentoDAO(context);
        List<Information> abastecimentoList = getAbastecimento(carro);
        Abastecimento[] itens = abastecimentoList.toArray(new Abastecimento[0]);
        if (itens.length > 0 ){
            int i = itens.length - 1;
            for(int t = 0; t < itens.length; t++){
                if (t < i){
                    itens[t].setKmdif(itens[t + 1].getKilometros() - itens[t].getKilometros());
                    itens[t].setLitros(itens[t + 1].getValorGasto()/itens[t + 1].getValorLitro());
                    if (itens[t+1].getTanqueCheio().equals("sim")){
                        dao.update(itens[t]);
                        dao.close();
                    }
                }
            }
            //limpa o ultima abastecimento
            if (itens[itens.length - 1].getKmdif() > 0 || itens[itens.length - 1].getLitros() > 0){
                itens[itens.length - 1].setKmdif(0);
                itens[itens.length - 1].setLitros(0);
                dao.update(itens[itens.length - 1]);
                dao.close();
            }
        }
    }
}
