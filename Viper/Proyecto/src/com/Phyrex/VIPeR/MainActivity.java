package com.Phyrex.VIPeR;

import com.Phyrex.VIPeR.BTConnectable;
import com.Phyrex.VIPeR.BTCommunicator;
import com.Phyrex.VIPeR.DeviceListActivity;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;

import android.support.v4.app.FragmentTransaction;
import android.text.Layout;

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
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;
import android.app.ProgressDialog;

import java.io.IOException;
import java.util.List;


public class MainActivity extends SherlockFragmentActivity implements BTConnectable{
	//Toast para mensajes
	private Toast reusableToast;
	///////////////////Variables Conexi�n Bluetooth////////////////////////////////////////
	private boolean pairing;
	private static boolean btOnByUs = false;
	private BTCommunicator myBTCommunicator = null;
	private Handler btcHandler;
	private boolean connected = false;
    private static final int REQUEST_CONNECT_DEVICE = 500;
    static final int REQUEST_ENABLE_BT = 2000;
    private ProgressDialog connectingProgressDialog;
    private Activity thisActivity;
    private boolean btErrorPending = false;
    private String programToStart;
	private ThreadClass thread;
	private String connectionType= null;
    String mac_nxt="";
    
    ///******** Variables del layout main activity //////////
    private Layout linear;
    private FrameLayout frame1;
    private FrameLayout frame2;
    
    
    ////////////////Menu/////////////
    public static final int MENU_TOGGLE_CONNECT = com.actionbarsherlock.view.Menu.FIRST;
    public static final int MENU_MAIN = com.actionbarsherlock.view.Menu.FIRST + 1;
    public static final int MENU_REMOTE_CONTROL = com.actionbarsherlock.view.Menu.FIRST + 2;
    public static final int MENU_PAIRING = com.actionbarsherlock.view.Menu.FIRST + 3;
    public static final int ACHIEVEMENTS = com.actionbarsherlock.view.Menu.FIRST + 4;
    public static final int MENU_QUIT = com.actionbarsherlock.view.Menu.FIRST + 5;
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
			ft.replace(R.id.frame2, fragment,"mainpet");
		}
		else{
			if(fragment.isDetached()){
				ft.attach(fragment);
			}
		}
		ft.commit();
		changeLayoutVisibility();
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
			ft.replace(R.id.frame2, fragment,"remotecontrol");
		}
		else{
			if(fragment.isDetached()){
				ft.attach(fragment);
			}
		}
		ft.commit();
		changeLayoutVisibility();
	}
    
    //mata el supa framento
    void detach_remotecontrol() {//identificamos y quitamos el fragmento control remoto 
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
		changeLayoutVisibility();
	}

    //llama al supa framento
    void launch_achievementlist() {//identificamos y cargamos el fragmento menu
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		SherlockFragment fragment = ((Achievement_Activity)getSupportFragmentManager().findFragmentByTag("achievementlist"));
		
		if(fragment==null){
			fragment = new Achievement_Activity();
			ft.replace(R.id.frame2, fragment,"achievementlist");
		}
		else{
			if(fragment.isDetached()){
				ft.attach(fragment);
			}
		}
		ft.commit();
		changeLayoutVisibility();
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
    
 //llama al supa framento
    
    void launch_statisticslist() {//identificamos y cargamos el fragmento menu
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		SherlockFragment fragment = ((Statistics_Activity)getSupportFragmentManager().findFragmentByTag("statisticslist"));
		
		if(fragment==null){
			fragment = new Statistics_Activity();
			ft.replace(R.id.linear, fragment,"statisticslist");
			
		}
		else{
			if(fragment.isDetached()){
				ft.attach(fragment);
			}
		}
		frame1 = (FrameLayout)this.findViewById(R.id.frame1);
		frame2 = (FrameLayout)this.findViewById(R.id.frame2);
		frame1.setVisibility(View.GONE);
		frame2.setVisibility(View.GONE);
		detachAll();
		ft.commit();
	}
    //llama al supa framento
    void detach_statisticslist() {//identificamos y cargamos el fragmento menu
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		SherlockFragment fragment = ((Statistics_Activity)getSupportFragmentManager().findFragmentByTag("statisticslist"));
		
		if(fragment!=null){
			if(!fragment.isDetached()){
				ft.detach(fragment);
			}
		}
		ft.commit();
	}
    
    private void changeLayoutVisibility(){
    	frame1 = (FrameLayout)this.findViewById(R.id.frame1);
		frame2 = (FrameLayout)this.findViewById(R.id.frame2);
		frame1.setVisibility(View.VISIBLE);
		frame2.setVisibility(View.VISIBLE);
		detach_statisticslist();
    }
    
    private void detachAll(){
    	detach_achievementlist();
    	detach_mainpet();
    	detach_remotecontrol();
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
        updateButtonsAndMenu();
    }
    
    ////Termina la conexion bluetooth (destruye el thread)//////////
    public void destroyBTCommunicator() {

        if (myBTCommunicator != null) {
            sendBTCmessage(BTCommunicator.NO_DELAY, BTCommunicator.DISCONNECT, 0, 0);
            myBTCommunicator = null;
        }

        updateButtonsAndMenu();
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
		if(!isConnected()){
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
		        
		}else if(isConnected()){
			//si esta conectado desconecta
			destroyBTCommunicator();
		}
	

	}
	
    
	@Override
    protected void onDestroy() {
        destroyBTCommunicator();
        if (btOnByUs){
            showToast(R.string.bt_off_message, Toast.LENGTH_SHORT);
            BluetoothAdapter.getDefaultAdapter().disable();
        }
        super.onDestroy();
         
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
    	if (myBTCommunicator != null){
    		connected = myBTCommunicator.isConnected();
    	}else{
    		connected =false;
    	}
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
                        //finish();
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
                    connectingProgressDialog.dismiss();
                    sendBTCmessage(BTCommunicator.NO_DELAY, BTCommunicator.GET_FIRMWARE_VERSION, 0, 0);
                	updateButtonsAndMenu();
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
        myMenu.add(0, MENU_MAIN, 2, "Mascota")/*.setIcon(R.drawable.ic_menu_start)*/;
        myMenu.add(0, MENU_REMOTE_CONTROL, 3, "Control Remoto")/*.setIcon(R.drawable.ic_menu_start)*/;
        myMenu.add(0, MENU_PAIRING, 4, "Parear")/*.setIcon(R.drawable.ic_menu_start)*/;
        myMenu.add(0, ACHIEVEMENTS, 5, "Logros")/*.setIcon(R.drawable.ic_menu_start)*/;
        myMenu.add(0, MENU_QUIT, 6, "Salir")/*.setIcon(R.drawable.ic_menu_exit)*/;
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
        	updateButtonsAndMenu();
            boolean startEnabled = false;
            if (myBTCommunicator != null) 
                startEnabled = myBTCommunicator.isConnected();
            if(menu.findItem(MENU_REMOTE_CONTROL)!=null)
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

                if (myBTCommunicator == null || !isConnected()) {
                	Database_Helper db = new Database_Helper(thisActivity);
					List<Pet> mascotas = db.getPets(); //lista de mascotas
					db.close();
					Pet petto = new Pet(mascotas.get(0).get_id(), mascotas.get(0).get_name(), mascotas.get(0).get_raza(), mascotas.get(0).get_color(), mascotas.get(0).get_birthdate(), mascotas.get(0).get_mac(), mascotas.get(0).get_death());
					connect(petto.get_mac());
                } else if(isConnected()){
                    destroyBTCommunicator();
                }

                return true;
                
            case MENU_REMOTE_CONTROL:
                	if (isConnected()){
    	        		launch_remotecontrol();
    	        	}else{
    	        		Toast.makeText(this, "Debe estar conectado para usar esta funci�n", Toast.LENGTH_SHORT).show();
    	        	}
                return true;
                
            case ACHIEVEMENTS:
            	launch_achievementlist(); 
               
        		return true;
        		
            case MENU_PAIRING:
                pairing();
                ///guardar nueva mac en BD
                return true;
            
            case MENU_MAIN:
            	launch_mainpet();
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
    void updateButtonsAndMenu() {
    	
        if (myMenu == null)
            return;

        myMenu.removeItem(MENU_TOGGLE_CONNECT);

        if (isConnected()) {
            myMenu.add(0, MENU_TOGGLE_CONNECT, 1, getResources().getString(R.string.disconnect))/*.setIcon(R.drawable.ic_menu_connected)*/;

        } else {
            myMenu.add(0, MENU_TOGGLE_CONNECT, 1, getResources().getString(R.string.connect))/*.setIcon(R.drawable.ic_menu_connect)*/;
        }
        
        SherlockFragment fragment1 = ((RemoteControl)getSupportFragmentManager().findFragmentByTag("remotecontrol"));
		myMenu.removeItem(MENU_REMOTE_CONTROL);
		if(fragment1==null || (fragment1!=null && fragment1.isDetached())){//si no estoy en el control remoto
			myMenu.add(0, MENU_REMOTE_CONTROL,3, "Control Remoto")/*.setIcon(R.drawable.ic_menu_connect)*/;
        }
		
		fragment1 = ((Achievement_Activity)getSupportFragmentManager().findFragmentByTag("achievementlist"));
		myMenu.removeItem(ACHIEVEMENTS);
		if(fragment1==null || (fragment1!=null && fragment1.isDetached())){
			myMenu.add(0, ACHIEVEMENTS, 5, "Logros")/*.setIcon(R.drawable.ic_menu_connect)*/;
	    }
		fragment1 = ((MainPetActivity)getSupportFragmentManager().findFragmentByTag("mainpet"));
		myMenu.removeItem(MENU_MAIN);
		if(fragment1==null || (fragment1!=null && fragment1.isDetached())){
			myMenu.add(0, MENU_MAIN, 2, "Mascota")/*.setIcon(R.drawable.ic_menu_connect)*/;
	    }
       
		 myMenu.removeItem(MENU_PAIRING);

	        if (!isConnected())
	            myMenu.add(0, MENU_PAIRING, 4, "Parear")/*.setIcon(R.drawable.ic_menu_connected)*/;
	        

    }
    
}