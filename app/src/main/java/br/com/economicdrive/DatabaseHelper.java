package br.com.economicdrive;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {
	 
    private static final int DATABASE_VERSION = 2;
 
    public DatabaseHelper(Context context) {
        super(context, "EconomicDrive.sqlite", null, DATABASE_VERSION);
    }
    
    public void onCreate(SQLiteDatabase db) {
    	try 
    	{
    		//CRIA AS TABELAS DE SISTEMA
    		db.execSQL("CREATE TABLE tb_marcas(idMARCA INTEGER PRIMARY KEY AUTOINCREMENT,nomeMARCA VARCHAR(100))");
    		db.execSQL("CREATE TABLE tb_local(codigoLOCAL INTEGER PRIMARY KEY AUTOINCREMENT,nomeLOCAL VARCHAR(100),enderecoLOCAL VARCHAR(100))");
    		db.execSQL("CREATE TABLE tb_usuario(idUSUARIO INTEGER PRIMARY KEY AUTOINCREMENT,senhaUSUARIO VARCHAR(100))");
    		db.execSQL("CREATE TABLE TB_MODELOS(idMODELO INTEGER PRIMARY KEY AUTOINCREMENT,nomeMODELO VARCHAR(100),marcaMODELO INTEGER)");
    		db.execSQL("CREATE TABLE TB_CARRO(idCARRO INTEGER PRIMARY KEY AUTOINCREMENT,nomeCARRO VARCHAR(100),placaCARRO VARCHAR(100),kmCARRO INT, marcaCARRO int, modeloCARRO int, combCARRO INT, ativo VARCHAR(3))");
    			
    		db.execSQL("CREATE TABLE TB_ABASTECIMENTO("
    				+ "codigoGasto INTEGER PRIMARY KEY AUTOINCREMENT,"
    				+ "idCarro INTEGER,"
    				+ "idCombustivel INTEGER,"
    				+ "idLocal INT,"
    				+ "valorGasto NUMERIC(18,2),"
       				+ "valorLitro NUMERIC(9,2),"
    				+ "dataGasto DATE,"
    				+ "kilometros INT,"
    				+ "tanqueCheio VARCHAR(3),"
    				+ "kilometrosrodados INT,"
    				+ "litrosgastos NUMERIC(9,2),"
					+ "FOREIGN KEY(idCarro) REFERENCES TB_CARRO (idCARRO),"
					+ "FOREIGN KEY(idCombustivel) REFERENCES tb_Combustivel (idCOMBUSTIVEL),"
					+ "FOREIGN KEY(idLocal) REFERENCES tb_LOVSL (codigoLOCAL))");
    		
    		db.execSQL("CREATE TABLE tb_Combustivel(idCOMBUSTIVEL INTEGER PRIMARY KEY,nomeCOMBUSTIVEL VARCHAR(50))");
    		db.execSQL("CREATE TABLE tb_tipo_combustivel(idTPCOMBUSTIVEL INTEGER PRIMARY KEY,nomeTPCOMBUSTIVEL VARCHAR(50))");

//    		Tabela de Despesas
    		db.execSQL("CREATE TABLE TB_DESPESAS("
    				+ "codigoGasto INTEGER PRIMARY KEY AUTOINCREMENT,"
    				+ "idCarro INTEGER,"
    				+ "valorGasto NUMERIC(18,2),"
    				+ "localGasto INT,"
    				+ "dataGasto DATE,"
    				+ "descDespesa VARCHAR(100),"
    				+ "FOREIGN KEY(idCarro) REFERENCES TB_CARRO (idCARRO))");
    		
//    		Tabela de Manutencao
    		db.execSQL("CREATE TABLE TB_MANUTENCAO("
    				+ "codigoGasto INTEGER PRIMARY KEY AUTOINCREMENT,"
    				+ "idCarro INTEGER,"
    				+ "valorGasto NUMERIC(18,2),"
    				+ "localGasto INT,"
    				+ "dataGasto DATE,"
    				+ "tipoManutencao VARCHAR(100),"
    				+ "descManutencao VARCHAR(100),"
    				+ "FOREIGN KEY(idCarro) REFERENCES TB_CARRO (idCARRO))");
    		
//    		Tabela de Tipo de Manutenção
    		db.execSQL("CREATE TABLE TB_TP_MANUTENCAO("
    				+ "idManutencao INTEGER PRIMARY KEY AUTOINCREMENT,"
    				+ "tipoManutencao VARCHAR(100))");
    		
    		//INSERE OS REGISTROS PADRÕES
	    		//MARCAS
	    		db.execSQL("INSERT INTO TB_MARCAS (nomeMARCA) SELECT 'Agrale'");
	    		db.execSQL("INSERT INTO TB_MARCAS (nomeMARCA) SELECT 'Aston Martin'");
	    		db.execSQL("INSERT INTO TB_MARCAS (nomeMARCA) SELECT 'Audi'");
	    		db.execSQL("INSERT INTO TB_MARCAS (nomeMARCA) SELECT 'Bentley'");
	    		db.execSQL("INSERT INTO TB_MARCAS (nomeMARCA) SELECT 'BMW'");
	    		db.execSQL("INSERT INTO TB_MARCAS (nomeMARCA) SELECT 'Changan'");
	    		db.execSQL("INSERT INTO TB_MARCAS (nomeMARCA) SELECT 'Chery'");
	    		db.execSQL("INSERT INTO TB_MARCAS (nomeMARCA) SELECT 'GM/Chevrolet'");
	    		db.execSQL("INSERT INTO TB_MARCAS (nomeMARCA) SELECT 'Chrysler'");
	    		db.execSQL("INSERT INTO TB_MARCAS (nomeMARCA) SELECT 'Citroën'");
	    		db.execSQL("INSERT INTO TB_MARCAS (nomeMARCA) SELECT 'Dodge'");
	    		db.execSQL("INSERT INTO TB_MARCAS (nomeMARCA) SELECT 'Effa'");
	    		db.execSQL("INSERT INTO TB_MARCAS (nomeMARCA) SELECT 'Ferrari'");
	    		db.execSQL("INSERT INTO TB_MARCAS (nomeMARCA) SELECT 'Fiat'");
	    		db.execSQL("INSERT INTO TB_MARCAS (nomeMARCA) SELECT 'Ford'");
	    		db.execSQL("INSERT INTO TB_MARCAS (nomeMARCA) SELECT 'Geely'");
	    		db.execSQL("INSERT INTO TB_MARCAS (nomeMARCA) SELECT 'Hafei'");
	    		db.execSQL("INSERT INTO TB_MARCAS (nomeMARCA) SELECT 'Honda'");
	    		db.execSQL("INSERT INTO TB_MARCAS (nomeMARCA) SELECT 'Hyundai'");
	    		db.execSQL("INSERT INTO TB_MARCAS (nomeMARCA) SELECT 'Iveco'");
	    		db.execSQL("INSERT INTO TB_MARCAS (nomeMARCA) SELECT 'Jac Motors'");
	    		db.execSQL("INSERT INTO TB_MARCAS (nomeMARCA) SELECT 'Jaguar'");
	    		db.execSQL("INSERT INTO TB_MARCAS (nomeMARCA) SELECT 'Jeep'");
	    		db.execSQL("INSERT INTO TB_MARCAS (nomeMARCA) SELECT 'Jinbei'");
	    		db.execSQL("INSERT INTO TB_MARCAS (nomeMARCA) SELECT 'Kia'");
	    		db.execSQL("INSERT INTO TB_MARCAS (nomeMARCA) SELECT 'Lamborghini'");
	    		db.execSQL("INSERT INTO TB_MARCAS (nomeMARCA) SELECT 'Land Rover'");
	    		db.execSQL("INSERT INTO TB_MARCAS (nomeMARCA) SELECT 'Lifan'");
	    		db.execSQL("INSERT INTO TB_MARCAS (nomeMARCA) SELECT 'Mahindra'");
	    		db.execSQL("INSERT INTO TB_MARCAS (nomeMARCA) SELECT 'Maserati'");
	    		db.execSQL("INSERT INTO TB_MARCAS (nomeMARCA) SELECT 'Mercedes-Benz'");
	    		db.execSQL("INSERT INTO TB_MARCAS (nomeMARCA) SELECT 'MG Motors'");
	    		db.execSQL("INSERT INTO TB_MARCAS (nomeMARCA) SELECT 'Mini'");
	    		db.execSQL("INSERT INTO TB_MARCAS (nomeMARCA) SELECT 'Mitsubishi'");
	    		db.execSQL("INSERT INTO TB_MARCAS (nomeMARCA) SELECT 'Nissan'");
	    		db.execSQL("INSERT INTO TB_MARCAS (nomeMARCA) SELECT 'Peugeot'");
	    		db.execSQL("INSERT INTO TB_MARCAS (nomeMARCA) SELECT 'Porsche'");
	    		db.execSQL("INSERT INTO TB_MARCAS (nomeMARCA) SELECT 'Ram'");
	    		db.execSQL("INSERT INTO TB_MARCAS (nomeMARCA) SELECT 'Renault'");
	    		db.execSQL("INSERT INTO TB_MARCAS (nomeMARCA) SELECT 'Smart'");
	    		db.execSQL("INSERT INTO TB_MARCAS (nomeMARCA) SELECT 'Ssangyong'");
	    		db.execSQL("INSERT INTO TB_MARCAS (nomeMARCA) SELECT 'Subaru'");
	    		db.execSQL("INSERT INTO TB_MARCAS (nomeMARCA) SELECT 'Suzuki'");
	    		db.execSQL("INSERT INTO TB_MARCAS (nomeMARCA) SELECT 'Toyota'");
	    		db.execSQL("INSERT INTO TB_MARCAS (nomeMARCA) SELECT 'Troller'");
	    		db.execSQL("INSERT INTO TB_MARCAS (nomeMARCA) SELECT 'Volkswagen'");
	    		db.execSQL("INSERT INTO TB_MARCAS (nomeMARCA) SELECT 'Volvo'");
	    		db.execSQL("INSERT INTO TB_MARCAS (nomeMARCA) SELECT 'Outra'");

	    		//MODELOS
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Marruá',1");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'DB9 Volante',2");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'DBS Coupe',2");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'DBS Volante',2");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Rapide',2");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'V12 Vantage Coupe',2");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'V8 Vantage Coupe',2");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'V8 Vantage Roadster',2");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Virage',2");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'A1',3");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'A3',3");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'A3 Sedan',3");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'A4 Avant',3");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'A4 Sedã',3");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'A5',3");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'A7',3");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'A8',3");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Q3',3");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Q5',3");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Q7',3");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'R8',3");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'R8 GT',3");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'RS 3 Sportback ',3");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'RS 5',3");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'RS6 Avant',3");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'TT Coupé',3");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'TT Roadster',3");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Continental Flying Spur',4");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Continental Supersports Coupé',4");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Série 1',5");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Série 1 Cabrio',5");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Série 1 Coupé',5");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Série 1 M',5");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Série 3 Cabrio',5");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Série 3 M3 Coupé',5");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Série 3 M3 Sedã',5");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Série 3 Sedã',5");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Série 5 Gran Turismo',5");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Série 5 Sedã',5");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Série 7 Sedã',5");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'X1',5");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'X3',5");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'X5',5");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'X6',5");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Z4 Roadster',5");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'i3',5");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Chana Cargo',6");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Chana Family',6");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Chana Utility',6");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Celer Hatch',7");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Celer Sedan',7");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Cielo Hatch',7");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Cielo Sedan',7");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Face',7");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'QQ',7");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'S-18',7");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Tiggo',7");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Agile',8");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Astra Hatch',8");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Astra Sedan',8");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Blazer',8");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Camaro',8");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Captiva',8");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Celta',8");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Classic',8");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Cobalt',8");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Corsa Hatch',8");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Corsa Sedã',8");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Cruze ',8");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Cruze Sport6',8");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Malibu',8");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Meriva',8");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Montana',8");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Omega',8");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Onix',8");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Prisma',8");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'S10',8");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Sonic',8");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Spin',8");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Tracker ',8");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Trailblazer',8");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Vectra',8");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Vectra GT',8");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Zafira',8");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT '300C',9");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Town & Country ',9");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Aircross',10");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'C3',10");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'C3 Picasso',10");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'C4',10");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'C4 Lounge',10");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'C4 Pallas',10");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'C4 Picasso',10");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'C5',10");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'C5 Tourer',10");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'DS3',10");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'DS5',10");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Jumper',10");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Xsara Picasso',10");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Journey ',11");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Effa Hafei Furgão ',12");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Effa Hafei Picape Baú',12");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Effa Hafei Picape Cabine Dupla ',12");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Effa Hafei Picape Cabine Simples ',12");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Effa Hafei Van',12");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Effa M100',12");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT '458',13");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'California',13");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'F599 GTO',13");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT '500',14");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Bravo',14");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Doblô',14");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Doblô Cargo',14");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Ducato',14");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Fiorino',14");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Freemont',14");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Grand Siena',14");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Idea',14");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Linea',14");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Mille',14");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Palio',14");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Palio Adventure',14");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Palio Weekend',14");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Punto',14");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Siena EL',14");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Strada',14");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Uno',14");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Courier',15");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'EcoSport',15");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Edge',15");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'F-250',15");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Fiesta Rocam Hatch',15");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Fiesta Rocam Sedan',15");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Focus Hatch',15");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Focus Sedan',15");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Fusion ',15");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Ka',15");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Ka+',15");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'New Fiesta',15");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'New Fiesta Hatch',15");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Ranger',15");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Transit',15");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'EC7',16");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Pick-up',17");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Towner Furgão',17");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Towner Jr',17");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Towner Passageiro',17");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Accord',18");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'CR-V',18");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'City',18");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Civic',18");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Civic Si',18");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Fit',18");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Azera',19");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Equus',19");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'HB20',19");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'HB20S',19");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'HB20X',19");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'HR',19");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Santa Fe',19");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Sonata',19");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Tucson',19");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Veloster',19");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Veracruz',19");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'i30',19");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'i30 CW',19");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'iX35',19");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Daily 35S14',20");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'J2',21");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'J3',21");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'J3 Turin',21");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'J5',21");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'J6',21");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'F-Type Coup?',22");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'XF',22");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'XJ Supersport',22");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'XKR',22");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Cherokee',23");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Grand Cherokee',23");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Wrangler',23");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Topic Furgão',24");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Topic Passageiro',24");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Bongo',25");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Cadenza',25");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Carens',25");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Carnival',25");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Cerato',25");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Mohave',25");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Optima',25");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Picanto',25");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Sorento',25");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Soul',25");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Sportage',25");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Gallardo LP 560 - 4',26");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Gallardo LP 560 - 4 Spyder ',26");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Defender',27");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Discovery 4',27");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Freelander 2',27");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Range Rover Evoque',27");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Range Rover Sport',27");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Range Rover Vogue',27");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT '320',28");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT '530',28");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT '620',28");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'X60',28");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Mahindra Picape',29");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Mahindra SUV',29");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Ghibli',30");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Gran Cabrio',30");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Gran Turismo',30");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Quattroporte',30");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'CLA',31");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Classe A',31");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Classe B',31");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Classe C',31");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Classe C 250 Turbo Sport ',31");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Classe C 63 AMG Touring',31");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Classe CL',31");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Classe CLS 63 AMG',31");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Classe E',31");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Classe G',31");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Classe GL',31");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Classe GLK',31");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Classe M',31");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Classe S',31");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Classe S 400 Hybrid',31");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Classe SLK',31");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Classe SLS AMG',31");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'MG 550',32");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'MG 6',32");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Cooper',33");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Cooper Cabrio ',33");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Cooper Countryman ',33");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Cooper S Clubman-Hampton',33");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'One',33");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'ASX',34");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'L200 Outdoor',34");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'L200 Savana',34");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'L200 Triton',34");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Lancer Evolution X',34");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Outlander',34");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Pajero Dakar',34");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Pajero Full ',34");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Pajero Sport',34");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Pajero TR4',34");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Frontier',35");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Grand Livina',35");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Livina',35");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'March',35");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Sentra',35");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Tiida Hatch',35");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Tiida Sedan',35");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Versa',35");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT '207',36");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT '207 SW',36");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT '207 Sedan',36");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT '208',36");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT '3008',36");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT '307',36");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT '308',36");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT '308 CC',36");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT '408',36");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT '508',36");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Boxer',36");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Hoggar',36");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Partner',36");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'RCZ',36");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT '911',37");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Boxster',37");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Boxster S',37");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Cayenne',37");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Cayman',37");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Cayman S',37");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Macan',37");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Panamera',37");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT '2500',38");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Clio',39");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Duster',39");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Fluence',39");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Grand Tour',39");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Kangoo Express',39");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Logan',39");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Master',39");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Sandero',39");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Sandero Stepway',39");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Symbol',39");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Fortwo MHD ',40");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Fortwo Passion Cabrio',40");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Fortwo Passion Coupé ',40");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Actyon Sports',41");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Korando',41");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Kyron',41");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Forester',42");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Impreza Hatch',42");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Impreza Sed?',42");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Legacy',42");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Outback',42");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Tribeca',42");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Grand Vitara',43");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Jimny',43");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'SX4',43");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Camry',44");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Corolla',44");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Etios Hatch',44");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Etios Sedã',44");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Hilux',44");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Prius',44");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'RAV4',44");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'SW4',44");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'T4 ',45");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Amarok',46");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Crossfox',46");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Fox',46");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Fusca',46");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Gol',46");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Gol G4',46");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Golf',46");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Jetta ',46");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Jetta Variant',46");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Kombi',46");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Parati',46");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Passat',46");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Passat Variant',46");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Polo',46");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Polo Sedan',46");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Saveiro',46");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Space Cross',46");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'SpaceFox',46");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Tiguan',46");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Touareg',46");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Up!',46");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'Voyage',46");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'C30',47");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'S60',47");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'V40',47");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'XC60 ',47");
	    		db.execSQL("INSERT INTO TB_MODELOS (nomeMODELO, marcaMODELO) SELECT 'XC90',47");
	    		
	    		//Tipos de combustiveis para carros
	    		db.execSQL("INSERT INTO tb_tipo_combustivel (nomeTPCOMBUSTIVEL) SELECT ('Gasolina')");
	    		db.execSQL("INSERT INTO tb_tipo_combustivel (nomeTPCOMBUSTIVEL) SELECT ('Álcool')");
				db.execSQL("INSERT INTO tb_tipo_combustivel (nomeTPCOMBUSTIVEL) SELECT ('Flex')");
				db.execSQL("INSERT INTO tb_tipo_combustivel (nomeTPCOMBUSTIVEL) SELECT ('Diesel')");
				
				db.execSQL("INSERT INTO tb_Combustivel (nomeCOMBUSTIVEL) SELECT ('Álcool')");
				db.execSQL("INSERT INTO tb_Combustivel (nomeCOMBUSTIVEL) SELECT ('Álcool Aditivado')");
				db.execSQL("INSERT INTO tb_Combustivel (nomeCOMBUSTIVEL) SELECT ('Diesel')");
				db.execSQL("INSERT INTO tb_Combustivel (nomeCOMBUSTIVEL) SELECT ('Gasolina')");
				db.execSQL("INSERT INTO tb_Combustivel (nomeCOMBUSTIVEL) SELECT ('Gasolina Aditivada')");
				
				//Tipos Manutencao
	    		db.execSQL("INSERT INTO TB_TP_MANUTENCAO (tipoManutencao) SELECT ('Preventiva')");
	    		db.execSQL("INSERT INTO TB_TP_MANUTENCAO (tipoManutencao) SELECT ('Corretiva')");
			    		
    	}catch (SQLException e){
            Log.e("Erro ao criar as tabelas e testar os dados", e.toString());
        }
    }

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}
}
