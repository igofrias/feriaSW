package com.Phyrex.VIPeR;

import java.util.ArrayList;
import java.util.List;
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
	
	//Elementos Achievement
	private static final String DB_table_ach = "Achievement_data";
	
	private static final String Key_id_ach = "_id";
	public static final String Key_name_ach = "name";
	public static final String Key_desc_ach = "desc";
	public static final String Key_done = "done";			
	public static final String Key_img = "img";	
	
	//Elementos Estadisticas
	private static final String DB_table_est = "Statistics_data";
	
	public static final String Key_id_est = "_id";
	public static final String Key_name_est = "name";
	public static final String Key_desc_est = "desc";
	public static final String Key_cant_est = "amount";
	public static final String Key_imgstat = "imgstat";
	
	//Elementos juegos
		private static final String DB_table_games = "Games_data";
		
		public static final String Key_id_game = "_id";
		public static final String Key_name_game = "name";
		public static final String Key_desc_game = "desc";
		public static final String Key_img_game = "img";
		public static final String Key_score_game = "score";
		
	
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
					+ Key_name + " TEXT NOT NULL," 
					+ Key_raza + " VARCHAR(15),"
					+ Key_color + " VARCHAR(15),"
					+ Key_date + " DATETIME, " 
					+ Key_mac + " VARCHAR(18), "
					+ Key_death +" INTEGER NOT NULL);");
			
			//db.execSQL("DROP TABLE IF EXISTS " + DB_table_ach);
			db.execSQL("CREATE TABLE " + DB_table_ach + " (" 
					+ Key_id_ach + " INTEGER PRIMARY KEY AUTOINCREMENT, " 
					+ Key_name_ach + " TEXT NOT NULL, " 
					+ Key_desc_ach + " TEXT NOT NULL, "
					+ Key_done + " INTEGER NOT NULL, "
					+ Key_img + " INTEGER NOT NULL);");	
			
			//db.execSQL("DROP TABLE IF EXISTS " + DB_table_est);
			db.execSQL("CREATE TABLE " + DB_table_est + " (" 
					+ Key_id_est + " INTEGER PRIMARY KEY AUTOINCREMENT, "
					+ Key_name_est + " TEXT NOT NULL," 
					+ Key_desc_est + " TEXT NOT NULL,"
					+ Key_cant_est + " INTEGER NOT NULL,"
					+ Key_imgstat + " INTEGER NOT NULL);");
			
			//db.execSQL("DROP TABLE IF EXISTS " + DB_table_est);
			db.execSQL("CREATE TABLE " + DB_table_games + " (" 
					+ Key_id_game + " INTEGER PRIMARY KEY AUTOINCREMENT, "
					+ Key_name_game + " TEXT NOT NULL," 
					+ Key_desc_game + " TEXT NOT NULL,"
					+ Key_img_game + " INTEGER NOT NULL,"
					+ Key_score_game + " INTEGER NOT NULL);");
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
	public void createEntry(String nombre, String datetime, String raza, String color, String mac_adress, int death) {
		ContentValues cv = new ContentValues();
		cv.put(Key_name, nombre);
		cv.put(Key_date, datetime);
		cv.put(Key_raza, raza);
		cv.put(Key_color, color);
		cv.put(Key_mac, mac_adress);
		cv.put(Key_death, death);
		Database.insert(DB_table, null, cv);
	}
	//obtener datos por el nombre
	public Cursor getEntry(String nombre) {
		String select = "Select * From "+DB_table+" Where "+Key_name+" = '"+nombre+"'";
    	Cursor c = Database.rawQuery(select, null);
		return c;
	}
	
	//obtener datos por el nombre
	public Cursor updatePetMac(String nombre, String Mac) {
		String select = "Update " + DB_table + " SET  "+ Key_mac + " = '" + Mac +"' WHERE " + Key_name + "= '" + nombre + "';";
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
	
	//obtener todo
	public Cursor getPetByName(String name) {
			String select = "Select * From "+DB_table + "where" + Key_name +"=" + name;
			select = select + " ORDER BY "+Key_id+" DESC";
	    	Cursor c = Database.rawQuery(select, null);
			return c;
	}
	

	public List<Pet> getPets() {
		List<Pet> listamascotas = new ArrayList<Pet>();
		// Select All Query
		String selectQuery = "SELECT * FROM " + DB_table + " WHERE " + Key_death + " = 0  ORDER BY " + Key_id + " ASC";

		helper = new DBhelper(context);
		Database = helper.getWritableDatabase();
		Cursor cursor = Database.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				Pet pet = new Pet();
				pet.set_id(Integer.parseInt(cursor.getString(0)));
				pet.set_name(cursor.getString(1));
				pet.set_raza(cursor.getString(2));
				pet.set_color(cursor.getString(3));
				pet.set_birthdate(cursor.getString(4));
				pet.set_mac(cursor.getString(5));
				pet.set_death(Integer.parseInt(cursor.getString(6)));
				// agregar mascota a lista
				listamascotas.add(pet);
			} while (cursor.moveToNext());
		}
		
		//Database.close();
		
		return listamascotas;
	}
	//borrar por nombre
	public void delete(String query) {
		Database.delete(DB_table, Key_name + " = '"+query+"'", null);
	}
	
	//Agregar Logro
	public void createAchievement(String name, String desc, int done, int img) {
		ContentValues cv = new ContentValues();
		cv.put(Key_name_ach, name);
		cv.put(Key_desc_ach, desc);
		cv.put(Key_done, done);
		cv.put(Key_img, img);
		Database.insert(DB_table_ach, null, cv);
	}	

	//Agregar Indicador en estadística
	public void createStatistics(String nombre, String desc, int amount, int imgstat) {
		ContentValues cv = new ContentValues();
		cv.put(Key_name_est, nombre);
		cv.put(Key_desc_est, desc);
		cv.put(Key_cant_est, amount);
		cv.put(Key_imgstat, imgstat);
		Database.insert(DB_table_est, null, cv);
	}
	
	//Agregar Indicador en Games
		public void createGames(String nombre, String desc, int img, int score) {
			ContentValues cv = new ContentValues();
			cv.put(Key_name_game, nombre);
			cv.put(Key_desc_game, desc);
			cv.put(Key_img_game, img);
			cv.put(Key_score_game, score);
			Database.insert(DB_table_games, null, cv);
		}
	
	//Obtener Achievement por nombre
	public Cursor getAchievement(String name) {
		String select = "Select * From "+DB_table_ach+" Where "+Key_name_ach+" = '"+name+"'";
    	Cursor c = Database.rawQuery(select, null);
		return c;
	}	
	
	//Obtener Indicador Estadístico por nombre
	public Cursor getStatistics(String name) {
		String select = "Select * From "+DB_table_est+" Where "+Key_name_est+" = '"+name+"'";
    	Cursor c = Database.rawQuery(select, null);
		return c;
	}
	
	//Obtener todas los achievements existentes
	public Cursor getAllAchievements() {
		String select = "Select * From "+DB_table_ach;
		select = select + " ORDER BY "+Key_id+" ASC";
    	Cursor c = Database.rawQuery(select, null);
		return c;
	}
	
	
	//Obtener todas las estadísticas existentes
	public Cursor getAllStatistics() {
		String select = "Select * From "+DB_table_est;
		select = select + " ORDER BY "+Key_id+" ASC";
    	Cursor c = Database.rawQuery(select, null);
		return c;
	}
	
	//Obtener todas las estadísticas existentes
		public Cursor getAllGames() {
			String select = "Select * From "+DB_table_games;
			select = select + " ORDER BY "+Key_id+" ASC";
	    	Cursor c = Database.rawQuery(select, null);
			return c;
		}
		
		//Obtener todas las estadísticas existentes
		public Cursor getGame(int id) {
			String select = "Select * From "+DB_table_games;
			select = select + " WHERE _id="+id;
			Cursor c = Database.rawQuery(select, null);
			return c;
		}
		
		//Modifica Estadísticas Actuales
		public void updateHighScore(int id, int score){
		    ContentValues cv = new ContentValues();
		    cv.put("_id", id);
			cv.put(Key_score_game, score);
		    Database.update(DB_table_games, cv, "_id=" + id, null);   
		}
	
	//Confirma Realización de achievement.
	//Cambios esperados: 
	//Inicial: Hecho = 0
	//Final: Hecho = 1
	public void confirmAchievement(int id, String name, String desc, int done){
	    ContentValues cv = new ContentValues();
	    cv.put("_id", id);
		cv.put(Key_name_ach, name);
		cv.put(Key_desc_ach, desc);
		cv.put(Key_done, done);
	    Database.update(DB_table_ach, cv, "_id=" + id, null);   
	}
	
	//Modifica Estadísticas Actuales
	public void modifyStatistics(int id, String name, String desc, int amount){
	    ContentValues cv = new ContentValues();
	    cv.put("_id", id);
		cv.put(Key_name_est, name);
		cv.put(Key_desc_est, desc);
		cv.put(Key_cant_est, amount);
	    Database.update(DB_table_est, cv, "_id=" + id, null);   
	}
	
	
	
}
