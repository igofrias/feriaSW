package com.Phyrex.VIPeR;

import java.util.Random;

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
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
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
	
	private Font font;
	private Font smallfont;
	HUD hud;
	private Text hudText;
	Control control;
	Scene scene;
	
	VertexBufferObjectManager vbo;
	
	Player player;
	Ball ball;
	BitmapTextureAtlas ballTexture;
	ITextureRegion ballTextureRegion;
	BitmapTextureAtlas playerTexture;
	ITextureRegion playerTextureRegion;
	
	BTService btservice;
	Activity thisActivity = this;
	int puntaje;
	int dificultad;
	int vidas;
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

	    FontFactory.setAssetBasePath("font/");
	    final ITexture fontTexture = new BitmapTextureAtlas(this.getTextureManager(), 256, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);

	    font = FontFactory.createFromAsset(getFontManager(), fontTexture, getAssets(), "font.ttf", 40.0f, true, Color.BLACK.getABGRPackedInt());
	    smallfont = FontFactory.createFromAsset(getFontManager(), fontTexture, getAssets(), "font.ttf", 20.0f, true, Color.BLACK.getABGRPackedInt());
	    font.load();
	    smallfont.load();
	    
	    BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
	    ballTexture = new BitmapTextureAtlas(getTextureManager(), 256, 256, TextureOptions.DEFAULT);
	    ballTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(ballTexture, this, "ball.png", 0, 0);
	    ballTexture.load(); 
	    playerTexture = new BitmapTextureAtlas(getTextureManager(), 256, 256, TextureOptions.DEFAULT);
	    playerTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(playerTexture, this, "tento.png", 0, 0);
	    playerTexture.load();  
	}
	protected void registerButtons()
	{
		//Inicializa botones del juego
		hud = new HUD();
		String hudStr = String.format("Puntaje:%d Vidas:%d",BallGame.this.puntaje, BallGame.this.vidas);
		hudText = new Text(70, 40, font, hudStr,100,BallGame.this.vbo);
		control = new Control();
	    hud.registerTouchArea(control.left_arrow);
	    hud.registerTouchArea(control.right_arrow);
	    hud.attachChild(control.left_arrow);
	    hud.attachChild(control.right_arrow);
	    hud.registerUpdateHandler(control);
	    hud.attachChild(hudText);
	    hud.setTouchAreaBindingOnActionDownEnabled(true);
	    //hud.setTouchAreaBindingOnActionMoveEnabled(true);
	    camera.setHUD(hud);
	}
	public void updateScore()
	{
		//Agrega 1 al puntaje actual
		puntaje += 1;
		if(puntaje % 2 == 0)
		{
			dificultad +=1; 
			updateSpeed();
		}
		hudText.setText("Puntaje:"+puntaje+" Vidas:" + vidas);
	}
	public void updateSpeed()
	{
		//Cambia la velocidad de caida de la pelota de acuerdo a la dificultad
		float min = 5;
		float max = 100;
		float steps = (float) (1.0/50.0); //Cantidad de "niveles" en los que sube la velocidad hasta llegar a max
		ball.speed = (float) (min + (max-min)*steps*dificultad);
	}
	public void removeLive()
	{
		//Quita una vida al tentosaurio. Si no quedan vidas sale del juego TODO
		if(vidas > 1)
		{
			vidas -=1;
			hudText.setText("Puntaje:"+puntaje+" Vidas:" + vidas);
		}
		else
		{
			//Aqui se mata
			hud.detachChild(hudText);
			hudText = new Text(70, 40, smallfont, "Game Over. Presione el boton atrás para volver",100,BallGame.this.vbo);
			hud.attachChild(hudText);
			hud.detachChild(control.left_arrow);
			hud.detachChild(control.right_arrow);
			this.getEngine().unregisterUpdateHandler(ball.spriteTimerHandler);
			ball.detach();
			player.detach();
		}
	}
	@Override
	protected Scene onCreateScene() {
		// TODO Auto-generated method stub
		scene = new Scene();
		puntaje = 0;
		vidas = 3;
		vbo = mEngine.getVertexBufferObjectManager();
		registerButtons();
		scene.setTouchAreaBindingOnActionDownEnabled(true);
	     scene.setBackground(new Background(0.09804f, 0.6274f, 0.8784f));
	     if(btservice != null)
	    	 if(btservice.isConnected())
	    	 {
	    		 btservice.startProgram("Eat.rxe");
	    	 }
	     player = new Player(BallGame.CAMERA_WIDTH/2,BallGame.CAMERA_HEIGHT-player.size_x);
	     ball = new Ball(BallGame.CAMERA_WIDTH/2,0);
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
    	Sprite sprite;
    	
    	static final int size_x = 40;
    	static final int size_y = 40;
    	TimerHandler spriteTimerHandler;
    	float speed;
    	float x;
    	float y;
    	public Ball(int x, int y)
    	{
    		this.x = x;
    		this.y = y;
    		sprite = new Sprite(x-size_x,y-size_y,size_x,size_y,ballTextureRegion,BallGame.this.vbo)
    		{
    		     @Override
    		     protected void onManagedUpdate(float pSecondsElapsed)
    		     {
    		         if (player.sprite.collidesWith(this))
    		         {                                                              
    		             //Aumenta puntaje si es que choca con el jugador. Ademas vuelve a poner a la pelota arriba
    		        	 //en una posicion x arbitraria
    		        	 BallGame.this.updateScore();
    		        	 Ball.this.y = 0;
    		        	 
    		        	 Ball.this.x = 2*size_x +(float) (Math.random()*(BallGame.CAMERA_WIDTH-4*size_x));//Margen de 2*size_x
    		        	 Ball.this.sprite.setPosition(Ball.this.x, Ball.this.y);
    		         }
    		     };
    		};;
    		this.speed = 5;
    	}
		
    	public void createFallUpdater()
    	{
    		float updateTime = 0.0333f;
			spriteTimerHandler = new TimerHandler(updateTime, new ITimerCallback()
	        {                      
	            @Override
	            public void onTimePassed(final TimerHandler pTimerHandler)
	            {
	            	if(spriteTimerHandler != null)
	            	{
	            		spriteTimerHandler.reset();
	            		if(Ball.this.y <= BallGame.CAMERA_HEIGHT)
	            		{
	            			Ball.this.y = Ball.this.y + speed;
	            		}
	            		else
	            		{
	            			Ball.this.x = 2*size_x +(float) (Math.random()*(BallGame.CAMERA_WIDTH-4*size_x));
	            			Ball.this.y = 0;
	            			BallGame.this.removeLive();
	            		}
	            		sprite.setPosition(x, y);
	            	}
	               
	            }
	        });
			
	        BallGame.this.getEngine().registerUpdateHandler(spriteTimerHandler );
    	}
    	public void detach()
    	{
    		BallGame.this.scene.detachChild(sprite);
    	}
    	
    }
    private class Player
    {
    	Sprite sprite;
    	
    	static final int size_x = 60;
    	static final int size_y = 80;
    	static final int speed = 10;
    	public Player(int x, int y)
    	{
    		//sprite = new Rectangle(x-size_x,y-size_y,size_x,size_y,BallGame.this.vbo);
    		sprite = new Sprite(x-size_x,y-size_y,size_x,size_y, playerTextureRegion, vbo);
    		
    	}
    	public void detach()
    	{
    		BallGame.this.scene.detachChild(sprite);
    	}
    }
    class DirButton extends Rectangle
    {
    	//Boton que controla una sola direccion
    	btnListener downListener = null;

		public DirButton(float pX, float pY, float pWidth, float pHeight,
				VertexBufferObjectManager pVertexBufferObjectManager,btnListener parentDown) {
			super(pX, pY, pWidth, pHeight, pVertexBufferObjectManager);
			downListener = parentDown;
			// TODO Auto-generated constructor stub
		}
		public boolean onAreaTouched(TouchEvent touchEvent, float X, float Y)
        {
        	
            if (touchEvent.isActionDown())
            {
            	downListener.isOn = true;
            	return true;
            }
            else if (touchEvent.isActionUp())
    		{
    			downListener.isOn = false;
    			return true;
    		}
            else if (touchEvent.isActionCancel())
            {
            	downListener.isOn = false;
    			return true;
            }
            return false;
        };
      
    	
    }
    class btnListener
    {
    	boolean isOn;
    	public btnListener()
    	{
    		isOn = false;
    	}
    }
    class Control implements IUpdateHandler
    {
    	//Clase que controla ambos botones y evita que ocurran interferencias
    	DirButton left_arrow;
    	DirButton right_arrow;
    	btnListener leftOn;
    	btnListener rightOn;
    	public Control()
    	{
    		leftOn = new btnListener();
    	    rightOn = new btnListener();
    		left_arrow = new DirButton(0, 0, CAMERA_WIDTH/2, CAMERA_HEIGHT, vbo,leftOn);
    	    left_arrow.setAlpha(0.0f);
    	    right_arrow = new DirButton(CAMERA_WIDTH/2, 0, CAMERA_WIDTH/2, CAMERA_HEIGHT, vbo,rightOn);
    	    right_arrow.setAlpha(0.0f);
    	    
    	}
		@Override
		public void onUpdate(float pSecondsElapsed) {
			// TODO Auto-generated method stub
			if(rightOn.isOn && leftOn.isOn)
			{
				rightOn.isOn = false;
				leftOn.isOn = false;
			}
			if (rightOn.isOn)
    		{
				
    			Player player = BallGame.this.player;
    			if(player.sprite.getX()< BallGame.CAMERA_WIDTH - Player.size_x)
    			{
    				player.sprite.setPosition(player.sprite.getX()+Player.speed, player.sprite.getY());
    			}
    		}
			else if (leftOn.isOn)
    		{
				
    			Player player = BallGame.this.player;
    			if(player.sprite.getX()>0)
    			{
    				player.sprite.setPosition(player.sprite.getX()-Player.speed, player.sprite.getY());
    			}
    		}
			
		}
		@Override
		public void reset() {
			// TODO Auto-generated method stub
			
		}
    }
}
