package com.example.estach;

import java.text.SimpleDateFormat;

import android.os.BatteryManager;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		final Database_Helper helper = new Database_Helper(this);
		
		final Intent i = new Intent(this, Logros.class);
		
		final TextView comp = (TextView)findViewById(R.id.textView2);	
		Button button1 = (Button) findViewById(R.id.button1);
        Button button2 = (Button) findViewById(R.id.button2);
        Button button3 = (Button) findViewById(R.id.button3);
        Button button4 = (Button) findViewById(R.id.button4); 
        Button button5 = (Button) findViewById(R.id.button5); 
        
        final String nombre;
        listaAchievements(helper);
        //listaEstadisticas(helper);
 
        //Ver estados
        button1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Perform action on click
            	helper.open_read();
            	Cursor lol2 = helper.getPet("Perro");
            	if(lol2.moveToFirst()){
                	AlertDialog.Builder builder1=new AlertDialog.Builder(MainActivity.this);
                	;
                	builder1.setMessage("Nombre:"+ lol2.getString(1) +
                			"\nRaza: "+ lol2.getString(2) + "\nCumpleaños: "+ lol2.getString(4) +
                			"\nSalud:" + "\nHambre: " + "\nEnergía:" + cargaBateria() +"\nFelicidad:");          	
                	builder1.show();
                	helper.close();            		
            	}
            	else{
                	AlertDialog.Builder builder1=new AlertDialog.Builder(MainActivity.this);
                	;
                	builder1.setMessage("Aun no inicializa su mascota");          	
                	builder1.show();
                	helper.close();   
            	}

            }
        });
        
        //Crea personaje
        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Perform action on click
            	AlertDialog.Builder builder1=new AlertDialog.Builder(MainActivity.this);
            	
            	helper.open_read();
            	Cursor lol  = helper.getAll();
            	
            	//Comprueba si se ha creado una mascota de antes
            	if (lol.moveToFirst() == false){
            		
            		   //el cursor está vacío. O sea, es la primera mascota.
            		   // ---> Achievement: Born to Be Wild
            		   //Muestra achievement en pantalla. Luego, registra el logro el BD.
            		
            		   builder1.setMessage("Achievement Unlocked: Born To be Wild");
            		   
            		   
                   	   builder1.setNeutralButton("OK",new DialogInterface.OnClickListener() {
                   			@Override
                   			public void onClick(DialogInterface dialog, int which) {
                   				// TODO Auto-generated method stub
                   			}
                   	   });
            		   
            		   builder1.show();
                   	   helper.close();
                	
                   	   //Ingresa valores por defecto. Aqui debiera ir el ingreso de los datos de la mascota
                   	   //FechaCorta ingresa el día de hoy como el día de cumpleaños del weta
                   	   helper.open_write();
                   	   
                   	   
                   	   helper.createPet("Perro", fechaCorta() , "perruno", "1" , "wut");
                	   //helper.confirmarAchievement("Born to be Wild", "Crea su primera mascota", 0);      	
                   	   //builder1.show();
                   	   helper.close(); 
                   	   
                   	   //Aqui debería entrar al juego en si con los estados.
                   	   
            		   return;
            	} 
            	
            	//En caso que ya exista una mascota en la BD
            	helper.close();   	
            	
            	helper.open_write();
            	
            	builder1.setMessage("Se insertará un personaje.");
            	helper.createPet("Perro", fechaCorta() , "perruno", "1" , "wut");
            	          	
            	builder1.show();
            	helper.close();
            }
        });
        
        //Mostrar mascota creada
        button3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Perform action on click
            	helper.open_read();
            	Cursor lol  = helper.getAll();
            	//AlertDialog.Builder builder1=new AlertDialog.Builder(MainActivity.this);
            	
            	if (lol.moveToFirst() == false){
            		   //el cursor está vacío.
            		   // ---> Achievement
            		   //builder1.setMessage("No hay nada =O");
            		   comp.setText("Nada");
            		   //builder1.show();
            		   return;
            	}
            	//builder1.setMessage("wea qla");
            	comp.setText(lol.getString(0)+ lol.getString(1)+" "+lol.getString(2)+" "+lol.getString(4));          	
            	//builder1.show();
            	helper.close();
            	
            	
            }
        });
        
        //Borrar BD
        button4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Perform action on click
            	helper.open_read();
            	AlertDialog.Builder builder1=new AlertDialog.Builder(MainActivity.this);
            	helper.deleteAll();
            	builder1.setMessage("wea qla");          	
            	builder1.show();
            	helper.close();
            }
        });
        
        //Ver Logros
        button5.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Perform action on click
            	
                startActivity(i);
            }
        });

		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	

	public String fechaCorta() {
			long tim = System.currentTimeMillis();
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			String curTime = df.format(tim);
			return curTime;
	}
	
    public int cargaBateria ()
    {
        try
        {
        	
            IntentFilter batIntentFilter =
            new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
            Intent battery =
            this.registerReceiver(null, batIntentFilter);
            int nivelBateria = battery.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            return nivelBateria;
        }
        catch (Exception e)
        {
        	
            Toast.makeText(getApplicationContext(),
                    "Error al obtener estado de la batería",
                    Toast.LENGTH_SHORT).show();
           return 0;
        }       
    } 
    
    
    public void listaAchievements(Database_Helper helper)
    {
    	helper.open_read();
    	Cursor aux = helper.getAllAchievements();
    	
    	if(aux.moveToFirst() == false){ 
    		//Ya existe la tabla de Achievements registrada
    		helper.close();
    		return;
    	}
    	helper.close();
    	
		helper.open_write();
		helper.createAchievement("Born to be Wild", "Crea su primera mascota", 0);
		//helper.createAchievement("Walk", "Camina", 0);
		//helper.createAchievement("Armed and Ready", "Tiene más del 75% de Energía", 0);
    	helper.close();    	
    }
    
    public void listaEstadisticas(Database_Helper helper)
    {
    	helper.open_read();
    	Cursor aux = helper.getAllEstadisticas();
    	
    	if(aux.moveToFirst()){ 
    		//Ya existe la tabla de Achievements registrada
    		helper.close();
    		return;
    	}
    	helper.close();
    	
		helper.open_write();
		helper.createEstadistica("Comer", "Cuantas veces la ha alimentado?", 0);
		//helper.createEstadistica("Dormir", "Cuantas veces ha dormido?", 0);
		//helper.createEstadistica("Jugar", "Cuantas veces ha jugado?", 0);
    	helper.close();    	
    }
    

}
