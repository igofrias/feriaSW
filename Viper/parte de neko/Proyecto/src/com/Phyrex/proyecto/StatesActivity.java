package com.Phyrex.proyecto;

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

public class StatesActivity extends SherlockFragment{
	////////Inicializacion del Task
	
	
	private static Activity thisActivity;
	private TextView name;
	private TextView lifetime;
	private ProgressBar salud;
	private ProgressBar hambre;
	private ProgressBar energia;
	private ProgressBar felicidad;
	private ImageView btstate;
	Task task = new Task(this);
	
	private String[] from = new String[]{Database_Helper.Key_name,Database_Helper.Key_name};
	private int[] to = new int[] {android.R.id.text1,android.R.id.text2}; 
	private Database_Helper db;
	private Cursor_Adapter adapter;
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
		super.onActivityCreated(savedInstanceState);    
		thisActivity = getActivity();
		salud = (ProgressBar)thisActivity.findViewById(R.id.salud);
		hambre = (ProgressBar)thisActivity.findViewById(R.id.hambre);
		felicidad = (ProgressBar)thisActivity.findViewById(R.id.felicidad);
		energia = (ProgressBar)thisActivity.findViewById(R.id.energia);
		btstate = (ImageView)thisActivity.findViewById(R.id.btstate);
		name = (TextView)thisActivity.findViewById(R.id.name);
		lifetime = (TextView)thisActivity.findViewById(R.id.lifetime);
		felicidad.setMax(100);
		hambre.setMax(100);
		salud.setMax(100);
		energia.setMax(100);
		//leerBD();
		Database_Helper db = new Database_Helper(thisActivity);
		List<Mascota> mascotas = db.getPets(); //lista de mascotas
		db.close();
		Mascota petto = new Mascota(mascotas.get(0).get_id(), mascotas.get(0).get_name(), mascotas.get(0).get_raza(), mascotas.get(0).get_color(), mascotas.get(0).get_birthdate(), mascotas.get(0).get_mac(), mascotas.get(0).get_death());
		name.setText(petto._name);
		lifetime.setText(lifetimecalc(petto._birthdate) + " Días");
		task.execute();
	}
	void leerBD(){ //guarda en base de datos
		
		//para guardar en la BD
		Database_Helper db = new Database_Helper(getActivity());
		
        Log.d("Reading: ", "Reading all contacts..");
        List<Mascota> mascotas = db.getPets();
        
        for (Mascota cn : mascotas) {
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
	
	private int lifetimecalc(String birthdate){
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
		int days = (int) (diff / (1000*60*60*24));  
		
		return days;
	}
	
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
	    	int tiempo = 2000;
	    	boolean flag= true;	
	    	while(flag){
	    		publishProgress(); 
	    		SystemClock.sleep(tiempo);
		    	if (isCancelled())
				     break;
	    	}
	    	return null;
	    }
	  
	    @Override
	    protected void onProgressUpdate(Integer... values) {
	    	super.onProgressUpdate(values);
	    	//si conexion = true logo visible

			Log.d("pepe", "task!");
	    	energia.setProgress((int) ((MainActivity)thisActivity).getBatteryLevel());
			if(((MainActivity)thisActivity).isConnected()){
				btstate.setVisibility(View.VISIBLE);
			}else{
				btstate.setVisibility(View.INVISIBLE);
			}
			
		}
	    
	}

}
