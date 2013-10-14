package com.Phyrex.VIPeR;

import java.util.LinkedList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Future;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import android.app.Activity;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

//Clase que instancia las acciones del robot que se accesan a traves de "accion"

public class PetActionManager  {
	//static PetActionManager instance;
	public static int CORE_NUM = Runtime.getRuntime().availableProcessors(); 
    private Handler handler;
    private Activity currentActivity;
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

	public PetActionManager(Activity activity)
	{
		currentActivity = activity;
		handler = new Handler(){
			@Override
            public void handleMessage(Message inputMessage) {
				
            }
		};
		futuretasklist = new LinkedList<Future<?>>();

	}
	
	public void execute()
	{
		//Aqui se ejecutan todas las tareas.
		EatTask eatTask = new EatTask(currentActivity, this);
		SleepTask sleepTask = new SleepTask(currentActivity, this);
		
		running = true;
		
		Log.d("PetActionManager", "Ejecutando acciones");
		
		new Thread(eatTask).start();
		new Thread(sleepTask).start();
		timer.start();
	}
	public void stop_everything()
	{
		
		running = false;
		timer.cancel();
	    
		
		
	}
}
