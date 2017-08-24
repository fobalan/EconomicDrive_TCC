package br.com.economicdrive;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;

public class TipoCombustivel {
	
	private int codigo;
	private String nome;
	
	
	public TipoCombustivel(int intCod, String strNome){
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
	
	public static List <TipoCombustivel> consultarCombustivel(Context context, String sql){
		Sqlite database = new Sqlite(context);
		String sql01 = sql;
		Cursor cursor = database.consulta(sql01);
		List <TipoCombustivel> tpcombustivel = new ArrayList <TipoCombustivel>();
		cursor.moveToFirst();
		while (!cursor.isAfterLast()){
			TipoCombustivel tpcombustivel2 = new TipoCombustivel(cursor.getInt(0), cursor.getString(1));
			tpcombustivel.add(tpcombustivel2);
			cursor.moveToNext();
		}
		return tpcombustivel;
	}
	
	public String toString(){
		return this.nome;
	}
}
