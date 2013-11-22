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


public class WashTask implements SensorEventListener, Runnable {

	SensorManager manager;
	Sensor accelerometer;
	double[] gravity;
	double mAccelLast;
	double mAccelCurrent;
	double mAccel;
	Boolean action;
	
	static long MIN_TIME = 1000; //en milisegundos
	static boolean running;
	Activity parent;
	WashTask thisTask = this;
	//Handler ext_handler;
	PetActionManager pet_manager;
	
	float xAccel ; 
	float yAccel; 
	float zAccel; 
  
	/* And here the previous ones */
	float xPreviousAccel;
  	float yPreviousAccel;
  	float zPreviousAccel;
      
  	/* Used to suppress the first shaking */
  	boolean firstUpdate = true;

  	/*What acceleration difference would we assume as a rapid movement? */
  	float shakeThreshold = 0.5f; //sensibilidad del shake
  	
	/* Has a shaking motion been started (one direction) */
	boolean shakeInitiated = false;
	
	
	  public WashTask(Activity activity, PetActionManager petman) {
		  //Sets view and sensor
		parent = activity;
		pet_manager = petman;
	    manager =(SensorManager) parent.getSystemService(Context.SENSOR_SERVICE);
		accelerometer = manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		action = false;
		
	    return;
	  }
	  CountDownTimer timer = new CountDownTimer(MIN_TIME, MIN_TIME/100)
		{

			@Override
			public void onFinish() {
				//Hace la accion apenas termina
				doTaskAction();
		    	
		    	Log.d("WashTask","End of task - Action");
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

	private void updateAccelParameters(float xNewAccel, float yNewAccel,
			float zNewAccel) {
                /* we have to suppress the first change of acceleration, it results from first values being initialized with 0 */
		if (firstUpdate) {  
			xPreviousAccel = xNewAccel;
			yPreviousAccel = yNewAccel;
			zPreviousAccel = zNewAccel;
			firstUpdate = false;
		} else {
			xPreviousAccel = xAccel;
			yPreviousAccel = yAccel;
			zPreviousAccel = zAccel;
		}
		xAccel = xNewAccel;
		yAccel = yNewAccel;
		zAccel = zNewAccel;
	}
	
	/* If the values of acceleration have changed on at least two axises, we are probably in a shake motion */
	private boolean isAccelerationChanged() {
		float deltaX = Math.abs(xPreviousAccel - xAccel);
		float deltaY = Math.abs(yPreviousAccel - yAccel);
		float deltaZ = Math.abs(zPreviousAccel - zAccel);
		return (deltaX > shakeThreshold && deltaY > shakeThreshold)
				|| (deltaX > shakeThreshold && deltaZ > shakeThreshold)
				|| (deltaY > shakeThreshold && deltaZ > shakeThreshold);
	}
	
	private void executeShakeAction() {
		
  	  //si hay shake, inicia la accion
		if(action == false)
  	  	{
  		  action = true;
  		  timer.start();
  		  Log.d("WashTask","Accion ejecutada");
  	  	}  
		
	}
	
	@Override
	public void onSensorChanged(SensorEvent se) {
		// Gets information from sensors and processes it
		
	updateAccelParameters(se.values[0], se.values[1], se.values[2]);   // (1)
    if ((!shakeInitiated) && isAccelerationChanged()) {                                      // (2) 
	    shakeInitiated = true; 
  	  if(action == true)
  	  {
  		  //Si pasa de esperar a que se mantenga inclinado a desinclinarse
  		  action = false;
  		  timer.cancel();
  		  cleanup();
  	  }
	} 
    else if ((shakeInitiated) && isAccelerationChanged()) {                              // (3)
	    executeShakeAction();
	} 
    else if ((shakeInitiated) && (!isAccelerationChanged())) {                           // (4)
	    shakeInitiated = false;
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
			
			WashTask.petAction(parent, pet_manager.updater, pet_manager.entry, pet_manager.states);
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
		if(!states.isSleeping()){
    		if(updater.wash(helper)){
        		//Toast.makeText(parent, "Logro Desbloqueado Reluciente", Toast.LENGTH_LONG).show();
        	}

     		if(states!=null && !states.isDetached()){//si el fragmento esta activo
     			states.washing();
            }
     		 if(((MainActivity)parent).isConnected())
    				((MainActivity)parent).startProgram("ShameEyes.rxe"); //ver que sea el mismo nombre de programa
    	}
    	else
    	{
    	//	if(((MainActivity)parent).isConnected())
			//	((MainActivity)parent).startProgram("Angry.rxe");
    		//Toast.makeText(parent.getBaseContext(), ">:(", Toast.LENGTH_SHORT).show();
    	}

	}
}
