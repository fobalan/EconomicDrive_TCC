package br.com.economicdrive.model;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;

import br.com.economicdrive.database.Sqlite;

public class TipoManutencao {

    private int codigo;
    private String nome;


    public TipoManutencao(int intCod, String strNome) {
        this.codigo = intCod;
        this.nome = strNome;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public static List<TipoManutencao> consultarTipoManutencao(Context context) {
        Sqlite database = new Sqlite(context);
        String sql01 = "SELECT * FROM TB_TP_MANUTENCAO";
        Cursor cursor = database.consulta(sql01);
        List<TipoManutencao> tpManutencao = new ArrayList<TipoManutencao>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            TipoManutencao tpManutencao2 = new TipoManutencao(cursor.getInt(0), cursor.getString(1));
            tpManutencao.add(tpManutencao2);
            cursor.moveToNext();
        }
        return tpManutencao;
    }

    public String toString() {
        return this.nome;
    }
}
