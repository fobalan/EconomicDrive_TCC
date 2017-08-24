package br.com.economicdrive;

import android.content.Context;
import android.database.Cursor;


public class Usuario {
	
	private String senha;
	
	public Usuario(String strSenha) {
		this.senha = strSenha;
	}

	public String getSenha() {
		return senha;
	}
	
	public void setSenha(String senha) {
		this.senha = senha;
	}
	
	public void insereSenha(Context context){
		Sqlite database = new Sqlite(context);
		String sql01 = "INSERT INTO tb_usuario (senhaUSUARIO) VALUES ('" + this.senha + "')";
		database.execBanco(sql01);
	}
	public void alteraUsuario(Context context){
		Sqlite database = new Sqlite(context);
		String sql01 = "UPDATE tb_usuario SET senhaUSUARIO = '" + this.senha + "'";
		database.execBanco(sql01);
	}
	public void deletaUsuario(Context context){
		Sqlite database = new Sqlite(context);
		String sql01 = "DELETE FROM tb_usuario";
		database.execBanco(sql01);
	}
	public static Usuario ConsultaUsuario(Context context){
		Sqlite database = new Sqlite(context);
		String sql01 = "SELECT senhaUSUARIO FROM tb_usuario";
		Cursor cursor = database.consulta(sql01);
		Usuario usuario = null;
		cursor.moveToFirst();
		while (!cursor.isAfterLast()){
			usuario = new Usuario(cursor.getString(0));
			cursor.moveToNext();
		}
		return usuario;
	}	
}
