package br.com.economicdrive.model;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;

import br.com.economicdrive.database.Sqlite;

public class Combustivel {
	
	private int codigo;
	private String nome;
	
	public Combustivel(int i, String strNome){
		this.codigo = i;
		this.nome = strNome;
	}
	
	public int getCodigo() {
		return codigo;
	}
	public String getNome() {
		return nome;
	}
	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public static List <Combustivel> consultarComb(Context context, String sql){
		Sqlite database = new Sqlite(context);
		String sql01 = sql;
		Cursor cursor = database.consulta(sql01);
		List <Combustivel> combustivel = new ArrayList <Combustivel>();
		cursor.moveToFirst();
		while (!cursor.isAfterLast()){
			Combustivel combustivel2 = new Combustivel(cursor.getInt(0), cursor.getString(1));
			combustivel.add(combustivel2);
			cursor.moveToNext();
		}
		return combustivel;
	}
	
	public String toString(){
		return this.nome;
	}
}
