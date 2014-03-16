package com.Phyrex.VIPeR;

import java.util.LinkedList;
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
import org.andengine.entity.scene.IOnAreaTouchListener;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.ITouchArea;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.scene.background.ParallaxBackground;
import org.andengine.entity.scene.background.ParallaxBackground.ParallaxEntity;
import org.andengine.entity.scene.background.SpriteBackground;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.input.sensor.acceleration.AccelerationData;
import org.andengine.input.sensor.acceleration.IAccelerationListener;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.ui.activity.BaseGameActivity;
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
import android.view.MotionEvent;
import android.widget.Toast;

public class BallGame extends SimpleBaseGameActivity implements IAccelerationListener{

	private Camera camera;
	private static final int CAMERA_WIDTH = 800;
	private static final int CAMERA_HEIGHT = 480;
	
	private Font font;
	private Font gameOverfont;
	HUD hud;
	private Text hudText;
	Control control;
	Scene scene;
	
	VertexBufferObjectManager vbo;
	
	Player player;
	Ball ball;
	BallGenerator ballGenerator;
	BitmapTextureAtlas ballTexture;
	ITextureRegion ballTextureRegion;
	BitmapTextureAtlas playerTexture;
	ITextureRegion playerTextureRegion;
	BitmapTextureAtlas bgTexture;
	ITextureRegion bgTextureRegion;
	
	btnListener leftListener;
	btnListener rightListener;
	BTService btservice;
	Activity thisActivity = this;
	
	int puntaje;
	int dificultad;
	int vidas;
	boolean inGame;

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
	    engineOptions.getTouchOptions().setNeedsMultiTouch(true);
	    return engineOptions;
	}

	@Override
	protected void onCreateResources() {
		// TODO Auto-generated method stub

	    FontFactory.setAssetBasePath("font/");
	    final ITexture fontTexture = new BitmapTextureAtlas(this.getTextureManager(), 256, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);

	    font = FontFactory.createFromAsset(getFontManager(), fontTexture, getAssets(), "font.ttf", 40.0f, true, Color.BLACK.getABGRPackedInt());
	    gameOverfont = FontFactory.createFromAsset(getFontManager(), fontTexture, getAssets(), "ARCADECLASSIC.TTF", 100.0f, true, Color.BLACK.getABGRPackedInt());
	    font.load();
	    gameOverfont.load();
	    
	    BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
	    ballTexture = new BitmapTextureAtlas(getTextureManager(), 256, 256, TextureOptions.BILINEAR);
	    ballTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(ballTexture, this, "ballred.png", 0, 0);
	    ballTexture.load(); 
	    playerTexture = new BitmapTextureAtlas(getTextureManager(), 256, 256, TextureOptions.BILINEAR);
	    playerTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(playerTexture, this, "tentosauriogame.png", 0, 0);
	    playerTexture.load();  
	    bgTexture = new BitmapTextureAtlas(getTextureManager(), 800, 480, TextureOptions.BILINEAR);
	    bgTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(bgTexture, this, "ballgamebg.png", 0, 0);
	    bgTexture.load();
	}
	protected void registerButtons()
	{
		//Inicializa botones del juego
		hud = new HUD();
		String hudStr = String.format("Puntaje:%d Vidas:%d",BallGame.this.puntaje, BallGame.this.vidas);
		hudText = new Text(70, 40, font, hudStr,100,BallGame.this.vbo);
		leftListener = new btnListener();
		rightListener = new btnListener();
		
	    //hud.attachChild(control.left_arrow);
	    //hud.attachChild(control.right_arrow);
	    
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
			ballGenerator.updateSpeed();
		}
		hudText.setText("Puntaje:"+puntaje+" Vidas:" + vidas);
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
			inGame = false;
			final Database_Helper entry = new Database_Helper(thisActivity);
    		final DB_Updater updater = new DB_Updater(thisActivity);
    		updater.updateHS(entry, 1, puntaje);
    		if(updater.unlock_achgame(entry, 1, puntaje, 5, "Atrapador principiante"))
    			this.runOnUiThread(new Runnable() {
    		        @Override
    		        public void run() {
    		        	Toast.makeText(thisActivity, "Logro Atrapador principiante Desbloqueado", Toast.LENGTH_SHORT).show();
    		        }
    		    });
    			
			hud.detachChild(hudText);
			hudText = new Text(0, CAMERA_HEIGHT/2, gameOverfont, "Game Over",100,BallGame.this.vbo);
			hud.attachChild(hudText);
			player.detach();
		}
	}
	SensorManager sensorManager;
	@Override
	protected Scene onCreateScene() {
		// TODO Auto-generated method stub
		scene = new Scene();
		puntaje = 0;
		vidas = 3;
		vbo = mEngine.getVertexBufferObjectManager();
		registerButtons();
		control = new Control(leftListener, rightListener);
		scene.setOnSceneTouchListener(control.tcontrol);
		scene.setTouchAreaBindingOnActionDownEnabled(true);
		hud.registerUpdateHandler(control);
		
//	     scene.setBackground(new Background(0.678f, 0.847f, 0.901f));
		ParallaxBackground background = new ParallaxBackground(0, 0, 0);
	    background.attachParallaxEntity(new ParallaxEntity(0, new Sprite(0, 0, bgTextureRegion, vbo)));
	    scene.setBackground(background);
	     if(btservice != null)
	    	 if(btservice.isConnected())
	    	 {
	    		 btservice.startProgram("Eat.rxe");
	    	 }
	     player = new Player();
	     ballGenerator = new BallGenerator();
	     scene.attachChild(player.sprite);
	     inGame = true;
	     ballGenerator.CreateBall();
	     
	     return scene;
	}
	
	@Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case BTService.REQUEST_CONNECT_DEVICE:

                
                if (resultCode == Activity.RESULT_OK) {
                	int brick = data.getExtras().getInt("Brick");
                    String address = data.getExtras().getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
                    pairing = data.getExtras().getBoolean(DeviceListActivity.PAIRING);
                    btservice.setMac(address, brick);
                    btservice.startBTCommunicator(address);
                    
	 		        
                }
                
                break;
                
            case BTService.REQUEST_ENABLE_BT:

                // When the request to enable Bluetooth returns
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        BTService.setBtOnByUs(true);
                        int brick = data.getExtras().getInt("Brick");
                        btservice.selectTypeCnt(brick);
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
    	BallGenerator currentGenerator;
    	static final int size_x = 40;
    	static final int size_y = 40;
    	TimerHandler spriteTimerHandler;
    	float speed;
    	float x;
    	float y;
    	boolean inPlay;
    	public Ball(BallGenerator generator,float speed)
    	{
    		currentGenerator = generator;
    		inPlay = true;
    		this.x = 2*size_x +(float) (Math.random()*(BallGame.CAMERA_WIDTH-4*size_x));//Margen de 2*size_x;
    		this.y = 0;
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
    		        	 Ball.this.detach();
    		         }
    		     };
    		};;
    		this.speed = speed;
    	}
		
    	public void createFallUpdater()
    	{
    		float updateTime = 0.0333f;
			spriteTimerHandler = new TimerHandler(updateTime, new ITimerCallback()
	        {                      
	            @Override
	            public void onTimePassed(final TimerHandler pTimerHandler)
	            {
	            	if(inPlay)
	            	{
	            		updatePos(spriteTimerHandler);
	            	}
	            	
	            }
	        });
			
	        BallGame.this.getEngine().registerUpdateHandler(spriteTimerHandler );
    	}
    	public void updatePos(TimerHandler timerHandler)
    	{
    		//Es lo que actualiza la posicion en un instante de tiempo
    		//La idea es que distintos tipos de Bolas cambien esto para actualizar 
    		//su posicion, para eso deberian cambiar inBoundaryAction() y outOfBoundaryAction()
    		if(spriteTimerHandler != null)
        	{
        		spriteTimerHandler.reset();
        		if(Ball.this.y <= BallGame.CAMERA_HEIGHT)
        		{
        			inBoundaryAction();
        		}
        		else
        		{
        			outOfBoundaryAction();
        		
        		}
        		sprite.setPosition(x, y);
        	}
    		
    	}
    	public void inBoundaryAction()
    	{
    		//Aqui va lo que la pelota hace cuando esta dentro del terreno de juego
    		//Las subclases deberian cambiar esto
    		Ball.this.y = Ball.this.y + speed;
    	}
    	public void outOfBoundaryAction()
    	{
    		inPlay = false;
			BallGame.this.removeLive();
			
			Ball.this.detach();
    	}
    	public void detach()
    	{
    		currentGenerator.removeBall(this);
    		currentGenerator.CreateBall();
    		BallGame.this.runOnUpdateThread(new Runnable()
    		{
    		    @Override
    		     public void run()
    		     {
    		    	BallGame.this.scene.detachChild(sprite);
    		    	BallGame.this.getEngine().unregisterUpdateHandler(Ball.this.spriteTimerHandler);
    		     }
    		});
    	}
    	
    }
    private class DiagonalBall extends Ball
    {	
    	//Clase que se mueve diagonalmente
    	float speed_x;
		public DiagonalBall(BallGenerator generator, float speed_mod) {
			//Genera el vector velocidad a partir de su modulo
			super(generator, 0);
			float angle = (float) (Math.random()*-140 +70); //Entre -70 y 70 grados
			speed = (float) (speed_mod*Math.cos(Math.toRadians(angle)));
			speed_x = (float)(speed_mod*Math.sin(Math.toRadians(angle)));
			
			// TODO Auto-generated constructor stub
		}
		public DiagonalBall(BallGenerator generator, float speed_x, float speed_y) {
			super(generator, speed_y);
			this.speed_x = speed_x;
			// TODO Auto-generated constructor stub
		}
		@Override
		public void inBoundaryAction()
		{
			//Hace que se mueva en diagonal y que rebote
			super.inBoundaryAction();
			this.x = this.x + this.speed_x;
			if(this.x <= 0 || this.x >= BallGame.CAMERA_WIDTH)
			{
				this.speed_x = -this.speed_x;
			}
		}
    }
    private class BallGenerator
    {
    	//Genera pelotas aleatoriamente TODO
    	LinkedList<Ball> currentBalls = new LinkedList<Ball>();
    	float speed;
    	public BallGenerator()
    	{
    		this.speed = 5;
    	}
    	public void CreateBall()
    	{
    		if(BallGame.this.inGame)
    		{
	    		//Ball current_ball = new Ball(this,speed);
    			Ball current_ball = selectBall();
	    		currentBalls.add(current_ball);
	    		BallGame.this.scene.attachChild(current_ball.sprite);
	    		current_ball.createFallUpdater();
    		}
    		
    	}
    	public Ball selectBall()
    	{
    		Random randgen = new Random();
    		int type =  randgen.nextInt(2);
    		switch(type)
    		{
    		case 0:
    			return new Ball(this,speed);
    		case 1:
    			return new DiagonalBall(this,speed);
    		}
    		return null;
    		
    		
    	}
    	public void removeBall(Ball toRemove)
    	{
    		currentBalls.remove(toRemove);
    	}
    	public void updateSpeed()
    	{
    		//Cambia la velocidad de caida de la pelota de acuerdo a la dificultad
    		float min = 5;
    		float max = 100;
    		float steps = (float) (1.0/50.0); //Cantidad de "niveles" en los que sube la velocidad hasta llegar a max
    		speed = (float) (min + (max-min)*steps*BallGame.this.dificultad);
    	}
    }
    private class Player
    {
    	Sprite sprite;
    	
    	static final int size_x = 80;
    	static final int size_y = 100;
    	static final int speed = 10;
    	public Player()
    	{
    		//sprite = new Rectangle(x-size_x,y-size_y,size_x,size_y,BallGame.this.vbo);
    		int x = BallGame.CAMERA_WIDTH/2;
    		int y = BallGame.CAMERA_HEIGHT-size_y/2;
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
    class TouchControl implements IOnSceneTouchListener
    {
    	btnListener leftListener;
    	btnListener rightListener;
    	public TouchControl(btnListener left, btnListener right)
    	{
    		leftListener = left;
    		rightListener = right;
    	}
		
		@Override
		public boolean onSceneTouchEvent(Scene pScene,
				TouchEvent event) {
			// TODO Auto-generated method stub
			float X = event.getX();
	    	float Y = event.getY();
	    	if(event.isActionDown())
	    	{
	    		if(X > CAMERA_WIDTH/2)
         	   {
         		  leftListener.isOn = false;
         		  rightListener.isOn = true;
         	   }
         	   else if(X <= CAMERA_WIDTH/2)
         	   {
         		  leftListener.isOn = true;
         		  rightListener.isOn = false;
         	   }
	    	}
	    	else if(event.isActionUp())
	    	{
	    		leftListener.isOn = false;
       		  rightListener.isOn = false;
	    	}
	    	
			return false;
		}
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
    	TouchControl tcontrol;
    	public Control(btnListener left, btnListener right)
    	{
    		leftOn = left;
    	    rightOn = right;
    		tcontrol = new TouchControl(leftOn,rightOn);
    	    
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
		public void toggleRightOn()
		{
			rightOn.isOn = true;
		}
		public void toggleLeftOn()
		{
			leftOn.isOn = true;
		}
		public void toggleRightOff()
		{
			rightOn.isOn = false;
		}
		public void toggleLeftOff()
		{
			leftOn.isOn = false;
		}
    }

    private class RestartScene extends Scene
    {
    	
    }
	
	@Override
	public void onAccelerationChanged(AccelerationData pAccelerationData) {
		//Va hacia algun lado de acuerdo a la inclinacion del celu
		float X = pAccelerationData.getX();
		if(X > 0)
		{
			control.toggleLeftOff();
			control.toggleRightOn();
		}
		else if (X < 0)
		{
			control.toggleLeftOn();
			control.toggleRightOff();
		}
	}
	@Override
	public void onAccelerationAccuracyChanged(AccelerationData pAccelerationData) {
		// TODO Auto-generated method stub
		
	};
	 
}
