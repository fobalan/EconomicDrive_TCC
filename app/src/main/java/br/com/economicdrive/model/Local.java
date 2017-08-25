package br.com.economicdrive.model;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import br.com.economicdrive.Information;
import br.com.economicdrive.database.Sqlite;

public class Local implements Parcelable, Information {
	
	private int codigo;
	private String endereco;
	private String nome;
	
	public Local (){
		
	}
	public Local(int codigo, String strEnde, String strLocal) {
		this.codigo = codigo;
		this.endereco = strEnde;
		this.nome = strLocal;
	}
	public Local(String strEnde, String strLocal) {
		this.endereco = strEnde;
		this.nome = strLocal;
	}
	public Local(Parcel in){
		codigo = in.readInt();
		endereco = in.readString();
		nome = in.readString();
	}

	public int getCodigo() {
		return codigo;
	}
	
	public String getEndereco() {
		return endereco;
	}
	
	public String getNome() {
		return nome;
	}
	
	public void insereLocal(Context context){
		Sqlite database = new Sqlite(context);
		String sql01 = "INSERT INTO tb_local (nomeLOCAL, enderecoLOCAL) VALUES ('" + this.nome + "','" + this.endereco + "')";
		database.execBanco(sql01);		
	}
	public void alteraLocal(Context context){
		Sqlite database = new Sqlite(context);
		String sql01 = "UPDATE tb_local SET nomeLOCAL = '" + this.nome + "', enderecoLOCAL = '" + this.endereco + "' WHERE codigoLOCAL = " + this.codigo;
		database.execBanco(sql01);
	}
	public void deletaLocal(Context context){
		Sqlite database = new Sqlite(context);
		String sql01 = "DELETE FROM tb_local WHERE codigoLOCAL = " + this.codigo;
		database.execBanco(sql01);
	}
	
	public static List <Information> ConsultaLocais(Context context, String sql){
		Sqlite database = new Sqlite(context);
		String sql01 = sql;
		Cursor cursor = database.consulta(sql01);
		List <Information> local = new ArrayList <Information>();
		cursor.moveToFirst();
		while (!cursor.isAfterLast()){
			Local local2 = new Local(cursor.getInt(0), cursor.getString(2), cursor.getString(1));
			local.add(local2);
			cursor.moveToNext();
		}
		return local;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(codigo);
		dest.writeString(endereco);
		dest.writeString(nome);
		
	}
	public static final Parcelable.Creator<Local> CREATOR = new Parcelable.Creator<Local>() {
		public Local createFromParcel(Parcel in) {
			return new Local(in);
		}

		@Override
		public Local[] newArray(int size) {
			return new Local[size];
		}
	};

	@Override
	public String getPrimaryText() {
		return "Local: " + nome;
	}

	@Override
	public String getSecondaryText() {
		return "Endereço: " + endereco;
	}
}