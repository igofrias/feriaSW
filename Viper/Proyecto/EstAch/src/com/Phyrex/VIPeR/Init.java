package com.Phyrex.VIPeR;

import android.content.Context;
import android.database.Cursor;

/******************************************
 * Inicializa lista de logros y estadísticas
 * totales en la BD
 * Necesaria al momento de crear una mascota

*******************************************/


public class Init {
	
	private final Context context;
	
	public Init (Context c){
		context = c;
	}

	public static final String ach1 = "Nacido para ser salvaje";	
	public static final String ach1_desc = "Crea su primera mascota";
	public static final String ach2 = "Primeras mordidas";	
	public static final String ach2_desc = "Alimentó ya 5 veces a su mascota";
	public static final String ach3 = "Jugueton";	
	public static final String ach3_desc = "Ha jugado 4 veces con el amo";
	public static final String ach4 = "Obediencia Inicial";	
	public static final String ach4_desc = "Ha hecho caso a 3 instrucciones de su amo";

	public static final int done = 0;

	public static final String est1 = "Comer";
	public static final String est1_desc = "Cuantas veces la ha alimentado?";
	public static final String est2 = "Dormir";
	public static final String est2_desc = "Cuantas veces ha dormido?";
	public static final String est3 = "Jugar";
	public static final String est3_desc = "Cuantas veces ha jugado?";

	public static final int amount = 0;

	//Inicialización lista de Achievements en la BD
	public void AchievementsList(Database_Helper helper)
	{
		helper.open_read();
		Cursor aux = helper.getAllAchievements();

		if(aux.moveToFirst()){ 
			//Ya existe la tabla de Achievements registrada
			helper.close();
			return;
		}
		helper.close();

		helper.open_write();
		helper.createAchievement(ach1, ach1_desc, done);
		helper.createAchievement(ach2, ach2_desc, done);
		helper.createAchievement(ach3, ach3_desc, done);
		helper.createAchievement(ach4, ach4_desc, done);
		helper.close();    	
	}

	//Inicialización lista de Estadísticas en la BD
	public void StatisticsList(Database_Helper helper)
	{
		helper.open_read();
		Cursor aux = helper.getAllStatistics();

		if(aux.moveToFirst()){ 
			//Ya existe la tabla de Achievements registrada
			helper.close();
			return;
		}
		helper.close();  

		helper.open_write();
		helper.createStatistics(est1, est1_desc, amount);
		helper.createStatistics(est2, est2_desc, amount);
		helper.createStatistics(est3, est3_desc, amount);
		helper.close();    	
	}
	
}
