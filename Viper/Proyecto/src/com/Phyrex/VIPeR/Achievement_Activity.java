package com.Phyrex.VIPeR;

import com.actionbarsherlock.app.SherlockFragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import android.widget.GridView;

public class Achievement_Activity extends SherlockFragment {

	//Adaptación del ejemplo de Taller de Android 
	//Fusión entre BaseAdapter (Row.xml) y Cursor_Adapter 
	//(para dinamicidad en caso q se agreguen mas cosas a la BD)
	private String[] from = new String[]{Database_Helper.Key_name_ach,Database_Helper.Key_desc_ach,Database_Helper.Key_done,Database_Helper.Key_img,Database_Helper.Key_id};
	private Database_Helper db;
	private Achievement_CursorAdapter adapter;
	private GridView lista;	
	private static Activity thisActivity;
	Activity parent_activity;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);        
		setRetainInstance(true);
	}

	@Override
	public void onAttach(Activity activity)
	{
		super.onAttach(activity);
		parent_activity = activity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.activity_cursor_adapter, container, false);
		return v;
	}	

	@Override
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
		thisActivity = (MainActivity) getActivity();


		lista = (GridView)thisActivity.findViewById(R.id.logros);		
		Reload();	//cargar datos de la BD
		lista.setCacheColorHint(Color.TRANSPARENT);

		//Evento de click para un item
		
		lista.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> l, View v, int position, long id) {
				
				LinearLayout linearLayot= (LinearLayout) l.getChildAt(position);
		     	TextView name = (TextView) linearLayot.getChildAt(1);
		     	TextView archdesc = (TextView) linearLayot.getChildAt(2);
		     	TextView imgach = (TextView) linearLayot.getChildAt(3);
			
				if(!name.getText().equals("void")){
					AlertDialog.Builder dialog = new AlertDialog.Builder((MainActivity)thisActivity);  
			        dialog.setTitle("Logro Desbloqueado");		//titulo (opcional)
			        dialog.setIcon(Integer.valueOf(imgach.getText().toString()));		//icono  (opcional)
			        dialog.setMessage("Titulo: " + name.getText() + "\n\n"
			        		+ "Descripción:\n"
			        		+ archdesc.getText()); 
			        
			        
			        dialog.setNegativeButton("OK", null);  //boton de salida  (opcional)
			        dialog.show();
				}else{
					AlertDialog.Builder dialog = new AlertDialog.Builder((MainActivity)thisActivity);  
			        dialog.setTitle("Logro Bloqueado");		//titulo (opcional)
			        dialog.setIcon(R.drawable.logrobloq);		//icono  (opcional)
			        dialog.setMessage("Aun no has desbloqueado este logro"); 
			        
			        
			        dialog.setNegativeButton("OK", null);  //boton de salida  (opcional)
			        dialog.show();
				}
			
			
			}
		});
		

	}


	private void Reload() {
		db = new Database_Helper(thisActivity);
		db.open_read();
		Cursor c = db.getAllAchievements();		//llenando el cursor con datos 
		adapter = new Achievement_CursorAdapter(thisActivity, R.layout.row, c, from);		//creando el adapter
		lista.setAdapter(adapter);		//entregandole el adapter a la lista
		db.close();
	} 

}
