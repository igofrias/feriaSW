package com.Phyrex.proyecto;


import com.Phyrex.proyecto.BTConnectable;
import com.Phyrex.proyecto.BTCommunicator;
import com.Phyrex.proyecto.DeviceListActivity;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;

import android.support.v4.app.FragmentTransaction;

import android.os.BatteryManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.app.ProgressDialog;
import android.view.View.OnClickListener;
import android.graphics.Color;
import android.os.AsyncTask;

import java.io.IOException;


public class MainActivity extends SherlockFragmentActivity implements BTConnectable{
	
	////////Inicializacion del Task
	Task task = new Task(this);
	///////////////////
	//Toast ara mensajes
	private Toast reusableToast;
	///////////////////////
	///////////////////Variables Conexión Bluetooth////////////////////////////////////////
	private boolean pairing;
	private static boolean btOnByUs = false;
	private BTCommunicator myBTCommunicator = null;
	private Handler btcHandler;
	private boolean connected = false;
    private static final int REQUEST_CONNECT_DEVICE = 1000;
    static final int REQUEST_ENABLE_BT = 2000;
    private ProgressDialog connectingProgressDialog;
    private Activity thisActivity;
    private boolean btErrorPending = false;
    private FrameLayout frame;
    private FrameLayout frame1;
    private FrameLayout frame2;
    
    //////////////////////////////////
	
    //***********Botones y texview*************////
    Button bncnt;
    
    /////*********Valores de motores*********//////
    private int motorActiona;
    private int motorActionb;
    private int motorActionc;
    private int directionAction;
  
    ///////////////////////////////////
    
  /*  /////Detecta si el Bluetooth esta activado////////////
    public static boolean isBtOnByUs() {
        return btOnByUs;
    }
	
    ///detecta si se activo el bluetooth////////
    public static void setBtOnByUs(boolean btOnByUs) {
        MainActivity.btOnByUs = btOnByUs;
    }
    
    ///Crea un nuevo objeto para realizar la conexion///////////
    private void createBTCommunicator() {
        // interestingly BT adapter needs to be obtained by the UI thread - so we pass it in in the constructor
        myBTCommunicator = new BTCommunicator(this, myHandler, BluetoothAdapter.getDefaultAdapter(), getResources());
        btcHandler = myBTCommunicator.getHandler();
    }
    //crea y arranca un thread para la conexion bluetooth/////////////77
    //recibe la mac del robot/////////////
    private void startBTCommunicator(String mac_address) {
        connected = false;        
        connectingProgressDialog = ProgressDialog.show(this, "", getResources().getString(R.string.connecting_please_wait), true);

        if (myBTCommunicator != null) {
            try {
                myBTCommunicator.destroyNXTconnection();
            }
            catch (IOException e) { }
        }
        createBTCommunicator();
        myBTCommunicator.setMACAddress(mac_address);
        myBTCommunicator.start();
    }
    
    ////Termina la conexion bluetooth (destruye el thread)//////////
    public void destroyBTCommunicator() {

        if (myBTCommunicator != null) {
            sendBTCmessage(BTCommunicator.NO_DELAY, BTCommunicator.DISCONNECT, 0, 0);
            myBTCommunicator = null;
        }

        connected = false;
    }*/

   ///muestra el Toast///
    ////recive el mensaje y la duracion del Toast///////////
    private void showToast(String textToShow, int length) {
        reusableToast.setText(textToShow);
        reusableToast.setDuration(length);
        reusableToast.show();
    }

    ///muestra el Toast///
    ////recive el mensaje  (int - ID) y la duracion del Toast///////////
    void showToast(int resID, int length) {
        reusableToast.setText(resID);
        reusableToast.setDuration(length);
        reusableToast.show();
    }
    
    @Override
    protected void onStart() {
        super.onStart();
    }
    
    @Override
    public void onResume() {
        super.onResume();
    }
    ///Creacion de la actividad, inicializacion de botoes y texto.
    @SuppressLint("ShowToast")
	/// ejecucon del task
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		thisActivity = this;
		setContentView(R.layout.activity_main);

		frame1 = (FrameLayout)findViewById(R.id.frame1);
		frame2 = (FrameLayout)findViewById(R.id.frame2);
		//bncnt.setOnClickListener(listener);
		reusableToast = Toast.makeText(thisActivity, "", Toast.LENGTH_SHORT);
		launch_create2();
		launch_states();
		
		
		
		//task.execute();	
	}
    
    
    //inicia servicio de blublu
    public void conect(){
    	startService(new Intent(getBaseContext(), BTService.class));
    }
    
    //llama al supa framento
    void launch_create() {//identificamos y cargamos el fragmento menu
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		SherlockFragment fragmento_crear1 = ((CreateActivity)getSupportFragmentManager().findFragmentByTag("main"));
		
		if(fragmento_crear1==null){
			fragmento_crear1 = new CreateActivity();
			ft.add(R.id.linear, fragmento_crear1,"create");
		}
		else{
			if(fragmento_crear1.isDetached()){
				ft.attach(fragmento_crear1);
			}
		}
		ft.commit();
	}
    //llama al supa framento
    private void launch_create2() {//identificamos y cargamos el fragmento menu
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		SherlockFragment fragmento_crear1 = ((CreateActivity)getSupportFragmentManager().findFragmentByTag("main"));
		
		if(fragmento_crear1==null){
			fragmento_crear1 = new CreateActivity();
			ft.add(R.id.frame2, fragmento_crear1,"create");
		}
		else{
			if(fragmento_crear1.isDetached()){
				ft.attach(fragmento_crear1);
			}
		}
		ft.commit();
	}
    
  //llama al supa framento
    private void launch_states() {//identificamos y cargamos el fragmento menu
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		SherlockFragment fragmento_crear1 = ((CreateActivity)getSupportFragmentManager().findFragmentByTag("main"));
		
		if(fragmento_crear1==null){
			fragmento_crear1 = new StatesActivity();
			ft.add(R.id.frame1, fragmento_crear1,"state");
		}
		else{
			if(fragmento_crear1.isDetached()){
				ft.attach(fragmento_crear1);
			}
		}
		ft.commit();
	}

    //llama al supa framento
      void launch_devicelist() {//identificamos y cargamos el fragmento menu
  		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
  		SherlockFragment fragmento_crear1 = ((CreateActivity)getSupportFragmentManager().findFragmentByTag("main"));
  		
  		if(fragmento_crear1==null){
  			fragmento_crear1 = new DeviceListActivity();
  			ft.add(R.id.frame2, fragmento_crear1,"device");
  		}
  		else{
  			if(fragmento_crear1.isDetached()){
  				ft.attach(fragmento_crear1);
  			}
  		}
  		ft.commit();
  	}
    
      public float getBatteryLevel() {
  	    Intent batteryIntent = registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
  	    int level = batteryIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
  	    int scale = batteryIntent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

  	    // Error checking that probably isn't needed but I added just in case.
  	    if(level == -1 || scale == -1) {
  	        return 50.0f;
  	    }

  	    return ((float)level / (float)scale) * 100.0f; 
  	}

	@Override
    protected void onDestroy() {
        super.onDestroy();
        //destroyBTCommunicator();
        if (btOnByUs){
            showToast(R.string.bt_off_message, Toast.LENGTH_SHORT);
            BluetoothAdapter.getDefaultAdapter().disable();
        }
            task.cancel(true);
    }

    @Override
    public void onPause() {
       // destroyBTCommunicator();
        super.onStop();
    }

    @Override
    public void onSaveInstanceState(Bundle icicle) {
        super.onSaveInstanceState(icicle);
    }
	 
    ///envia al bthandler los mensajes via blublu   (enteros)
   /* void sendBTCmessage(int delay, int message, int value1, int value2) {
        Bundle myBundle = new Bundle();
        myBundle.putInt("message", message);
        myBundle.putInt("value1", value1);
        myBundle.putInt("value2", value2);
        Message myMessage = myHandler.obtainMessage();
        myMessage.setData(myBundle);

        if (delay == 0)
            btcHandler.sendMessage(myMessage);

        else
            btcHandler.sendMessageDelayed(myMessage, delay);
    }*/

  ///envia al bthandler los mensajes via blublu   (string)  
 /*   void sendBTCmessage(int delay, int message, String name) {
        Bundle myBundle = new Bundle();
        myBundle.putInt("message", message);
        myBundle.putString("name", name);
        Message myMessage = myHandler.obtainMessage();
        myMessage.setData(myBundle);

        if (delay == 0)
            btcHandler.sendMessage(myMessage);
        else
            btcHandler.sendMessageDelayed(myMessage, delay);
    }
    //llama a la actividad que  busca dispositivos
    void selectNXT() {
        Intent serverIntent = new Intent(this, DeviceListActivity.class);
        startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
    }
    
    //ejecuta la accion del boton 1, toca mozart y mueve el motor A
   /* private void boton1() {
    		motorActiona = BTCommunicator.MOTOR_A;
    		directionAction = 1;
            // Wolfgang Amadeus Mozart 
            // "Zauberfloete - Der Vogelfaenger bin ich ja"
            sendBTCmessage(BTCommunicator.NO_DELAY, 
                BTCommunicator.DO_BEEP, 392, 100);
            sendBTCmessage(200, BTCommunicator.DO_BEEP, 440, 100);
            sendBTCmessage(400, BTCommunicator.DO_BEEP, 494, 100);
            sendBTCmessage(600, BTCommunicator.DO_BEEP, 523, 100);
            sendBTCmessage(800, BTCommunicator.DO_BEEP, 587, 300);
            sendBTCmessage(1200, BTCommunicator.DO_BEEP, 523, 300);
            sendBTCmessage(1600, BTCommunicator.DO_BEEP, 494, 300);
            
            int direction =  1;                
		    sendBTCmessage(BTCommunicator.NO_DELAY, motorActiona, 
		        30*direction*directionAction, 0);
		    sendBTCmessage(500, motorActiona, 
		        -30*direction*directionAction, 0);
		    sendBTCmessage(1000, motorActiona, 0, 0);


    }       
    */
    //ejecuta la accion del boton 1, toca musica y mueve el motor A
   /* private void boton2() {
		motorActiona = BTCommunicator.MOTOR_A;
		directionAction = 1;
        // G-F-E-D-C
        sendBTCmessage(BTCommunicator.NO_DELAY, 
            BTCommunicator.DO_BEEP, 392, 100);
        sendBTCmessage(200, BTCommunicator.DO_BEEP, 349, 100);
        sendBTCmessage(400, BTCommunicator.DO_BEEP, 330, 100);
        sendBTCmessage(600, BTCommunicator.DO_BEEP, 294, 100);
        sendBTCmessage(800, BTCommunicator.DO_BEEP, 262, 300);

        int direction =  -1;                
        sendBTCmessage(BTCommunicator.NO_DELAY, motorActiona, 
            30*direction*directionAction, 0);
        sendBTCmessage(500, motorActiona, 
            -30*direction*directionAction, 0);
        sendBTCmessage(1000, motorActiona, 0, 0);

    }*/
	
    ///ejecuta funciones para caminar
    /*private void caminar(int direccion) {
		motorActionb = BTCommunicator.MOTOR_B;
		motorActionc = BTCommunicator.MOTOR_C;
		directionAction = 1;
	    int direction1 =  1;
	    int direction2 =  -1;
	    int potencia1=0;
	    int potencia2=0;
	    if(direccion == 1){
	    	potencia1=75;
	    	potencia2=75;
	    	direction1=-1;
	    	direction2=-1;
	    }else if(direccion == 2){
	    	potencia1=15;
	    	potencia2=55;
	    	direction1=-1;
	    	direction2=-1;
	    }else if(direccion == 3){
	    	potencia1=75;
	    	potencia2=75;
	    	direction1=1;
	    	direction2=1;
	    }else if(direccion == 4){
	    	potencia1=55;
	    	potencia2=15;
	    	direction1=-1;
	    	direction2=-1;
	    }
	    sendBTCmessage(BTCommunicator.NO_DELAY, motorActionb, potencia1*direction1*directionAction, 0);
	    sendBTCmessage(1000, motorActionb, 0, 0);
		sendBTCmessage(BTCommunicator.NO_DELAY, motorActionc, potencia2*direction2*directionAction, 0);
		sendBTCmessage(1000, motorActionc, 0, 0);

}*/
    
   /* private int byteToInt(byte byteValue) {
        int intValue = (byteValue & (byte) 0x7f);

        if ((byteValue & (byte) 0x80) != 0)
            intValue |= 0x80;

        return intValue;
    }
    
    //retorna si esta pareado 
    @Override
    public boolean isPairing() {
        return pairing;
    }*/
    
    //recibe datos de dispositivo e inicia la conexion
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CONNECT_DEVICE:

                
                if (resultCode == Activity.RESULT_OK) {
                    String address = data.getExtras().getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
                    pairing = data.getExtras().getBoolean(DeviceListActivity.PAIRING);
                   // startBTCommunicator(address);
                    
                }
                
                break;
                
            case REQUEST_ENABLE_BT:

                // When the request to enable Bluetooth returns
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        btOnByUs = true;
                       // selectNXT();
                        break;
                    case Activity.RESULT_CANCELED:
                        showToast(R.string.bt_needs_to_be_enabled, Toast.LENGTH_SHORT);
                        finish();
                        break;
                    default:
                        showToast(R.string.problem_at_connecting, Toast.LENGTH_SHORT);
                        finish();
                        break;
                }
                
                break;
             
        }
    }
    
    
    //administra los errores que pueden suceder antes y durante la conexion.
   /* final Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message myMessage) {
            switch (myMessage.getData().getInt("message")) {
                case BTCommunicator.DISPLAY_TOAST:
                    showToast(myMessage.getData().getString("toastText"), Toast.LENGTH_SHORT);
                    break;
                case BTCommunicator.STATE_CONNECTED:
                    connected = true;
                    connectingProgressDialog.dismiss();
                    sendBTCmessage(BTCommunicator.NO_DELAY, BTCommunicator.GET_FIRMWARE_VERSION, 0, 0);
                    break;
                    
                case BTCommunicator.MOTOR_STATE:

                    if (myBTCommunicator != null) {
                        byte[] motorMessage = myBTCommunicator.getReturnMessage();
                        int position = byteToInt(motorMessage[21]) + (byteToInt(motorMessage[22]) << 8) + (byteToInt(motorMessage[23]) << 16)
                                       + (byteToInt(motorMessage[24]) << 24);
                        showToast(getResources().getString(R.string.current_position) + position, Toast.LENGTH_SHORT);
                    }

                    break;

                case BTCommunicator.STATE_CONNECTERROR_PAIRING:
                    connectingProgressDialog.dismiss();
                    destroyBTCommunicator();
                    break;

                case BTCommunicator.STATE_CONNECTERROR:
                    connectingProgressDialog.dismiss();
                case BTCommunicator.STATE_RECEIVEERROR:
                case BTCommunicator.STATE_SENDERROR:

                    destroyBTCommunicator();
                    if (btErrorPending == false) {
                        btErrorPending = true;
                        // inform the user of the error with an AlertDialog
                        AlertDialog.Builder builder = new AlertDialog.Builder(thisActivity);
                        builder.setTitle(getResources().getString(R.string.bt_error_dialog_title))
                        .setMessage(getResources().getString(R.string.bt_error_dialog_message)).setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                btErrorPending = false;
                                dialog.cancel();
                                //agregar re conexion
                                selectNXT();
                            }
                        });
                        builder.create().show();
                    }

                    break;

                	case BTCommunicator.FIRMWARE_VERSION:

                    if (myBTCommunicator != null) {
                        byte[] firmwareMessage = myBTCommunicator.getReturnMessage();
                        // check if we know the firmware
                        for (int pos=0; pos<4; pos++) {
                            if (firmwareMessage[pos + 3] != LCPMessage.FIRMWARE_VERSION_LEJOSMINDDROID[pos]) {
                                break;
                            }
                        }
                    }

                    break;
                
             
            }
        }
    };*/
    
    //task para realizar tareas paralelas
    private class Task extends AsyncTask<Void, Integer, Void>{
		
    	

		public Task(MainActivity mainActivity) {
			// TODO Auto-generated constructor stub
		}

		@Override
	    protected void onPreExecute() {
	    	super.onPreExecute();
	    }
	      
	    @Override
	    protected Void doInBackground(Void... params) {
	    	//ejecuta tarea cada 30ms
	    	int tiempo = 30;
	    	boolean flag= true;	
	    	while(flag){
	    		publishProgress(); 
	    		SystemClock.sleep(tiempo);
	    	}
	    	return null;
	    }
	  
	    @Override
	    protected void onProgressUpdate(Integer... values) {
	    	super.onProgressUpdate(values);
	    	//si no esta conectado desactiva botones de accion y setea textos de botones y texview
	    	if(connected==false){
				bncnt.setText(R.string.Conectar);
			}else if(connected==true){//si esta conectado activa botones y setea texto.
				bncnt.setText(R.string.Desconectar);
			}
		}	   
	}


	@Override
	public boolean isPairing() {
		// TODO Auto-generated method stub
		return false;
	}

    
}
