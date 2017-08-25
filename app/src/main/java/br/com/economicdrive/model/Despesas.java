package br.com.economicdrive.model;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import br.com.economicdrive.Gastos;
import br.com.economicdrive.Information;
import br.com.economicdrive.database.Sqlite;

public class Despesas extends Gastos {
    private String descricaoDespesa;

    public Despesas (){

    }

    public Despesas (Context context){
        super(context);
    }

    public Despesas(Parcel in) {
        super(in);
        descricaoDespesa = in.readString();
    }
    public String getDescricaoDespesa() {
        return descricaoDespesa;
    }
    public void setDescricaoDespesa(String descricaoDespesa) {
        this.descricaoDespesa = descricaoDespesa;
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
        dest.writeString(getDescricaoDespesa());
    }
    public static final Parcelable.Creator<Despesas> CREATOR = new Parcelable.Creator<Despesas>() {
        public Despesas createFromParcel(Parcel in) {
            return new Despesas(in);
        }

        @Override
        public Despesas[] newArray(int size) {
            return new Despesas[size];
        }
    };

    public void insereDespesas(Context context){
        Sqlite database = new Sqlite(context);
        String sql01 = "INSERT INTO TB_DESPESAS "
                + "(idCarro, "
                + "valorGasto, "
                + "localGasto, "
                + "dataGasto, "
                + "descDespesa) "
                + "VALUES ('" + getIdCarro() + "','"
                + getValorGasto() + "','"
                + getLocalGasto() + "','"
                + getDataGasto() + "','"
                + getDescricaoDespesa()
                + "')";
        database.execBanco(sql01);
    }
    public void alteraDespesas(Context context){
        Sqlite database = new Sqlite(context);
        String sql01 = "UPDATE TB_DESPESAS SET "
                + "valorGasto = '"
                + getValorGasto()
                + "', localGasto = '"
                + getLocalGasto()
                + "', dataGasto = '"
                + getDataGasto()
                + "', descDespesa = '"
                + getDescricaoDespesa()
                + "' WHERE codigoGasto = " + getCodigoGasto();
        database.execBanco(sql01);
    }
    public void deletaDespesas(Context context){
        Sqlite database = new Sqlite(context);
        String sql01 = "DELETE FROM TB_DESPESAS WHERE codigoGasto = " + getCodigoGasto();
        database.execBanco(sql01);
    }

    public static List<Information> ConsultaDespesas(Context context, String sql){
        Sqlite database = new Sqlite(context);
        Cursor cursor = database.consulta(sql);
        List <Information> despesas = new ArrayList <>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            Despesas newDespesa = new Despesas(context);
            newDespesa.setCodigoGasto(cursor.getInt(0));
            newDespesa.setIdCarro(cursor.getInt(1));
            newDespesa.setValorGasto(cursor.getFloat(2));
            newDespesa.setLocalGasto(cursor.getInt(3));
            newDespesa.setDataGasto(cursor.getString(4));
            newDespesa.setDescricaoDespesa(cursor.getString(5));
            despesas.add(newDespesa);
            cursor.moveToNext();
        }
        return despesas;
    }
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

    public static int ContaDespesas(Context context, int codigo){
        Sqlite database = new Sqlite(context);
        Cursor cursor = database.consulta(
                "Select count (*) from tb_despesas where idCarro = "+ codigo +" ;"
        );
        cursor.moveToFirst();
        return cursor.getInt(0);
    }

    @Override
    public String getPrimaryText() {
        return "Valor: " + NumberFormat.getCurrencyInstance().format(getValorGasto());
    }

    @Override
    public String getSecondaryText() {
        return "Descrição: " + getDescricaoDespesa() + "\n"
                + "Local: " + ConsultaLocal(getContext(), "SELECT *"
                + " FROM tb_local"
                + " WHERE codigoLOCAL = " + getLocalGasto()).getNome()
                + "\n"
                + "Data: " + tratadata()
                ;
    }
}