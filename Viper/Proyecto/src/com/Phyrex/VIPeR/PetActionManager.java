package com.Phyrex.VIPeR;

import java.util.concurrent.BlockingQueue;
<<<<<<< HEAD
import java.util.concurrent.Future;
=======
>>>>>>> 48c9e7b9fded8f2b2368ed0159abe291b9f60d30
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
<<<<<<< HEAD
    Future<?> futuretask;
    boolean running;
=======
>>>>>>> 48c9e7b9fded8f2b2368ed0159abe291b9f60d30
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
<<<<<<< HEAD
	
		
=======
>>>>>>> 48c9e7b9fded8f2b2368ed0159abe291b9f60d30
		
	}
	
	public void execute()
	{
		//Aqui se ejecutan todas las tareas.
		EatTask eatTask = new EatTask(currentActivity, this);
<<<<<<< HEAD
		running = true;
		futuretask = executor.submit(eatTask);
=======
		executor.execute(eatTask);
>>>>>>> 48c9e7b9fded8f2b2368ed0159abe291b9f60d30
		
	}
	public void stop_everything()
	{
		executor.shutdownNow();
	}
}
