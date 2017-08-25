package br.com.economicdrive.model;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import br.com.economicdrive.Information;


public abstract class Gastos implements Parcelable, Information {
	private int codigoGasto;
	private int idCarro;
	private float valorGasto;
	private int localGasto;
	private String dataGasto;
	private Context context;
	
	public Gastos(){}

	public Gastos(Context context){
		this.context = context;
	}


	public Gastos (Parcel in){
		codigoGasto = in.readInt();
		idCarro     = in.readInt();
		valorGasto  = in.readFloat();
		localGasto  = in.readInt();
		dataGasto 	= in.readString();
	}
	public String getDataGasto() {
		return dataGasto;
	}
	public void setDataGasto(String dataGasto) {
		if (dataGasto.indexOf("/") > 0){
			this.dataGasto = dataGasto.substring(6,10) + "-" + dataGasto.subSequence(3, 5) + "-"  + dataGasto.subSequence(0,2);
		}else{
			this.dataGasto  = dataGasto;
		}
	}
	public int getCodigoGasto() {
		return codigoGasto;
	}
	public void setCodigoGasto(int codigoGasto) {
		this.codigoGasto = codigoGasto;
	}
	public float getValorGasto() {
		return valorGasto;
	}
	public void setValorGasto(float valorGasto) {
		this.valorGasto = valorGasto;
	}
	public int getLocalGasto() {
		return localGasto;
	}
	public void setLocalGasto(int localGasto) {
		this.localGasto = localGasto;
	}
	public int getIdCarro() {
		return idCarro;
	}
	public void setIdCarro(int idCarro) {
		this.idCarro = idCarro;
	}
	public void setContext(Context context){this.context = context;}
    protected Context getContext (){
        return this.context;
    }
	
	public String tratadata(){	
		return getDataGasto().substring(8,10) + "/" + getDataGasto().subSequence(5, 7) + "/" + getDataGasto().subSequence(0, 4);
	}
	
}
