package com.Phyrex.VIPeR;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Future;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;

//Clase que instancia las acciones del robot que se accesan a traves de "accion"

public class PetActionManager  {
	//static PetActionManager instance;
	public static int CORE_NUM = Runtime.getRuntime().availableProcessors(); 
	private final BlockingQueue<Runnable> mDecodeWorkQueue;
    private static final int KEEP_ALIVE_TIME = 1;
    private static final TimeUnit KEEP_ALIVE_TIME_UNIT = TimeUnit.SECONDS;
    private Handler handler;
    private ThreadPoolExecutor executor;
    private Activity currentActivity;

    Future<?> futuretask;
    boolean running;

	public PetActionManager(Activity activity)
	{
		currentActivity = activity;
		mDecodeWorkQueue = new LinkedBlockingQueue<Runnable>();
		executor = new ThreadPoolExecutor(
				1,
				CORE_NUM,KEEP_ALIVE_TIME,
				KEEP_ALIVE_TIME_UNIT,
				mDecodeWorkQueue);
		handler = new Handler(){
			@Override
            public void handleMessage(Message inputMessage) {
				
            }
		};

	}
	
	public void execute()
	{
		//Aqui se ejecutan todas las tareas.
		EatTask eatTask = new EatTask(currentActivity, this);
		SleepTask sleepTask = new SleepTask(currentActivity, this);
		
		running = true;
		
		futuretask = executor.submit(eatTask);
		futuretask = executor.submit(sleepTask);
		
		
		// ESTA LINEA HACIA QUE SE CAIGA EL PROGRAMA!!!
		//executor.execute(eatTask);
		//executor.execute(sleepTask);
		
	}
	public void stop_everything()
	{
		executor.shutdownNow();
	}
}
