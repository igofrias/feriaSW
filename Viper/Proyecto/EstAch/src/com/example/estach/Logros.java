package com.example.estach;

import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

//Logros Actuando como Activity_Base Adapter
public class Logros extends Activity {
	
	static List<LogroBD> listaEstadisticas;

	/** Called when the activity is first created. */
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_adapter);
        
        Intent startingIntent = getIntent();
        AlertDialog.Builder builder1=new AlertDialog.Builder(this);
        
        Bundle b = startingIntent.getBundleExtra("android.intent.extra.INTENT");
        //this.helper = (Database_Helper)getIntent().getSerializableExtra("helper");
        
        /*Cursor random = helper.getAllEstadisticas();
        
    	if (random.moveToFirst() == false){
 		   return;
    	} 
    	else{
    		do{
    			
    			builder1.setMessage(random.getString(1)+random.getString(2));
    			builder1.show(); 
    			
    		}while(random.moveToNext());
    	}
    	*/
        /*
        List<LogroBD> listaEstadisticas = (List<LogroBD>)getIntent().getSerializableExtra("listaEstadisticas"); //lista de logros logrados       
        Iterator iterador = listaEstadisticas.listIterator();
        while( iterador.hasNext() ) {
            LogroBD b = (LogroBD)iterador.next(); //Obtener elemento
            builder1.setMessage(b.getName()+b.getDescripcion());
        }
        */
        
        	    
    }
    
    public void cerrar(View view) {
    	finish();
    }  
    
    
}

