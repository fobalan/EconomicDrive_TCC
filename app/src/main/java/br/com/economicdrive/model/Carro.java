package br.com.economicdrive.model;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import br.com.economicdrive.Information;
import br.com.economicdrive.database.Sqlite;

public class Carro implements Parcelable, Information {
		
	private int codigo;
	private String nome;
	private int km;
	private String placa;
	private int marca;
	private int modelo;
	private int comb;
	private String ativo;
	
	public Carro (Parcel in){
		this.codigo = in.readInt();
		this.nome 	= in.readString();
		this.km 	= in.readInt();
		this.placa 	= in.readString();
		this.marca 	= in.readInt();
		this.modelo = in.readInt();
		this.comb 	= in.readInt();
	}
	
	public Carro(int i, String strNome, int intKm, String strPlaca, int intMarca, int intModelo, int intComb, String ativo) {
		this.codigo = i;
		this.nome = strNome;
		this.km = intKm;
		this.placa = strPlaca;
		this.marca = intMarca;
		this.modelo = intModelo;
		this.comb = intComb;
		this.ativo = ativo;
	}	
	public int getKm() {
		return km;
	}
	public String getAtivo() {
		return ativo;
	}

	public String getPlaca() {
		return placa;
	}
	public String getNome() {
		return nome;
	}
	public int getComb() {
		return comb;
	}
	public int getMarca() {
		return marca;
	}
	public int getModelo() {
		return modelo;
	}
	public int getCodigo() {
		return codigo;
	}
	
	public void insereCarro(Context context){
		Sqlite database = new Sqlite(context);
		String sql01 =  "INSERT INTO TB_CARRO (nomeCARRO, placaCARRO, kmCARRO, marcaCARRO,modeloCARRO, combCARRO, ativo)" +
						" SELECT '" + this.nome + "','" + this.placa + "'," + this.km  + "," +this.marca + "," + this.modelo + 
						"," + this.comb + ",'" + this.ativo + "'";
		database.execBanco(sql01);
		
	}
	public void deletaCarro(Context context){
		Sqlite database = new Sqlite(context);
		String sql01 = "DELETE FROM tb_carro WHERE idCARRO = " + this.codigo;
		database.execBanco(sql01);
	}
	public void alteraCarro(Context context){
		Sqlite database = new Sqlite(context);
		String sql01 = 	"UPDATE tb_carro SET nomeCARRO = '" + this.nome + "'" +  
				 		" ,placaCARRO = '" + this.placa + "'" +
				 		" ,marcaCARRO = " + this.marca +
				 		" ,modeloCARRO = " + this.modelo + 
				 		" ,kmCARRO = " + this.km + 
				 		" ,combCARRO = " + this.comb + 
				 		" WHERE idCARRO = " + this.codigo;	
		database.execBanco(sql01);
	}
	public static List <Information> consultaCarro(Context context, String sql){
		Sqlite database = new Sqlite(context);
		Cursor cursor = database.consulta(sql);
		List <Information> carro = new ArrayList <>();
		cursor.moveToFirst();
		while (!cursor.isAfterLast()){
			Carro carro2 = new Carro(cursor.getInt(0), cursor.getString(1),cursor.getInt(2),cursor.getString(3), cursor.getInt(4),cursor.getInt(5),cursor.getInt(6), cursor.getString(7));
			carro.add(carro2);
			cursor.moveToNext();
		}
		
		return carro;
	} 
	public void alteraCarroParaAtivo(Context context){
		Sqlite database = new Sqlite(context);
		String sql01 = 	"UPDATE tb_carro SET ativo = 'nao';";
		database.execBanco(sql01);
		String sql02 = "UPDATE tb_carro SET ativo = 'sim' WHERE idCARRO = " + getCodigo();
		database.execBanco(sql02);
	}
	public static Carro consultaCarroAtivo (Context context){
		Sqlite database = new Sqlite(context);
		Carro carro = null;
		String sql01 = 	"SELECT * FROM tb_carro WHERE ativo = 'sim';";
		Cursor cursor = database.consulta(sql01);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()){
			carro = new Carro(cursor.getInt(0), cursor.getString(1),cursor.getInt(2),cursor.getString(3), cursor.getInt(4),cursor.getInt(5),cursor.getInt(6), cursor.getString(7));
			cursor.moveToNext();
		}
		
		return carro;
	}
	
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(getCodigo()); 
		dest.writeString(getNome()); 	
		dest.writeInt(getKm()); 	
		dest.writeString(getPlaca()); 
		dest.writeInt(getMarca());
		dest.writeInt(getModelo()); 
		dest.writeInt(getComb()); 	
		
	}
	
	public static final Parcelable.Creator<Carro> CREATOR = new Parcelable.Creator<Carro>() {
		public Carro createFromParcel(Parcel in) {
			return new Carro(in);
		}

		@Override
		public Carro[] newArray(int size) {
			return new Carro[size];
		}
	};

	@Override
	public String getPrimaryText() {
		return "Nome: " + nome;
	}

	@Override
	public String getSecondaryText() {
		return "Placa:  " + placa;
	}

	@Override
	public String toString() {
		return getPrimaryText();
	}
}
