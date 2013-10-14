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
import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

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
	Task task = new Task(this);

	private String[] from = new String[]{Database_Helper.Key_name,Database_Helper.Key_name};
	private int[] to = new int[] {android.R.id.text1,android.R.id.text2}; 
	private Database_Helper db;
	private Cursor_Adapter adapter;
	/////////////////////////////////
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);        
        setRetainInstance(true);	
    }

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View v = inflater.inflate(R.layout.activity_states, container, false);
		return v;
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
		name = (TextView)thisActivity.findViewById(R.id.name);
		lifetime = (TextView)thisActivity.findViewById(R.id.lifetime);
		hapiness.setMax(100);
		hungry.setMax(100);
		health.setMax(100);
		energy.setMax(100);
		///obtiene los datos de la BD/////////
		Database_Helper db = new Database_Helper(thisActivity);
		List<Pet> mascotas = db.getPets(); //lista de mascotas
		db.close();
		Pet petto = new Pet(mascotas.get(0).get_id(), mascotas.get(0).get_name(), mascotas.get(0).get_raza(), mascotas.get(0).get_color(), mascotas.get(0).get_birthdate(), mascotas.get(0).get_mac(), mascotas.get(0).get_death());
		name.setText(petto._name);
		setlifetime(petto._birthdate);
		task.execute();///se corre el task
		//////////
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
		
        Log.d("Reading: ", "Reading all contacts..");
        List<Pet> mascotas = db.getPets();
        
        for (Pet cn : mascotas) {
            String log = "Id: "+cn.get_id()+", Name: " + cn.get_name() + ", Score: " + cn.get_raza();
                // Writing Contacts to log
        Log.d("Name: ", log);
        
        db.close();
        
        }
	}

	protected void OnDetach(){
		   task.cancel(true);
	}
	
	public void onPause(){
		super.onPause();
		task.cancel(true);
	}
	
	public void onDestroy(){
		// TODO Auto-generated method stub
		super.onDestroy();
		task.cancel(true);
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
	private long lifetimecalc(String birthdate){
		SimpleDateFormat format = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
		Date date1= new Date();
		Date date2= new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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
	    	Log.d("pepe", "fbdftask!");
	    	int time = 2000;
	    	boolean flag= true;	
	    	while(flag){
	    		publishProgress(); 
	    		SystemClock.sleep(time);
		    	if (isCancelled())
				     break;
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
			Log.d("pepe", "task!");
	    	energy.setProgress((int) ((MainActivity)thisActivity).getBatteryLevel());
			if(((MainActivity)thisActivity).isConnected()){
				btstate.setVisibility(View.VISIBLE);
			}else{
				btstate.setVisibility(View.INVISIBLE);
			}
			
		}
	    
	}

}
