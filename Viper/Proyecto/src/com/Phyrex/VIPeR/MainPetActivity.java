package com.Phyrex.VIPeR;

import java.util.concurrent.ExecutionException;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
 
public class MainPetActivity extends SherlockFragment {
	private Button action;
	private Button calibrate;
	private Button shake;
	private static Activity thisActivity;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);        
        setRetainInstance(true);
    }

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View v = inflater.inflate(R.layout.activity_mainpet, container, false);
		return v;
		
	}	
	
	@Override
    public void onActivityCreated(Bundle savedInstanceState) {
		 super.onActivityCreated(savedInstanceState); 
		 thisActivity = getActivity();
		 action = (Button)thisActivity.findViewById(R.id.action);
		 calibrate = (Button)thisActivity.findViewById(R.id.calibrar);
		 shake = (Button)thisActivity.findViewById(R.id.shake);
		 
		 action.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v) {
					Handler handler = new Handler();
					EatTask eatTask = new EatTask(thisActivity, handler);
					Log.d("MainPetActivity","Ejecutando acciones");
					
					handler.post(eatTask);
					
				}});
		 calibrate.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v) {
					if(((MainActivity)thisActivity).isConnected()){
						((MainActivity)thisActivity).startProgram("Calibration.rxe");
					}else{
						Toast.makeText(thisActivity, "No hay robot conectado", Toast.LENGTH_SHORT).show();
					}
				}});
		 shake.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v) {
					if(((MainActivity)thisActivity).isConnected()){
						((MainActivity)thisActivity).startProgram("Shake.rxe");
					}else{
						Toast.makeText(thisActivity, "No hay robot conectado", Toast.LENGTH_SHORT).show();
					}
				}});
		 
	}
 
}





/////////////////////uso de libreria grafica no aplicable a fragmentos :C///////////////////
/*
import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.util.FPSLogger;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.ui.activity.SimpleBaseGameActivity;

public class MainPetActivity extends SimpleBaseGameActivity{
	// ===========================================================
	// Constants
	// ===========================================================
	static final int CAMERA_WIDTH = 480;
	static final int CAMERA_HEIGHT = 800;

	// ===========================================================
	// Fields
	// ===========================================================

	private Camera mCamera;
	private BitmapTextureAtlas mBitmapTextureAtlas;
	private TextureRegion mPlayerTextureRegion;
	private Scene mMainScene;

	// ===========================================================
	// Constructors
	// ===========================================================

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public EngineOptions onCreateEngineOptions() {
		this.mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);

		return new EngineOptions(true, ScreenOrientation.PORTRAIT_FIXED, new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), this.mCamera);
	}

	@Override
	protected void onCreateResources() {
		/* Load all the textures this game needs. 
		mBitmapTextureAtlas = new BitmapTextureAtlas(this.getTextureManager(), 331, 611);
		mPlayerTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mBitmapTextureAtlas, this, "tentogrande.png", 0, 0);
		mBitmapTextureAtlas.load();
	}

	@Override
	protected Scene onCreateScene() {
		this.mEngine.registerUpdateHandler(new FPSLogger()); // logs the frame rate

		// Create Scene and set background colour to (1, 1, 1) = white
		this.mMainScene = new Scene();
		this.mMainScene.setBackground(new Background(1, 1, 1));

		// Centre the player on the camera.
		final int iStartX = (CAMERA_WIDTH - mBitmapTextureAtlas.getWidth()) / 2;
		final int iStartY = (CAMERA_HEIGHT - mBitmapTextureAtlas.getHeight()) / 2;

		/* Create the sprite and add it to the scene. 
		final Sprite oPlayer = new Sprite(iStartX, iStartY, mPlayerTextureRegion, getVertexBufferObjectManager());
		this.mMainScene.attachChild(oPlayer);

		return this.mMainScene;
	}




}
*/