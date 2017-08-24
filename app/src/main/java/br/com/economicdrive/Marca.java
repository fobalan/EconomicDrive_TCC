package br.com.economicdrive;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;

public class Marca {
	private int codigo;
	private String nome;
	
	public Marca(int i, String strNome){
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
	
	public static List <Marca> consultarMarca(Context context, String sql){
		Sqlite database = new Sqlite(context);
		String sql01 = sql;
		Cursor cursor = database.consulta(sql01);
		List <Marca> marca = new ArrayList <Marca>();
		cursor.moveToFirst();
		while (!cursor.isAfterLast()){
			Marca marca2 = new Marca(cursor.getInt(0), cursor.getString(1));
			marca.add(marca2);
			cursor.moveToNext();
		}
		return marca;
	}
	
	
	public String toString(){
		return this.nome;
	}
}
