package br.com.economicdrive;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;

public class Modelo {
	private int codigo;
	private String nome;
	private int marca;
	
	public Modelo(int i, String strnome, int intMarca){
		this.codigo = i;
		this.nome = strnome;
		this.marca = intMarca;
	}
	
	public int getMarca() {
		return marca;
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
	public void setMarca(int marca) {
		this.marca = marca;
	}
	
	public static List <Modelo> consultarModelo(Context context, String sql){
		Sqlite database = new Sqlite(context);
		String sql01 = sql;
		Cursor cursor = database.consulta(sql01);
		List <Modelo> modelo = new ArrayList <Modelo>();
		cursor.moveToFirst();
		while (!cursor.isAfterLast()){
			Modelo modelo2 = new Modelo(cursor.getInt(0), cursor.getString(1), cursor.getInt(2));
			modelo.add(modelo2);
			cursor.moveToNext();
		}
		return modelo;
	}
	
	public String toString(){
		return this.nome;
	}
}
