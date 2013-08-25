package com.Phyrex.proyecto;

import java.text.SimpleDateFormat;
import java.util.Date;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.database.Cursor;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;

@SuppressLint("SimpleDateFormat")
public class CreateActivity extends SherlockFragment{

	/*"from" contiene los nombres de las columnas de la tabla correspondiente
	 * "to" contiene los los id de los recursos (textview, imageview, etc...)
	 * en este ejemplo se emplea un xml perteneciente al SO.
	 * "android.R.layout.simple_list_item_2" que corresponde a un LinearLayout con 2 TextViews
	 * */
	private String[] from = new String[]{Database_Helper.Key_name,Database_Helper.Key_name};
	private int[] to = new int[] {android.R.id.text1,android.R.id.text2}; 
	private Database_Helper db;
	private Cursor_Adapter adapter;
	private ExpandableListView lista;
	private EditText name;
	private Button btn;
	private TextView nametext;
	private static Activity thisActivity;
	private Button bncnt;
	MainActivity blublu = new MainActivity();
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);        
        setRetainInstance(true);
    }

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View v = inflater.inflate(R.layout.activity_create, container, false);
		return v;
	}	
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState); 
		thisActivity = getActivity();
		//setContentView(R.layout.activity_create);
		final Spinner raza = (Spinner) thisActivity.findViewById(R.id.raza);
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(
        		getActivity(), R.array.mnth_array, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        final Spinner color = (Spinner) thisActivity.findViewById(R.id.color);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(
        		getActivity(), R.array.colors_array, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        raza.setAdapter(adapter1);
        color.setAdapter(adapter2);
		name = (EditText)thisActivity.findViewById(R.id.name);
		btn = (Button)thisActivity.findViewById(R.id.crear);
		nametext = (TextView)thisActivity.findViewById(R.id.nametext);
		
		bncnt = (Button)thisActivity.findViewById(R.id.bncnt);
		//Reload();	//cargar datos de la BD
		
		bncnt.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				((MainActivity)thisActivity).conect();
			}});


        btn.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {	
				//nametext.setText(raza.getSelectedItemId().toString());
				if(name.getText()!=null){
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					String currentDateandTime = sdf.format(new Date());//si hay nombre crea el bicho en la base de datos
					Database_Helper entry = new Database_Helper(CreateActivity.thisActivity);
					entry.open_write();            	
			    	entry.createEntry(name.getText().toString(), currentDateandTime, raza.getSelectedItem().toString(), color.getSelectedItem().toString());
			    	entry.close();
			  }  	
			}});
        
      //Evento de click para un item
        /*lista.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> l, View v, int position, long id) {
			
			}
  		});
		*/
	}
	
	
	/*private void Reload() {
		db = new Database_Helper(this);
	    db.open_read();
	    Cursor c = db.getAll();		//llenando el cursor con datos 
	    adapter =  new ArrayAdapter(this, 0, c);		//creando el adapter
	    lista.setAdapter(adapter);		//entregandole el adapter a la lista
	    db.close();
	} */

}
