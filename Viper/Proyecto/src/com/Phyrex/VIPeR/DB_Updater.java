package com.Phyrex.VIPeR;

import android.content.Context;
import android.database.Cursor;
import android.widget.Toast;

/******************************************
 * Clase dedicada a modificar estad�sticas 
 * y registrar logros conseguidos.
 * IMPORTANTE: LAs funciones requieren como argumento:
 * Database_Helper

*******************************************/
public class DB_Updater {
	
	private final Context context;
	
	public DB_Updater (Context c){
		context = c;
	}

	public int up(int aux){
		aux = aux+1;
		return aux;
	}
	

	//Aumenta cantidad de veces de jugar
	public void play(Database_Helper helper){
		
		int aux_amount, flag = 0;
		Statistics aux_st = new Statistics();
		helper.open_read();
		Cursor aux_cursor  = helper.getStatistics("Jugar");

		if (aux_cursor.moveToFirst() == false){
			return;
		} 

		aux_amount = up(Integer.parseInt(aux_cursor.getString(3)));
		
		//Achievement
		if(aux_amount == 4){
			flag = 1;
			helper.close();
			achievement_4_played(helper);
		}
		
		if(flag == 0){
			helper.close();
		}
		
		aux_st._id = Integer.parseInt(aux_cursor.getString(0));
		aux_st._name = aux_cursor.getString(1);
		aux_st._desc = aux_cursor.getString(2);		
		
		helper.open_write();
		helper.modifyStatistics(aux_st._id, aux_st._name, aux_st._desc, aux_amount);           	          	    	
		helper.close();
	}

	//
	//Solo cuenta cantidad de veces totales q se le ha dado de comer.
	//Es posible implementar para cantidad de comida
	public boolean eat(Database_Helper helper){
		boolean achievement= false;
		int aux_amount, flag = 0;
		Statistics aux_st = new Statistics();
		helper.open_read();
		Cursor aux_cursor  = helper.getStatistics("Comer");

		if (aux_cursor.moveToFirst() == false){
			return false;
		} 

		aux_amount = up(Integer.parseInt(aux_cursor.getString(3)));
		
		//Achievement
		if(aux_amount == 5){
			flag = 1;
			helper.close();
			achievement_5_eaten(helper);
			achievement=true;
		}
		if(flag == 0){
			helper.close();
		}

		aux_st._id = Integer.parseInt(aux_cursor.getString(0));
		aux_st._name = aux_cursor.getString(1);
		aux_st._desc = aux_cursor.getString(2);		
		helper.close();
		helper.open_write();
		helper.modifyStatistics(aux_st._id, aux_st._name, aux_st._desc, aux_amount);           	          	    	
		helper.close();
		return achievement;
	}   

	//Veces que se le mand� a dormir
	//Es posible que se cuenten  la cantidad de tiempo que durmi� 
	//si existiera el estado
	public void sleep(Database_Helper helper){
		int aux_amount;
		Statistics aux_st = new Statistics();
		helper.open_read();
		Cursor aux_cursor  = helper.getStatistics("Dormir");

		if (aux_cursor.moveToFirst() == false){
			return;
		} 
		
		aux_amount = up(Integer.parseInt(aux_cursor.getString(3)));

		aux_st._id = Integer.parseInt(aux_cursor.getString(0));
		aux_st._name = aux_cursor.getString(1);
		aux_st._desc = aux_cursor.getString(2);		
		helper.close();
		helper.open_write();
		helper.modifyStatistics(aux_st._id, aux_st._name, aux_st._desc, aux_amount);           	          	    	
		helper.close();
	}
	
	//Funci�n que env�a datos para modificar achievement "1era mascota" en BD
	public static boolean achievement_first_pet(Database_Helper helper){
		Achievement newAch = new Achievement();
		helper.open_write();
		Cursor aux_cursor_ach  = helper.getAchievement("Nacido para ser salvaje");

		if (aux_cursor_ach.moveToFirst() == false){
			return false;
		}
		if(Integer.parseInt(aux_cursor_ach.getString(3))==1)
			return false;
		
		//Datos a actualizar en achievement en BD
		newAch._id = Integer.parseInt(aux_cursor_ach.getString(0));
		newAch._name = aux_cursor_ach.getString(1);
		newAch._desc = aux_cursor_ach.getString(2);
		newAch._done = Integer.parseInt(aux_cursor_ach.getString(3)) + 1;

		helper.confirmAchievement(newAch._id, newAch._name, newAch._desc, newAch._done);
		
		helper.close();
		
		return true;
	}
	
	//Funci�n que env�a datos para modificar achievement "Juguet�n" en BD
	public boolean achievement_4_played(Database_Helper helper){
		Achievement newAch = new Achievement();
		helper.open_write();
		Cursor aux_cursor_ach  = helper.getAchievement("Jugueton");

		if (aux_cursor_ach.moveToFirst() == false){
			return false;
		}
		if(Integer.parseInt(aux_cursor_ach.getString(3))==1)
			return false;
		
		//Datos a actualizar en achievement en BD
		newAch._id = Integer.parseInt(aux_cursor_ach.getString(0));
		newAch._name = aux_cursor_ach.getString(1);
		newAch._desc = aux_cursor_ach.getString(2);
		newAch._done = Integer.parseInt(aux_cursor_ach.getString(3)) + 1;

		helper.confirmAchievement(newAch._id, newAch._name, newAch._desc, newAch._done);
		
		helper.close();
		
		return true;
	}
	
	//Funci�n que env�a datos para modificar achievement "Come sus 1eras 5 veces" en BD
	public boolean achievement_5_eaten(Database_Helper helper){
		Achievement newAch = new Achievement();
		helper.open_write();
		Cursor aux_cursor_ach  = helper.getAchievement("Primeras mordidas");

		if (aux_cursor_ach.moveToFirst() == false){
			return false;
		}
		if(Integer.parseInt(aux_cursor_ach.getString(3))==1)
			return false;
		
		//Datos a actualizar en achievement en BD
		newAch._id = Integer.parseInt(aux_cursor_ach.getString(0));
		newAch._name = aux_cursor_ach.getString(1);
		newAch._desc = aux_cursor_ach.getString(2);
		newAch._done = Integer.parseInt(aux_cursor_ach.getString(3)) + 1;

		helper.confirmAchievement(newAch._id, newAch._name, newAch._desc, newAch._done);
		
		helper.close();
		return true;
	}
	 
	//Funcion que verifica y marca achiviement 6, PEPE!!!!!!! 
		public static boolean achievement_6_pepe(Database_Helper helper){
			Achievement newAch = new Achievement();
			helper.open_write();
			Cursor aux_cursor_ach  = helper.getAchievement("Pepe");

			if (aux_cursor_ach.moveToFirst() == false){
				return false;
			}
			if(Integer.parseInt(aux_cursor_ach.getString(3))==1)
				return false;
			
			//Datos a actualizar en achievement en BD
			newAch._id = Integer.parseInt(aux_cursor_ach.getString(0));
			newAch._name = aux_cursor_ach.getString(1);
			newAch._desc = aux_cursor_ach.getString(2);
			newAch._done = Integer.parseInt(aux_cursor_ach.getString(3)) + 1;

			helper.confirmAchievement(newAch._id, newAch._name, newAch._desc, newAch._done);
			helper.close();
			return true;
		}
}
