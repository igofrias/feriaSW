package com.Phyrex.VIPeR;


import com.actionbarsherlock.app.SherlockFragment;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.GridView;

public class Statistics_Activity extends SherlockFragment {

	/*"from" contiene los nombres de las columnas de la tabla correspondiente
	 * "to" contiene los los id de los recursos (textview, imageview, etc...)
	 * en este ejemplo se emplea un xml perteneciente al SO.
	 * "android.R.layout.simple_list_item_2" que corresponde a un LinearLayout con 2 TextViews
	 * */
	private String[] from = new String[]{Database_Helper.Key_name_est,Database_Helper.Key_desc_est,Database_Helper.Key_cant_est,Database_Helper.Key_id_est}; 
	private Database_Helper db;
	private Statistics_CursorAdapter adapter;
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
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		thisActivity = (MainActivity) getActivity();
		
		
		lista = (GridView)thisActivity.findViewById(R.id.logros);		
		Reload();	//cargar datos de la BD
        lista.setCacheColorHint(Color.TRANSPARENT);
        
      //Evento de click para un item
        /*lista.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> l, View v, int position, long id) {
			
			}
  		});
		*/
	}
	
	
	private void Reload() {
		db = new Database_Helper(thisActivity);
	    db.open_read();
	    Cursor c = db.getAllStatistics();		//llenando el cursor con datos 
	    adapter = new Statistics_CursorAdapter(thisActivity, R.layout.row, c, from);		//creando el adapter
	    lista.setAdapter(adapter);		//entregandole el adapter a la lista
	    db.close();
	} 
	
}
