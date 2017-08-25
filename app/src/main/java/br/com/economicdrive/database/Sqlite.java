package br.com.economicdrive.database;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class Sqlite {
	
	SQLiteDatabase sqlite = null;
	DatabaseHelper helper;
	
	public Sqlite(Context context) {          
		helper = new DatabaseHelper(context);
	}
	public void execBanco(String sqlquery){
        sqlite = helper.getReadableDatabase();
        sqlite.execSQL(sqlquery);
	}
	
	public Cursor consulta(String sqlquery){
		sqlite = helper.getReadableDatabase();
		Cursor cursor = sqlite.rawQuery(sqlquery, null);
		return cursor;
	}
}
