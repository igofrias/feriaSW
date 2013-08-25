package com.Phyrex.proyecto;

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

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;

import android.os.AsyncTask;

public class StatesActivity extends SherlockFragment{
	////////Inicializacion del Task
	
	
	private static Activity thisActivity;
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
		energia = (ProgressBar)thisActivity.findViewById(R.id.hambre);
		hambre = (ProgressBar)thisActivity.findViewById(R.id.felicidad);
		felicidad = (ProgressBar)thisActivity.findViewById(R.id.energia);
		btstate = (ImageView)thisActivity.findViewById(R.id.btstate);
		felicidad.setMax(100);
		hambre.setMax(100);
		salud.setMax(100);
		energia.setMax(100);
		task.execute();
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
			if(((MainActivity)thisActivity).connected()){
				btstate.setVisibility(View.VISIBLE);
			}else{
				btstate.setVisibility(View.INVISIBLE);
			}
			
		}
	    
	}

}
