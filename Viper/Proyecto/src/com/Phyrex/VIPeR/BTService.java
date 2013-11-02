package com.Phyrex.VIPeR;

import com.Phyrex.VIPeR.BTConnectable;
import com.Phyrex.VIPeR.BTCommunicator;
import com.Phyrex.VIPeR.DeviceListActivity;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;

import android.support.v4.app.FragmentTransaction;

import android.os.BatteryManager;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.util.Log;
import android.widget.Toast;
import android.app.ProgressDialog;

import java.io.IOException;
import java.util.List;


public class BTService extends Service implements BTConnectable{
    
	//Toast para mensajes
	private Toast reusableToast;
	///////////////////Variables Conexión Bluetooth////////////////////////////////////////
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
    
    /////*********Valores de motores*********//////
   /* private int motorActiona;
    private int motorActionb;
    private int motorActionc;
    private int directionAction;
  */
    ///////////////////////////////////
	public void setCurrentActivity(Activity main)
	{
		thisActivity = main;
		
	}
	public class BTBinder extends Binder {
         BTService getService() {
            // Return this instance of LocalService so clients can call public methods
            return BTService.this;
        }
    }
	public final BTBinder btbinder = new BTBinder();
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
    	//bncnt.setOnClickListener(listener);
    			reusableToast = Toast.makeText(thisActivity, "", Toast.LENGTH_SHORT);
    			thread = new ThreadClass(new Handler() {
    				@Override
    				public void handleMessage(Message m) {

    				}
    			});
    			thread.setRunning(true);
    			thread.start();
    			
    	
    	return startId;
        // TODO Auto-generated method stub
    	
    }

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return btbinder;
	}

	@Override
	public boolean isPairing() {
		// TODO Auto-generated method stub
		return false;
	}

    
	/*private void Reload() {
	db = new Database_Helper(this);
    db.open_read();
    Cursor c = db.getAll();		//llenando el cursor con datos 
    adapter =  new ArrayAdapter(this, 0, c);		//creando el adapter
    lista.setAdapter(adapter);		//entregandole el adapter a la lista
    db.close();
	} */
    //retorna true si la bd esta vacia
    private boolean checkBD(){
    	Database_Helper db = new Database_Helper(thisActivity);
		List<Pet> pets = db.getPets(); //lista de mascotas
		db.close();
		return pets.isEmpty();
    }
    
    /////Detecta si el Bluetooth esta activado////////////
    public static boolean isBtOnByUs() {
        return btOnByUs;
    }
	
    ///detecta si se activo el bluetooth////////
    public static void setBtOnByUs(boolean btOn) {
        btOnByUs = btOnByUs;
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
        connectingProgressDialog = ProgressDialog.show(thisActivity, "", getResources().getString(R.string.connecting_please_wait), true);

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
            //finish();
            return;
        }
    	connectionType = mac;
    	if (!BluetoothAdapter.getDefaultAdapter().isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            thisActivity.startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
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
		            //finish();
		            return;
		        }            
				//si existe blublu y no esta activado lo activa
				connectionType=null;
		        if (!BluetoothAdapter.getDefaultAdapter().isEnabled()) {
		            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
		            thisActivity.startActivityForResult(enableIntent, REQUEST_ENABLE_BT);		     
		        } else {//si esta activado busca dispositivos para conectarse
		           selectNXT();
		        }
		        
		}else if(connected==true){
			//si esta conectado desconecta
			destroyBTCommunicator();
		}
	

	}
    
	@Override
	public void onDestroy() {
        super.onDestroy();
        destroyBTCommunicator();
        if (btOnByUs){
            showToast(R.string.bt_off_message, Toast.LENGTH_SHORT);
            BluetoothAdapter.getDefaultAdapter().disable();
        }
         
    }

  /*  @Override
    public void onPause() {
        destroyBTCommunicator();
        super.onStop();
    }

    @Override
    public void onSaveInstanceState(Bundle icicle) {
        super.onSaveInstanceState(icicle);
    }*/
	 
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
        Intent serverIntent = new Intent(thisActivity, DeviceListActivity.class);
        thisActivity.startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
    }
    
    private int byteToInt(byte byteValue) {
        int intValue = (byteValue & (byte) 0x7f);

        if ((byteValue & (byte) 0x80) != 0)
            intValue |= 0x80;

        return intValue;
    }
    
    public boolean isConnected(){
    	return connected;
    }
    
   /* //recibe datos de dispositivo e inicia la conexion
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
    }*/
    
    
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
    public BTCommunicator getCommunicator()
    {
    	return myBTCommunicator;
    }
}