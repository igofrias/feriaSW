package com.Phyrex.VIPeR;

import org.andengine.engine.Engine;
import org.andengine.engine.LimitedFPSEngine;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.camera.hud.HUD;
import org.andengine.engine.handler.physics.PhysicsHandler;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.FillResolutionPolicy;
import org.andengine.entity.modifier.MoveModifier;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
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
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.color.Color;
import android.util.Log;


public class FleaGame extends SimpleBaseGameActivity
{
	private Camera camera;
    private static final int CAMERA_WIDTH = 720;
    private static final int CAMERA_HEIGHT = 480;
    
    private BitmapTextureAtlas tento; //tentosaurio
    private ITextureRegion tento_region;
    
    public ITextureRegion flea_region; //flea
    private BitmapTextureAtlas splashTextureAtlas;
    
	HUD hud; //HUD
	private Text hudText;
	private int score = 0; 	
	private int time = 0;
	private Font font;
	private Font fontGame;
	
	private int level=0; //levels game
	private int amountFlea = 5;
	
	private Text gameOverText; //game over
	private boolean gameOverDisplayed = false;
	
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
	    fontGame = FontFactory.createFromAsset(getFontManager(), fontTexture2, getAssets(), "ARCADECLASSIC.TTF", 100.0f, true, Color.BLACK.getABGRPackedInt());
	    font.load();
	    fontGame.load();
	    	    
    	splashTextureAtlas = new BitmapTextureAtlas(this.getTextureManager(), 60, 47, TextureOptions.BILINEAR); //cargar pulga
    	flea_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(splashTextureAtlas, this, "cartoon_flea.png", 0, 0);
    	splashTextureAtlas.load();
    	
    	tento = new BitmapTextureAtlas(this.getTextureManager(), 470, 482, TextureOptions.BILINEAR); //cargar tentosaurio
    	tento_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(tento, this, "tentosaurioeating.png", 0, 0);
    	tento.load();
    	
    }

    private void createHUD()
    {
    	hud = new HUD();
        
        // CREATE SCORE TEXT
    	hudText = new Text(20, 20, font, "Score: 0123456789", 100, this.getVertexBufferObjectManager());
    	hudText.setText("Puntaje: 0 Tiempo: 0");
        hud.attachChild(hudText);
        camera.setHUD(hud);
    }
    
    private void createGameOverText()
    {
        gameOverText = new Text(0, 0, fontGame, "Game Over!", this.getVertexBufferObjectManager());
    }
    
    private void displayGameOverText()
    {
        camera.setChaseEntity(null);
        gameOverText.setPosition(CAMERA_WIDTH/2 - 250,CAMERA_HEIGHT/2);
        scene.attachChild(gameOverText);
        gameOverDisplayed = true;
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
    Scene scene;
    @Override
    protected Scene onCreateScene()
    {
		this.mEngine.registerUpdateHandler(new FPSLogger());
		scene = new Scene();
		createGameOverText();
    	scene.setBackground(new Background(0.09804f, 0.6274f, 0.8784f));
    	final float tentoX = this.tento_region.getWidth();
    	final float tentoY = this.tento_region.getHeight();
    	
    	level = 0;
    	time = 30;
    	
    	//coordenadas para que este en el centro
		final float centerX = (CAMERA_WIDTH - tentoX) / 2; //centro de tento
		final float centerY = (CAMERA_HEIGHT - tentoY) / 2;
		
		final float centerFX = (CAMERA_WIDTH - this.flea_region.getWidth()) / 2; //centro de pulga
		final float centerFY = (CAMERA_HEIGHT - this.flea_region.getHeight()) / 2;
		
		createHUD();
		
		//create tento and add to scene
		
		final Sprite tento = new Sprite(centerX, centerY, this.tento_region, this.getVertexBufferObjectManager());
		scene.attachChild(tento);
		
		scene.registerUpdateHandler(new TimerHandler(1f, true, new ITimerCallback() { //countdown
	        @Override
	        public void onTimePassed(TimerHandler pTimerHandler) {
	        	SubstractTime(1);	                
	                if(time==0){
		                scene.unregisterUpdateHandler(pTimerHandler);
		                displayGameOverText();	                
	                }        
	                pTimerHandler.reset();
	        	}
		}));
		
		final Sprite[] flea = new Sprite[200]; //200 fleas :o
		
		float randX;
		float randY;
		
		//DESDE AQUI
		
		for(int i=0;i<amountFlea;i++){
			randX = randFloat(-centerX*2, centerX*2);
			randY = randFloat(-centerY*75, centerY*75);
			Log.d("randomX","pulga " + i + " randX " + (centerFX+randX) + " randY " + (centerFY+randY));
			flea[i] = new Sprite((centerFX+randX),(centerFY+randY), this.flea_region, this.getVertexBufferObjectManager()){
				float deltaX=0;
				float deltaY=0;
				boolean moving = false;
				float velX = 0;
				float velY = 0;
				
				//funciones de tiempo y gameover
				
				@Override
			    public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
					
			        float localX = this.getX();
			        float localY = this.getY();
			        Log.d("Coordenadas","LocalX: " + localX);
		        	Log.d("Coordenadas","Localy: " + localY);
			        
			        //this.setPosition(pSceneTouchEvent.getX() - this.getWidth() / 2, pSceneTouchEvent.getY() - this.getHeight() / 2);
			        
			        if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_UP) {
			        	moving = false;
			        				        	
			        	Log.d("Action_UP","Dentro de actionUp: " + pSceneTouchEvent.getX() + " - " + pSceneTouchEvent.getY());
			        		        	
			        }
			        if(pSceneTouchEvent.isActionMove())
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
				@Override
				protected void onManagedUpdate(float pSecondsElapsed)
				{
					
					if(moving)
					{
						deltaX =  this.getX() - lastX;
			        	deltaY = this.getY() - lastY;
						velX = deltaX/2;
						velY = deltaY/2;
						lastX = this.getX();
						lastY = this.getY();
					}
					else{
						setPosition(this.getX() + velX,this.getY() + velY);
					}
					//if(checkPosition((CAMERA_WIDTH - tentoX) / 2, (CAMERA_WIDTH - tentoX) / 2 + tentoX, (CAMERA_HEIGHT - tentoY) / 2, (CAMERA_HEIGHT - tentoY) / 2 + tentoY,this.getX() - this.getWidth() / 2, this.getY() - this.getHeight() / 2)==false)
					if(checkPosition(0, CAMERA_WIDTH, 0, CAMERA_HEIGHT, this.getX() - this.getWidth() / 2, this.getY() - this.getHeight() / 2)==false)
					{
		        		this.setVisible(false);	//make flea invisible
		        		addToScore(50);
		        		dispose_sprite(this); //delete flea
		        		extractFlea();
		        		Log.d("Pulgas","Cantidad de pulgas: " + amountFlea);
		        	}
				}
			};
			scene.registerTouchArea(flea[i]);
			scene.setTouchAreaBindingOnActionDownEnabled(true);
			scene.attachChild(flea[i]);
		} //END IF
		
		//HASTA AQUI

    	return scene;
    } //end create scene
    
    void extractFlea(){
    	amountFlea-=1;
    	checkStateGame();
    }
    void checkStateGame(){
    	if(amountFlea == 0 && time>=0){ 
    		//populate again
    		addTime(30); //agregar tiempo
    	}
    	/*else
    		if(!gameOverDisplayed)
    			displayGameOverText();
    	*/
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
    public void onPause()
    {
    	super.onPause();
    }
    
    @Override
	public void onStop() 
    {
		super.onStop(); 
	}
    
    @Override
    protected void onDestroy() 
    {
        super.onDestroy();
    }
}