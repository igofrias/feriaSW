package com.Phyrex.VIPeR;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import android.os.AsyncTask;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;

import android.os.AsyncTask;



/******* Fragmento que controla las estadisticas en la parte superior de la pantalla
 * 
 * muestra la salud, el hambre, la energia y la felicidad
 * tb muestra el nombre los dias de vida y un avatar que supuestamente viene en base a la razay color
 * 
 * 
 * @author Neko-sama
 *
 */
public class StatesActivity extends SherlockFragment{

	
	///declaracion de variables ////////
	private static Activity thisActivity;
	private TextView name;
	private TextView lifetime;
	private ProgressBar health;
	private ProgressBar hungry;
	private ProgressBar energy;
	private ProgressBar hapiness;
	private ImageView btstate;
	private ImageView petava;
	private boolean sleeping;
	Task task = new Task(this);
	Thread currentUpdaterThread = null;

	private String[] from = new String[]{Database_Helper.Key_name,Database_Helper.Key_name};
	private int[] to = new int[] {android.R.id.text1,android.R.id.text2}; 
	private Database_Helper db;
	private Cursor_Adapter adapter;
	/////////////////////////////////
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sleeping = false;
        setRetainInstance(true);	
    }

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View v = inflater.inflate(R.layout.activity_states, container, false);
		return v;
	}	
	
	public void onStart(){
		super.onStart();
		currentUpdaterThread = new Thread(updaterThread);
		updaterThread.running = true;
		currentUpdaterThread.start();
	}
	
	public void onDestroyView(){
		super.onDestroyView();
		try {
			updaterThread.running = false;
			if(currentUpdaterThread != null)
			{
				currentUpdaterThread.join();
				currentUpdaterThread = null;
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

	@Override
    public void onActivityCreated(Bundle savedInstanceState) {
		///inicializacion////
		super.onActivityCreated(savedInstanceState);    
		thisActivity = getActivity();
		health = (ProgressBar)thisActivity.findViewById(R.id.salud);
		hungry = (ProgressBar)thisActivity.findViewById(R.id.hambre);
		hapiness = (ProgressBar)thisActivity.findViewById(R.id.felicidad);
		energy = (ProgressBar)thisActivity.findViewById(R.id.energia);
		btstate = (ImageView)thisActivity.findViewById(R.id.btstate);
		petava = (ImageView)thisActivity.findViewById(R.id.avatar);
		name = (TextView)thisActivity.findViewById(R.id.name);
		lifetime = (TextView)thisActivity.findViewById(R.id.lifetime);
		hapiness.setMax(1000);
		hungry.setMax(1000);
		health.setMax(1000);
		energy.setMax(1000);
		hapiness.setProgress(500);
		hungry.setProgress(500);
		health.setProgress(500);
		energy.setProgress(500);
		
		///obtiene los datos de la BD/////////
		Database_Helper db = new Database_Helper(thisActivity);
		List<Pet> mascotas = db.getPets(); //lista de mascotas
		db.close();
		Pet petto = new Pet(mascotas.get(0).get_id(), mascotas.get(0).get_name(), mascotas.get(0).get_raza(), mascotas.get(0).get_color(), mascotas.get(0).get_birthdate(), mascotas.get(0).get_mac(), mascotas.get(0).get_death());
		name.setText(petto._name);
		setlifetime(petto._birthdate);
		
		petava.setOnTouchListener(new OnTouchListener()
        {

            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
            	((MainActivity) thisActivity).getSM().showMenu(true);
            	return false;
            }
       });
		
	}
	/**********setea el tiempo de vida de la mascota
	 * calcula el tiempo de vida ylos transforma den dias, minutos u horas segun corresponda D:
	 * recibe un estring con la fecha de nacimiento
	 * @param birthdate
	 */
	void setlifetime(String birthdate){
		
		if (((int) (lifetimecalc(birthdate) / (60*60*24))==0))
		{
			if (((int) (lifetimecalc(birthdate) / (60*60)))==0){
				if(((int)lifetimecalc(birthdate) / (60))==1){
					lifetime.setText(((int)lifetimecalc(birthdate) / (60)) + " Minuto");
				}else{
					lifetime.setText(((int)lifetimecalc(birthdate) / (60)) + " Minutos");
				}		
			}else{
				if(((int)lifetimecalc(birthdate) / (60*60))==1){
					lifetime.setText(((int)lifetimecalc(birthdate) / (60*60)) + " Hora");
				}else{
					lifetime.setText(((int)lifetimecalc(birthdate) / (60*60)) + " Horas");
				}	
			}
		}else{
			if(((int)lifetimecalc(birthdate) / (60*60*24))==1){
				lifetime.setText(((int)lifetimecalc(birthdate) / (60*60*24)) + " Día");
				final DB_Updater updater = new DB_Updater(thisActivity);
				if(lifetime.equals("1 Día")){
					updater.achievement_unlock(db, "Creciendo");
				}
			}else{
				lifetime.setText(((int)lifetimecalc(birthdate) / (60*60*24)) + " Días");
			}	
		}
	}
	
	/*********ReadBD
	 * lee los dos de la mascota de la base de datos
	 */
	void readBD(){ 
		
		//para guardar en la BD
		Database_Helper db = new Database_Helper(getActivity());
		
     
        List<Pet> mascotas = db.getPets();
        
        for (Pet cn : mascotas) {
            String log = "Id: "+cn.get_id()+", Name: " + cn.get_name() + ", Score: " + cn.get_raza();
                // Writing Contacts to log
     
        
        db.close();
        
        }
	}

	protected void OnDetach(){
		super.onDetach();
		
	}
	
	public void onPause(){
		super.onPause();
		
		
	}
	
	public void onDestroy(){
		// TODO Auto-generated method stub
		super.onDestroy();
		updaterThread.running = false;
		try {
			if(currentUpdaterThread != null)
			{
				currentUpdaterThread.join();
				currentUpdaterThread = null;
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
    }
	
	public void onStop(){
		super.onStop();
		updaterThread.running = false;
		try {
			currentUpdaterThread.join();
			currentUpdaterThread = null;
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void eating(){
		StatesService.sendCommandToStatesService("eating", thisActivity);
	}
	public boolean isFull()
	{
		StatesService.sendCommandToStatesService("isFull", thisActivity);
		return StatesService.getCurrentReceiver().full;
	}
	public boolean isFullSleep()
	{
		StatesService.sendCommandToStatesService("isFullSleep", thisActivity);
		return StatesService.getCurrentReceiver().fullSleep;
	}
	
	
	public void playing(){
		StatesService.sendCommandToStatesService("playing", thisActivity);
	}
	public void sleep(){
		StatesService.sendCommandToStatesService("sleeping", thisActivity);
	}
	
	public void washing(){
		StatesService.sendCommandToStatesService("washing", thisActivity);
	}
	
	public void pooing(){
		StatesService.sendCommandToStatesService("pooing", thisActivity);
	} 
	
	public void cleaning(){
		StatesService.sendCommandToStatesService("cleaning", thisActivity);
	} 
	public void actionwhenfull(){
		StatesService.sendCommandToStatesService("actionwhenfull", thisActivity);
	}
	boolean isSleeping()
	{
		StatesService.sendCommandToStatesService("isSleeping", thisActivity);
		return StatesService.getCurrentReceiver().sleeping;
	}
	
	int getEnergylevel()
	{
		int energypercent=(energy.getProgress()*100)/energy.getMax();
		return energypercent;
	}
	int getHungrylevel()
	{
		int hungrypercent=(hungry.getProgress()*100)/hungry.getMax();
		return hungrypercent;
	}
	int getHapinesslevel()
	{
		int hapinesspercent=(hapiness.getProgress()*100)/hapiness.getMax();
		return hapinesspercent;
	}
	int getHealthlevel()
	{
		int healthpercent=(health.getProgress()*100)/health.getMax();
		return healthpercent;
	}
	public void wake()
	{
		StatesService.sendCommandToStatesService("wake", thisActivity);
	}
	public void hungrypet(){
		if(hungry.getProgress()!=0){
			hungry.setProgress(hungry.getProgress()-1);
		}else{
			health.setProgress(health.getProgress()-1);
			hapiness.setProgress(hapiness.getProgress()-1);
		}
	}
	
	private void read_db() {
		db = new Database_Helper(thisActivity);
	    db.open_read();
	    Cursor c = db.getAll();		//llenando el cursor con datos 
	    //adapter = new Cursor_Adapter(thisActivity, R.layout.row, c, from, to);		//creando el adapter
	   // lista.setAdapter(adapter);		//entregandole el adapter a la lista
	    db.close();
	} 
	/********* lifetimecalc
	 *  recibe la cfecha de nacimiento de la mascota
	 *  y retorna el tiempo de vida en segundos
	 * @param birthdate
	 * @return diff
	 */
	@SuppressLint("SimpleDateFormat")
	private long lifetimecalc(String birthdate){
		SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss dd-MM-yyyy");
		Date date1= new Date();
		Date date2= new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss dd-MM-yyyy");
		String currentDateandTime = sdf.format(new Date());//si hay nombre crea el bicho en la base de datos
		try {
			date1 = format.parse(birthdate);
			date2= format.parse(currentDateandTime);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		long diff = date2.getTime() - date1.getTime();
		
		return diff/1000;
	}
	
	
	private class UpdaterThread implements Runnable
	{
		//Este thread cambia las barras por lo que está en el StateService
		//Reemplaza al asynctask
		boolean running = true;
		Pet petto = null;
		@Override
		public void run() {
			Database_Helper db = new Database_Helper(thisActivity);
			List<Pet> mascotas = db.getPets(); //lista de mascotas
			petto = new Pet(mascotas.get(0).get_id(), mascotas.get(0).get_name(), mascotas.get(0).get_raza(), mascotas.get(0).get_color(), mascotas.get(0).get_birthdate(), mascotas.get(0).get_mac(), mascotas.get(0).get_death());
			db.close();
			long currentTime = System.currentTimeMillis();
			long lastUpdate = currentTime;
			while(running)
			{
				//Espera una cantidad de milisegundos antes de updatear las barras
				currentTime = System.currentTimeMillis();
				if(currentTime - lastUpdate >= 100.0)
				{
					thisActivity.runOnUiThread(new Runnable()
					{

						@Override
						public void run() {
							// TODO Auto-generated method stub

							health.setProgress(StatesService.getCurrentReceiver().health);
							hungry.setProgress(StatesService.getCurrentReceiver().hunger);
							hapiness.setProgress(StatesService.getCurrentReceiver().hapiness);
							energy.setProgress(StatesService.getCurrentReceiver().energy);
							setlifetime(petto._birthdate);

							if(((MainActivity)thisActivity).isConnected()){
								btstate.setVisibility(View.VISIBLE);

							}else{
								btstate.setVisibility(View.INVISIBLE);

							}
						}

					});
					lastUpdate = System.currentTimeMillis();
				}
				

				
			}
		}
		
	}
	UpdaterThread updaterThread = new UpdaterThread();
	/********Task
	 * corre tareas en paralelo y se encarga de actualizar los datos de la 
	 * mascota mientras corre la app
	 * @author Neko-sama
	 *
	 */
    //task para realizar tareas paralelas
    private class Task extends AsyncTask<Void, Integer, Void>{
		
    	

		public Task(StatesActivity tarea) {
			// TODO Auto-generated constructor stub
		}

		@Override
	    protected void onPreExecute() {
	    	super.onPreExecute();
	    }
	      
	    @Override
	    protected Void doInBackground(Void... params) {
	    	//ejecuta tarea cada 2000ms
	    	int time = 1000;
	    	boolean flag= true;	
	    	while(flag){
		    	if (isCancelled()){
		    		try {
		    			
		    	         flag=false;
		    	     } catch (UnsupportedOperationException e) {
		    	         e.printStackTrace();
		    	     }
		    	}else{
		    		publishProgress(); 
		    		SystemClock.sleep(time);
		    	}
	    	}
	    	return null;
	    }
	  
	    @Override
	    protected void onProgressUpdate(Integer... values) {
	    	super.onProgressUpdate(values);
	    	//si conexion = true logo visible
	    	Database_Helper db = new Database_Helper(thisActivity);
			List<Pet> mascotas = db.getPets(); //lista de mascotas
			db.close();
			Pet petto = new Pet(mascotas.get(0).get_id(), mascotas.get(0).get_name(), mascotas.get(0).get_raza(), mascotas.get(0).get_color(), mascotas.get(0).get_birthdate(), mascotas.get(0).get_mac(), mascotas.get(0).get_death());
			setlifetime(petto._birthdate);
			
	    	hungrypet();
			if(((MainActivity)thisActivity).isConnected()){
				btstate.setVisibility(View.VISIBLE);
			
			}else{
				btstate.setVisibility(View.INVISIBLE);
			
			}
			if(sleeping && energy.getProgress()<500){
				
				energy.setProgress(energy.getProgress()+5);
			}
				
	
			
		}
	    
	    @Override
	    protected void onPostExecute(Void voids) {
	    

	    }
	    
	}

}
