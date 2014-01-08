package com.Phyrex.VIPeR;

import com.actionbarsherlock.app.SherlockFragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
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

public class GameList_Fragment extends SherlockFragment {

	//Adaptación del ejemplo de Taller de Android 
	//Fusión entre BaseAdapter (Row.xml) y Cursor_Adapter 
	//(para dinamicidad en caso q se agreguen mas cosas a la BD)
	private String[] from = new String[]{Database_Helper.Key_id_game,Database_Helper.Key_name_game,Database_Helper.Key_desc_game,Database_Helper.Key_img_game,Database_Helper.Key_score_game};
	private Database_Helper db;
	private GameList_CursorAdapter adapter;
	private ListView lista;	
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
	
	public void onResume(){
		super.onResume();
		Reload(((MainActivity)thisActivity).isConnected());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.fragment_gamelist, container, false);
		return v;
	}	

	@Override
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
		thisActivity = (MainActivity) getActivity();


		lista = (ListView)thisActivity.findViewById(R.id.listgames);		
		Reload(((MainActivity)thisActivity).isConnected());	//cargar datos de la BD
		lista.setCacheColorHint(Color.TRANSPARENT);
		//Evento de click para un item

		lista.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> l, View v, int position, long id) {
				
				Log.e("pepe","antes "+String.valueOf(position));
				////seteo de restar 
				if(position==0){
					Intent intent = new Intent(thisActivity, BallGame.class);
				    startActivity(intent);
				}else if(position==1){
					((MainActivity) thisActivity).launch_remotecontrolgame();
				}else if(position==2){
					Intent intent = new Intent(thisActivity, FleaGame.class);
				    startActivity(intent);
				}else{
					Log.e("pepe",String.valueOf(position));
				}
				Reload(((MainActivity)thisActivity).isConnected());
			}
		});
		

	}


	private void Reload(boolean connected) {
		db = new Database_Helper(thisActivity);
		db.open_read();
		Cursor c = db.getAllGames();		//llenando el cursor con datos 
		adapter = new GameList_CursorAdapter(thisActivity, R.layout.rowgames, c, from, connected);		//creando el adapter
		lista.setAdapter(adapter);	
		Log.e("pepe","reload");//entregandole el adapter a la lista
		db.close();
	} 

}
