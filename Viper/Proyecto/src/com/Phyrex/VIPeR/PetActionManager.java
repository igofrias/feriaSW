package com.Phyrex.VIPeR;

import java.util.LinkedList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Future;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.app.SherlockFragment;

import android.app.Activity;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.Toast;

//Clase que instancia las acciones del robot que se accesan a traves de "accion"

public class PetActionManager  {
	//static PetActionManager instance;
	final DB_Updater updater;
 	final Database_Helper entry;
    private Handler handler;
    private Activity currentActivity;
    StatesActivity states;
    static long MIN_TIME = 3000; //en milisegundos
	CountDownTimer timer = new CountDownTimer(MIN_TIME, MIN_TIME/100)
	{

		@Override
		public void onFinish() {
			//Detiene todo cuando termina el contador
			Log.d("PetActionManager","Finalizando conteo");
			stop_everything();
			Toast.makeText(currentActivity.getBaseContext(), "No hay acciones disponibles", Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onTick(long millisUntilFinished) {
			//No hace nada en cada tick
			
		}
		
	};
    LinkedList<Future<?>> futuretasklist; //almacena los futuretasks de las actividades
    boolean running;

	public PetActionManager(Activity activity, StatesActivity currstates)
	{
		currentActivity = activity;
		handler = new Handler(){
			@Override
            public void handleMessage(Message inputMessage) {
				
            }
		};
		futuretasklist = new LinkedList<Future<?>>();
		updater = new DB_Updater(currentActivity);
     	entry = new Database_Helper(currentActivity);
     	states = currstates;
	}
	
	public void execute()
	{
		//Aqui se ejecutan todas las tareas.
		EatTask eatTask = new EatTask(currentActivity, this);
		SleepTask sleepTask = new SleepTask(currentActivity, this);
		WashTask washTask = new WashTask(currentActivity, this);
		
		running = true;
		
		Log.d("PetActionManager", "Ejecutando acciones");
		
		new Thread(washTask).start(); //cuidad con conflicto entre wash y eat
		new Thread(eatTask).start();
		new Thread(sleepTask).start();
		timer.start();
	}
	public boolean stop_everything()
	{
		//Si logra detener todo retorna true. Retorna false si ya se mando la
		//señal para detenerse
		if(running == true)
		{
			running = false;
			timer.cancel();
			return true;
		}
		return false;
		
		
		
	}
}
