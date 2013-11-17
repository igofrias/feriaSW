package com.Phyrex.VIPeR;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.camera.hud.HUD;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.FillResolutionPolicy;
import org.andengine.entity.modifier.MoveYModifier;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.color.Color;

import com.Phyrex.VIPeR.BTService.BTBinder;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.widget.Toast;

public class BallGame extends SimpleBaseGameActivity{

	private Camera camera;
	private static final int CAMERA_WIDTH = 800;
	private static final int CAMERA_HEIGHT = 480;
	
	
	VertexBufferObjectManager vbo;
	
	Player player;
	Ball ball;
	BTService btservice;
	Activity thisActivity = this;
	protected boolean mBound;
	private Toast reusableToast;
	private boolean pairing;
	public BallGame()
	{
		mBound = false;
	}
	@Override
	protected void onStart() {
		if(!mBound)
		{
			bindService(new Intent(this,BTService.class),btconnection,Context.BIND_AUTO_CREATE);
		}
		super.onStart();
	}
	@Override
	protected void onResume() {
		if(!mBound)
		{
			bindService(new Intent(this,BTService.class),btconnection,Context.BIND_AUTO_CREATE);
		}
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
            boolean mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };
    @Override
    protected void onDestroy() {
        //btservice.destroyBTCommunicator();
        if (mBound) {
            unbindService(btconnection);
            mBound = false;
        }
        super.onDestroy();
        finish();
    }
    @Override
	public void onStop() {
//		if (mBound) {
//			unbindService(btconnection);
//			mBound = false;
//		}
		super.onStop(); 
//		finish(); //Queremos que el juego finalize cuando se retroceda.
	}
    @Override
    public void onPause()
    {
    	super.onPause();
    }
	@Override
	public EngineOptions onCreateEngineOptions() {
		// TODO Auto-generated method stub
		camera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
	    EngineOptions engineOptions = new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED, 
	    new FillResolutionPolicy(), camera);
	    return engineOptions;
	}

	@Override
	protected void onCreateResources() {
		// TODO Auto-generated method stub
		
	}
	protected void registerButtons()
	{
		//Inicializa botones del juego
		HUD  hud = new HUD();
		final Rectangle left_arrow = new Rectangle(20, 100, 60, 60, vbo)
	    {
	        public boolean onAreaTouched(TouchEvent touchEvent, float X, float Y)
	        {
	            if (touchEvent.isActionUp())
	            {
	            	Player player = BallGame.this.player;
	                player.sprite.setPosition(player.sprite.getX()-20, player.sprite.getY());
	            }
	            return true;
	        };
	    };
	    left_arrow.setColor(new Color(0.3333f, 0.3f, 0.3f));
	    final Rectangle right_arrow = new Rectangle(730, 100, 60, 60, vbo)
	    {
	        public boolean onAreaTouched(TouchEvent touchEvent, float X, float Y)
	        {
	            if (touchEvent.isActionUp())
	            {
	            	Player player = BallGame.this.player;
	                player.sprite.setPosition(player.sprite.getX()+20, player.sprite.getY());
	            }
	            return true;
	        };
	    };

	    hud.registerTouchArea(left_arrow);
	    hud.registerTouchArea(right_arrow);
	    hud.attachChild(left_arrow);
	    hud.attachChild(right_arrow);
	    
	    camera.setHUD(hud);
	}
	@Override
	protected Scene onCreateScene() {
		// TODO Auto-generated method stub
		Scene scene = new Scene();
		vbo = mEngine.getVertexBufferObjectManager();
		registerButtons();
	     scene.setBackground(new Background(0.09804f, 0.6274f, 0.8784f));
	     if(btservice != null)
	    	 if(btservice.isConnected())
	    	 {
	    		 btservice.startProgram("Eat.rxe");
	    	 }
	     player = new Player(BallGame.CAMERA_WIDTH/2,player.size_x);
	     ball = new Ball(BallGame.CAMERA_WIDTH/2,BallGame.CAMERA_HEIGHT, 10);
	     scene.attachChild(player.sprite);
	     scene.attachChild(ball.sprite);
	     ball.createFallUpdater();
	     return scene;
	}
	
	@Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case BTService.REQUEST_CONNECT_DEVICE:

                
                if (resultCode == Activity.RESULT_OK) {
                    String address = data.getExtras().getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
                    pairing = data.getExtras().getBoolean(DeviceListActivity.PAIRING);
                    btservice.startBTCommunicator(address);
                    
	 		        
                }
                
                break;
                
            case BTService.REQUEST_ENABLE_BT:

                // When the request to enable Bluetooth returns
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        btservice.setBtOnByUs(true);
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
    
    private class Ball
    {
    	Rectangle sprite;
    	
    	int size_x = 40;
    	int size_y = 40;
    	TimerHandler spriteTimerHandler;
    	float speed;
    	float x;
    	float y;
    	public Ball(int x, int y, float speed)
    	{
    		this.x = x;
    		this.y = y;
    		sprite = new Rectangle(x-size_x,y-size_y,size_x,size_y,BallGame.this.vbo);
    		this.speed = speed;
    	}
		
    	public void createFallUpdater()
    	{
    		float updateTime = 0.1f;
			spriteTimerHandler = new TimerHandler(updateTime, new ITimerCallback()
	        {                      
	            @Override
	            public void onTimePassed(final TimerHandler pTimerHandler)
	            {
	            	if(spriteTimerHandler != null)
	            	{
	            		spriteTimerHandler.reset();
	            		if(Ball.this.y >= 0)
	            		{
	            			Ball.this.y = Ball.this.y - speed;
	            		}
	            		else
	            		{
	            			Ball.this.y = BallGame.CAMERA_HEIGHT;
	            		}
	            		sprite.setPosition(x, y);
	            	}
	               
	            }
	        });
			
	        BallGame.this.getEngine().registerUpdateHandler(spriteTimerHandler );
    	}
    	
    	
    }
    private class Player
    {
    	Rectangle sprite;
    	
    	static final int size_x = 60;
    	static final int size_y = 60;
    	public Player(int x, int y)
    	{
    		sprite = new Rectangle(x-size_x,y-size_y,size_x,size_y,BallGame.this.vbo);
    		
    		
    	}
    }
    private class Ground 
    {
    	Rectangle ground = new Rectangle(0,0,BallGame.CAMERA_WIDTH,10,BallGame.this.vbo);
    	
    }
}
