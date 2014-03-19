package com.Phyrex.VIPeR;

import java.text.SimpleDateFormat;
import java.util.Date;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import com.actionbarsherlock.app.SherlockFragment;

/*******Create Activity
 * se encarga de crear una mascota nueva en la base de datos
 * obtiene algunos datos y recibe otros por parte de el usuario
 * @author Neko-sama
 *
 */
@SuppressLint("SimpleDateFormat")
public class CreateActivity extends SherlockFragment{

	////////declaración de variables///////
	private EditText name;
	private Button btn;
	private static Activity thisActivity;
	private Button bncnt;
	private Button bncnt2;
	private ImageButton leftarrow;
	private ImageButton rightarrow;
	private ImageButton btncolor []= new ImageButton [3];
	private String color;
	private String raze;
	private ImageView screenshot;
	MainActivity blublu = new MainActivity();
	///////////////////////////
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
	
	OnClickListener listener = new OnClickListener(){
		@Override
		public void onClick(View v) {
			switch(v.getId()){
			case R.id.btncolor1:
				Log.e("Create","1");
				screenshot.setImageResource(R.drawable.tentosaurioshot);
				color="Verde";
				break;
			case R.id.btncolor2:
				Log.e("Create","2");
				screenshot.setImageResource(R.drawable.tentosaurioshot2);
				color="Amarillo";
				break;
			case R.id.btncolor3:
				Log.e("Create","3");
				screenshot.setImageResource(R.drawable.tentosaurioshot3);
				color="Azul";
				break;
			}
		}
	};
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState); 
		thisActivity = getActivity();
		name = (EditText)thisActivity.findViewById(R.id.name);
		btn = (Button)thisActivity.findViewById(R.id.crear);
		screenshot= (ImageView)thisActivity.findViewById(R.id.screenshot);
		screenshot.setImageResource(R.drawable.tentosaurioshot);
		color="Verde";
		raze="Tentosaurio";
		btncolor[0]  = (ImageButton)thisActivity.findViewById(R.id.btncolor1);
		btncolor[1]  = (ImageButton)thisActivity.findViewById(R.id.btncolor2);
		btncolor[2]  = (ImageButton)thisActivity.findViewById(R.id.btncolor3);
		for(int i=0; i<3; i++)
			btncolor[i].setOnClickListener(listener);
		leftarrow = (ImageButton)thisActivity.findViewById(R.id.leftarrow);
		rightarrow = (ImageButton)thisActivity.findViewById(R.id.rightarrow);
		bncnt = (Button)thisActivity.findViewById(R.id.bncnt);
		bncnt2 = (Button)thisActivity.findViewById(R.id.bncnt2);
		
		leftarrow.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				if(raze.equals("Tentosaurio")){
					screenshot.setImageResource(R.drawable.scorpion);
					btncolor[1].setVisibility(View.GONE);
					btncolor[2].setVisibility(View.GONE);
					raze="Escorpion";
				}else{
					screenshot.setImageResource(R.drawable.tentosaurioshot);
					btncolor[1].setVisibility(View.VISIBLE);
					btncolor[2].setVisibility(View.VISIBLE);
					btncolor[0].setVisibility(View.VISIBLE);
					raze="Tentosaurio";
				}
			}});
		rightarrow.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				if(raze.equals("Tentosaurio")){
					screenshot.setImageResource(R.drawable.scorpion);
					btncolor[1].setVisibility(View.GONE);
					btncolor[2].setVisibility(View.GONE);
					btncolor[0].setImageResource(R.id.btncolor3);
					raze="Escorpion";
				}else{
					screenshot.setImageResource(R.drawable.tentosaurioshot);
					btncolor[1].setVisibility(View.VISIBLE);
					btncolor[2].setVisibility(View.VISIBLE);
					btncolor[0].setVisibility(View.VISIBLE);
					btncolor[0].setImageResource(R.id.btncolor1);
					raze="Tentosaurio";
				}
			}});
		bncnt.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				((MainActivity)thisActivity).pairing(0);
			}});
		
		bncnt2.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				((MainActivity)thisActivity).pairing(1);
				
			}});
		
		/************bnt listener
		 *  boton para crear mascota
		 *  muestra un dialog para confirmacion y se encarga de validar datos minimos
		 *  y luego los guarda en la BD como una nueva mascota
		 *  
		 */
        btn.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {	
				//nametext.setText(raza.getSelectedItemId().toString());
				if(raze.equals("Tentosaurio")){
					if(color.equals("Verde")){
						if(!isEmpty(name)){
							SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss dd-MM-yyyy");
							final String currentDateandTime = sdf.format(new Date());//si hay nombre crea el bicho en la base de datos
							if(((MainActivity)thisActivity).return_mac()!=""){	
								final Database_Helper entry = new Database_Helper(CreateActivity.thisActivity);
								final DB_Updater updater = new DB_Updater(CreateActivity.thisActivity);
								final Init initialize = new Init(CreateActivity.thisActivity);
							 /*ESTE DIALOG MUESTRA COMO SE LE CARGAN COSAS PREDEFINIDAS (no alterables)
								 * */
								AlertDialog.Builder dialog = new AlertDialog.Builder(thisActivity);  
						        dialog.setTitle("Confirmación de datos");		//titulo (opcional)
						        dialog.setIcon(R.drawable.ic_launcher);		//icono  (opcional)
						        dialog.setMessage("¿Desea Crear la mascota con los siguientes datos?\n" + 
						        "Nombre: "+ name.getText().toString() +  "\n"+
						        "Fecha de Nacimiento: " + currentDateandTime+ "\n"+
						        "Raza: "+ raze + "\n"+
						        "Color: "+ color + "\n"+
						        "MAC: "+((MainActivity)thisActivity).return_mac()); 
						        
						        
						        dialog.setNegativeButton("Salir", null);  //boton de salida  (opcional)
						        dialog.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {  //boton positivo (opcional)
						            public void onClick(DialogInterface dialogo1, int id) {  
						            	entry.open_write();            	
								    	entry.createEntry(name.getText().toString(), currentDateandTime, raze, color, ((MainActivity)thisActivity).return_mac(),
								    							((MainActivity)thisActivity).getBTService().mac_slave, 0);
								    	entry.close();
								    	initialize.AchievementsList(entry);
								    	initialize.StatisticsList(entry);
								    	initialize.GamesList(entry);
								    	updater.newpet(entry);
								    	if(updater.achievement_unlock(entry, "Primera Mascota"))
								    		Toast.makeText(thisActivity, "Logro Primera Mascota Desbloqueado", Toast.LENGTH_LONG).show();
								    	if(name.getText().toString().equals("Pepe") || name.getText().toString().equals("pepe")){
								    		Log.d("pepe", "es pepe");
								    		if(updater.achievement_unlock(entry, "Pepe"))
								    			Toast.makeText(thisActivity, "Logro Pepe Debloqueado", Toast.LENGTH_LONG).show();
								    	}
								    	if(raze.equals("Tentosaurio")){
								    		Log.d("pepe", "es tentosaurio");
								    		if(updater.achievement_unlock(entry, "Tentosaurio"))
								    			Toast.makeText(thisActivity, "Logro Pepe Debloqueado", Toast.LENGTH_LONG).show();
								    	}
								    	((MainActivity)thisActivity).detach_create();
								    	((MainActivity)thisActivity).launch_states();
								    	((MainActivity)thisActivity).launch_mainpet();
						            }  
						        });
						        dialog.show();
								
							}else{
								Toast.makeText(thisActivity, "No hay Robot pareado", Toast.LENGTH_SHORT).show();
							}
					  }else{
						  Toast.makeText(thisActivity, "Ingresar nombre de Mascota", Toast.LENGTH_SHORT).show();
					  }
					}else{
						 Toast.makeText(thisActivity, "Color no disponible", Toast.LENGTH_SHORT).show();
					}		
			  }else{
				  Toast.makeText(thisActivity, "Raza no disponible", Toast.LENGTH_SHORT).show();
			  }
			}});
        
	}
	
	 private boolean isEmpty(EditText etText) {
         return etText.getText().toString().trim().length() == 0;
     }
	 
	 public void onDestroy(){
		 super.onDestroy();
	 }
	 
	 public  void onStop(){
		 super.onStop();
	 }
	 
	 public void onResume(){
		 super.onResume();
	 }

}
