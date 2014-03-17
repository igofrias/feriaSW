package com.Phyrex.VIPeR;

import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.andengine.engine.Engine;
import org.andengine.engine.LimitedFPSEngine;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.camera.hud.HUD;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.FillResolutionPolicy;
import org.andengine.entity.modifier.MoveYModifier;
import org.andengine.entity.modifier.SequenceEntityModifier;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.util.FPSLogger;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.color.Color;
import android.util.Log;
import android.view.MotionEvent;
import android.os.Vibrator;
import com.Phyrex.VIPeR.BTService.BTBinder;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import android.widget.Toast;


public class FleaGame extends SimpleBaseGameActivity
{

	Activity parent;
	
	private Camera camera;
    private static final int CAMERA_WIDTH = 720;
    private static final int CAMERA_HEIGHT = 480;
    
    final int BUTTON_WIDTH = 120;
    final int BUTTON_HEIGHT = 60;
    
    private BitmapTextureAtlas tentoImage; //tentosaurio
    private ITextureRegion tento_region;
    
    public ITextureRegion flea_region; //flea
    private BitmapTextureAtlas splashTextureAtlas;
    
    private BitmapTextureAtlas retry_image;
    private BitmapTextureAtlas exit_image;
    
    private ITextureRegion retry_region; //boton de jugar de nuevo
    private ITextureRegion exit_region;  //boton de salir
   
    
	BTService btservice; //bluetooth
	Activity thisActivity = this;
	
	protected boolean mBound;
	private Toast reusableToast;
	private boolean pairing;
	int maxFlea = 200;
	float randX;
	float randY;

	
	int level=0;
	boolean reload=true;
	
	List<Sprite> flea = new LinkedList<Sprite>();
	Sprite tento = null;
    ButtonSprite exitSprite = null;
    ButtonSprite retrySprite = null;
	public HUD hud; //HUD
	public HUD yourHud;
	private Text hudText;
	private Text hudTextFlea;
	private int score = 0; 	
	private int time = 30;
	private int killFlea =0;
	private int oldScore = 0;
	private Font font;
	private Font fontGame;
	
	float centerX, centerY; //posicion tentosaurio
	float centerFX, centerFY; //posicion pulga
	
	int amountFlea = 5;
	
	private Text gameOverText; //game over
	
	//final Path path = new Path(3).to(CAMERA_WIDTH,CAMERA_HEIGHT-5).to(CAMERA_WIDTH,CAMERA_HEIGHT).to(CAMERA_WIDTH,CAMERA_HEIGHT+5);
	
	public FleaGame()
	{
		mBound = false;
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
		btservice.sendBTCmessage(delay, message, value1, value2,btservice.myHandler,btservice.btcHandler);
	}
	public void sendMessageBTNumber(int box, int message) throws IOException{
		// TODO Auto-generated method stub
		if(btservice != null)
		{
			btservice.sendNumberMessage(box, message);
		}

	}
	public BTService getCurrentBTService()
	{
		return btservice;
	}
	public BTService getBTService()
	{
		return this.btservice;
	}
    @Override
    public Engine onCreateEngine(EngineOptions pEngineOptions) 
    {
        return new LimitedFPSEngine(pEngineOptions, 60);
    }
    
    @Override
    public EngineOptions onCreateEngineOptions()
    {
    	camera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
        EngineOptions engineOptions = new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED, 
        new FillResolutionPolicy(), camera);
        return engineOptions;
    }

    @Override
    public void onCreateResources() 
    {
    	
    	BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
	    FontFactory.setAssetBasePath("font/");
	    
	    final ITexture fontTexture = new BitmapTextureAtlas(this.getTextureManager(), 256, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
	    final ITexture fontTexture2 = new BitmapTextureAtlas(this.getTextureManager(), 256, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);

	    font = FontFactory.createFromAsset(getFontManager(), fontTexture, getAssets(), "font.ttf", 40.0f, true, Color.BLACK.getABGRPackedInt());
	    fontGame = FontFactory.createFromAsset(getFontManager(), fontTexture2, getAssets(), "ARCADECLASSIC.TTF", 100.0f, true, Color.WHITE.getABGRPackedInt());
	    font.load();
	    fontGame.load();
	    	    
    	splashTextureAtlas = new BitmapTextureAtlas(this.getTextureManager(), 60, 47, TextureOptions.BILINEAR); //cargar pulga
    	flea_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(splashTextureAtlas, this, "cartoon_flea.png", 0, 0);
    	splashTextureAtlas.load();
    	
    	retry_image = new BitmapTextureAtlas(this.getTextureManager(),150,75,TextureOptions.BILINEAR);
    	retry_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(retry_image, this, "backbuttonMid.png",0,0);
    	retry_image.load();
    	
    	exit_image = new BitmapTextureAtlas(this.getTextureManager(), 150, 75, TextureOptions.BILINEAR);
    	exit_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(exit_image, this, "retrybuttonMid.png",0,0);
    	exit_image.load();
    	
    	//tento = new BitmapTextureAtlas(this.getTextureManager(), 470, 482, TextureOptions.BILINEAR); //cargar tentosaurio
    	//tento_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(tento, this, "tentosaurioeating.png", 0, 0);
    	tentoImage = new BitmapTextureAtlas(this.getTextureManager(), 650, 301, TextureOptions.BILINEAR); //cargar tentosaurio
    	tento_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(tentoImage, this, "tento-pulgoso.png", 0, 0);
    	tentoImage.load();
    	
    }

    private void createHUD()
    {
    	hud = new HUD();
        
        // CREATE SCORE TEXT
    	hudText = new Text(20, 20, font, "Score: 0123456789", 100, this.getVertexBufferObjectManager());
    	hudText.setText("Puntaje: 0 Tiempo: 30");
        hud.attachChild(hudText);
        
    	hudTextFlea = new Text(20, 400, font, "Fleas: 0123456789", 100, this.getVertexBufferObjectManager());
    	hudTextFlea.setText("Pulgas: 0");
    	hud.attachChild(hudTextFlea);
        camera.setHUD(hud);
    }
    
    private void createGameOverText()
    {
        gameOverText = new Text(0, 0, fontGame, "Game Over!", this.getVertexBufferObjectManager());
    }
    
    private void displayGameOverText()
    {
        camera.setChaseEntity(null);
        gameOverText.setPosition(CAMERA_WIDTH/2 - 250,CAMERA_HEIGHT/4);
        scene.attachChild(gameOverText);
        scene.attachChild(exitSprite);
        scene.attachChild(retrySprite);

        createControllers();
    }
    
    private void addToScore(int i)
    {
        score += i;
        hudText.setText("Puntaje: " + score + " Tiempo: " + time);
    }
    private void addTime(int i){
    	time += i;
        hudText.setText("Puntaje: " + score + " Tiempo: " + time);	
    }
    private void SubstractTime(int i){
    	time -= i;
        hudText.setText("Puntaje: " + score + " Tiempo: " + time);	
    }
    private void addFlea(){
    	hudTextFlea.setText("Pulgas restantes: " + amountFlea);
    }
    
    Scene scene;
    
    public void timer(){
    	
    	scene.registerUpdateHandler(new TimerHandler(1f, true, new ITimerCallback() { //countdown
	        @Override
	        public void onTimePassed(TimerHandler pTimerHandler) {
	        	SubstractTime(1);	                
	                if(time==0){
		                scene.unregisterUpdateHandler(pTimerHandler);
		                displayGameOverText();
		                emptyGame();
		                achievement();
		                final Database_Helper entry = new Database_Helper(thisActivity);
	            		final DB_Updater updater = new DB_Updater(thisActivity);
	            		updater.updateHS(entry, 3, score);
	            		if(updater.unlock_achgame(entry, 3, score, 1500, "Desparacitador principiante"))
	            			thisActivity.runOnUiThread(new Runnable() {
	            		        @Override
	            		        public void run() {
	            		        	Toast.makeText(thisActivity, "Logro Desparacitador principiante Desbloqueado", Toast.LENGTH_SHORT).show();
	            		        }
	            		    });
	            			
	                }        
	                pTimerHandler.reset();
	        	}
		}));
    	
    }
    @Override
    protected Scene onCreateScene()
    {
		this.mEngine.registerUpdateHandler(new FPSLogger());
		scene = new Scene();
		createGameOverText();
    	scene.setBackground(new Background(0.09804f, 0.6274f, 0.8784f));
    	final float tentoX = this.tento_region.getWidth();
    	final float tentoY = this.tento_region.getHeight();
    	
    	//coordenadas para que este en el centro
		centerX = (CAMERA_WIDTH - tentoX) / 2; //centro de tento
		centerY = (CAMERA_HEIGHT - tentoY) / 2;
		
		centerFX = (CAMERA_WIDTH - this.flea_region.getWidth()) / 2; //centro de pulga
		centerFY = (CAMERA_HEIGHT - this.flea_region.getHeight()) / 2;
		
		createHUD();
		
		//create tento and add to scene
		tento = new Sprite(centerX, centerY, this.tento_region, this.getVertexBufferObjectManager());
		scene.attachChild(tento);
		scene.setTouchAreaBindingOnActionDownEnabled(true);
		scene.setTouchAreaBindingOnActionMoveEnabled(true);
		
		//create game over buttons
		exitSprite = new ButtonSprite(CAMERA_WIDTH/2+120-60, CAMERA_HEIGHT/2, this.exit_region, this.getVertexBufferObjectManager())
				{
		    @Override
		    public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float X, float Y) 
		    {
		        if (pSceneTouchEvent.isActionDown())
		        {
		        	Log.d("BotonesVisual","Boton derecho");
                    restartGame();
		        }
		        return false;
		    };
		};
		
		retrySprite = new ButtonSprite(CAMERA_WIDTH/2-240+60, CAMERA_HEIGHT/2, this.retry_region, this.getVertexBufferObjectManager())
		{
		    @Override
		    public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float X, float Y) 
		    {
		        if (pSceneTouchEvent.isActionDown())
		        {
		        	Log.d("BotonesVisual","Boton izquierdo");
                    finish();
		        }
		        return false;
		    };
		};
		
		
		timer(); //HACE ANDAR EL TIEMPO!
		
/*		scene.registerUpdateHandler(new TimerHandler(1f, true, new ITimerCallback() { //countdown
	        @Override
	        public void onTimePassed(TimerHandler pTimerHandler) {
	        	SubstractTime(1);	                
	                if(time==0){
		                scene.unregisterUpdateHandler(pTimerHandler);
		                displayGameOverText();
		                emptyGame();
		                final Database_Helper entry = new Database_Helper(thisActivity);
	            		final DB_Updater updater = new DB_Updater(thisActivity);
	            		updater.updateHS(entry, 3, score);
	            		if(updater.unlock_achgame(entry, 3, score, 1500, "Desparacitador principiante"))
	            			thisActivity.runOnUiThread(new Runnable() {
	            		        @Override
	            		        public void run() {
	            		        	Toast.makeText(thisActivity, "Logro Desparacitador principiante Desbloqueado", Toast.LENGTH_SHORT).show();
	            		        }
	            		    });
	            			 
	                }        
	                pTimerHandler.reset();
	        	}
		}));*/
		level=1;
		loadAmountFleas(); //carga cantidad de pulgas exterminadas hasta la fecha

		populateGame(amountFlea, centerX, centerY, centerFX, centerFY, level, reload );
		reload=false;

    	return scene;
    } //end create scene
   
   
    void populateGame(int numPulgas, float centerX, float centerY, float centerFX, float centerFY, int nivel, boolean status){
    	Log.d("Posicion","Inicio Populate");
    	final Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);		
    	addFlea();
		//DESDE AQUI
		if(status==true){
			status=false;
		for(int j=0;j<numPulgas;j++){
			randY = randFloat(-centerX*2, centerX*2);
			randX = randFloat(-centerY*2 + 75, centerY*2 + 75);
			
			Log.d("randomX","pulga " + j + " randX " + (centerFX+randX) + " randY " + (centerFY+randY));
			Sprite CurrentFlea = new Sprite((centerFX+randX),(centerFY+randY), this.flea_region, this.getVertexBufferObjectManager()){
				float deltaX=0;
				float deltaY=0;
				boolean moving = false;
				float velX = 0;
				float velY = 0;
								
				@Override
			    public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
					
			        float localX = this.getX();
			        float localY = this.getY();
			        Log.d("Coordenadas","LocalX: " + localX);
		        	Log.d("Coordenadas","Localy: " + localY);
			        
			        //this.setPosition(pSceneTouchEvent.getX() - this.getWidth() / 2, pSceneTouchEvent.getY() - this.getHeight() / 2);
			        
			        if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_UP || (pSceneTouchEvent.isActionDown() && moving == true)) {
			        	moving = false;
			        				        	
			        	Log.d("Action_UP","Dentro de actionUp: " + pSceneTouchEvent.getX() + " - " + pSceneTouchEvent.getY());
			        		        	
			        }
			        else if(pSceneTouchEvent.isActionMove())
			        {
			        	setPosition(pSceneTouchEvent.getX(),pSceneTouchEvent.getY());
			        	moving = true;
			        	
			        }
			        Log.d("Coordenadas","XFINAL: " + this.getX());
			        Log.d("Coordenadas","YFINAL: " + this.getY());
			        return true;
			    }
				float lastX = this.getX();
				float lastY = this.getY();
				int i=0;
				float arregloDelta[][] = new float[2][2];
				@Override
		
				protected void onManagedUpdate(float pSecondsElapsed)
				{	
				
					if(moving)
					{
						arregloDelta[0][0]=deltaX;
						arregloDelta[0][1]=deltaY;
						
						deltaX =  this.getX() - lastX;
			        	deltaY = this.getY() - lastY;
						velX = deltaX/2;
						velY = deltaY/2;
						lastX = this.getX();
						lastY = this.getY();
						
						arregloDelta[1][0]=deltaX;
						arregloDelta[1][1]=deltaY;
						
						Log.d("Action_UP","delta x: " + deltaX);
						Log.d("Action_UP","delta y: " + deltaY);
						Log.d("Action_UP", "iteracion numero: " + i);
						i++;
					}
					
					else{
						if(arregloDelta[1][0]==0 && arregloDelta[1][1]==0)
						{
						
							velX = arregloDelta[0][0]/2;
							velY = arregloDelta[0][1] /2;
						}
						setPosition(this.getX() + velX,this.getY() + velY);
						i=0;


					}
					if(checkPosition(10, CAMERA_WIDTH-50, 10, CAMERA_HEIGHT-50, this.getX() - this.getWidth() / 2, this.getY() - this.getHeight() / 2)==false)
					{
		        		this.setVisible(false);	//make flea invisible
		        		addToScore(50);
		        		dispose_sprite(this); //delete flea
		        		
		        		extractFlea();
		        		
		        		vibe.vibrate(250);   
		        		shake_sprite(tento);
		        		Log.d("Pulgas","Cantidad de pulgas: " + amountFlea);
		        		Log.d("Posicion","fin de IF populate");
		        	}
				}
			};
			flea.add(CurrentFlea);
			scene.registerTouchArea(CurrentFlea);
			scene.setTouchAreaBindingOnActionDownEnabled(true);
			scene.attachChild(CurrentFlea);
			
			reload=false;
		} 
		}//END IF
		else{
			Log.d("Posicion","else de populate");
		}
    }
    
    private void createControllers() //botones van aqui
    {
        HUD yourHud = new HUD();
        
/*        final Rectangle left = new Rectangle(CAMERA_WIDTH/2-240+60, CAMERA_HEIGHT/2, 120, 60, this.getVertexBufferObjectManager())//posx, posy, sizex, sizey-------
        {
            public boolean onAreaTouched(TouchEvent touchEvent, float X, float Y)
            {
                if (touchEvent.isActionUp())
                {
                    Log.d("Botones","Boton izquierdo");
                    restartGame();
                }
                return true;
            };
        };
        
        final Rectangle right = new Rectangle(CAMERA_WIDTH/2+120-60, CAMERA_HEIGHT/2, 120, 60, this.getVertexBufferObjectManager()) //posx, posy, sizex, sizey
        {
            public boolean onAreaTouched(TouchEvent touchEvent, float X, float Y)
            {
                if (touchEvent.isActionUp())
                {
                	Log.d("Botones","Boton derecho");     
                	finish(); //hace lo mismo que boton atras, por ende regresa a seleccion de juegos
                }
                return true;
            };
        };*/
        
        scene.registerTouchArea(exitSprite);
        scene.registerTouchArea(retrySprite);
        
/*      yourHud.registerTouchArea(left);
        yourHud.registerTouchArea(right);
        yourHud.attachChild(left);
        yourHud.attachChild(right);*/
        
        camera.setHUD(yourHud);
    }
    
    
    void loadAmountFleas(){ //carga cantidad de pulgas exterminadas hasta antes de iniciar el juego
    	final DB_Updater loadScore = new DB_Updater(thisActivity);
    	final Database_Helper helperScore = new Database_Helper(thisActivity);;
    	oldScore = loadScore.getHS(helperScore, 3);
    
    }
    
    void achievement(){
    	final DB_Updater updaterGame = new DB_Updater(thisActivity);
    	final Database_Helper helper = new Database_Helper(thisActivity);
    	int totalKilledFleas = oldScore + killFlea;
    	Log.d("AchievementFlea","oldScore: " + oldScore);
    	Log.d("AchievementFlea","currentScore: " + killFlea);
    	Log.d("AchievementFlea","newScore: " + totalKilledFleas);
    	
		updaterGame.updateAmount(helper, 3, totalKilledFleas); //update database
		if(updaterGame.unlock_achgame(helper, 3, totalKilledFleas, 100, "Exterminador"))
			thisActivity.runOnUiThread(new Runnable() {
		        @Override
		        public void run() {
		        	Toast.makeText(thisActivity, "Logro Exterminador Desbloqueado", Toast.LENGTH_SHORT).show();
		        }
		    });	
    }
    
    void extractFlea(){

    	Log.d("Posicion","extractFlea");
    	amountFlea-=1;
    	addFlea(); //updatea HUD de pulgas restantes
    	
    	killFlea+=1; //pulgas totales matadas

	    	if(isConnected()){
		    		getBTService().sendPetMessage(0, "Shake");
		    	}
    	if(amountFlea==0){
        	reload=true;
        	checkStateGame();
    	}			
    }
    
    void checkStateGame(){
    	Log.d("Posicion","checkStateGame");
    	
    	if((amountFlea == 0) && (time>0) && (reload == true))
    	{ 
    		level+=1;
    		amountFlea=5*level;
    		addTime(5);
    		populateGame(amountFlea, centerX, centerY, centerFX, centerFY, level, reload);
    		reload=false;
    	}
    }
    
    void emptyGame(){
    	int size = flea.size();
    	for(int i=flea.size()-1;i>=0;i--){
    		Sprite currentFlea = flea.get(i);
    		currentFlea.setVisible(false);
    		scene.detachChild(currentFlea);
    		Log.d("Eliminar","Pulga " + "eliminada en nivel "+i + level);
    		currentFlea = null;
    	}
    	flea.clear();
    }
    
    void restartGame(){
    	
    	addTime(30);
    	timer();
    	amountFlea = 5;
    	level = 1;
    	score = 0;
    	reload = true;
    	loadAmountFleas();
  
    	scene.detachChild(gameOverText);    	
    	scene.detachChild(exitSprite);
    	scene.detachChild(retrySprite);
    	
    	scene.unregisterTouchArea(exitSprite);
    	scene.unregisterTouchArea(retrySprite);
    	
    	dispose_other_sprite(exitSprite);
    	dispose_other_sprite(retrySprite);
    	
    	createHUD(); //muestra puntajes y tiempos
    	populateGame(amountFlea, centerX, centerY, centerFX, centerFY, level, reload);
    }
    
    void shake()
    {
    	if(btservice != null)
	    	 if(btservice.isConnected())
	    	 {
	    		 //AQUI CODIGO DE ENVIAR MENSAJE A MAIN PARA QUE HAGA SHAKE!!! mensaje 57
	    		 btservice.startProgram("Eat.rxe");
	    	 }
    	
    }
    
    void dispose_other_sprite(final Sprite toDispose)
    {
    	this.runOnUpdateThread(new Runnable()
		{
		    @Override
		     public void run()
		     {
		    	scene.detachChild(toDispose);
		     }
		});
    	
    }
    
    void dispose_sprite(final Sprite toDispose)
    {
    	this.runOnUpdateThread(new Runnable()
		{
		    @Override
		     public void run()
		     {
		    	scene.detachChild(toDispose);
		     }
		});
    	
    	flea.remove(toDispose);
    }
    
    void shake_sprite(final Sprite toShake)
    {
				MoveYModifier spriteShake1 = new MoveYModifier((float) 0.02,centerY, centerY+20);
				MoveYModifier spriteShake2 = new MoveYModifier((float) 0.02,centerY+20, centerY);
				MoveYModifier spriteShake3 = new MoveYModifier((float) 0.02,centerY, centerY-20);
				MoveYModifier spriteShake4 = new MoveYModifier((float) 0.02,centerY-20, centerY);

				SequenceEntityModifier modifier = new SequenceEntityModifier(spriteShake1, spriteShake2, spriteShake3, spriteShake4);
				toShake.registerEntityModifier(modifier);
    }

    boolean checkPosition(float xMin, float xMax, float yMin, float yMax, float posFleaX, float posFleaY){ //falso si fuera area
    	if(posFleaY>yMax || posFleaX>xMax)
    		return false;
    	if(posFleaY<yMin || posFleaX<xMin)
    		return false;
    	else
    		return true;	
    }
    
    float randFloat(float min, float max)
    {
       float range = (max - min) + 1;     
       return (float)(Math.random() * range) + min;
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
    
}