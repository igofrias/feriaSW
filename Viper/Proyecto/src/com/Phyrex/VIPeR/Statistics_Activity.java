package com.Phyrex.VIPeR;


import java.util.List;

import com.actionbarsherlock.app.SherlockFragment;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

public class Statistics_Activity extends SherlockFragment {

	/*"from" contiene los nombres de las columnas de la tabla correspondiente
	 * "to" contiene los los id de los recursos (textview, imageview, etc...)
	 * en este ejemplo se emplea un xml perteneciente al SO.
	 * "android.R.layout.simple_list_item_2" que corresponde a un LinearLayout con 2 TextViews
	 * */
	private String[] from = new String[]{Database_Helper.Key_name_est,Database_Helper.Key_desc_est,Database_Helper.Key_cant_est, Database_Helper.Key_imgstat ,Database_Helper.Key_id_est}; 
	private Database_Helper db;
	private Statistics_CursorAdapter adapter;
	private ListView lista;
	private TextView statsname;
	private TextView statscolor;
	private TextView statsrace;
	private TextView statsbirthdate;
	private ImageButton statsback;
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
		
		View v = inflater.inflate(R.layout.fragmento_ranking, container, false);
		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		thisActivity = (MainActivity) getActivity();
		
		String birthdate[] = new String [2];
		lista = (ListView)thisActivity.findViewById(R.id.lst_1);		
		Reload();	//cargar datos de la BD
        lista.setCacheColorHint(Color.TRANSPARENT);
        lista.setItemsCanFocus(false);
        Database_Helper db = new Database_Helper(thisActivity);
		List<Pet> mascotas = db.getPets(); //lista de mascotas
		db.close();
		Pet petto = mascotas.get(0);
		statsname= (TextView)thisActivity.findViewById(R.id.statname);
		statscolor= (TextView)thisActivity.findViewById(R.id.statscolor);
		statsrace = (TextView)thisActivity.findViewById(R.id.statrace);
		statsbirthdate = (TextView)thisActivity.findViewById(R.id.statsbirthdate);
		statsback = (ImageButton)thisActivity.findViewById(R.id.statsback);
		statsname.setText(petto._name);
		statscolor.setText(petto._color);
		statsrace.setText(petto._raza);
		birthdate = petto._birthdate.split(" ");
		statsbirthdate.setText(birthdate[1]);
		
		//Evento de click para un item
        statsback.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				((MainActivity)thisActivity).launch_mainpet();
				((MainActivity)thisActivity).launch_states();
			}
  		});
		
	}
	
	
	private void Reload() {
		db = new Database_Helper(thisActivity);
	    db.open_read();
	    Cursor c = db.getAllStatistics();		//llenando el cursor con datos 
	    adapter = new Statistics_CursorAdapter(thisActivity, R.layout.rowlist, c, from);		//creando el adapter
	    lista.setAdapter(adapter);		//entregandole el adapter a la lista
	    db.close();
	} 
	
	public void onBackPressed()
	{
		((MainActivity)thisActivity).launch_mainpet();
		((MainActivity)thisActivity).launch_states();
	}
	
}
