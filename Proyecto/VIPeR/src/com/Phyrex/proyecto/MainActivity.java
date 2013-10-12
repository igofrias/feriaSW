package com.Phyrex.VIPeR;

import com.Phyrex.VIPeR.BTConnectable;
import com.Phyrex.VIPeR.BTCommunicator;
import com.Phyrex.VIPeR.DeviceListActivity;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;

import android.support.v4.app.FragmentTransaction;

import android.os.BatteryManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.util.Log;
import android.widget.Toast;
import android.app.ProgressDialog;

import java.io.IOException;
import java.util.List;


public class MainActivity extends SherlockFragmentActivity implements BTConnectable{
	//Toast para mensajes
	private Toast reusableToast;
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
    private String programToStart;
	private ThreadClass thread;
	private String connectionType= null;
    String mac_nxt="";
    
    ////////////////Menu/////////////
    public static final int MENU_TOGGLE_CONNECT = com.actionbarsherlock.view.Menu.FIRST;
    public static final int MENU_REMOTE_CONTROL = com.actionbarsherlock.view.Menu.FIRST + 1;
    public static final int MENU_PAIRING = com.actionbarsherlock.view.Menu.FIRST + 2;
    public static final int ACHIEVEMENTS = com.actionbarsherlock.view.Menu.FIRST + 3;
    public static final int MENU_QUIT = com.actionbarsherlock.view.Menu.FIRST + 4;
    private com.actionbarsherlock.view.Menu myMenu;
    //////////////////////////////////
    
    /////*********Valores de motores*********//////
   /* private int motorActiona;
    private int motorActionb;
    private int motorActionc;
    private int directionAction;
  */
    ///////////////////////////////////
    
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
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		thisActivity = this;
		setContentView(R.layout.activity_main);
		
		//bncnt.setOnClickListener(listener);
		reusableToast = Toast.makeText(thisActivity, "", Toast.LENGTH_SHORT);
		thread = new ThreadClass(new Handler() {
			@Override
			public void handleMessage(Message m) {

			}
		});
		thread.setRunning(true);
		thread.start();
		
		if(checkBD()){
			launch_create();
		}else{
			launch_states();
			launch_mainpet();
		}
		
		
	}
    
    //llama al supa framento
    void launch_create() {//identificamos y cargamos el fragmento menu
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		SherlockFragment fragment = ((CreateActivity)getSupportFragmentManager().findFragmentByTag("create"));
		
		if(fragment==null){
			fragment = new CreateActivity();
			ft.add(R.id.linear, fragment,"create");
		}
		else{
			if(fragment.isDetached()){
				ft.attach(fragment);
			}
		}
		ft.commit();
	}
    //llama al supa framento
    void detach_create() {//identificamos y cargamos el fragmento menu
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		SherlockFragment fragment = ((CreateActivity)getSupportFragmentManager().findFragmentByTag("create"));
		
		if(fragment!=null){
			if(!fragment.isDetached()){
				ft.detach(fragment);
			}
		}
		ft.commit();
	}

    //llama al supa framento
    void launch_mainpet() {//identificamos y cargamos el fragmento menu
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		SherlockFragment fragment = ((MainPetActivity)getSupportFragmentManager().findFragmentByTag("mainpet"));
		
		if(fragment==null){
			fragment = new MainPetActivity();
			ft.add(R.id.frame2, fragment,"mainpet");
		}
		else{
			if(fragment.isDetached()){
				ft.attach(fragment);
			}
		}
		ft.commit();
	}
    
    //llama al supa framento
    void detach_mainpet() {//identificamos y cargamos el fragmento menu
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		SherlockFragment fragment = ((MainPetActivity)getSupportFragmentManager().findFragmentByTag("mainpet"));
		
		if(fragment!=null){
			if(!fragment.isDetached()){
				ft.detach(fragment);
			}
		}
		ft.commit();
	}
    
    //llama al supa framento
    void launch_remotecontrol() {//identificamos y cargamos el fragmento control remoto
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		SherlockFragment fragment = ((RemoteControl)getSupportFragmentManager().findFragmentByTag("remotecontrol"));
		
		if(fragment==null){
			fragment = new RemoteControl();
			ft.add(R.id.frame2, fragment,"remotecontrol");
		}
		else{
			if(fragment.isDetached()){
				ft.attach(fragment);
			}
		}
		ft.commit();
	}
    
    //mata el supa framento
    void detach_controlremoto() {//identificamos y quitamos el fragmento control remoto 
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		SherlockFragment fragment = ((RemoteControl)getSupportFragmentManager().findFragmentByTag("remotecontrol"));

		if(fragment!=null){
			if(!fragment.isDetached()){
				ft.detach(fragment);
			}
		}
		ft.commit();
	}
    
  //llama al supa framento
    void launch_states() {//identificamos y cargamos el fragmento de barrita de estado
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		SherlockFragment fragment = ((StatesActivity)getSupportFragmentManager().findFragmentByTag("state"));
		
		if(fragment==null){
			fragment = new StatesActivity();
			ft.add(R.id.frame1, fragment,"state");
		}
		else{
			if(fragment.isDetached()){
				ft.attach(fragment);
			}
		}
		ft.commit();
	}

    //llama al supa framento
    void launch_achievementlist() {//identificamos y cargamos el fragmento menu
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		SherlockFragment fragment = ((Achievement_Activity)getSupportFragmentManager().findFragmentByTag("achievementlist"));
		
		if(fragment==null){
			fragment = new Achievement_Activity();
			ft.add(R.id.frame2, fragment,"achievementlist");
		}
		else{
			if(fragment.isDetached()){
				ft.attach(fragment);
			}
		}
		ft.commit();
	}
    //llama al supa framento
    void detach_achievementlist() {//identificamos y cargamos el fragmento menu
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		SherlockFragment fragment = ((Achievement_Activity)getSupportFragmentManager().findFragmentByTag("achievementlist"));
		
		if(fragment!=null){
			if(!fragment.isDetached()){
				ft.detach(fragment);
			}
		}
		ft.commit();
	}

    //retorna true si la bd esta vacia
    private boolean checkBD(){
    	Database_Helper db = new Database_Helper(thisActivity);
		List<Pet> pets = db.getPets(); //lista de mascotas
		db.close();
		return pets.isEmpty();
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

    /////Detecta si el Bluetooth esta activado////////////
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
    //crea y arranca un thread para la conexion bluetooth/////////////
    //recibe la mac del robot/////////////
    public void startBTCommunicator(String mac_address) {
        mac_nxt= mac_address;
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
        connected=true;
    }
    
    ////Termina la conexion bluetooth (destruye el thread)//////////
    public void destroyBTCommunicator() {

        if (myBTCommunicator != null) {
            sendBTCmessage(BTCommunicator.NO_DELAY, BTCommunicator.DISCONNECT, 0, 0);
            myBTCommunicator = null;
        }

        connected = false;
        //updateButtonsAndMenu();
    }

   ///muestra el Toast///
    ////recive el mensaje y la duracion del Toast///////////
    private void showToast(String textToShow, int length) {
        reusableToast.setText(textToShow);
        reusableToast.setDuration(length);
        reusableToast.show();
    }

    ///muestra el Toast///
    ////recive el mensaje  (int - ID) y la duracion del Toast///////////
    private void showToast(int resID, int length) {
        reusableToast.setText(resID);
        reusableToast.setDuration(length);
        reusableToast.show();
    }
    
    public String return_mac(){
    		return mac_nxt;
    }
    
    public void selectTypeCnt(){
    	if(connectionType!=null){//si contiene una mac conecta
    		connect(connectionType);
    	}else{//si no parea
    		pairing();
    	}
    }
   
    public void connect(String mac){
    	if (BluetoothAdapter.getDefaultAdapter()==null) {
            showToast(R.string.bt_initialization_failure, Toast.LENGTH_LONG);
            destroyBTCommunicator();
            finish();
            return;
        }
    	connectionType = mac;
    	if (!BluetoothAdapter.getDefaultAdapter().isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        } else {//si esta activado busca dispositivos para conectarse
        	startBTCommunicator(mac);
        }
    	
    }
    
    //conexion intent a dispositivo
	public void pairing(){
    	//si no esta conectado verifica presencia de bluetooth 
		if(connected==false){
			///inicia el adaptador
			if (BluetoothAdapter.getDefaultAdapter()==null) {
		            showToast(R.string.bt_initialization_failure, Toast.LENGTH_LONG);
		            destroyBTCommunicator();
		            finish();
		            return;
		        }            
				//si existe blublu y no esta activado lo activa
				connectionType=null;
		        if (!BluetoothAdapter.getDefaultAdapter().isEnabled()) {
		            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
		            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);		     
		        } else {//si esta activado busca dispositivos para conectarse
		           selectNXT();
		        }
		        
		}else if(connected==true){
			//si esta conectado desconecta
			destroyBTCommunicator();
		}
	

	}
	
    
	@Override
    protected void onDestroy() {
        super.onDestroy();
        destroyBTCommunicator();
        if (btOnByUs){
            showToast(R.string.bt_off_message, Toast.LENGTH_SHORT);
            BluetoothAdapter.getDefaultAdapter().disable();
        }
         
    }

    @Override
    public void onPause() {
        destroyBTCommunicator();
        super.onStop();
    }

    @Override
    public void onSaveInstanceState(Bundle icicle) {
        super.onSaveInstanceState(icicle);
    }
	 
    /**
     * Starts a program on the NXT robot.
     * @param name The program name to start. Has to end with .rxe on the LEGO firmware and with .nxj on the 
     *             leJOS NXJ firmware.
     */   
    public void startProgram(String name) {
        // for .rxe programs: get program name, eventually stop this and start the new one delayed
        // is handled in startRXEprogram()
        if (name.endsWith(".rxe")) {
        	Log.d("pepe", "rxe");
            programToStart = name;        
            sendBTCmessage(BTCommunicator.NO_DELAY, BTCommunicator.GET_PROGRAM_NAME, 0, 0);
            Log.d("pepe", "startprogram fin D:");
            //return;
        }
              
       /// for .nxj programs: stop bluetooth communication after starting the program
        /*if (name.endsWith(".nxj")) {
            sendBTCmessage(BTCommunicator.NO_DELAY, BTCommunicator.START_PROGRAM, name);
            destroyBTCommunicator();
            return;
        } */      
        Log.d("pepe", "start!!!!!!!!!!!!!!!");
        // for all other programs: just start the program
        sendBTCmessage(BTCommunicator.NO_DELAY, BTCommunicator.START_PROGRAM, name);
    }
    
    /**
     * Depending on the status (whether the program runs already) we stop it, wait and restart it again.
     * @param status The current status, 0x00 means that the program is already running.
     */   
    public void startRXEprogram(byte status) {
        if (status == 0x00) {
        	 Log.d("pepe", "status 0x00");
            sendBTCmessage(BTCommunicator.NO_DELAY, BTCommunicator.STOP_PROGRAM, 0, 0);
            sendBTCmessage(1000, BTCommunicator.START_PROGRAM, programToStart);
        }    
        else {
        	 Log.d("pepe", "no status 0x00");
            sendBTCmessage(BTCommunicator.NO_DELAY, BTCommunicator.START_PROGRAM, programToStart);
        }
    }   
    
    ///envia al bthandler los mensajes via blublu   (enteros)
    void sendBTCmessage(int delay, int message, int value1, int value2) {
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
    }

  ///envia al bthandler los mensajes via blublu   (string)  
    void sendBTCmessage(int delay, int message, String name) {
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
    /*  private void boton1() {
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
   
    //ejecuta la accion del boton 1, toca musica y mueve el motor A
    private void boton2() {
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

    }
	
    ///ejecuta funciones para caminar
    private void caminar(int direccion) {
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
    
    private int byteToInt(byte byteValue) {
        int intValue = (byteValue & (byte) 0x7f);

        if ((byteValue & (byte) 0x80) != 0)
            intValue |= 0x80;

        return intValue;
    }
    
    //retorna si esta pareado 
    @Override
    public boolean isPairing() {
        return pairing;
    }
    
    public boolean isConnected(){
    	return connected;
    }
    
    //recibe datos de dispositivo e inicia la conexion
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CONNECT_DEVICE:

                
                if (resultCode == Activity.RESULT_OK) {
                    String address = data.getExtras().getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
                    pairing = data.getExtras().getBoolean(DeviceListActivity.PAIRING);
                    startBTCommunicator(address);
                    
                }
                
                break;
                
            case REQUEST_ENABLE_BT:

                // When the request to enable Bluetooth returns
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        btOnByUs = true;
                        selectTypeCnt();
                        break;
                    case Activity.RESULT_CANCELED:
                        showToast(R.string.bt_needs_to_be_enabled, Toast.LENGTH_SHORT);
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
    final Handler myHandler = new Handler() {
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
                    
                case BTCommunicator.PROGRAM_NAME:
                    if (myBTCommunicator != null) {
                        byte[] returnMessage = myBTCommunicator.getReturnMessage();
                        Log.d("pepe", "handler bsdgns");
                        startRXEprogram(returnMessage[2]);
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
                        .setMessage(getResources().getString(R.string.bt_error_dialog_message2)).setCancelable(false)
                        .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                btErrorPending = false;
                                dialog.cancel();
                                //agregar re conexion
                                //selectNXT();
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
    };
    
    /**
     * Creates the menu items
     */
    @Override
    public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {
        myMenu = menu;
        myMenu.add(0, MENU_TOGGLE_CONNECT, 1, "Conectar")/*.setIcon(R.drawable.ic_menu_connect)*/;
        myMenu.add(0, MENU_REMOTE_CONTROL, 2, "Control Remoto")/*.setIcon(R.drawable.ic_menu_start)*/;
        myMenu.add(0, MENU_PAIRING, 3, "Parear")/*.setIcon(R.drawable.ic_menu_start)*/;
        myMenu.add(0, ACHIEVEMENTS, 4, "Logros")/*.setIcon(R.drawable.ic_menu_start)*/;
        myMenu.add(0, MENU_QUIT, 5, "Salir")/*.setIcon(R.drawable.ic_menu_exit)*/;
        updateButtonsAndMenu();
        return true;
    }
    
    /**
     * Enables/disables the menu items
     */
    @Override
    public boolean onPrepareOptionsMenu(com.actionbarsherlock.view.Menu menu) {
        boolean displayMenu;
        displayMenu = super.onPrepareOptionsMenu(menu);
        if (displayMenu) {
            boolean startEnabled = false;
            if (myBTCommunicator != null) 
                startEnabled = myBTCommunicator.isConnected();
            menu.findItem(MENU_REMOTE_CONTROL).setEnabled(startEnabled);
        }
        return displayMenu;
    }
    
    /**
     * Handles item selections
     */
    @Override
    public boolean onOptionsItemSelected(com.actionbarsherlock.view.MenuItem item) {
        switch (item.getItemId()) {
            case MENU_TOGGLE_CONNECT:

                if (myBTCommunicator == null || connected == false) {
                	Database_Helper db = new Database_Helper(thisActivity);
					List<Pet> mascotas = db.getPets(); //lista de mascotas
					db.close();
					Pet petto = new Pet(mascotas.get(0).get_id(), mascotas.get(0).get_name(), mascotas.get(0).get_raza(), mascotas.get(0).get_color(), mascotas.get(0).get_birthdate(), mascotas.get(0).get_mac(), mascotas.get(0).get_death());
					connect(petto.get_mac());
                } else {
                    destroyBTCommunicator();
                    updateButtonsAndMenu();
                }

                return true;
                
            case MENU_REMOTE_CONTROL:
                
            	SherlockFragment fragment1 = ((RemoteControl)getSupportFragmentManager().findFragmentByTag("remotecontrol"));
        		SherlockFragment fragment2 = ((MainPetActivity)getSupportFragmentManager().findFragmentByTag("mainpet"));
        		if(fragment1!=null && !fragment1.isDetached()){//si estoy en el control remoto
        			detach_controlremoto();
    	    		launch_mainpet();
                } else if(fragment2!=null && !fragment2.isDetached()){//si estoy en el main
                	if (isConnected()){
    	        		detach_mainpet();
    	        		launch_remotecontrol();
    	        	}else{
    	        		Toast.makeText(this, "Debe estar conectado para usar esta función", Toast.LENGTH_SHORT).show();
    	        	}
                }
            
            	
            	
                return true;
                
            case ACHIEVEMENTS:
            	fragment1 = ((Achievement_Activity)getSupportFragmentManager().findFragmentByTag("achievementlist"));
        		fragment2 = ((MainPetActivity)getSupportFragmentManager().findFragmentByTag("mainpet"));
        		if(fragment1!=null && !fragment1.isDetached()){
        			detach_achievementlist();
    	    		launch_mainpet();
                } else if(fragment2!=null && !fragment2.isDetached()){//si estoy en el main
	        		detach_mainpet();
	        		launch_achievementlist();
                }  
               
        		return true;
        		
            case MENU_PAIRING:
                pairing();
                ///guardar nueva mac en BD
                return true;
                
            case MENU_QUIT:
                destroyBTCommunicator();
                finish();

                if (btOnByUs)
                    showToast(R.string.bt_off_message, Toast.LENGTH_SHORT);
                return true;
        }

        return false;
    }

    /**
     * Updates the menus and possible buttons when connection status changed.
     */
    private void updateButtonsAndMenu() {
    	SherlockFragment fragment1;
    	SherlockFragment fragment2;
    	
        if (myMenu == null)
            return;

        myMenu.removeItem(MENU_TOGGLE_CONNECT);

        if (connected) {
            myMenu.add(0, MENU_TOGGLE_CONNECT, 1, getResources().getString(R.string.disconnect))/*.setIcon(R.drawable.ic_menu_connected)*/;

        } else {
            myMenu.add(0, MENU_TOGGLE_CONNECT, 1, getResources().getString(R.string.connect))/*.setIcon(R.drawable.ic_menu_connect)*/;
        }
        
        myMenu.removeItem(MENU_REMOTE_CONTROL);
		fragment1 = ((RemoteControl)getSupportFragmentManager().findFragmentByTag("remotecontrol"));
		fragment2 = ((MainPetActivity)getSupportFragmentManager().findFragmentByTag("mainpet"));
		if(fragment1!=null && !fragment1.isDetached()){
            myMenu.add(0, MENU_REMOTE_CONTROL, 2, "Main")/*.setIcon(R.drawable.ic_menu_connected)*/;

        } else if(fragment2!=null && !fragment2.isDetached()){
            myMenu.add(0, MENU_REMOTE_CONTROL, 2, "Control Remoto")/*.setIcon(R.drawable.ic_menu_connect)*/;
            boolean startEnabled = false;
            if (myBTCommunicator != null) 
                startEnabled = myBTCommunicator.isConnected();
            myMenu.findItem(MENU_REMOTE_CONTROL).setEnabled(startEnabled);
        }
		
		myMenu.removeItem(ACHIEVEMENTS);
		fragment1 = ((Achievement_Activity)getSupportFragmentManager().findFragmentByTag("achievementlist"));
		fragment2 = ((MainPetActivity)getSupportFragmentManager().findFragmentByTag("mainpet"));
		if(fragment1!=null && !fragment1.isDetached()){
            myMenu.add(0, ACHIEVEMENTS, 4, "Main")/*.setIcon(R.drawable.ic_menu_connected)*/;

        } else if(fragment2!=null && !fragment2.isDetached()){
            myMenu.add(0, ACHIEVEMENTS, 4, "Logros")/*.setIcon(R.drawable.ic_menu_connect)*/;
        }
		
		 myMenu.removeItem(MENU_PAIRING);

	        if (!connected)
	            myMenu.add(0, MENU_PAIRING, 3, "Parear")/*.setIcon(R.drawable.ic_menu_connected)*/;
	        

    }
    
}
