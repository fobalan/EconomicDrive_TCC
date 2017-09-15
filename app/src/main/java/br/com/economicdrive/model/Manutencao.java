package br.com.economicdrive.model;

import android.content.Context;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import br.com.economicdrive.Information;
import br.com.economicdrive.database.Sqlite;

public class Manutencao extends Gastos {
    private int tipoManutencao;
    private String descricao;

    public Manutencao(){}

    public Manutencao (Context context){
        super(context);
    }

    public Manutencao(Parcel in) {
        super(in);
        tipoManutencao = in.readInt();
        descricao = in.readString();
    }
    public int getTipoManutencao() {
        return tipoManutencao;
    }
    public void setTipoManutencao(int tipoManutencao) {
        this.tipoManutencao = tipoManutencao;
    }
    public String getDescricaoManutencao() {
        return descricao;
    }
    public void setDescricaoManutencao(String descricao) {
        this.descricao = descricao;
    }

    public void insereManutencao(Context context){
        Sqlite database = new Sqlite(context);
        String sql01 = "INSERT INTO TB_MANUTENCAO "
                + "( "
                + "idCarro, "
                + "valorGasto, "
                + "localGasto, "
                + "dataGasto, "
                + "tipoManutencao, "
                + "descManutencao) "
                + "VALUES ('" + getIdCarro() + "','"
                + getValorGasto() + "','"
                + getLocalGasto() + "','"
                + getDataGasto() + "','"
                + getTipoManutencao() + "','"
                + getDescricaoManutencao()
                + "')";
        database.execBanco(sql01);
    }
    public void alteraManutencao(Context context){
        Sqlite database = new Sqlite(context);
        String sql01 = "UPDATE TB_MANUTENCAO SET "
                + "valorGasto = '"
                + getValorGasto()
                + "', localGasto = '"
                + getLocalGasto()
                + "', dataGasto = '"
                + getDataGasto()
                + "', tipoManutencao = '"
                + getTipoManutencao()
                + "', descManutencao = '"
                + getDescricaoManutencao()
                + "' WHERE codigoGasto = " + getCodigoGasto();
        database.execBanco(sql01);
    }

    public void deletaManutencao(Context context){
        Sqlite database = new Sqlite(context);
        String sql01 = "DELETE FROM TB_MANUTENCAO WHERE codigoGasto = " + getCodigoGasto();
        database.execBanco(sql01);
    }

    public static List<Information> ConsultaManutencao(Context context, String sql) {
        Sqlite database = new Sqlite(context);
        Cursor cursor = database.consulta(sql);
        List<Information> manutencao = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Manutencao newManutencao = new Manutencao(context);
            newManutencao.setCodigoGasto(cursor.getInt(0));
            newManutencao.setIdCarro(cursor.getInt(1));
            newManutencao.setValorGasto(cursor.getFloat(2));
            newManutencao.setLocalGasto(cursor.getInt(3));
            newManutencao.setDataGasto(cursor.getString(4));
            newManutencao.setTipoManutencao(cursor.getInt(5));
            newManutencao.setDescricaoManutencao(cursor.getString(6));
            manutencao.add(newManutencao);
            cursor.moveToNext();
        }
        return manutencao;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(getCodigoGasto());
        dest.writeInt(getIdCarro());
        dest.writeFloat(getValorGasto());
        dest.writeInt(getLocalGasto());
        dest.writeString(getDataGasto());
        dest.writeInt(getTipoManutencao());
        dest.writeString(getDescricaoManutencao());

    }

    public static int ContaManutencao(Context context, int codigo){
        Sqlite sqlite = new Sqlite(context);
        Cursor cursor = sqlite.consulta(
                "Select count (*) from tb_manutencao where idCarro = "+ codigo + " ;"
        );
        cursor.moveToFirst();
        return cursor.getInt(0);
    }


    public static final Parcelable.Creator<Manutencao> CREATOR = new Parcelable.Creator<Manutencao>() {
        public Manutencao createFromParcel(Parcel in) {
            return new Manutencao(in);
        }

        @Override
        public Manutencao[] newArray(int size) {
            return new Manutencao[size];
        }
    };
    public static Local ConsultaLocal(Context context, String sql){
        Sqlite database = new Sqlite(context);
        Local local = null;
        Cursor cursor = database.consulta(sql);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            local = new Local(cursor.getInt(0), cursor.getString(2), cursor.getString(1));
            cursor.moveToNext();
        }
        return local;
    }

    @Override
    public String getPrimaryText() {
        return "Valor: " + NumberFormat.getCurrencyInstance().format(getValorGasto());
    }

    @Override
    public String getSecondaryText() {
        return "Descrição: " + getDescricaoManutencao() + "\n"
                + "Local: " + ConsultaLocal(getContext(), "SELECT *"
                + " FROM tb_local"
                + " WHERE codigoLOCAL = " + getLocalGasto()).getNome()
                + "\n"
                + "Data: " + tratadata()
                ;
    }
}