package com.example.estach;

import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

//Logros Actuando como Activity_Base Adapter
public class Logros extends Activity {
	

	/** Called when the activity is first created. */
		
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragmento_ranking);
        
        	    
    }
    
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
    	Database_Helper helper = new Database_Helper(this);
    	
    	List<LogroBD> listaLogros = helper.getAllDoneAchievements();
    	
		View v = inflater.inflate(R.layout.fragmento_ranking, container, false);
		ListView listView1 = (ListView)v.findViewById(R.id.lst_1);
		
		ArrayAdapter<LogroBD> adapter = new ArrayAdapter<LogroBD>(this,
                android.R.layout.simple_list_item_1, listaLogros);

    listView1.setAdapter(adapter);
		//db.close();
		return v;
	}
    
    public void cerrar(View view) {
    	finish();
    }  
    
	void leerBD(String nombre, String descripcion, int score){ //guarda en base de datos
		
		//para guardar en la BD
		Database_Helper helper = new Database_Helper(this);
		
        Log.d("Reading: ", "Reading all contacts..");
        List<LogroBD> logroBD = helper.getAllDoneAchievements();
        
        for (LogroBD cn : logroBD) {
            String log = "Id: "+cn.getID()+", Name: " + cn.getName() + "Description: " + cn.getDescripcion()+" Score: " + cn.getHecho();
                // Writing Contacts to log
        Log.d("Name: ", log);
        
        //db.close();
        
        }
	}
    
    
}

