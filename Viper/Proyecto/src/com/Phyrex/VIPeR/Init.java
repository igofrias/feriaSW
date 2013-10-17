package com.Phyrex.VIPeR;

import java.util.ArrayList;
import java.util.List;

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

	public static final String ach1 = "Primera Mascota";	
	public static final String ach1_desc = "¡Has Creado tu primera mascota!";
	public static final int img1 = R.drawable.firstpetarch;
	public static final String ach2 = "Primeras mordidas";	
	public static final String ach2_desc = "Ya has alimentado 5 veces a tu mascota";
	public static final int img2 = R.drawable.logropromerasmordidas;
	public static final String ach3 = "Juguetón";	
	public static final String ach3_desc = "Has jugado 5 veces con tu mascota";
	public static final int img3 = R.drawable.firstpetarch;
	public static final String ach4 = "Obediencia Inicial";	
	public static final String ach4_desc = "Ha hecho caso a 3 instrucciones de su amo";
	public static final int img4 = R.drawable.firstpetarch;
	public static final String ach5 = "Pepe";	
	public static final String ach5_desc = "Has creado una mascota con el mejor nombre del mundo";
	public static final int img5 = R.drawable.logropepe;
	public static final String ach6 = "Tentosaurio";	
	public static final String ach6_desc = "Has creado una mascota de raza Tentosaurio";
	public static final int img6 = R.drawable.firstpetarch;
	public static final String ach7 = "Perezoso";	
	public static final String ach7_desc = "Tu mascota ha dormido 5 veces";
	public static final int img7 = R.drawable.logropepe;
	int done =0;
	
	

	public static final String est1 = "Ha Comido";
	public static final String est1_desc = "¿Cuantas veces la ha alimentado?";
	public static final int imgstat1 = R.drawable.eat;
	public static final String est2 = "Ha Dormido";
	public static final String est2_desc = "¿Cuantas veces ha dormido?";
	public static final int imgstat2 = R.drawable.sleep;
	public static final String est3 = "Ha Jugado";
	public static final String est3_desc = "¿Cuantas veces ha jugado?";
	public static final int imgstat3 = R.drawable.play;
	public static final String est4 = "Mascotas Creadas";
	public static final String est4_desc = "¿Cuantas mascotas ha tenido?";
	public static final int imgstat4 = R.drawable.petsegg;
	public static final String est5 = "Desechos Generados";
	public static final String est5_desc = "¿Cuantas veces ha hecho sus necesidades?";
	public static final int imgstat5 = R.drawable.poopstat;
	public static final String est6 = "Desechos Limpiados";
	public static final String est6_desc = "¿Cuantas veces ha limpiado a sus mascotas?";
	public static final int imgstat6 = R.drawable.toiletpaper;
	public static final String est7= "Ha sido Lavada";
	public static final String est7_desc = "¿Cuantas veces ha limpiado a sus mascotas?";
	public static final int imgstat7 = R.drawable.soapstat;

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
		helper.createAchievement(ach1, ach1_desc, done, img1);
		helper.createAchievement(ach2, ach2_desc, done, img2);
		helper.createAchievement(ach3, ach3_desc, done, img3);
		helper.createAchievement(ach4, ach4_desc, done, img4);
		helper.createAchievement(ach5, ach5_desc, done, img5);
		helper.createAchievement(ach6, ach6_desc, done, img6);
		helper.createAchievement(ach7, ach7_desc, done, img7);
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
		helper.createStatistics(est1, est1_desc, amount, imgstat1);
		helper.createStatistics(est2, est2_desc, amount, imgstat2);
		helper.createStatistics(est3, est3_desc, amount, imgstat3);
		helper.createStatistics(est4, est4_desc, amount, imgstat4);
		helper.createStatistics(est5, est5_desc, amount, imgstat5);
		helper.createStatistics(est6, est6_desc, amount, imgstat6);
		helper.createStatistics(est7, est7_desc, amount, imgstat7);
		helper.close();    	
	}
	
}
