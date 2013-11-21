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


public class SleepTask implements SensorEventListener, Runnable {

	SensorManager manager;
	Sensor proxSensor;
	
	String valor;
	Boolean action;
	static double ACCEL_MIN = 3;
	static long MIN_TIME = 1000; //en milisegundos
	static boolean running;
	Activity parent;
	SleepTask thisTask = this;
	//Handler ext_handler;
	PetActionManager pet_manager;
	
	public SleepTask(Activity activity, PetActionManager petman) {
	  //Sets view and sensor
	
	parent = activity;
	pet_manager = petman;
    manager =(SensorManager) parent.getSystemService(Context.SENSOR_SERVICE);
    proxSensor = manager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
	action = false;
	
    return;
	}
	CountDownTimer timer = new CountDownTimer(MIN_TIME, MIN_TIME/100)
		{

			@Override
			public void onFinish() {
				//Hace la accion apenas termina
				doTaskAction();
				Log.d("SleepTask","End of task");
			}

			@Override
			public void onTick(long millisUntilFinished) {
				//Revisa que la accion se haya ejecutado dentro de este tick.
				Log.d("SleepTask","It's Ticking");
				
			}
			
		};
	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub

	}
	
	@Override
	public void onSensorChanged(SensorEvent event) {
		// Gets information from sensors and processes it
		
		valor = String.valueOf(event.values[0]);
		int valorNum = (int)Double.parseDouble(valor);
       //After covering, it does the action and ends
		
		if (valorNum==0){
			//Toast.makeText(thisActivity, "Luz apagada es valor " + valor, Toast.LENGTH_SHORT).show();
			
			Log.d("SleepTask","Accion ejecutada");
			//valSensor = Double.parseDouble(valor);
			action = true;
	    	doTaskAction();
	    	Log.d("SleepTask","End of task - Action");
		}
		
		if (valorNum!=0){
			//Toast.makeText(thisActivity, "Luz prendida es valor " + valor, Toast.LENGTH_SHORT).show();	
			action = false;
			//status.setText("luz prendida es valor " + valor);
			//valSensor = Double.parseDouble(valor);
			Log.d("SleepTask","Despirto, accion no ejecutada");
		}	
			
	     /*if (delta > ACCEL_MIN)
	      {
	    	  Log.d("SleepTask","Accion ejecutada");
	    	  action = true;
	    	  timer.cancel();
	    	  doTaskAction();
	    	  Log.d("SleepTask","End of task - Action");
				
	      }
	      else 
	      {
	    	  action = false;
	      }*/
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		Log.d("SleepTask","Running");
		running = true;
		manager.registerListener(thisTask,proxSensor,SensorManager.SENSOR_DELAY_NORMAL);
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
		manager.unregisterListener(thisTask);
		
		
	}
	public void cleanup()
	{
		//Se ocupa cuando se termina desde afuera este task.
		action = false;
		manager.unregisterListener(thisTask);
	}
	public static void petAction(Activity parent, DB_Updater updater, Database_Helper helper, StatesActivity states){
		//para que vibre al realizar accion
		Vibrator vibe = (Vibrator) parent.getSystemService(Context.VIBRATOR_SERVICE);	
		vibe.vibrate(100); 
		
		if(states!=null && !states.isDetached()){//si el fragmento esta activo
			if(!states.isSleeping())
			{
				states.sleep();
				if(updater.sleep(helper)){
		 			//Toast.makeText(parent, "Logro Desbloqueado Perezoso", 
		 				//	Toast.LENGTH_LONG).show();
				}
				//if(((MainActivity)parent).isConnected())
					//((MainActivity)parent).startProgram("Sleep.rxe");
			}
			else
			{
				states.wake();
				//if(((MainActivity)parent).isConnected())
					//((MainActivity)parent).startProgram("Vader.rxe");
				
			}
        }
		 
 		
    	
	}
}
