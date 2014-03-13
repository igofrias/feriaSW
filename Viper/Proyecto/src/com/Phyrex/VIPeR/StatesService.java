package com.Phyrex.VIPeR;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

public class StatesService extends Service {
	boolean started = false;
	boolean sleeping;
	int health;
	int hunger;
	int energy;
	int hapiness;
	int dirtstate;
	boolean poop;
	static int MAX_HEALTH = 1000;
	static int MAX_HUNGER = 1000;
	static int MAX_ENERGY = 1000;
	static int MAX_HAPINESS = 1000;
	static int INITIAL_HUNGER = 500;
	static int INITIAL_HEALTH = 500;
	static int INITIAL_ENERGY = 500;
	static int INITIAL_HAPINESS = 500;
	public StatesService(){
		//En el constructor inicializo vida y todo lo demas
		health = INITIAL_HEALTH;
		hunger = INITIAL_HUNGER;
		energy = INITIAL_ENERGY;
		hapiness = INITIAL_HAPINESS;
		sleeping = false;
		getCurrentReceiver();
		
	}
	static class StatesReceiver extends BroadcastReceiver
	{
		//Es el BroadcastReceiver aprobado para ser usado en otras aplicaciones
		int health;
		int hunger;
		int energy;
		int hapiness;
		int dirtstate;
		boolean sleeping;
		boolean full;
		boolean fullSleep;
		boolean poop;
		public StatesReceiver()
		{
			super();
			health = 0;
			hunger = 0;
			energy = 0;
			hapiness = 0;
			sleeping = false;
			full = false;
			fullSleep = false;
		}
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			Log.d("StatesReceiver","Receiving");
			Bundle extras = intent.getExtras();
			if(extras != null)
			{
				String command = extras.getString("StateReceiverCommand");
				if(command != null)
					if(command.equals("HapinessState"))
					{
						hapiness = extras.getInt("HapinessState");
						//Log.i("StateReceiver","Hapiness=" + hapiness);
					}
					else if(command.equals("EnergyState"))
					{
						energy = extras.getInt("EnergyState");
						//Log.i("StateReceiver","Energys=" + energy);
					}
					else if(command.equals("HealthState"))
					{
						health = extras.getInt("HealthState");
						//Log.i("StateReceiver","Health=" + health);
					}
					else if(command.equals("HungerState"))
					{
						hunger = extras.getInt("HungerState");
						//Log.i("StateReceiver","Hunger=" + hunger);
					}
					else if(command.equals("SleepState"))
					{
						sleeping = extras.getBoolean("SleepState");
					}
					else if(command.equals("FullState"))
					{
						full = extras.getBoolean("FullState");
					}
					else if(command.equals("FullSleepState"))
					{
						fullSleep = extras.getBoolean("FullSleepState");
					}
			}
		}
	}
	private static StatesReceiver currentReceiver = null;
	public static StatesReceiver getCurrentReceiver()
	{
		//Crea el unico StatesReceiver que funciona para todos
		if(currentReceiver == null)
		{
			currentReceiver = new StatesReceiver();
		}
		return currentReceiver;
	}
	public static void sendCommandToStatesService(String command, Context thisContext)
	{
		//Envia un comando al servicio
		//Pide el contexto (ej: la actividad que esta corriendo actual)
		Intent intent = new Intent(thisContext,StatesService.class);
		intent.setAction("StatesService");
		intent.putExtra("Command",command);
		thisContext.startService(intent);
	}
	public static void stopStatesService(Context thisContext)
	{
		//Hace que el servicio se detenga
		Intent intent = new Intent(thisContext,StatesService.class);
		intent.setAction("StatesService");
		thisContext.stopService(intent);
	}
	@Override
	public int onStartCommand(Intent intent, int flags, int startId)
	{
		//Aqui ejecuta el thread que calcula los estados
		if(!started)
		{
			getCurrentReceiver();
			LocalBroadcastManager.getInstance(this).registerReceiver(currentReceiver,
				      new IntentFilter("com.Phyrex.VIPeR.StatesService.ACTION"));
			currentStatesRunnable = new StatesRunnable();
			statesHandler.post(currentStatesRunnable);
			started = true;
			Log.d("StatesService","StatesService started");
		}
		receiveCommand(intent);
		
		return super.onStartCommand(intent, flags, startId);
		
	}
	@Override
	public void onDestroy()
	{
		//Lo que hace cuando sale el servicio
		started = false;
		super.onDestroy();
	}
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	Handler statesHandler = new Handler(); //Ayuda a ejecutar el StatesRunnable mientras este funciona
	class StatesRunnable implements Runnable
	{
		long postTime = 400;
		//Clase que implementa la logica para los estados
		
		
		static final int runsBeforePoop = 1000;
		static final int runsBeforeDirt = 100;
		int currentRun;
		@Override
		public void run() {
			// TODO Auto-generated method stub

			hungrypet();
			sleepingPet();
			updateReceiverStates();
			//Esto permite controlar la cantidad de veces que el runnable se ejecuta antes de hacer caca
			//o de ensuciarse. Equivale a postTime*runsBeforeWhatever ms antes de hacer Whatever
			if(currentRun % runsBeforePoop == 0 && currentRun <= runsBeforeDirt)
			{
				dirtyPet();
			}
			if(currentRun % runsBeforeDirt == 0 && currentRun <= runsBeforeDirt)
			{
				poopingPet();
				currentRun = 0;
			}
			if(started){
				statesHandler.postDelayed(this, postTime);
			}
			
		}
		
	}
	public void dirtyMod(int val)
	{
		//Modifica el estado de suciedad para que quede consistente
		//Le suma el valor de val
		dirtstate += val;
		if(dirtstate > 8){
			dirtstate = 8;
		}	
		else if (dirtstate < 0)
		{
			dirtstate = 0;
		}
	
	}
	public void dirtyPet()
	{
		//Hace que la mascota se ensucie
		dirtyMod(1);
	}
	public void poopingPet()
	{
		//Hace que la mascota se cague
		poop = true;
	}

	public void updateReceiverStates()
	{
		//Envia los estados del servicio al receiver
		if(currentReceiver != null)
		{
			currentReceiver.energy = energy;
			currentReceiver.hapiness = hapiness;
			currentReceiver.health = health;
			currentReceiver.hunger = hunger;
			currentReceiver.full = isFull();
			currentReceiver.fullSleep = isFullSleep();
			currentReceiver.sleeping = sleeping;
			currentReceiver.dirtstate = dirtstate;
			currentReceiver.poop = poop;
		}
	}
	//Las funciones mod** modifican el atributo de tal forma que no se salgan de sus rangos
	//Suman el delta al valor actual
	public void modHealth(int delta)
	{
		health += delta;
		if(health < 0)
		{
			health = 0;
		}
		else if(health > MAX_HEALTH)
		{
			health = MAX_HEALTH;
		}
	}
	public void modHunger(int delta)
	{
		hunger += delta;
		if(hunger < 0)
		{
			hunger = 0;
		}
		else if(hunger > MAX_HUNGER)
		{
			hunger = MAX_HUNGER;
		}
	}
	public void modHapiness(int delta)
	{
		hapiness += delta;
		if(hapiness < 0)
		{
			hapiness = 0;
		}
		else if(hapiness > MAX_HAPINESS)
		{
			hapiness= MAX_HAPINESS;
		}
	}
	public void modEnergy(int delta)
	{
		energy += delta;
		if(energy < 0)
		{
			energy = 0;
		}
		else if(energy > MAX_ENERGY)
		{
			energy = MAX_ENERGY;
		}
	}
	public void sleepingPet()
	{
		//Lo que hace la mascota cuando duerme
		if(sleeping && energy<MAX_ENERGY){
			
			modEnergy(10);
			modHunger(-1);
		}
	}
	public void hungrypet(){
		if(hunger>0){
			modHunger(-1);
		}else{
			modHealth(-1);
			modHapiness(-1);
		}
	}
	StatesRunnable currentStatesRunnable;
	public void receiveCommand(Intent intent)
	{
		//Esto procesa el intent para ver que hace con cada comando por parte de la actividad
		String comando = null;
		if(intent != null)
		{
			comando = intent.getStringExtra("Command");
		}
		
		if(comando == null)
		{
			return;
		}
		//Comandos para obtener niveles
		if(comando.equals("getHungryLevel"))
		{
			sendStates("HungerState",this.hunger);
		}
		else if(comando.equals("getHapinessLevel"))
		{
			sendStates("HapinessState",this.hapiness);
		}
		else if(comando.equals("getHealthLevel"))
		{
			sendStates("HealthState",this.health);
		}
		else if(comando.equals("getEnergyLevel"))
		{
			sendStates("EnergyState",this.energy);
		}
		else if(comando.equals("isSleeping"))
		{
			sendStates("SleepState",sleeping);
		}
		else if(comando.equals("isFull"))
		{
			sendStates("FullState",isFull());
		}
		else if(comando.equals("isFullSleep"))
		{
			sendStates("FullSleepState",isFullSleep());
		}
		//Comandos para acciones que el tento ha hecho
		else if(comando.equals("eating"))
		{
			eatingCommand();
		}
		else if(comando.equals("playing"))
		{
			playing();
		}
		else if(comando.equals("sleep"))
		{
			sleep();
		}
		else if(comando.equals("wake"))
		{
			wake();
		}
		else if(comando.equals("washing"))
		{
			washing();
		}
		else if(comando.equals("cleaning"))
		{
			cleaning();
		}
		else if(comando.equals("actionwhenfull"))
		{
			actionwhenfull();
		}
	}
	public void sendStates(String state,int value)
	{
		//Envia los estados a la actividad ocupando el nombre state
		Intent intent = new Intent("com.Phyrex.VIPeR.StatesService.ACTION");
		intent.setAction("com.Phyrex.VIPeR.StatesService.ACTION");
		intent.putExtra("StateReceiverCommand",state);
		intent.putExtra(state, value);
		LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
		Log.d("StatesService","Sending States");
	}
	
	public void sendStates(String state, boolean value)
	{
		//Envia estados booleanos al receiver
		Intent intent = new Intent("com.Phyrex.VIPeR.StatesService.ACTION");
		intent.setAction("com.Phyrex.VIPeR.StatesService.ACTION");
		intent.putExtra("StateReceiverCommand",state);
		intent.putExtra(state, value);
		LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
		Log.d("StatesService","Sending States");
	}
	public void eatingCommand()
	{
		modHunger(100);
	}
	public boolean isFull()
	{
		if(hunger >= MAX_HUNGER - 50)
		{
			return true;
		}
		else
		{
			return false;
		}
		
	}
	public boolean isFullSleep()
	{
		if(energy >= MAX_ENERGY - 50)
		{
			return true;
		}
		else
		{
			return false;
		}
		
	}
	public void playing()
	{
		modHapiness(100);
		modEnergy(-100);
		modHunger(-50);
	}
	public void sleep()
	{
		sleeping = true;
		modHealth(100);
		modHunger(-50);
	}
	public void wake()
	{
		sleeping = false;
	}
	public void washing()
	{
		modHealth(100);
		modHapiness(50);
	}
	public void pooing()
	{
		modHealth(-50);
		modHapiness(10);
	}
	public void cleaning()
	{
		modHealth(60);
	}
	public void actionwhenfull()
	{
		modHapiness(-100);
	}
}
