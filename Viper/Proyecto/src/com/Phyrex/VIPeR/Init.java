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
	public static final int img3 = R.drawable.logrojugueton;
	public static final String ach4 = "Creciendo";	
	public static final String ach4_desc = "Tu mascota ha cumplido su primer dia de vida";
	public static final int img4 = R.drawable.firstpetarch;
	public static final String ach5 = "Pepe";	
	public static final String ach5_desc = "Has creado una mascota con el mejor nombre del mundo";
	public static final int img5 = R.drawable.logropepe;
	public static final String ach6 = "Tentosaurio";	
	public static final String ach6_desc = "Has creado una mascota de raza Tentosaurio";
	public static final int img6 = R.drawable.logrotentosaurio;
	public static final String ach7 = "Perezoso";	
	public static final String ach7_desc = "Tu mascota ha dormido 5 veces";
	public static final int img7 = R.drawable.logroperesozo;
	public static final String ach8 = "Reluciente";	
	public static final String ach8_desc = "Tu mascota ha sido lavada 5 veces";
	public static final int img8 = R.drawable.logrolavar;
	public static final String ach9 = "Buena Digestion";	
	public static final String ach9_desc = "Tu mascota ha hecho desechos 5 veces";
	public static final int img9 = R.drawable.logropepe;
	public static final String ach10 = "Cajita de Arena";	
	public static final String ach10_desc = "Tu mascota ha sido limpiada 5 veces";
	public static final int img10 = R.drawable.logrolimpcaca;
	public static final String ach11 = "Atrapador principiante";	
	public static final String ach11_desc = "Has obtenido un puntaje superior a 5 en el mini juego" +
			"Atrapa las pelotas";
	public static final int img11 = R.drawable.logrolavar;
	public static final String ach12 = "Controlador principiante";	
	public static final String ach12_desc = "Has obtenido un puntaje superior a 1000 en el juego" +
			"Atraar pelota con Robot";
	public static final int img12 = R.drawable.logropepe;
	public static final String ach13 = "Desparacitador principiante";	
	public static final String ach13_desc = "Has obtenido un puntaje superior a 1500 en el juego " +
			"Saca pulgas";
	public static final int img13 = R.drawable.logrolimpcaca;
	
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
	public static final String est7_desc = "¿Cuantas veces ha lavado a sus mascotas?"; //limpiado por lavado
	public static final int imgstat7 = R.drawable.soapstat;
	public static final String game1= "Atrapa las pelotas";
	public static final String game1_desc = "Debes atrapar las pelotas que van callendo," +
			"¡evita que estas toquen el piso!";
	
	
	public static final int imggame1 = R.drawable.game1;
	public static final String game2= "Atrapa las pelotas (con Robot)";
	public static final String game2_desc = "Utuliza el robot para atrapar las pelota obtetivo," +
			"¡debe ser dentro de un tiempo limite!";
	public static final int imggame2 = R.drawable.game2;
	public static final String game3= "Saca pulgas";
	public static final String game3_desc = "Ayuda a tentosaurio que esta infestado  de pulgas," +
			"¡debes quitar las pulgas dentro del tiempo!";
	public static final int imggame3 = R.drawable.game3;

	public static final int score = 0;
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
		helper.createAchievement(ach8, ach8_desc, done, img8);
		helper.createAchievement(ach9, ach9_desc, done, img9);
		helper.createAchievement(ach10, ach10_desc, done, img10);
		helper.createAchievement(ach11, ach11_desc, done, img11);
		helper.createAchievement(ach12, ach12_desc, done, img12);
		helper.createAchievement(ach13, ach13_desc, done, img13);

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
		helper.createStatistics(est7, est7_desc, amount, imgstat7); //lavado
		helper.close();    	
	}
	
	//Inicialización lista de Estadísticas en la BD
		public void GamesList(Database_Helper helper)
		{
			helper.open_read();
			Cursor aux = helper.getAllGames();

			if(aux.moveToFirst()){ 
				//Ya existe la tabla de Achievements registrada
				helper.close();
				return;
			}
			helper.close();  

			helper.open_write();
			helper.createGames(game1, game1_desc, imggame1, score);
			helper.createGames(game2, game2_desc, imggame2, score);
			helper.createGames(game3, game3_desc, imggame3, score);
			helper.close();    	
		}
	
}
