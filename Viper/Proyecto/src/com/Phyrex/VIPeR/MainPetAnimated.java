package com.Phyrex.VIPeR;


import com.actionbarsherlock.app.SherlockFragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.BitmapFactory;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Toast;

//Maneja el control remoto usando acelerometro
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class MainPetAnimated extends SherlockFragment{
	double gravity[];
    boolean running=true;
	float cor_x = 0;
	float cor_y = 0;
	
	DrawJoystick canvas;
	Activity parent_activity;
	MainActivity thisActivity;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
	}
	@Override
	public void onAttach(Activity activity)
	{
		super.onAttach(activity);
		parent_activity = activity;
	}
	@Override
	  public View onCreateView(LayoutInflater inflater, ViewGroup container,
	      Bundle savedInstanceState) {
		  //Creates fragment
		canvas = new DrawJoystick(getActivity());
		canvas.setWillNotDraw(false);
	    return canvas;
	  }	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		thisActivity = (MainActivity) getActivity();
		
	}
	 

	@Override
	public void onResume() {
		super.onResume();
		running=true;
	}
	
	
	
	@Override
	public void onPause() {
		super.onPause();
		running=false;
	}
        @Override
	public void onDetach(){
		super.onDetach();
		running = false;
	}
        
   /////////////////////////////clase draw joytick////////////////////////////////////
	
	private class DrawJoystick extends SurfaceView implements SurfaceHolder.Callback, Runnable
	{
		//Clase que maneja el dibujo del joystick. Tiene un thread que llama a que
		//se dibje en el canvas
		Paint color;
		Paint color_center;
		SurfaceHolder hold;
		Canvas can;
		Thread drawthread;
		Boolean running;
		//tentosaurio
		Bitmap tento;
		Bitmap tentosleeping;
		Bitmap tentoeating;
		//objetos
		Bitmap food;
		Bitmap bowl;
		Bitmap clock;
		Bitmap soap;
		//ojos
		Bitmap eyesnormal;
		Bitmap eyespooping;
		Bitmap eyesclose;
		Bitmap eyeshappy;
		Bitmap dirt[]=new Bitmap[10];
		//cola ? xD
		Bitmap tail;
		float corx, cory =0;
		boolean foodFingerMove = false;
		boolean petFingerMove = false;
		boolean soapFingerMove = false;
		boolean cleanning = false;
	    //Frame speed
	    long timeNow;
	    long timePrev = 0;
	    long timePrevFrame = 0;
	    long timeDelta;
	    int tailposition=0;
		int timetail=0;
		int eyesposition=0;
		int timeeyes=0;
		int petstate=0; //0 normal //1 eating // 2 sleeping etc...
		
		public DrawJoystick(Context context) {
			
			super(context);
			color = new Paint();
			getHolder().addCallback(this);
			color.setColor(Color.GREEN);
			color_center = new Paint();
			color_center.setColor(Color.GRAY);
			running = false;
			tento = BitmapFactory.decodeResource(getResources(), 
					R.drawable.tentosaurio);
			tentosleeping=BitmapFactory.decodeResource(getResources(), 
					R.drawable.tentosauriosleeping);
			tentoeating=BitmapFactory.decodeResource(getResources(),
					R.drawable.tentosaurioeating);
			food = BitmapFactory.decodeResource(getResources(), 
					R.drawable.food);
			clock = BitmapFactory.decodeResource(getResources(), 
					R.drawable.clock);
			bowl = BitmapFactory.decodeResource(getResources(), 
					R.drawable.bowl);
			soap = BitmapFactory.decodeResource(getResources(), 
					R.drawable.soappet);
			eyesnormal = BitmapFactory.decodeResource(getResources(), 
					R.drawable.eyesnormal);
			eyespooping = BitmapFactory.decodeResource(getResources(), 
					R.drawable.eyespooping);
			eyesclose = BitmapFactory.decodeResource(getResources(), 
					R.drawable.eyesclose);
			eyeshappy = BitmapFactory.decodeResource(getResources(), 
					R.drawable.eyeshappy);
			tail = BitmapFactory.decodeResource(getResources(), 
					R.drawable.tentosauriotail);
		}

		@Override
		public void surfaceChanged(SurfaceHolder holder, int format, int width,
				int height) {
			// TODO Auto-generated method stub
			
		}
		
		public boolean onTouchEvent(MotionEvent e) {
			Log.e("Draw", "pepe");
			float touchX = (int)e.getX();
			float touchY = (int)e.getY();
			float width = canvas.getWidth();
			float height= canvas.getHeight();
			
				switch (e.getAction()) {
					case MotionEvent.ACTION_DOWN:
						if(!soapFingerMove && touchX>0 && touchX<food.getWidth() && touchY>height*5/6 && touchY<height*5/6+food.getHeight()){
							update_coordinates(touchX, touchY);
							foodFingerMove = true;
						}else if(!foodFingerMove && !soapFingerMove && touchX>width/2-tento.getWidth()*1/3 && touchX<width/2+tento.getWidth()*1/3 && touchY>height/2 - tento.getHeight()*4/7 && touchY<height/2 + tento.getHeight()/7){
							update_coordinates(touchX, touchY);
							petFingerMove = true;
						}else if(!foodFingerMove && touchX>width/2-soap.getWidth() && touchX<width/2+soap.getWidth() && touchY>height*5/6 && touchY<height*5/6+soap.getHeight()){
							update_coordinates(touchX, touchY);
							soapFingerMove = true;
						}
				      break;
				    case MotionEvent.ACTION_MOVE:
				    	if(foodFingerMove){
				    		update_coordinates(touchX, touchY);
				    	}else if(soapFingerMove){
				    		update_coordinates(touchX, touchY);
				    	}
				    	if(!soapFingerMove && !foodFingerMove && touchX>width/2-tento.getWidth()*1/3 && touchX<width/2+tento.getWidth()*1/3 && touchY>height/2 - tento.getHeight()*4/7 && touchY<height/2 + tento.getHeight()*4/7){
				    		petFingerMove = true;
				    	}else{
				    		petFingerMove = false;
				    	}
				      break;
				    case MotionEvent.ACTION_UP:
				    	petFingerMove = false;
				    	foodFingerMove = false;
				    	soapFingerMove = false;
				    	cleanning=false;
				      break;
				}
			if(touchX>width/4 && touchX<width/4+clock.getWidth() && touchY>height*5/6 && touchY<height*5/6+clock.getHeight()){
				switch (e.getAction()) {
				case MotionEvent.ACTION_DOWN:
					if(petstate==2){
						petstate=0;
					}else{
						petstate=2;
					}
					Toast.makeText(thisActivity, "-.- zzZZ", Toast.LENGTH_SHORT).show();
			      break;
			    }
			}
			  return true;
		}

		@Override
		public void surfaceCreated(SurfaceHolder holder) {
			// TODO Auto-generated method stub
			
			hold = canvas.getHolder();
			running = true;
			drawthread = new Thread(this);
			drawthread.start();
		}

		@Override
		public void surfaceDestroyed(SurfaceHolder holder) {
			// TODO Auto-generated method stub
			running = false;
			Boolean retry = true;
			while(retry)
			{
				try {
					
					drawthread.join();
					retry = false;
					
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		public void Draw(Canvas canvas)
		{
			float width = canvas.getWidth();
			float height= canvas.getHeight();
			float center_x = canvas.getWidth()/2;
			float center_y = canvas.getHeight()/2;
			float x = corx;
			float y = cory;
			canvas.drawARGB(255, 50, 50, 255);
			canvas.drawBitmap(bowl, width/16, height*4/6, color);//bowl
			if(petstate==0){//si la mascta en estado normal
				Drawtail(center_x, center_y, width, height);
				canvas.drawBitmap(tento, center_x-tento.getWidth()*1/3, 
						center_y - tento.getHeight()*4/7, color);
				Drawtouchingpet(center_x,center_y);
			}else if(petstate==2){
				Drawsleeping(center_x,center_y);
			}else if(petstate==1){
				Draweating(width, height, center_x, center_y);
			}	
		
			canvas.drawBitmap(clock, width/4, height*5/6, color);
			//canvas.drawBitmap(soap, width/2, height*5/6, color);
			//si se arrastra la comida
	        Drawfood(x,y,width,height);
	        Drawsoap(x,y,width,height);
		}
		
		public void Drawsleeping(float center_x, float center_y){
			can.drawBitmap(tentosleeping, center_x-tentosleeping.getWidth()/2, 
					center_y - tentosleeping.getHeight()*2/7, color);
		}
		
		public void Drawtail(float center_x, float center_y, float width, float height){
			if(!petFingerMove){
				can.drawBitmap(tail, center_x-tail.getWidth()*1/3, center_y - tail.getHeight()*4/7, color);
			}else{
				if(tailposition==0 || tailposition==-2 || tailposition==2){
					can.drawBitmap(tail, center_x-tail.getWidth()*1/3, center_y - tail.getHeight()*4/7, color);
					if(timetail==5){
						if(tailposition==-2){
							tailposition=1;
							timetail =0;
						}else{
							tailposition=-1;
							timetail=0;
						}
					}
					timetail++;
				}else if(tailposition==1){
					can.rotate(5, width*10/16, height*4/9);
					can.drawBitmap(tail, center_x-tail.getWidth()*1/3, center_y - tail.getHeight()*4/7, color);
					if(timetail==5){
						tailposition=2;
						timetail=0;
					}
					timetail++;
					can.restore();
				}else if(tailposition==-1){
					can.rotate(-5, width*10/16, height*4/9);
					can.drawBitmap(tail, center_x-tail.getWidth()*1/3, center_y - tail.getHeight()*4/7, color);
					if(timetail==5){
						tailposition=-2;
						timetail=0;
					}
					timetail++;
					can.restore();
				}
						
			}
		}
		
		public void Draweating(float width, float height, float center_x, float center_y){
			can.drawBitmap(tentoeating, center_x-tentoeating.getWidth()/2, 
					center_y - tentoeating.getHeight()*4/7, color);
		}
		
		public void Drawfood(float x, float y, float width, float height){
			if (foodFingerMove) {
	        	if(x>width*2/16 && x<width*2/16+bowl.getWidth() &&y>height*5/8 && y<height*5/8+bowl.getHeight()){
					can.rotate(-40, width*3/16, height*4/7);
					can.drawBitmap(food, width*2/16, height*5/8, color);
					can.restore();
					petstate=1;
				}else{
					can.drawBitmap(food, x-food.getWidth()/2, y-food.getHeight()/2, color);
				}
	        	
	        }else{
	        	can.drawBitmap(food, 0, height*5/6, color);
	        }
		}
		
		public void Drawsoap(float x, float y, float width, float height){
			if (soapFingerMove) {
	        	if(x>width/2-tento.getWidth()*1/3 && x<width/2+tento.getWidth()*2/3 && y>height/2 - tento.getHeight()*4/7 && y<height/2 + tento.getHeight()*4/7){
	        		
	        		can.drawBitmap(soap, x-soap.getWidth()/2, y-soap.getHeight()/2, color);
	        		Log.e("Draw", "clean");
	        		cleanning=true;
				}else{
					can.drawBitmap(soap, x, y, color);
					cleanning=false;
				}
	        	
	        }else{
	        	can.drawBitmap(soap, width/2, height*5/6, color);
	        }
		} 
		
		public void Drawtouchingpet(float center_x, float center_y){
			if(cleanning){
				can.drawBitmap(eyespooping, center_x-eyespooping.getWidth()*1/3, 
						center_y - eyespooping.getHeight()*4/7, color);	
			}else if(petFingerMove){
				can.drawBitmap(eyeshappy, center_x-eyeshappy.getWidth()*1/3, 
						center_y - eyeshappy.getHeight()*4/7, color);
			}else{
				if(eyesposition==0){
					can.drawBitmap(eyesnormal, center_x-eyesnormal.getWidth()*1/3, 
							center_y - eyesnormal.getHeight()*4/7, color);
					if(timeeyes==140){
						eyesposition=1;
						timeeyes =0;
					}
					timeeyes++;
				}else if(eyesposition==1){
					can.drawBitmap(eyesclose, center_x-eyesclose.getWidth()*1/3, center_y - eyesclose.getHeight()*4/7, color);
					if(timeeyes==8){
						eyesposition=0;
						timeeyes=0;
					}
					timeeyes++;
				}
			}
		}
		
		public void update_coordinates(float x, float y)
		{
			corx= x;
			cory = y;
			
		}
		
		@Override
		public void run() {
			// TODO Auto-generated method stub

			while(running){
				can= null;
				//limit frame rate to max 60fps
                timeNow = System.currentTimeMillis();
                timeDelta = timeNow - timePrevFrame;
                if ( timeDelta < 16) {
                    try {
                        Thread.sleep(16 - timeDelta);
                    }
                    catch(InterruptedException e) {

                    }
                }
                timePrevFrame = System.currentTimeMillis();

                try {
                	can = hold.lockCanvas();
                    synchronized (hold.getSurface()) {
                       //call methods to draw and process next fame
                    	canvas.Draw(can);
                    }
                } finally {
                    if (can != null) {
                    	hold.unlockCanvasAndPost(can);
                    }
                }
			}
		}
	}
	
}
