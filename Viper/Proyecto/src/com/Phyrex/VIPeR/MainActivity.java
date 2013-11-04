package com.Phyrex.VIPeR;

import com.Phyrex.VIPeR.BTConnectable;
import com.Phyrex.VIPeR.BTCommunicator;
import com.Phyrex.VIPeR.BTService.BTBinder;
import com.Phyrex.VIPeR.DeviceListActivity;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;

import android.support.v4.app.FragmentTransaction;
import android.text.Layout;

import android.os.BatteryManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.content.ServiceConnection;
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
	///////////////////Variables Conexión Bluetooth////////////////////////////////////////
	private boolean pairing;
	private static boolean btOnByUs = false;
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
    private BTService btservice;
    private boolean mBound;
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
    	bindService(new Intent(thisActivity,BTService.class),btconnection,Context.BIND_AUTO_CREATE);
        super.onStart();
    }
    
    @Override
    public void onResume() {
        super.onResume();
    }
    
    private ServiceConnection btconnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            BTBinder binder = (BTBinder) service;
            btservice =  binder.getService();
            btservice.setCurrentActivity(thisActivity);
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };
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
    
    void changeLayoutVisibility(){
    	frame1 = (FrameLayout)this.findViewById(R.id.frame1);
		frame2 = (FrameLayout)this.findViewById(R.id.frame2);
		frame1.setVisibility(View.VISIBLE);
		frame2.setVisibility(View.VISIBLE);
		detach_statisticslist();
    }
    
    void detachAll(){
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
    
    
    
    
	@Override
    protected void onDestroy() {
        btservice.destroyBTCommunicator();
        if (mBound) {
            unbindService(btconnection);
            mBound = false;
        }
        if (btOnByUs){
            showToast(R.string.bt_off_message, Toast.LENGTH_SHORT);
            BluetoothAdapter.getDefaultAdapter().disable();
        }
        super.onDestroy();
        finish();
         
	}
	@Override
	public void onStop() {
		//btservice.destroyBTCommunicator();
//		if (mBound) {
//			unbindService(btconnection);
//			mBound = false;
//		}
		super.onStop(); 
	}
    @Override
    public void onPause() {
        //btservice.destroyBTCommunicator();
//        if (mBound) {
//            unbindService(btconnection);
//            mBound = false;
//        }
        super.onPause(); //WHY IS CALLING STOP ON PAUSE;
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
    
    
    /**
     * Depending on the status (whether the program runs already) we stop it, wait and restart it again.
     * @param status The current status, 0x00 means that the program is already running.
     */   
    private int byteToInt(byte byteValue) {
        int intValue = (byteValue & (byte) 0x7f);

        if ((byteValue & (byte) 0x80) != 0)
            intValue |= 0x80;

        return intValue;
    }
    
    
    //recibe datos de dispositivo e inicia la conexion
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CONNECT_DEVICE:

                
                if (resultCode == Activity.RESULT_OK) {
                    String address = data.getExtras().getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
                    pairing = data.getExtras().getBoolean(DeviceListActivity.PAIRING);
                    btservice.startBTCommunicator(address);
                    
	 		        
                }
                
                break;
                
            case REQUEST_ENABLE_BT:

                // When the request to enable Bluetooth returns
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        btOnByUs = true;
                        btservice.selectTypeCnt();
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
            if (btservice.getCommunicator() != null) 
                startEnabled = btservice.getCommunicator().isConnected();
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

                if (btservice.getCommunicator() == null || !btservice.isConnected()) {
                	Database_Helper db = new Database_Helper(thisActivity);
					List<Pet> mascotas = db.getPets(); //lista de mascotas
					db.close();
					Pet petto = new Pet(mascotas.get(0).get_id(), mascotas.get(0).get_name(), mascotas.get(0).get_raza(), mascotas.get(0).get_color(), mascotas.get(0).get_birthdate(), mascotas.get(0).get_mac(), mascotas.get(0).get_death());
					btservice.connect(petto.get_mac());
                } else if(btservice.isConnected()){
                    btservice.destroyBTCommunicator();
                }

                return true;
                
            case MENU_REMOTE_CONTROL:
                	if (btservice.isConnected()){
                		detachAll();
    	        		launch_remotecontrol();
    	        	}else{
    	        		Toast.makeText(this, "Debe estar conectado para usar esta función", Toast.LENGTH_SHORT).show();
    	        	}
                return true;
                
            case ACHIEVEMENTS:
            	detachAll();
            	launch_achievementlist(); 
               
        		return true;
        		
            case MENU_PAIRING:
                btservice.pairing();
                ///guardar nueva mac en BD
                return true;
            
            case MENU_MAIN:
            	detachAll();
            	launch_mainpet();
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

        if (btservice.isConnected()) {
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

	        if (!btservice.isConnected())
	            myMenu.add(0, MENU_PAIRING, 4, "Parear")/*.setIcon(R.drawable.ic_menu_connected)*/;
	        

    }

	@Override
	public boolean isPairing() {
		// TODO Auto-generated method stub
		return false;
	}
	
	public BTService getCurrentBTService()
	{
		return btservice;
	}
	
	//Funciones para que clases viejas no se quejen por el cambio a service
	public void pairing() {
		// TODO Auto-generated method stub
		btservice.pairing();
	}

	public boolean isConnected() {
		// TODO Auto-generated method stub
		if(btservice != null)
		{
			return btservice.isConnected();
		}
		else return false;
	}

	public void sendBTCmessage(int delay, int message, int value1,
			int value2) {
		// TODO Auto-generated method stub
		btservice.sendBTCmessage(delay, message, value1, value2);
	}
	public String return_mac(){
		return btservice.return_mac();
}
	public void startProgram(String name) {
	       btservice.startProgram(name);
	    }
}
