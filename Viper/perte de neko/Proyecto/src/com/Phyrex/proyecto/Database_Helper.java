
package com.Phyrex.proyecto;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Database_Helper {
 
	public static final String Key_id = "_id";
	public static final String Key_name = "name";
	public static final String Key_raza = "raza";
	public static final String Key_color = "color";
	public static final String Key_date = "birthdate";
	private static final String Key_mac = "mac_adress";
	private static final String Key_death = "death";
	private static final String DB_name = "BD_1";
	private static final String DB_table = "Pet_data";

	private static final int DB_version= 1;
	
	private DBhelper helper;
	private final Context context;
	private SQLiteDatabase Database;
	
	private static class DBhelper extends SQLiteOpenHelper{

		public DBhelper(Context context) {
			super(context, DB_name, null, DB_version);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {		//creacion de la base de datos
														//SQLITE
			//db.execSQL("DROP TABLE IF EXISTS " + DB_table);
			db.execSQL("CREATE TABLE " + DB_table + " (" 
					+ Key_id + " INTEGER PRIMARY KEY AUTOINCREMENT, " 
					+ Key_name + " TEXT NOT NULL," + Key_raza + " VARCHAR(15),"+ Key_color + " VARCHAR(15),"
					+ Key_date + " DATETIME, " + Key_mac + " VARCHAR(18), "+ Key_death +" boolean);");
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			/*//Se ocupa para modificar las bases de datos existente, 
			 * sin que los usuarios pierdan información.
			 * EJ: Alter table cuando la version de bd actual es menor a 2
			if (oldVersion < 2) {
	           Programar un alter table aca!
	        }*/
		}
		
	}
	
	public Database_Helper (Context c){
		context = c;
	}
	
	public Database_Helper open_write(){
		helper = new DBhelper(context);
		Database = helper.getWritableDatabase();
		return this;
	}
	
	public Database_Helper open_read(){
		helper = new DBhelper(context);
		Database = helper.getReadableDatabase();
		return this;
	}
	
	public void close(){
		helper.close();
	}
	
	
	/*Métodos base para la manipulacion de datos
	 * */
	
	void deleteAll()
	{
		helper = new DBhelper(context);
		Database = helper.getWritableDatabase();
	    Database.delete(DB_table, null, null);
	    helper.close();
	}
	//agregar entrada nueva
	public void createEntry(String nombre, String datetime, String raza, String color, String mac_adress) {
		ContentValues cv = new ContentValues();
		cv.put(Key_name, nombre);
		cv.put(Key_date, datetime);
		cv.put(Key_raza, raza);
		cv.put(Key_color, color);
		cv.put(Key_mac, mac_adress);
		Database.insert(DB_table, null, cv);
	}
	//obtener datos por el nombre
	public Cursor getEntry(String nombre) {
		String select = "Select * From "+DB_table+" Where "+Key_name+" = '"+nombre+"'";
    	Cursor c = Database.rawQuery(select, null);
		return c;
	}
	//obtener todo
	public Cursor getAll() {
		String select = "Select *From "+DB_table;
		select = select + " ORDER BY "+Key_id+" DESC";
    	Cursor c = Database.rawQuery(select, null);
		return c;
	}
	//borrar por nombre
	public void delete(String query) {
		Database.delete(DB_table, Key_name + " = '"+query+"'", null);
	}
}
