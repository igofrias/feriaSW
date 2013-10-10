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

public class EatTask implements SensorEventListener, Runnable {

	SensorManager manager;
	Sensor accelerometer;
	double[] gravity;
	double mAccelLast;
	double mAccelCurrent;
	double mAccel;
	Boolean action;
	static double ACCEL_MIN = 3;
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
				Log.d("EatTask","End of task");
			}

			@Override
			public void onTick(long millisUntilFinished) {
				//Revisa que la accion se haya ejecutado dentro de este tick.
				Log.d("EatTask","It's Ticking");
				
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
	      if (delta > ACCEL_MIN)
	      {
	    	  Log.d("EatTask","Accion ejecutada");
	    	  action = true;
	    	  timer.cancel();
	    	  doTaskAction();
	    	  Log.d("EatTask","End of task - Action");
				
	    	  
	      }
	      else 
	      {
	    	  action = false;
	      }
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		manager.registerListener(thisTask, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
		timer.start();
		while(running)
		{
			if(!running || action)
			{
				break;
			}
		}
		this.doTaskAction();
		
	}
	public boolean actionDone(){
		//Retorna si ha sido ejecutada la accion
		return action;
	}
	public void doTaskAction()
	{
		//Accion que debe ejecutar la task, incluido detener todo;
		if (this.actionDone())
		{
			Toast.makeText(parent.getBaseContext(), "Comio", Toast.LENGTH_SHORT).show();
		}
		else
		{
			Toast.makeText(parent.getBaseContext(), "No hay acciones disponibles", Toast.LENGTH_SHORT).show();
		}
		action = false;
		manager.unregisterListener(thisTask);
		pet_manager.stop_everything();
		
	}
}
