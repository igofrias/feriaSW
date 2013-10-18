package com.Phyrex.VIPeR;
import com.actionbarsherlock.app.SherlockFragment;

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


public class EatTask implements SensorEventListener, Runnable {

	SensorManager manager;
	Sensor accelerometer;
	double[] gravity;
	double mAccelLast;
	double mAccelCurrent;
	double mAccel;
	Boolean action;
	static double ACCEL_MIN = 2;
	static long MIN_TIME = 1000; //en milisegundos
	static boolean running;
	Activity parent;
	EatTask thisTask = this;
	//Handler ext_handler;
	PetActionManager pet_manager;
	  public EatTask(Activity activity, PetActionManager petman) {
		  //Sets view and sensor
		
		parent = activity;
		pet_manager = petman;
	    manager =(SensorManager) parent.getSystemService(Context.SENSOR_SERVICE);
		accelerometer = manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		gravity = new double[3];
		action = false;
		
	    return;
	  }
	  CountDownTimer timer = new CountDownTimer(MIN_TIME, MIN_TIME/100)
		{

			@Override
			public void onFinish() {
				//Hace la accion apenas termina
				doTaskAction();
		    	
		    	Log.d("EatTask","End of task - Action");
			}

			@Override
			public void onTick(long millisUntilFinished) {
				//Revisa que la accion se haya ejecutado dentro de este tick.
			}
			
		};
	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub

	}

	public double setGravity(double raw_accel_x, double raw_accel_y, 
			double raw_accel_z)
	{
		//Calcula la gravedad para despues quitar su influencia
		//alpha  = t/(dt + t) t=constante del filtro dt=event delivery rate
		double alpha = 0.8;
		for (int i = 0; i < 3; i++)
		{
			gravity[i] = 0;
		}
		  // Isolate the force of gravity with the low-pass filter.
		  gravity[0] = alpha * gravity[0] + (1 - alpha) * raw_accel_x;
		  gravity[1] = alpha * gravity[1] + (1 - alpha) * raw_accel_y;
		  gravity[2] = alpha * gravity[2] + (1 - alpha) * raw_accel_z;
		  
		  double x = raw_accel_x - gravity[0];
		  double y = raw_accel_y - gravity[1];
		  double z = raw_accel_z - gravity[2];
		double gravitymod = Math.sqrt(x*x + y*y);
		return gravitymod;
	}
	@Override
	public void onSensorChanged(SensorEvent arg0) {
		// Gets information from sensors and processes it
		float x = arg0.values[0];
	      float y = arg0.values[1];
	      float z = arg0.values[2];
	      mAccelLast = mAccelCurrent;
	      mAccelCurrent = setGravity(x,y,z); 
	      double delta = mAccelCurrent - mAccelLast;
	      mAccel = mAccel * 0.9f + delta*0.1f; // perform low-cut filter
	      //After shaking does the action and ends
	      if (mAccelCurrent > ACCEL_MIN)
	      {
	    	  Log.d("EatTask","Accion ejecutada");
	    	  //Si la inclincacion se mantiene durante el tiempo dado
	    	  //inicia la accion
	    	  if(action == false)
	    	  {
	    		  
	    		  action = true;
	    		  timer.start();
	    	  }
				
	    	  
	      }
	      else 
	      {
	    	  if(action == true)
	    	  {
	    		  //Si pasa de esperar a que se mantenga inclinado a desinclinarse
	    		  action = false;
	    		  timer.cancel();
	    		  cleanup();
	    	  }
	      }
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		running = true;
		manager.registerListener(thisTask, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
		while(running || Thread.currentThread().isInterrupted())
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
			
			EatTask.petAction(parent, pet_manager.updater, pet_manager.entry, pet_manager.states);
			cleanup();
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
		if(states.isSleeping()){
	    	if(!states.isFull())
	    	{
	    		if(updater.eat(helper)){
	        		Toast.makeText(parent, "Logro Desbloqueado Primeras mordidas", Toast.LENGTH_LONG).show();
	        	}
	
	     		if(states!=null && !states.isDetached()){//si el fragmento esta activo
	     			states.eating();
	            }
	     		 if(((MainActivity)parent).isConnected())
	    				((MainActivity)parent).startProgram("Eat.rxe");
	        	Toast.makeText(parent.getBaseContext(), "Om nom nom nom", Toast.LENGTH_SHORT).show();
	    	}
	    	else
	    	{
	    		if(((MainActivity)parent).isConnected())
					((MainActivity)parent).startProgram("Angry.rxe");
	    		Toast.makeText(parent.getBaseContext(), ">:(", Toast.LENGTH_SHORT).show();
	    	}
		}else{
			Toast.makeText(parent.getBaseContext(), "no puedes molestar a la mascota mientras duerme", Toast.LENGTH_SHORT).show();
		}
	}
}
