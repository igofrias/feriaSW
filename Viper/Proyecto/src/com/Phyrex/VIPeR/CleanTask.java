package com.Phyrex.VIPeR;
import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Vibrator;


public class CleanTask implements Runnable {

	Boolean action;
	static boolean running;
	Activity parent;
	CleanTask thisTask = this;
	//Handler ext_handler;
	PetActionManager pet_manager;
	
	public CleanTask(Activity activity, PetActionManager petman) {
	  //Sets view 
		
	parent = activity;
	pet_manager = petman;
	action = false;
	
    return;
	}
	
	
	public boolean actionDone(){
		//Retorna si ha sido ejecutada la accion
		return action;
	}
	public void doTaskAction()
	{
		//Accion que debe ejecutar la task. Pide antes de ejecutar la task
		//que haya mandado el mensaje para detener todo y que haya detenido todo

		if (this.actionDone() && pet_manager.stop_everything())
		{
		
			SleepTask.petAction(parent, pet_manager.updater, pet_manager.entry,pet_manager.states);
			
		}
		action = false;		
		
	}
	public void cleanup()
	{
		//Se ocupa cuando se termina desde afuera este task.
		action = false;
	}
	public static void petAction(Activity parent, DB_Updater updater, Database_Helper helper, StatesActivity states){
		//para que vibre al realizar accion
		Vibrator vibe = (Vibrator) parent.getSystemService(Context.VIBRATOR_SERVICE);	
		vibe.vibrate(100); 
		
		if(states!=null && !states.isDetached()){//si el fragmento esta activo
			if(!states.isSleeping())
			{
				states.cleaning();
				if(updater.clean(helper)){
		 			//Toast.makeText(parent, "Logro Desbloqueado Cajita de Arena", //se puede cambiar xD 
		 				//	Toast.LENGTH_LONG).show();
				}
				//Toast.makeText(parent, "wipe", Toast.LENGTH_SHORT).show();
				if(((MainActivity)parent).isConnected())
					((MainActivity)parent).startProgram("Clean.rxe");
			}
        }
		     	
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		Log.d("CleanTask","Running");
		running = true;
		while(running||Thread.currentThread().isInterrupted())
		{
			running = pet_manager.running;
			if(!running)
			{
				break;
			}
		}
		this.cleanup();
		
	}
}
