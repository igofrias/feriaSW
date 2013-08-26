package com.Phyrex.demo;

import com.Phyrex.demo.BTConnectable;
import com.Phyrex.demo.BTCommunicator;
import com.Phyrex.demo.DeviceListActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.app.ProgressDialog;
import android.view.View.OnClickListener;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

import java.io.IOException;
import java.util.Random;


public class MainActivity extends FragmentActivity implements BTConnectable{
	
	////////task
	Task task = new Task(this);
	//toast
	private Toast reusableToast;
	///////////////////Conexion blueblue////////////////////////////////////////
	private boolean pairing;
	private static boolean btOnByUs = false;
	private BTCommunicator myBTCommunicator = null;
	private Handler btcHandler;
	private Hilitos thread;
	private boolean connected = false;
	public static final int UPDATE_TIME = 200;
    public static final int ACTION_BUTTON_SHORT = 0;
    public static final int ACTION_BUTTON_LONG = 1;
    public static final int MENU_TOGGLE_CONNECT = Menu.FIRST;
    private static final int REQUEST_CONNECT_DEVICE = 1000;
    private static final int REQUEST_ENABLE_BT = 2000;
    //mensajes blue
    private ProgressDialog connectingProgressDialog;
    private Activity thisActivity;
    private boolean btErrorPending = false;
    private FragmentManager fragman;
    //////////////////////////////////
	
    //***********Botones y texview*************////
    TextView state;
    Button bncnt;
    Button action;
    Button action2;
    ///////////////////////
    
    /////*********Valores de motores*********//////
    private int motorAction;
    private int directionAction;
  
    ///////////////////////////////////
    
    
	 /**
     * Asks if bluetooth was switched on during the runtime of the app. For saving 
     * battery we switch it off when the app is terminated.
     * @return true, when bluetooth was switched on by the app
     */
    public static boolean isBtOnByUs() {
        return btOnByUs;
    }
	
    /**
     * Sets a flag when bluetooth was switched on during runtime
     * @param btOnByUs true, when bluetooth was switched on by the app
     */
    public static void setBtOnByUs(boolean btOnByUs) {
        MainActivity.btOnByUs = btOnByUs;
    }
    
    /**
     * Creates a new object for communication to the NXT robot via bluetooth and fetches the corresponding handler.
     */
    private void createBTCommunicator() {
        // interestingly BT adapter needs to be obtained by the UI thread - so we pass it in in the constructor
        myBTCommunicator = new BTCommunicator(this, myHandler, BluetoothAdapter.getDefaultAdapter(), getResources());
        btcHandler = myBTCommunicator.getHandler();
    }
    
    /**copiado
     * Creates and starts the a thread for communication via bluetooth to the NXT robot.
     * @param mac_address The MAC address of the NXT robot.
     */
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
    
    /**
     * Sends a message for disconnecting to the communication thread.
     */
    public void destroyBTCommunicator() {

        if (myBTCommunicator != null) {
            sendBTCmessage(BTCommunicator.NO_DELAY, BTCommunicator.DISCONNECT, 0, 0);
            myBTCommunicator = null;
        }

        connected = false;
        //updateButtonsAndMenu();
    }

    /**
     * Displays a message as a toast
     * @param textToShow the message
     * @param length the length of the toast to display
     */
    private void showToast(String textToShow, int length) {
        reusableToast.setText(textToShow);
        reusableToast.setDuration(length);
        reusableToast.show();
    }

    /**
     * Displays a message as a toast
     * @param resID the ressource ID to display
     * @param length the length of the toast to display
     */
    private void showToast(int resID, int length) {
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
        try {
            //mView.registerListener();
        }
        catch (IndexOutOfBoundsException ex) {
            showToast(R.string.sensor_initialization_failure, Toast.LENGTH_LONG);
            destroyBTCommunicator();
            finish();
        }
    }

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		motorAction = BTCommunicator.MOTOR_A;
	    directionAction = 1;
	    thisActivity = this;
		state = (TextView)findViewById(R.id.state);
		action = (Button)findViewById(R.id.action);
		action2 = (Button)findViewById(R.id.action2);
		bncnt = (Button)findViewById(R.id.bncnt);
		bncnt.setOnClickListener(listener);
		action.setOnClickListener(listener);
		action2.setOnClickListener(listener);
		state.setText("Desconectado");
		state.setTextColor(Color.parseColor("#FF0000"));
		task.execute();
		reusableToast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
		
		thread = new Hilitos(new Handler() {
			@Override
			public void handleMessage(Message m) {

			}
		});
		fragman = getSupportFragmentManager();
		thread.setRunning(true);
		thread.start();
		
		
		
		
		
	}

	OnClickListener listener = new OnClickListener(){
		  @Override
		  public void onClick(View v) {
			  switch(v.getId()){
				case R.id.bncnt:
					
					
					if(connected==false){
						////////////////////////////////
						/*Intent serverIntent = new Intent(this, DeviceListActivity.class);
				        startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
						Intent intent = new Intent(MainActivity.this, DialogActivity.class);
						startActivity(intent); */
				        ///////////////////////////////////
						 if (BluetoothAdapter.getDefaultAdapter()==null) {
					            showToast(R.string.bt_initialization_failure, Toast.LENGTH_LONG);
					            destroyBTCommunicator();
					            finish();
					            return;
					        }            
	
					        if (!BluetoothAdapter.getDefaultAdapter().isEnabled()) {
					            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
					            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
					            
					        } else {
					           selectNXT();
					           
					        }
					}else if(connected==true){
						destroyBTCommunicator();
					}
				        
				break;
				case R.id.action:
					boton1();
				break;
				case R.id.action2:
					boton2();    
				break;
			  }
		  }
	};
	
	@Override
    protected void onDestroy() {
        super.onDestroy();
        destroyBTCommunicator();
		task.cancel(true);
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

	/*@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}*/
	 /**
     * Sends the message via the BTCommuncator to the robot.
     * @param delay time to wait before sending the message.
     * @param message the message type (as defined in BTCommucator)
     * @param value1 first parameter
     * @param value2 second parameter
     */   
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

    /**
     * Sends the message via the BTCommuncator to the robot.
     * @param delay time to wait before sending the message.
     * @param message the message type (as defined in BTCommucator)
     * @param String a String parameter
     */       
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
    
    void selectNXT() {
        Intent serverIntent = new Intent(this, DeviceListActivity.class);
        startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
    }
    
    /**
     * Does something special depending on the robot-type.
     * @param buttonMode short, long or other press types.
     */
    private void boton1() {
        
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
         // MOTOR ACTION: forth an back
            
		            // other robots: 180 degrees forth and back
		    int direction =  1;                
		    sendBTCmessage(BTCommunicator.NO_DELAY, motorAction, 
		        75*direction*directionAction, 0);
		    sendBTCmessage(500, motorAction, 
		        -75*direction*directionAction, 0);
		    sendBTCmessage(1000, motorAction, 0, 0);


    }       
     
    private void boton2() {
            // G-F-E-D-C
            sendBTCmessage(BTCommunicator.NO_DELAY, 
                BTCommunicator.DO_BEEP, 392, 100);
            sendBTCmessage(200, BTCommunicator.DO_BEEP, 349, 100);
            sendBTCmessage(400, BTCommunicator.DO_BEEP, 330, 100);
            sendBTCmessage(600, BTCommunicator.DO_BEEP, 294, 100);
            sendBTCmessage(800, BTCommunicator.DO_BEEP, 262, 300);

        // MOTOR ACTION: forth an back
       
                // other robots: 180 degrees forth and back
        int direction =  -1;                
        sendBTCmessage(BTCommunicator.NO_DELAY, motorAction, 
            75*direction*directionAction, 0);
        sendBTCmessage(500, motorAction, 
            -75*direction*directionAction, 0);
        sendBTCmessage(1000, motorAction, 0, 0);


    }
	
    /**
     * Receive messages from the BTCommunicator
     */
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

                case BTCommunicator.STATE_CONNECTERROR_PAIRING:
                    connectingProgressDialog.dismiss();
                    destroyBTCommunicator();
                    break;
               
                case BTCommunicator.STATE_DISCONNECTING:
                	destroyBTCommunicator();
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
                                selectNXT();
                            }
                        });
                        builder.create().show();
                    }

                    break;
            }
        }
    };
    
    private int byteToInt(byte byteValue) {
        int intValue = (byteValue & (byte) 0x7f);

        if ((byteValue & (byte) 0x80) != 0)
            intValue |= 0x80;

        return intValue;
    }
    
    /**
     * @return true, when currently pairing 
     */
    @Override
    public boolean isPairing() {
        return pairing;
    }
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CONNECT_DEVICE:

                // When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {
                    // Get the device MAC address and start a new bt communicator thread
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
                        selectNXT();
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
	    	if(connected==false){
				state.setText("Desconectado");
				state.setTextColor(Color.parseColor("#FF0000"));
				action.setClickable(false);
				action2.setClickable(false);
				bncnt.setText("Conectar");
			}else if(connected==true){
				state.setText("Conectado");
				state.setTextColor(Color.parseColor("#00FF00"));
				action.setClickable(true);
				action2.setClickable(true);
				bncnt.setText("Desconectar");
			}
	    	
		}	   
	}
    //*
    ////////////runnable
    /*class ActualizaVista implements Runnable {
    	 
        public void run() {
            while (! Thread.currentThread().isInterrupted()) {
            	public void handleMessage(Message m) {

    			}
                MainActivity.this.myHandler.sendMessage(message);
 
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }*/
    public boolean isConnected()
    {
    	return connected;
    }
}
