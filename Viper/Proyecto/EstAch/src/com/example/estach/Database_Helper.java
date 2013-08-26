package com.example.estach;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Database_Helper {
	
	private static final String DB_name = "BD_1";
	
	//Elementos Mascota
	public static final String Key_id = "_id";
	public static final String Key_name = "name";
	public static final String Key_raza = "raza";
	public static final String Key_color = "color";
	public static final String Key_date = "birthdate";
	private static final String Key_mac = "mac_adress";
		
	private static final String DB_table = "Pet_data";
	
	//Elementos Achievement
	private static final String Key_id_ach = "_id";
	public static final String Key_name_ach = "name";
	public static final String Key_desc_ach = "descripcion";
	public static final String Key_hecho = "hecho";
	private static final String DB_table_ach = "Achievement_data";	
	
	//Elementos Estadisticas
	private static final String Key_id_est = "_id";
	public static final String Key_name_est = "name";
	public static final String Key_desc_est = "descripcion";
	public static final String Key_cant_est = "cantidad";
	private static final String DB_table_est = "Estadisticas_data";			

	private static final int DB_version= 1;
	
	private DBhelper helper;
	private final Context context;
	private SQLiteDatabase Database;
	
	public static class DBhelper extends SQLiteOpenHelper{

		public DBhelper(Context context) {
			super(context, DB_name, null, DB_version);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {		//creacion de la base de datos
														//SQLITE
			db.execSQL("DROP TABLE IF EXISTS " + DB_table);
			db.execSQL("CREATE TABLE " + DB_table + " (" 
					+ Key_id + " INTEGER PRIMARY KEY AUTOINCREMENT, " 
					+ Key_name + " TEXT NOT NULL," 
					+ Key_raza + " VARCHAR(15),"
					+ Key_color + " VARCHAR(15),"
					+ Key_date + " DATETIME, " 
					+ Key_mac + " VARCHAR(18));");

			db.execSQL("DROP TABLE IF EXISTS " + DB_table_ach);
			db.execSQL("CREATE TABLE " + DB_table_ach + " (" 
					+ Key_id_ach + " INTEGER PRIMARY KEY AUTOINCREMENT, " 
					+ Key_name_ach + " TEXT NOT NULL, " 
					+ Key_desc_ach + " TEXT NOT NULL, "
					+ Key_hecho + " INTEGER NOT NULL);");	
			
			db.execSQL("DROP TABLE IF EXISTS " + DB_table_est);
			db.execSQL("CREATE TABLE " + DB_table_est + " (" 
					+ Key_id_est + " INTEGER PRIMARY KEY AUTOINCREMENT, " 
					+ Key_name_est + " TEXT NOT NULL," 
					+ Key_desc_est + " TEXT NOT NULL,"
					+ Key_cant_est + " INTEGER NOT NULL);");
			
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
	    Database.delete(DB_table_ach, null, null);
	    Database.delete(DB_table_est, null, null);
	    helper.close();
	}
	
	//Agregar mascota
	public void createPet(String nombre, String datetime, String raza, String color, String mac_adress) {
		ContentValues cv = new ContentValues();
		cv.put(Key_name, nombre);
		cv.put(Key_date, datetime);
		cv.put(Key_raza, raza);
		cv.put(Key_color, color);
		cv.put(Key_mac, mac_adress);
		Database.insert(DB_table, null, cv);
	}
	
	//agregar achievement
	public void createAchievement(String nombre, String descripcion, int hecho) {
		ContentValues cv = new ContentValues();
		cv.put(Key_name_ach, nombre);
		cv.put(Key_desc_ach, descripcion);
		cv.put(Key_hecho, hecho);
		Database.insert(DB_table_ach, null, cv);
	}	

	//agregar indicador en estadística
	public void createEstadistica(String nombre, String descripcion, int cantidad) {
		ContentValues cv = new ContentValues();
		cv.put(Key_name_est, nombre);
		cv.put(Key_desc_est, descripcion);
		cv.put(Key_cant_est, cantidad);
		Database.insert(DB_table_est, null, cv);
	}
	
	//obtener mascota por el nombre
	public Cursor getPet(String nombre) {
		String select = "Select * From "+DB_table+" Where "+Key_name+" = '"+nombre+"'";
    	Cursor c = Database.rawQuery(select, null);
		return c;
	}
	
	//Obtener achievement por nombre
	public Cursor getAchievement(String nombre) {
		String select = "Select * From "+DB_table_ach+" Where "+Key_name_ach+" = '"+nombre+"'";
    	Cursor c = Database.rawQuery(select, null);
		return c;
	}	
	
	//Obtener Indicador Estadístico por nombre
	public Cursor getEstadistica(String nombre) {
		String select = "Select * From "+DB_table_est+" Where "+Key_name_est+" = '"+nombre+"'";
    	Cursor c = Database.rawQuery(select, null);
		return c;
	}	
	
	//Obtener todas las mascotas creadas
	public Cursor getAll() {
		String select = "Select * From "+DB_table;
		select = select + " ORDER BY "+Key_id+" DESC";
    	Cursor c = Database.rawQuery(select, null);
		return c;
	}
	
	
	public List<LogroBD> getAllDoneAchievements() {
		List<LogroBD> listaLogros = new ArrayList<LogroBD>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + DB_table_ach + " Where " + Key_hecho + " = 1";

		Cursor cursor = Database.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				LogroBD logro = new LogroBD();
				logro.setID(Integer.parseInt(cursor.getString(0)));
				logro.setName(cursor.getString(1));
				logro.setDescripcion(cursor.getString(2));
				logro.setHecho(Integer.parseInt(cursor.getString(3)));
				// Adding contact to list
				listaLogros.add(logro);
			} while (cursor.moveToNext());
		}
		Database.close();
		// return contact list
		return listaLogros;
	}
	
	public List<LogroBD> getAllDoneEstadisticas() {
		List<LogroBD> listaEstadisticas = new ArrayList<LogroBD>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + DB_table_est + " Where " + Key_cant_est + " >= 1";

		Cursor cursor = Database.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				LogroBD estad = new LogroBD();
				estad.setID(Integer.parseInt(cursor.getString(0)));
				estad.setName(cursor.getString(1));
				estad.setDescripcion(cursor.getString(2));
				estad.setHecho(Integer.parseInt(cursor.getString(3)));
				// Adding contact to list
				listaEstadisticas.add(estad);
			} while (cursor.moveToNext());
		}
		Database.close();
		// return contact list
		return listaEstadisticas;
	}

	//Obtener todas los achievements existentes
	public Cursor getAllAchievements() {
		String select = "Select * From "+DB_table_ach;
		select = select + " ORDER BY "+Key_id+" DESC";
    	Cursor c = Database.rawQuery(select, null);
		return c;
	}
	
	
	//Obtener todas las estadísticas existentes
	public Cursor getAllEstadisticas() {
		String select = "Select * From "+DB_table_est;
		select = select + " ORDER BY "+Key_id+" DESC";
    	Cursor c = Database.rawQuery(select, null);
		return c;
	}	
	
	
	//Confirma realización de achievement
	public void confirmarAchievement(int id, String nombre, String descripcion, int hecho){
	    ContentValues cv = new ContentValues();
	    cv.put("_id", id);
		cv.put(Key_name_ach, nombre);
		cv.put(Key_desc_ach, descripcion);
		cv.put(Key_hecho, hecho);
	    Database.update(DB_table_ach, cv, "_id=" + id, null);   
	}
	
	//Confirma realización de achievement
	public void modificarEstadistica(int id, String nombre, String descripcion, int cantidad){
	    ContentValues cv = new ContentValues();
	    cv.put("_id", id);
		cv.put(Key_name_est, nombre);
		cv.put(Key_desc_est, descripcion);
		cv.put(Key_cant_est, cantidad);
	    Database.update(DB_table_est, cv, "_id=" + id, null);   
	}
	
}
