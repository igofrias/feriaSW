package com.Phyrex.VIPeR;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.widget.Toast;

/******************************************
 * Clase dedicada a modificar estadísticas 
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
	public boolean play(Database_Helper helper){
		
		boolean achiviement=false;
		int aux_amount, flag = 0;
		Statistics aux_st = new Statistics();
		helper.open_read();
		Cursor aux_cursor  = helper.getStatistics("Ha Jugado");

		if (aux_cursor.moveToFirst() == false){
			return false;
		} 

		aux_amount = up(Integer.parseInt(aux_cursor.getString(3)));
		
		//Achievement
		if(aux_amount == 4){
			flag = 1;
			helper.close();
			achievement_unlock(helper, "Juguetón");
			achiviement = true;
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
		
		return achiviement;
	}

	//
	//Solo cuenta cantidad de veces totales q se le ha dado de comer.
	//Es posible implementar para cantidad de comida
	public boolean eat(Database_Helper helper){
		boolean achievement= false;
		int aux_amount, flag = 0;
		Statistics aux_st = new Statistics();
		helper.open_read();
		Cursor aux_cursor  = helper.getStatistics("Ha Comido");

		if (aux_cursor.moveToFirst() == false){
			return false;
		} 

		aux_amount = up(Integer.parseInt(aux_cursor.getString(3)));
		
		//Achievement
		if(aux_amount == 5){
			flag = 1;
			helper.close();
			achievement_unlock(helper, "Primeras mordidas");
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

	//Veces que se le mandó a dormir
	//Es posible que se cuenten  la cantidad de tiempo que durmió 
	//si existiera el estado
	public boolean sleep(Database_Helper helper){
		boolean achievement= false;
		int aux_amount, flag=0;
		Statistics aux_st = new Statistics();
		helper.open_read();
		Cursor aux_cursor  = helper.getStatistics("Ha Dormido");

		if (aux_cursor.moveToFirst() == false){
			return false;
		} 
		
		aux_amount = up(Integer.parseInt(aux_cursor.getString(3)));
		//Achievement
		if(aux_amount == 5){
			flag = 1;
			helper.close();
			achievement_unlock(helper, "Perezoso");
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
		
		//Funcion que verifica y marca achiviement 6, PEPE!!!!!!! 
		public boolean achievement_unlock(Database_Helper helper, String name){
			Achievement newAch = new Achievement();
			helper.open_write();
			Cursor aux_cursor_ach  = helper.getAchievement(name);

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
		

		//
		//Solo cuenta cantidad de veces totales q se le ha lavado.
		public boolean wash(Database_Helper helper){
			boolean achievement= false;
			int aux_amount, flag = 0;
			Statistics aux_st = new Statistics();
			helper.open_read();
			Cursor aux_cursor  = helper.getStatistics("Ha sido Lavada");

			if (aux_cursor.moveToFirst() == false){
				return false;
			} 

			aux_amount = up(Integer.parseInt(aux_cursor.getString(3)));
			
			//Achievement
			if(aux_amount == 5){
				flag = 1;
				helper.close();
				achievement_unlock(helper, "Reluciente");
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
		
		public boolean poo(Database_Helper helper){
			boolean achievement= false;
			int aux_amount, flag = 0;
			Statistics aux_st = new Statistics();
			helper.open_read();
			Cursor aux_cursor  = helper.getStatistics("Desechos Generados"); //palabra mas elegante O.o

			if (aux_cursor.moveToFirst() == false){
				return false;
			} 

			aux_amount = up(Integer.parseInt(aux_cursor.getString(3)));
			
			//Achievement
			if(aux_amount == 5){
				flag = 1;
				helper.close();
				achievement_unlock(helper, "Buena Digestion");
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
		
		
		public boolean clean(Database_Helper helper){
			boolean achievement= false;
			int aux_amount, flag = 0;
			Statistics aux_st = new Statistics();
			helper.open_read();
			Cursor aux_cursor  = helper.getStatistics("Desechos Limpiados"); 

			if (aux_cursor.moveToFirst() == false){
				return false;
			} 

			aux_amount = up(Integer.parseInt(aux_cursor.getString(3)));
			
			//Achievement
			if(aux_amount == 5){
				flag = 1;
				helper.close();
				achievement_unlock(helper, "Cajita de Arena");
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
		
		//Aumenta cantidad de veces de jugar
		public void newpet(Database_Helper helper){
			
			int aux_amount;
			Statistics aux_st = new Statistics();
			helper.open_read();
			Cursor aux_cursor  = helper.getStatistics("Mascotas Creadas");

			if (aux_cursor.moveToFirst() == false){
				return;
			} 

			aux_amount = up(Integer.parseInt(aux_cursor.getString(3)));

			helper.close();
			
			aux_st._id = Integer.parseInt(aux_cursor.getString(0));
			aux_st._name = aux_cursor.getString(1);
			aux_st._desc = aux_cursor.getString(2);		
			
			helper.open_write();
			helper.modifyStatistics(aux_st._id, aux_st._name, aux_st._desc, aux_amount);           	          	    	
			helper.close();
		}

		
		//obtiene puntaje de juego
		public int getHS(Database_Helper helper, int id){
			int score;
			helper.open_write();
			Cursor gamescore  = helper.getGame(id);
			
			if (gamescore.moveToFirst() == false)
				return -1;
				
			score = Integer.parseInt(gamescore.getString(5));
			helper.close();
			
			return score;
		}
		
		
		//funcion que actualiza puntaje de juego en la bd
		public void updateHS(Database_Helper helper, int id, int score){
			helper.open_write();
			Cursor gamescore  = helper.getGame(id);

			if (gamescore.moveToFirst() == false){
				return;
			}
			if(Integer.parseInt(gamescore.getString(4))<score)
				helper.updateHighScore(id, score);
			
			helper.close();
			return;
		}
		
		public void updateAmount(Database_Helper helper, int id, int score){
			helper.open_write();
			Cursor gamescore  = helper.getGame(id);

			if (gamescore.moveToFirst() == false){
				return;
			}
			if(Integer.parseInt(gamescore.getString(5))<score)
				helper.updateAmount(id, score);
			
			helper.close();
			return;
		}
		
		//funcion que actualiza puntaje de juego en la bd
		public boolean unlock_achgame(Database_Helper helper, int id, int score, int scoreach, String ach){
			helper.open_write();
			Achievement newAch = new Achievement();
			Cursor aux_cursor_ach  = helper.getAchievement(ach);

			if (aux_cursor_ach.moveToFirst() == false){
				return false;
			}
			if(scoreach>score || Integer.parseInt(aux_cursor_ach.getString(3))==1)
				return false;
			//Datos a actualizar en achievement en BD
			newAch._id = Integer.parseInt(aux_cursor_ach.getString(0));
			newAch._name = aux_cursor_ach.getString(1);
			newAch._desc = aux_cursor_ach.getString(2);
			newAch._done = 1;

			helper.confirmAchievement(newAch._id, newAch._name, newAch._desc, newAch._done);

			helper.close();
			return true;
		}
		
		
	/*		public boolean fleaGame(Database_Helper helper){
			boolean achievement= false;
			int aux_amount, flag = 0;
			Statistics aux_st = new Statistics();
			helper.open_read();
			Cursor aux_cursor  = helper.getStatistics("Ha matado");
			Log.d("Exterminador","dentro de fleaGame dbupdater");

			if (aux_cursor.moveToFirst() == false){
				return false;
			} 

			aux_amount = up(Integer.parseInt(aux_cursor.getString(3)));
			
			//Achievement
			if(aux_amount == 20){ //matar 20 pulgas
				flag = 1;
				helper.close();
				achievement_unlock(helper, "Exterminador");
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
		}  */
}
