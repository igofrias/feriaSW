package com.Phyrex.VIPeR;

import com.Phyrex.VIPeR.BTConnectable;
import com.Phyrex.VIPeR.BTCommunicator;
import com.Phyrex.VIPeR.DeviceListActivity;

import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.util.Log;
import android.widget.Toast;
import android.app.ProgressDialog;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;


public class BTService extends Service implements BTConnectable{
    
	//Toast para mensajes
	String mac_slave = "00:16:53:17:67:EA";
	private Toast reusableToast;
	///////////////////Variables Conexión Bluetooth////////////////////////////////////////
	private static boolean btOnByUs = false;
	private BTCommunicator myBTCommunicator = null;
	private BTCommunicator slaveBTCommunicator = null;
	Handler btcHandler;
	Handler slaveBtcHandler;
	private boolean connected = false;
    static final int REQUEST_CONNECT_DEVICE = 500;
    static final int REQUEST_ENABLE_BT = 2000;
    static final int REQUEST_ENABLE_BT_2BRICK = 2001;
    private ProgressDialog connectingProgressDialog;
    private Activity thisActivity = null;
    private boolean btErrorPending = false;
    private String programToStart;
	private ThreadClass thread;
	private String connectionType= null;
	private int lastColor;
    String mac_nxt="";
    HashMap<String, Integer> messageMap;
    
    /////*********Valores de motores*********//////
   /* private int motorActiona;
    private int motorActionb;
    private int motorActionc;
    private int directionAction;
  */
    ///////////////////////////////////
    public BTService()
    {
    	createMessageMap();
    }
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
        slaveBTCommunicator = new BTCommunicator(this, slaveHandler, BluetoothAdapter.getDefaultAdapter(), getResources());
        btcHandler = myBTCommunicator.getHandler();
        slaveBtcHandler = slaveBTCommunicator.getHandler();
        myHandler = new btHandler(btcHandler);
        slaveHandler = new btHandler(slaveBtcHandler);
    }
    public void setMac(String mac, int brick)
    {
    	//setea el macde la cabeza o del lomo dependiendo de que numero de brick
    	//se le de
    	switch(brick)
    	{
    	case 0:
    		mac_nxt = mac;
    		break;
    	case 1:
    		mac_slave = mac;
    		break;
    	default:
    		break;
    	}
    	Log.d("BTService", "pairing: MAC="+mac+"Brick="+brick);
    }
    //crea y arranca un thread para la conexion bluetooth/////////////
    //recibe la mac del robot/////////////
    //A partir de ahora la mac la setea por otro lado (setMac())
    //Potencialmente se podria quitar el mac_address pero no lo voy a hacer por flojera
    public void startBTCommunicator(String mac_address) {
        //mac_nxt= mac_address;
        connectingProgressDialog = ProgressDialog.show(thisActivity, "", getResources().getString(R.string.connecting_please_wait), true);

        if (myBTCommunicator != null) {
            try {
                myBTCommunicator.destroyNXTconnection();
            }
            catch (IOException e) { }
        }
        if (slaveBTCommunicator != null) {
            try {
                slaveBTCommunicator.destroyNXTconnection();
            }
            catch (IOException e) { }
        }
        createBTCommunicator();
        myBTCommunicator.setMACAddress(mac_address);
        myBTCommunicator.start();
        slaveBTCommunicator.setMACAddress(mac_slave);
        slaveBTCommunicator.start();
        connected=true;
//        startProgram("MasterMain.rxe",0);
//        startProgram("SlaveMain.rxe",1);
    }
    
    ////Termina la conexion bluetooth (destruye el thread)//////////
    public void destroyBTCommunicator() {
    	//Para los programas antes de salir
    	
        if (myBTCommunicator != null) {
        	sendBTCmessage(BTCommunicator.NO_DELAY,BTCommunicator.STOP_PROGRAM,"MasterMain.rxe",btcHandler);
            sendBTCmessage(BTCommunicator.NO_DELAY, BTCommunicator.DISCONNECT, 0, 0,myHandler,btcHandler);
            myBTCommunicator = null;
        }
        if(slaveBTCommunicator != null)
        {
        	sendBTCmessage(BTCommunicator.NO_DELAY,BTCommunicator.STOP_PROGRAM,"SlaveMain.rxe",slaveBtcHandler);
        	sendBTCmessage(BTCommunicator.NO_DELAY, BTCommunicator.DISCONNECT, 0, 0,slaveHandler,slaveBtcHandler);
            slaveBTCommunicator = null;
        }
        connected = false;
        //updateButtonsAndMenu();
    }
   public void destroyBTCommunicator(int brick)
   {
	   Handler btchandler = null;
	   Handler myhandler = null;
	   switch(brick)
	   {
	   case 1:
		   btchandler = slaveBtcHandler;
		   myhandler = slaveHandler;
		   break;
	   default:
		   btchandler = btcHandler;
		   myhandler = myHandler;
		   break;
	   }
	   if (myBTCommunicator != null) {
           sendBTCmessage(BTCommunicator.NO_DELAY, BTCommunicator.DISCONNECT, 0, 0,myhandler,btchandler);
           myBTCommunicator = null;
       }
       
       connected = false;
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
    
    public void selectTypeCnt(int bricknumber){
    	if(connectionType!=null){//si contiene una mac conecta
    		connect(connectionType);
    	}else{//si no parea
    		pairing(bricknumber);
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
	public void pairing(int bricknumber){
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
		            if(bricknumber == 2)
		            	thisActivity.startActivityForResult(enableIntent, REQUEST_ENABLE_BT_2BRICK);
		            else
		            	thisActivity.startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
		        } else {//si esta activado busca dispositivos para conectarse al brick
		        	//dado por bricknumber
		           selectNXT(bricknumber);
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
//        if (name.endsWith(".rxe")) {
//        	Log.d("pepe", "rxe");
//            programToStart = name;        
//            sendBTCmessage(BTCommunicator.NO_DELAY, BTCommunicator.GET_PROGRAM_NAME, 0, 0);
//            Log.d("pepe", "startprogram fin D:");
//            //return;
//        }
//              
       /// for .nxj programs: stop bluetooth communication after starting the program
        /*if (name.endsWith(".nxj")) {
            sendBTCmessage(BTCommunicator.NO_DELAY, BTCommunicator.START_PROGRAM, name);
            destroyBTCommunicator();
            return;
        } */      
        Log.d("pepe", "start!!!!!!!!!!!!!!!");
        // for all other programs: just start the program
        sendBTCmessage(BTCommunicator.NO_DELAY, BTCommunicator.START_PROGRAM, name, btcHandler);
    }
    public void startProgram(String name, int brick)
    {
    	//Como el anterior pero pide explicitamente a que brick enviar
    	switch(brick)
    	{
    	case 0:
    		sendBTCmessage(BTCommunicator.NO_DELAY, BTCommunicator.START_PROGRAM, name, btcHandler);
    		break;
    	case 1:
    		sendBTCmessage(BTCommunicator.NO_DELAY, BTCommunicator.START_PROGRAM, name, slaveBtcHandler);
    		break;
    	}
    }
    /**
     * Depending on the status (whether the program runs already) we stop it, wait and restart it again.
     * @param status The current status, 0x00 means that the program is already running.
     */   
    public void startRXEprogram(byte status) {
        if (status == 0x00) {
        	 Log.d("pepe", "status 0x00");
            sendBTCmessage(BTCommunicator.NO_DELAY, BTCommunicator.STOP_PROGRAM, 0, 0,myHandler,btcHandler);
            sendBTCmessage(1000, BTCommunicator.START_PROGRAM, programToStart,btcHandler);
        }    
        else {
        	 Log.d("pepe", "no status 0x00");
            sendBTCmessage(BTCommunicator.NO_DELAY, BTCommunicator.START_PROGRAM, programToStart,btcHandler);
        }
    }   
    public void startRXEprogram(byte status,int brick) {
    	//Mismo que el anterior pero selecciona a quien enviar explicitamente
    	Handler btchandler = null;
    	Handler myhandler = null;
    	switch(brick)
    	{
    	case 1:
    		btchandler = slaveBtcHandler;
    		myhandler = slaveHandler;
    		break;
    	default:
    		btchandler = btcHandler;
    		myhandler = myHandler;
    		break;
    	}
        if (status == 0x00) {
        	 Log.d("pepe", "status 0x00");
            sendBTCmessage(BTCommunicator.NO_DELAY, BTCommunicator.STOP_PROGRAM, 0, 0,myhandler,btchandler);
            sendBTCmessage(1000, BTCommunicator.START_PROGRAM, programToStart,btchandler);
        }    
        else {
        	 Log.d("pepe", "no status 0x00");
            sendBTCmessage(BTCommunicator.NO_DELAY, BTCommunicator.START_PROGRAM, programToStart,btchandler);
        }
    }   
    public void createMessageMap()
    {
    	//Crea el mapeo de mensajes a enteros
    	messageMap = new HashMap<String,Integer>(50); 
    	messageMap.put("OpenEyes", 1);
    	messageMap.put("AngryEyes", 2);
    	messageMap.put("BoredEyes",3);
    	messageMap.put("CloseEyes", 4);
    	messageMap.put("HappyEyes", 5);
    	messageMap.put("ShameEyes", 6);
    	messageMap.put("SadEyes", 7);
    	messageMap.put("CryEyes", 8);
    	messageMap.put("DeadEyes", 30);
    	messageMap.put("StopSensors", 31);
    	messageMap.put("StartSensors", 32);
    	messageMap.put("OpenClamps", 41);
    	messageMap.put("CloseClamps", 42);
    	messageMap.put("CatchBall", 43);
    	messageMap.put("ReleaseBall", 44);
    	messageMap.put("StartMoveHead", 45);
    	messageMap.put("MoveTail", 46);
    	messageMap.put("Shake", 47);
    	messageMap.put("StopMoveHead", 48);
    	messageMap.put("YawnSound", 61);
    	messageMap.put("SleepSound", 62);
    	messageMap.put("EatSound", 63);
    	messageMap.put("FartSound", 64);
    	messageMap.put("Shutdown", 100);
    	
    }
    public void sendPetMessage(int brick, String message)
    {
    	//Envia un mensaje al brick apropiado
    	Handler btchandler = null;
    	Handler myhandler = null;
    	switch(brick)
    	{
    	case 1:
    		btchandler = slaveBtcHandler;
    		myhandler = slaveHandler;
    		break;
    	default:
    		btchandler = btcHandler;
    		myhandler = myHandler;
    		break;
    	}
    	int inbox = 0;
    	int messageNumber = 0;
    	messageNumber = (Integer) messageMap.get(message);
    	
    	sendBTCmessage(BTCommunicator.NO_DELAY,BTCommunicator.SEND_PET_MESSAGE,inbox,messageNumber,myhandler,btchandler);
    }
    ///envia al bthandler los mensajes via blublu   (enteros)
    void sendBTCmessage(int delay, int message, int value1, int value2,Handler myhandler, Handler btchandler) {
        Bundle myBundle = new Bundle();
        myBundle.putInt("message", message);
        myBundle.putInt("value1", value1);
        myBundle.putInt("value2", value2);
        Message myMessage = myhandler.obtainMessage();
        myMessage.setData(myBundle);

        if (delay == 0)
            btchandler.sendMessage(myMessage);

        else
            btchandler.sendMessageDelayed(myMessage, delay);
    }
    
    //inbox = inbox al cul se manda mensaje
    //message= mensaje numerico 
    void sendNumberMessage(int inbox, int message) throws IOException { 
    	
    	byte[] msg = new byte[6];
    	
    	msg[0] = (byte)0x00; 		//0x00 0x80
    	msg[1] = (byte)0x09; 		//0x09 
    	msg[2] = (byte) inbox;      //inbox number (0-9)
    	msg[3] = (byte) 5;			//messageSize
    	msg[4] = (byte) message;	//MENSAJE  NUMERICO!!
    	msg[5] = '\0'; 				//null, end of message
    	myBTCommunicator.sendMessageAndState(msg); //funcion esta en BTCommunicator
    	Log.d("Mensaje Enviado",Integer.toString(message));
}
   

  ///envia al bthandler los mensajes via blublu   (string)  
    void sendBTCmessage(int delay, int message, String name,Handler btchandler) {
        Bundle myBundle = new Bundle();
        myBundle.putInt("message", message);
        myBundle.putString("name", name);
        Message myMessage = myHandler.obtainMessage();
        myMessage.setData(myBundle);

        if (delay == 0)
            btchandler.sendMessage(myMessage);
        else
            btchandler.sendMessageDelayed(myMessage, delay);
    }
    //llama a la actividad que  busca dispositivos
    void selectNXT(int bricknumber) {
    	//Parea al brick dado por bricknumber (0 para cabeza, 1 para lomo)
        Intent serverIntent = new Intent(thisActivity, DeviceListActivity.class);
        serverIntent.putExtra("Brick", bricknumber);
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
    private class btHandler extends Handler
    {
    	private Handler btchandler;
    	public btHandler(Handler btchandler)
    	{	
    		this.btchandler = btchandler;
    	}
    	 @Override
         public void handleMessage(Message myMessage) {
         	int messageType = myMessage.getData().getInt("message");
             switch (messageType) {
                 case BTCommunicator.DISPLAY_TOAST:
                     //showToast(myMessage.getData().getString("toastText"), Toast.LENGTH_SHORT);
                     break;
                 case BTCommunicator.STATE_CONNECTED:
                	 connected = true;
            		 if(connectingProgressDialog.isShowing())
            		 {
            			 connectingProgressDialog.dismiss();
            		 }
                	 if(btchandler != null)
                	 {
                         sendBTCmessage(BTCommunicator.NO_DELAY, BTCommunicator.GET_FIRMWARE_VERSION, 0, 0,(Handler)this,btchandler);
                	 }
                     
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
                     if (btErrorPending == false && thisActivity != null) {
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
                         if(thisActivity != null)
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
                 	case BTCommunicator.RECEIVE_INT_MESSAGE:
                 		byte[] message = myBTCommunicator.getReturnMessage();
//                 		Toast.makeText(thisActivity, String.valueOf(message[4]), 
//     		 					Toast.LENGTH_SHORT).show();
                 		if(thisActivity instanceof MainActivity)
                 		{
                 				RemoteControl control = ((MainActivity)thisActivity).getRemoteControl();
                 				if(control != null)
                 				{
                 					try {
                 						control.canvas.validcatchball(message[4]);
                 					} catch (IOException e) {
                 						// TODO Auto-generated catch block
                 						e.printStackTrace();
                 					}
                 				}
                 		}
                 		//TODO Hay que despachar mensaje a MainActivity->ControlRemoto
                     break;
                 
              
             }
         }
    }
    Handler myHandler = new btHandler(btcHandler);
    Handler slaveHandler = new btHandler(slaveBtcHandler);
    public BTCommunicator getCommunicator()
    {
    	return myBTCommunicator;
    }
    
    public int reciveBTmessage(){
    	byte[] recibir = new byte[20];
    	String msgRecibido;
    	try{
     	recibir = myBTCommunicator.receiveMessage(); //ver que la funcion pida retorno
    	}catch(IOException e){
    		if(connected){
    			myBTCommunicator.sendState(BTCommunicator.STATE_RECEIVEERROR);
    		}
    		return -1;
    	}
     	msgRecibido = recibir.toString();
    	if(!msgRecibido.isEmpty()){ // && (byte)recibir[0]==0x02
    		Log.e("Mensare Recibido", String.valueOf(byteToInt(recibir[4])));
        	return byteToInt(recibir[4]);	
    	}
    	return 0;
    }
}