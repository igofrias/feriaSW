package com.example.estach;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class achievements {

	public static final String Key_id = "_id";
	public static final String Key_name = "name";
	public static final String Key_tipo = "tipo";
	private static final String Key_descripcion = "descripcion";
	private static final String DB_name = "BD_1";
	private static final String DB_table = "Achievements";
	
	private static final int DB_version= 1;
	
	private static class DBhelper extends SQLiteOpenHelper{

		public DBhelper(Context context) {
			super(context, DB_name, null, DB_version);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {		//creacion de la base de datos
														//SQLITE
			db.execSQL("DROP TABLE IF EXISTS " + DB_table);
			db.execSQL("CREATE TABLE " + DB_table + " (" 
					+ Key_id + " INTEGER PRIMARY KEY AUTOINCREMENT, " 
					+ Key_name + " TEXT NOT NULL," + Key_tipo + " VARCHAR(15),"
					+ Key_descripcion + " VARCHAR(50), hecho INT NOT NULL;");
		}
		
		

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
			
		}	
		
	
	}
}
