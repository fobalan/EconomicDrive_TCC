package br.com.economicdrive.model;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import br.com.economicdrive.Information;
import br.com.economicdrive.database.Sqlite;

public class Abastecimento extends Gastos {
	private int combustivel;
	private float valorLitro;
	private int kilometros;	
	private String tanqueCheio;
	private int kmdif;
	private float litros;

	public Abastecimento (){}
	public Abastecimento (Context context){
		super(context);
	}

	public Abastecimento (Parcel in){
		super(in);
		valorLitro = in.readFloat();
		kilometros = in.readInt();
		tanqueCheio = in.readString();
		combustivel = in.readInt();
		kmdif = in.readInt();
	}
	
	public int getCombustivel() {
		return combustivel;
	}
	public void setCombustivel(int combustivel) {
		this.combustivel = combustivel;
	}
	public float getValorLitro() {
		return valorLitro;
	}
	public void setValorLitro(float valorLitro) {
		this.valorLitro = valorLitro;
	}
	public String getTanqueCheio() {
		return tanqueCheio;
	}
	public void setTanqueCheio(String tanqueCheio) {
		this.tanqueCheio = tanqueCheio;
	}
	public int getKilometros() {
		return kilometros;
	}
	public void setKilometros(int kilometros) {
		this.kilometros = kilometros;
	}
	public int getKmdif() {
		return kmdif;
	}
	public void setKmdif(int kmdif) {
		this.kmdif = kmdif;
	}
	public void setLitros(float litros) {
		this.litros = litros;
	}
	public float getLitros() {
		return litros;
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
		dest.writeFloat(valorLitro);
		dest.writeInt(kilometros);
		dest.writeString(tanqueCheio);
		dest.writeInt(combustivel);
		dest.writeInt(kmdif);
		
	}
	public static final Parcelable.Creator<Abastecimento> CREATOR = new Parcelable.Creator<Abastecimento>() {
		public Abastecimento createFromParcel(Parcel in) {
			return new Abastecimento(in);
		}

		@Override
		public Abastecimento[] newArray(int size) {
			return new Abastecimento[size];
		}
	};
	public void insereAbastecimento(Context context){
		Sqlite database = new Sqlite(context);
		String sql01 = "INSERT INTO TB_ABASTECIMENTO "
				+ "(idCarro,"
				+ "idCombustivel,"
				+ "idLocal, "
				+ "valorGasto, "
				+ "dataGasto, "
				+ "valorLitro, "
				+ "kilometros, "
				+ "tanqueCheio,"
				+ "kilometrosrodados) "
				+ "VALUES ('" + getIdCarro() + "','"
							  + getCombustivel() + "','"
							  + getLocalGasto() + "','"
							  + getValorGasto() + "','"
							  + getDataGasto() + "','"
							  + getValorLitro() + "','"
							  + getKilometros() + "','"
							  + getTanqueCheio() + "','"
							  + getKmdif()
							  + "')";
		database.execBanco(sql01);		
	}
	public void alteraAbastecimento(Context context){
		Sqlite database = new Sqlite(context);
		String sql01 = "UPDATE TB_ABASTECIMENTO SET "
					 + "idCarro = '"
				     + getIdCarro()
					 + "', idCombustivel = '"
					 + getCombustivel()
					 + "', idLocal = '" 
					 + getLocalGasto()
					 + "', valorGasto = '" 
					 + getValorGasto()
					 + "', dataGasto = '" 
					 + getDataGasto() 
					 + "', valorLitro = '" 
					 + getValorLitro()
					 + "', kilometros = '" 
					 + getKilometros() 
					 + "', tanqueCheio = '" 
					 + getTanqueCheio() 
 					 + "', kilometrosrodados = '" 
					 + getKmdif() 
					 + "', litrosgastos = '" 
					 + getLitros() 
					 + "' WHERE codigoGasto = " + getCodigoGasto();
		database.execBanco(sql01);
	}
	public void deletaAbastecimento(Context context){
		Sqlite database = new Sqlite(context);
		String sql01 = "DELETE FROM TB_ABASTECIMENTO WHERE codigoGasto = " + getCodigoGasto();
		database.execBanco(sql01);
	}
	
	public static List <Information> ConsultaAbastecimentos(Context context, String sql){
		Sqlite database = new Sqlite(context);
		Cursor cursor = database.consulta(sql);
		List <Information> Abastecimento = new ArrayList <>();
		cursor.moveToFirst();
		while (!cursor.isAfterLast()){
			Abastecimento newAbastecimento = new Abastecimento(context);
			newAbastecimento.setCodigoGasto(cursor.getInt(0));
			newAbastecimento.setIdCarro(cursor.getInt(1));
			newAbastecimento.setCombustivel(cursor.getInt(2));
			newAbastecimento.setLocalGasto(cursor.getInt(3));
			newAbastecimento.setValorGasto(cursor.getFloat(4));
			newAbastecimento.setValorLitro(cursor.getFloat(5));
			newAbastecimento.setDataGasto(cursor.getString(6));
			newAbastecimento.setKilometros(cursor.getInt(7));
			newAbastecimento.setTanqueCheio(cursor.getString(8));
			newAbastecimento.setKmdif(cursor.getInt(9));
			newAbastecimento.setLitros(cursor.getFloat(10));
			Abastecimento.add(newAbastecimento);
			cursor.moveToNext();
		}
		return Abastecimento;
	}

	public static int ContaAbastecimentos(Context context, int codigo){
		Sqlite sqlite = new Sqlite(context);
		Cursor cursor = sqlite.consulta(
				"Select count (*) from tb_abastecimento where idCarro = " + codigo +" ;"
		);
		cursor.moveToFirst();
		return cursor.getInt(0);
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
	public static int ConsultaUltimaKM (Context context, String sql){
		int km = 0;
		Sqlite database = new Sqlite(context);
		Cursor cursor = database.consulta(sql);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()){
			km = cursor.getInt(0);
			cursor.moveToNext();
		}
		return km;
	}

	public String mediaRodada(){
		DecimalFormat df = new DecimalFormat("0.00"); 
		return "Local: " + getLocalGasto() + "\n"
			   + "Data: " + tratadata()
			   + " Valor: " + NumberFormat.getCurrencyInstance().format(getValorGasto()) + "\n"
			   + "Km/l: " + df.format((getKmdif() / getLitros()));
	}
	
	public void AtualizakmRodado(Context context, int carro){
		String sql;
		sql = "SELECT * FROM TB_ABASTECIMENTO WHERE idCarro = " + carro + " ORDER BY dataGasto, codigoGasto";
		List<Information> abastecimentoList = Abastecimento.ConsultaAbastecimentos(context, sql);
		Abastecimento[] itens = abastecimentoList.toArray(new Abastecimento[0]);
		if (itens.length > 0 ){
			int i = itens.length - 1;
			for(int t = 0; t < itens.length; t++){
				if (t < i){
					itens[t].setKmdif(itens[t + 1].getKilometros() - itens[t].getKilometros());
					itens[t].setLitros(itens[t + 1].getValorGasto()/itens[t + 1].getValorLitro());
					if (itens[t+1].getTanqueCheio().equals("sim")){
						itens[t].alteraAbastecimento(context);
					}
				}		
			}	
			//limpa o ultima abastecimento
			if (itens[itens.length - 1].getKmdif() > 0 || itens[itens.length - 1].getLitros() > 0){
				itens[itens.length - 1].setKmdif(0);
				itens[itens.length - 1].setLitros(0);
				itens[itens.length - 1].alteraAbastecimento(context);
			}
		}
	}

    @Override
    public String getPrimaryText() {
        return "Valor: " + NumberFormat.getCurrencyInstance().format(getValorGasto());
    }

    @Override
    public String getSecondaryText() {
        return "Local: " + ConsultaLocal(getContext(), "SELECT *"
                + " FROM tb_local"
                + " WHERE codigoLOCAL = " + getLocalGasto()).getNome()
                + "\n"
                + "Data:  " + tratadata() +"\n";
    }

	public static int getMaxHodometro(Context context, Carro carro) {
		Sqlite sqlite = new Sqlite(context);
		String sql = "SELECT MAX(kilometros) FROM TB_ABASTECIMENTO WHERE idCarro = " + carro.getCodigo() +" ;";
		Cursor cursor = sqlite.consulta(sql);
		cursor.moveToFirst();
		return cursor.getInt(0);
	}
}
