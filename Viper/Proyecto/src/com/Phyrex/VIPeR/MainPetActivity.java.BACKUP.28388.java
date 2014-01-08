package com.Phyrex.VIPeR;


import java.util.ArrayList;

import com.actionbarsherlock.app.SherlockFragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.BitmapFactory;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
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

public class MainPetActivity extends SherlockFragment{
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
		canvas.startCanvas();
		
	}
	
	@Override
	public void onPause() {
		super.onPause();
	
		canvas.stopCanvas();
	}
	
	@Override
	public void onStop() {
		super.onStop();
		canvas.stopCanvas();
	}
	
        @Override
	public void onDetach(){
		super.onDetach();
		canvas.stopCanvas();
	}
        
    @Override
	public void onDestroy(){
		super.onDestroy();
		canvas.stopCanvas();
	}
        
    public boolean getGameselect(){
    	return canvas.gameselect;
    }
    public void setGameselect(){
    	canvas.gameselect=false;
    }
    boolean eyesOpen = true;
    void sendOpenEyes()
    {
    	//Solo envia el aviso de abrir ojos si antes estaban cerrados
    	if(!eyesOpen)
    	{
    		Log.d("sendOpenEyes","Sending OpenEyes message");
    		eyesOpen = true;
    		if(((MainActivity)thisActivity).isConnected())
			{
				((MainActivity)thisActivity).getBTService().sendPetMessage(0, "OpenEyes");
			} 
    		
    	}
    }
    void sendNonOpenEyes(String mensaje)
    {
    	if(eyesOpen)
    	{
    		eyesOpen = false;
    		Log.d("sendNonOpenEyes","Sending "+mensaje+" message");
    		if(((MainActivity)thisActivity).isConnected())
			{
				((MainActivity)thisActivity).getBTService().sendPetMessage(0, mensaje);
			}
    	}
    }
    void sendMiscAction(String mensaje,int brick)
    {
    	if(((MainActivity)thisActivity).isConnected())
		{
			((MainActivity)thisActivity).getBTService().sendPetMessage(brick, mensaje);
		}
    }
    public void Actions(int action){
		final DB_Updater updater = new DB_Updater(thisActivity);
     	final Database_Helper entry = new Database_Helper(thisActivity);
     	SherlockFragment fragment1 = ((StatesActivity)getFragmentManager().findFragmentByTag("state"));
     	
		switch(action){
			case 1://comer
				EatTask.petAction(thisActivity, updater, entry, (StatesActivity)fragment1);
				sendOpenEyes();
				break;
			case 2://dormir
				SleepTask.petAction(thisActivity, updater, entry, (StatesActivity)fragment1);
				if(((StatesActivity)fragment1).isSleeping())
				{
					sendNonOpenEyes("CloseEyes");
					sendMiscAction("YawnSound",0);
				}
				else
				{
					sendOpenEyes();
				}
				break;
			case 3://lavar - Wash
				WashTask.petAction(thisActivity, updater, entry, (StatesActivity)fragment1);
				break;
			case 4://limpiar caca /clean
				CleanTask.petAction(thisActivity, updater, entry, (StatesActivity)fragment1);
			break;
			case 5://jugar
				if(fragment1!=null && !fragment1.isDetached()){//si el fragmento esta activo
		 			if(!((StatesActivity)fragment1).isSleeping()){
		 				if(updater.play(entry))
		         			Toast.makeText(thisActivity, "Logro Desbloqueado Jugueton", Toast.LENGTH_LONG).show();
		 				((StatesActivity)fragment1).playing();
	 			    	if(((MainActivity)thisActivity).isConnected()){
	 			    		((MainActivity)thisActivity).getBTService().sendPetMessage(1, "MoveTail");
	 			    	}
		 			}else{
		 				Toast.makeText(thisActivity, "no puedes molestar a la mascota mientras duerme", Toast.LENGTH_SHORT).show();
		 			}
		 			//aca intent
     			}	
			break;
			case 6://boton accion
				fragment1 = ((StatesActivity)getFragmentManager().findFragmentByTag("state"));
				PetActionManager petman = new PetActionManager(thisActivity,(StatesActivity)fragment1);
				Log.d("MainPetActivity","Ejecutando acciones");
				petman.execute();
			break;
			
			case 7://cagar pooping
				PooTask.petAction(thisActivity, updater, entry, (StatesActivity)fragment1);
			case 8://Volver al estado original
				sendOpenEyes();
			break;
			
		}
	}
    
    
    void eyesClosed()
    {
    	eyesOpen = false;
    }
    
   /////////////////////////////clase draw joytick////////////////////////////////////
	
	private class DrawJoystick extends SurfaceView implements SurfaceHolder.Callback, Runnable
	{
		//Clase que maneja el dibujo del joystick. Tiene un thread que llama a que
		//se dibje en el canvas
		Paint color;
		Paint colorgray;
		Paint colorblack;
		SurfaceHolder hold;
		Canvas can;
		Thread drawthread;
		//tentosaurio
		Bitmap tento;
		Bitmap tentosleeping;
		Bitmap tentoeating;
		//objetos
		Bitmap food;
		Bitmap bowl[] = new Bitmap[4];
		Bitmap clock;
		Bitmap soap;
		Bitmap poo;
		Bitmap play;
		Bitmap action;
		Bitmap bubbles[] = new Bitmap[3];
		//ojos
		Bitmap eyesnormal;
		Bitmap eyespooping;
		Bitmap eyesclose;
		Bitmap eyeshappy;
		Bitmap dirt[]=new Bitmap[9];
		//cola ? xD
		Bitmap tail;
		float corx, cory =-1;
		boolean foodFingerMove = false;
		boolean petFingerMove = false;
		boolean soapFingerMove = false;
		boolean cleanning = false;
		boolean sleeping = false;
		boolean poop =false;
	    //Frame speed
	    long timeNow;
	    long timePrev = 0;
	    long timePrevFrame = 0;
	    long timeDelta;
	    //tiempos de movimientos
		int timetail=0;
		int timeeat=0;
		int cleantime=0;
		int timeeyes=0;
		int timesleep=0;
		//posiciones y estados
		//burbujas
		float posbubblesx=0;
		float posbubblesy=0;
		int tailposition=0;
		int bowlstate=0;
		int eyesposition=0;	
		int petstate=0;//0 normal //1 eating // 2 sleeping etc...
		int dirtstate=0; //de 0 a el numero de mugre D:
		//Measure frames per second.
	    long now;
	    int framesCount=0;
	    int framesCountAvg=0;
	    long framesTimer=0;
	    Paint fpsPaint=new Paint();	
	    boolean gameselect = false;
		ArrayList<Bubbles> bubblesimgs;	
	    
		public DrawJoystick(Context context) {
			
			super(context);
			color = new Paint();
			getHolder().addCallback(this);
			color.setColor(Color.GREEN);
			colorblack = new Paint();
			colorblack.setColor(Color.BLACK);
			colorgray = new Paint();
			colorgray.setColor(Color.GRAY);
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
			bowl[0] = BitmapFactory.decodeResource(getResources(), 
					R.drawable.bowl);
			bowl[1] = BitmapFactory.decodeResource(getResources(), 
					R.drawable.bowl0);
			bowl[2] = BitmapFactory.decodeResource(getResources(), 
					R.drawable.bowl1);
			bowl[3] = BitmapFactory.decodeResource(getResources(), 
					R.drawable.bowl2);
			soap = BitmapFactory.decodeResource(getResources(), 
					R.drawable.soap);
			poo = BitmapFactory.decodeResource(getResources(), 
					R.drawable.poo);
			play = BitmapFactory.decodeResource(getResources(), 
					R.drawable.playpet);
			action = BitmapFactory.decodeResource(getResources(), 
					R.drawable.action);
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
			dirt[0]= BitmapFactory.decodeResource(getResources(), 
					R.drawable.dirt1);
			dirt[1]= BitmapFactory.decodeResource(getResources(), 
					R.drawable.dirt2);
			dirt[2]= BitmapFactory.decodeResource(getResources(), 
					R.drawable.dirt3);
			dirt[3]= BitmapFactory.decodeResource(getResources(), 
					R.drawable.dirt4);
			dirt[4]= BitmapFactory.decodeResource(getResources(), 
					R.drawable.dirt5);
			dirt[5]= BitmapFactory.decodeResource(getResources(), 
					R.drawable.dirt6);
			dirt[6]= BitmapFactory.decodeResource(getResources(), 
					R.drawable.dirt7);
			dirt[7]= BitmapFactory.decodeResource(getResources(), 
					R.drawable.dirt8);
			dirt[8]= BitmapFactory.decodeResource(getResources(), 
					R.drawable.dirt9);
			bubbles[0] = BitmapFactory.decodeResource(getResources(), 
					R.drawable.bubble1);
			bubbles[1] = BitmapFactory.decodeResource(getResources(), 
					R.drawable.bubble2);
			bubbles[2] = BitmapFactory.decodeResource(getResources(), 
					R.drawable.bubble3);
			bubblesimgs= new ArrayList<Bubbles>();
		}

		@Override
		public void surfaceChanged(SurfaceHolder holder, int format, int width,
				int height) {
			
		}
		
		public boolean onTouchEvent(MotionEvent e) {
			float touchX = (int)e.getX();
			float touchY = (int)e.getY();
			float width = canvas.getWidth();
			float height= canvas.getHeight();
<<<<<<< HEAD
				if(!gameselect){
					switch (e.getAction()) {
						case MotionEvent.ACTION_DOWN:
							if(!sleeping && timeeat==0){
								if(!soapFingerMove && touchX>0 && touchX<food.getWidth() && touchY>height*5/6 && touchY<height*5/6+food.getHeight()){
									update_coordinates(touchX, touchY);//comida
									foodFingerMove = true;
								}else if(!foodFingerMove && !soapFingerMove && touchX>width/2-tento.getWidth()*1/3 && touchX<width/2+tento.getWidth()*1/3 && touchY>height/2 - tento.getHeight()*4/7 && touchY<height/2 + tento.getHeight()/7){
									update_coordinates(touchX, touchY);//tocar mascota
									petFingerMove = true;
									poop=true;
									Runnable shameFace = new Runnable()
									{

										@Override
										public void run() {
											// TODO Auto-generated method stub
											sendNonOpenEyes("ShameEyes");
										}
										
									};
									Handler h = new Handler();
									h.postDelayed(shameFace, 10);
						    		Actions(7); //hacer caca
								}else if(!foodFingerMove && touchX>width/2 && touchX<width/2+soap.getWidth()
										&& touchY>height*5/6 && touchY<height*5/6+soap.getHeight()){
									update_coordinates(touchX, touchY);//arrastrar soap
									soapFingerMove = true;
								}
							}
					      break;
					    case MotionEvent.ACTION_MOVE:
				    		if(foodFingerMove){
				    		update_coordinates(touchX, touchY);
					    	}else if(soapFingerMove){
					    		update_coordinates(touchX, touchY);
					    		sendNonOpenEyes("ShameEyes");
					    	}
					    	if(!soapFingerMove && !foodFingerMove && touchX>width/2-tento.getWidth()*1/3 && touchX<width/2+tento.getWidth()*1/3 && touchY>height/2 - tento.getHeight()*4/7 && touchY<height/2 + tento.getHeight()*4/7){
					    		petFingerMove = true;
					    		sendNonOpenEyes("HappyEyes");
					    		if(dirtstate<9){
					    			dirtstate++;
					    		}
					    		
					    	}else{
					    		petFingerMove = false;
					    	}
					      break;
					    case MotionEvent.ACTION_UP:
					    	if(soapFingerMove || petFingerMove)
					    	{
					    		//Envia OpenEyes solo si antes se estaba moviendo para limpiarlo o para acariciarlo
					    		sendOpenEyes();
					    	}
					    	petFingerMove = false;
					    	foodFingerMove = false;
					    	soapFingerMove = false;
					    	cleanning=false;
					    	update_coordinates(-1, -1);//comida
					    	
					      break;
					}
				if(timeeat==0 && !soapFingerMove && !foodFingerMove && touchX>width/4 && touchX<width/4+clock.getWidth() && touchY>height*5/6 && touchY<height*5/6+clock.getHeight()){
					switch (e.getAction()) {
					case MotionEvent.ACTION_DOWN:
						if(petstate==2){
							petstate=0;
							sleeping=false;
							
							Actions(2);//despertar
						}else{
							petstate=2;
							sleeping=true;
							timesleep=90;
							Actions(2);//accion dormir
=======
				switch (e.getAction()) {
					case MotionEvent.ACTION_DOWN:
						if(!sleeping && timeeat==0){
							if(!soapFingerMove && touchX>0 && touchX<food.getWidth() && touchY>height*5/6 && touchY<height*5/6+food.getHeight()){
								update_coordinates(touchX, touchY);//comida
								foodFingerMove = true;
							}else if(!foodFingerMove && !soapFingerMove && touchX>width/2-tento.getWidth()*1/3 && touchX<width/2+tento.getWidth()*1/3 && touchY>height/2 - tento.getHeight()*4/7 && touchY<height/2 + tento.getHeight()/7){
								update_coordinates(touchX, touchY);//tocar mascota
								petFingerMove = true;
								poop=true;
					    		Actions(7); //hacer caca
							}else if(!foodFingerMove && touchX>width/2 && touchX<width/2+soap.getWidth()
									&& touchY>height*5/6 && touchY<height*5/6+soap.getHeight()){
								update_coordinates(touchX, touchY);//arrastrar soap
								soapFingerMove = true;
							}
>>>>>>> Neko-Draw
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
				    		if(dirtstate<9){
				    			dirtstate++;
				    		}
				    		
				    	}else{
				    		petFingerMove = false;
				    	}
				      break;
				    case MotionEvent.ACTION_UP:
				    	petFingerMove = false;
				    	foodFingerMove = false;
				    	soapFingerMove = false;
				    	cleanning=false;
				    	update_coordinates(-1, -1);//comida
				      break;
				}
			if(timeeat==0 && !soapFingerMove && !foodFingerMove && touchX>width/4 && touchX<width/4+clock.getWidth() && touchY>height*5/6 && touchY<height*5/6+clock.getHeight()){
				switch (e.getAction()) {
				case MotionEvent.ACTION_DOWN:
					if(petstate==2){
						petstate=0;
						sleeping=false;
						if(((MainActivity)thisActivity).isConnected())
							((MainActivity)thisActivity).getBTService().sendPetMessage(0, "OpenEyes");
						Actions(2);//despertar
					}else{
						petstate=2;
						sleeping=true;
						timesleep=90;
						Actions(2);//accion dormir
					}
			      break;
			    }
			}
			if(poop && timeeat==0 && touchX>width*14/16-poo.getWidth()/2 && touchX<width*14/16+poo.getWidth()/2 && touchY>height*9/12- poo.getHeight()/2 && touchY<height*9/12+ poo.getHeight()/2){
				switch (e.getAction()) {
				case MotionEvent.ACTION_DOWN:
					poop= false;
					Actions(4);
			      break;
			    }
			}
			if(timeeat==0 && !sleeping && touchX>width*3/4 && touchX<width*3/4+play.getWidth() && touchY>height*5/6 && touchY<height*5/6+play.getHeight()){
				switch (e.getAction()) {
				case MotionEvent.ACTION_DOWN:
					//gameselect=true;
					Actions(5);//accion de jugar
					((MainActivity)thisActivity).launch_gameselect();
			      break;
			    }
			}
			return true;
		}

		@Override
		public void surfaceCreated(SurfaceHolder holder) {
			// Auto-generated method stub
			
			hold = canvas.getHolder();
			running = true;
			drawthread = new Thread(this);
			drawthread.start();
		}

		@Override
		public void surfaceDestroyed(SurfaceHolder holder) {
			stopCanvas();
		}
		
		
		
		public void Draw(Canvas canvas)
		{
			float width = canvas.getWidth();
			float height= canvas.getHeight();
			float center_x = canvas.getWidth()/2;
			float center_y = canvas.getHeight()/2;
			float x = -1;
			float y = -1;
			if(corx !=-1 && cory!=-1){
				x = corx;
				y = cory;
			}
			canvas.drawARGB(255, 50, 50, 255);
			if(!gameselect){
				if(bowlstate==0){
					can.drawBitmap(bowl[0], width*1/16, height*5/8, color);
				}else if(bowlstate==1){
					can.drawBitmap(bowl[1], width*1/16, height*5/8, color);
				}else if(bowlstate==2){
					can.drawBitmap(bowl[2], width*1/16, height*5/8, color);
				}else if(bowlstate==3){
					can.drawBitmap(bowl[3], width*1/16, height*5/8, color);
				}
				if(petstate==0){//si la mascta en estado normal
					
					Drawtail(center_x, center_y, width, height);
					canvas.drawBitmap(tento, center_x-tento.getWidth()*1/3, 
							center_y - tento.getHeight()*4/7, color);
					Drawtouchingpet(center_x,center_y, x , y);
					Drawdirt(center_x,center_y, canvas);
				}else if(petstate==2){
					Drawsleeping(center_x,center_y);
				}else if(petstate==1){
					sendNonOpenEyes("HappyEyes");
					Draweating(width, height, center_x, center_y);
					
				}	
				/*//Measure frame rate (unit: frames per second).
		         now=System.currentTimeMillis();
		         canvas.drawText(framesCountAvg+" fps", 40, 70, fpsPaint);
		         framesCount++;
		         if(now-framesTimer>1000) {
		                 framesTimer=now;
		                 framesCountAvg=framesCount;
		                 framesCount=0;
		         }*/
				canvas.drawBitmap(clock, width/4, height*5/6, color);
				canvas.drawBitmap(play, width*3/4, height*5/6, color);
			
				if(poop){
					
					can.drawBitmap(poo, center_x*28/16-poo.getWidth()/2, 
						center_y*9/6- poo.getHeight()/2, color);
				}
				//si se arrastra la comida
				if(bubblesimgs!=null)
					Drawbubbles(bubblesimgs, x, y);
				if(foodFingerMove){
					Drawsoap(x,y,width,height);
					Drawfood(x,y,width,height);
				}else{
					Drawfood(x,y,width,height);
					Drawsoap(x,y,width,height);
				}
			//game selection
			}else{//rectangulo con seleccion de juegos
				canvas.drawRect(width/16, height/20, width*15/16 , height*19/20, colorgray);
				canvas.drawRect(width*2/16, height*2/20, width*6/16 , height*6/20, color);
				canvas.drawText("Juego 1", width*3/16 , height*4/20, colorblack);
				canvas.drawRect(width*2/16, height*8/20, width*6/16 , height*12/20, color);
				canvas.drawText("Juego 2", width*3/16 , height*10/20, colorblack);
				//dibujar links a juegos y nombres D:	
			}
	       
	       
		}
		
		public void Drawsleeping(float center_x, float center_y){ //TODO
			can.drawBitmap(tentosleeping, center_x-tentosleeping.getWidth()/2, 
					center_y - tentosleeping.getHeight()*2/7, color);
			can.drawARGB(150, 0, 0, 0);
			DrawZetas(center_x*2,center_y*2);
			if(sleeping){
			}
		}
		
		public void DrawZetas(float width, float height){
			String text = "Z";
			int textColor = Color.WHITE;
			float textSize=0;
			textSize= ZetaTextSizedpi(textSize);
			can.save();
			float posx, posy=0;
			if(timesleep>60){
				timesleep--;
				posx=width*5/16;
				posy=height*9/25;
			}else if(timesleep>30){
				posx=width*6/16;
				posy=height*8/25;
				timesleep--;
			}else{
				posx=width*7/16;
				posy=height*7/25;
				timesleep--;
				if (timesleep==0)
					timesleep=90;
			}
			can.rotate(-45, posx, posy);
			Paint textPaint = new Paint();
			textPaint.setAntiAlias(true);
			textPaint.setColor(textColor);
			textPaint.setTextSize(textSize);
			Rect bounds = new Rect();
			textPaint.getTextBounds(text, 0, text.length(), bounds);
			can.drawText(text, posx, posy, textPaint);
			can.restore();
		}
		
		public float ZetaTextSizedpi(float textSize){
			float dpi = getResources().getDisplayMetrics().density;
			if(dpi ==0.75){
				if(timesleep>60){
					textSize=15;
				}else if(timesleep>30){
					textSize=30;
				}else{
					textSize=45;
				}
			}else if(dpi==1){
				if(timesleep>60){
					textSize=25;
				}else if(timesleep>30){
					textSize=50;
				}else{
					textSize=75;
				}
			}
			else if(dpi==1.5){
				if(timesleep>60){
					textSize=35;
				}else if(timesleep>30){
					textSize=70;
				}else{
					textSize=105;
				}
			}
			return textSize;
		}
		
		public void Drawdirt(float center_x, float center_y, Canvas canvas){
			if(dirtstate>0){
				int i=0;
				do{
					canvas.drawBitmap(dirt[i], center_x-dirt[i].getWidth()*1/3, 
							center_y - dirt[i].getHeight()*4/7, color);
					
					i++;
				}while(i<dirtstate && i>0);
			}
		}
		
		public void Drawclean(float center_x, float center_y){
			if(dirtstate>0){
				dirtstate--;
				if(dirtstate==0)
					Actions(3);
			}
		}
		
		public void Drawtail(float center_x, float center_y, float width, float height){
			if(!petFingerMove){
				can.drawBitmap(tail, center_x-tail.getWidth()*1/3, center_y - tail.getHeight()*4/7, color);
			}else{
				if(tailposition==0 || tailposition==-2 || tailposition==2){
					can.drawBitmap(tail, center_x-tail.getWidth()*1/3, center_y - tail.getHeight()*4/7, color);
					if(timetail==4){
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
					can.save();
					can.rotate(5, width*10/16, height*4/9);
					can.drawBitmap(tail, center_x-tail.getWidth()*1/3, center_y - tail.getHeight()*4/7, color);
					if(timetail==4){
						tailposition=2;
						timetail=0;
					}
					timetail++;
					can.restore();
				}else if(tailposition==-1){
					can.save();
					can.rotate(-5, width*10/16, height*4/9);
					can.drawBitmap(tail, center_x-tail.getWidth()*1/3, center_y - tail.getHeight()*4/7, color);
					if(timetail==4){
						tailposition=-2;
						timetail=0;
					}
					timetail++;
					can.restore();
				}
						
			}
		}
		
		public void Draweating(float width, float height, float center_x, float center_y){
			//masticando
			if(timeeat>80){
				bowlstate=3;
				timeeat--;
				can.drawBitmap(tentoeating, center_x-tentoeating.getWidth()/2, 
						center_y - tentoeating.getHeight()*4/7, color);
			}else if(timeeat>30){
				bowlstate=2;
				timeeat--;
				can.drawBitmap(tentoeating, center_x-tentoeating.getWidth()/2, 
						center_y - tentoeating.getHeight()*4/7, color);
			}else if(timeeat>0){
				bowlstate=1;
				timeeat--;
				can.drawBitmap(tentoeating, center_x-tentoeating.getWidth()/2, 
						center_y - tentoeating.getHeight()*4/7, color);
			}else if(timeeat==0){
				bowlstate=0;
				petstate=0;
				Actions(1);//uno para comer
			}
		}
		
		public void Drawfood(float x, float y, float width, float height){
			if (foodFingerMove) {
	        	if(bowlstate<3 && timeeat==0 && x>width*2/16 && x<width*2/16+bowl[0].getWidth() &&y>height*6/11 && y<height*6/11+bowl[0].getHeight()){
					can.save();
	        		can.rotate(-40, width*3/16, height*4/7);
					can.drawBitmap(food, width*1/16, height*6/11, color);
					can.restore();
					bowlstate=3;
					petstate=1;
					timeeat=150;
				}else if(bowlstate==3 || (x==-1 && y==-1)){
					can.drawBitmap(food, 0, height*5/6, color);
				}else{
					can.drawBitmap(food, x-food.getWidth()/2, y-food.getHeight()/2, color);
				}
	        	
	        }else{
	        	can.drawBitmap(food, 0, height*5/6, color);
	        }	
		}
		
		
		public void Drawsoap(float x, float y, float width, float height){
			if (soapFingerMove && (x!=-1 && y!=-1)) {
	        	if(x>width/2-tento.getWidth()*1/3 && x<width/2+tento.getWidth()*2/3 && y>height/2 - tento.getHeight()*4/7 && y<height/2 + tento.getHeight()*4/7){
	        		can.drawBitmap(soap, x-soap.getWidth()/2, y-soap.getHeight()/2, color);
	        		cleanning=true;
				}else{
					can.drawBitmap(soap, x-soap.getWidth()/2, y-soap.getHeight()/2, color);
					if(!(Math.sqrt(((int)x-(int)posbubblesx)^2+((int)y-(int)posbubblesy)^2)>width/50 || Math.sqrt(((int)x-(int)posbubblesx)^2+((int)y-(int)posbubblesy)^2)>height/70 ))
		    			cleanning = false;
				}
	        	
	        }else{
	        	can.drawBitmap(soap, width/2, height*5/6, color);
	        }
		} 
		
		public void Drawtouchingpet(float center_x, float center_y, float x, float y){
			if(cleanning){
				can.drawBitmap(eyespooping, center_x-eyespooping.getWidth()*1/3, 
						center_y - eyespooping.getHeight()*4/7, color);
				
				Createbubbles(x,y,center_x*2, center_y*2);
				if(cleantime%10==0){
					sendNonOpenEyes("ShameEyes");
					Drawclean(center_x, center_y);
				}
				cleantime++;
			}else if(petFingerMove){
				can.drawBitmap(eyeshappy, center_x-eyeshappy.getWidth()*1/3, 
						center_y - eyeshappy.getHeight()*4/7, color);
			}else{
				if(eyesposition==0){
					can.drawBitmap(eyesnormal, center_x-eyesnormal.getWidth()*1/3, 
							center_y - eyesnormal.getHeight()*4/7, color);
					if(timeeyes==70){
						eyesposition=1;
						timeeyes =0;
					}
					timeeyes++;
				}else if(eyesposition==1){
					can.drawBitmap(eyesclose, center_x-eyesclose.getWidth()*1/3, center_y - eyesclose.getHeight()*4/7, color);
					if(timeeyes==4){
						eyesposition=0;
						timeeyes=0;
					}
					timeeyes++;
				}
			}
		}
		
		public void Createbubbles(float x, float y, float width, float height){//TODO :D
			if(Math.sqrt(((int)x-(int)posbubblesx)^2+((int)y-(int)posbubblesy)^2)>width/50 || Math.sqrt(((int)x-(int)posbubblesx)^2+((int)y-(int)posbubblesy)^2)>height/70 ){
				posbubblesx=x;
				posbubblesy=y;
				for(int i=0; i<3; i++){
					int rand1 = (int) (Math.random() * 10)+1;
					int rand2 = (int) (Math.random() * 10)+1;
					int dirx = (int) Math.floor(Math.random() * (1 + 1)) - 1;
					int diry = (int) Math.floor(Math.random() * (1 + 1)) - 1;
					if(dirx==0)
						dirx++;
					if(diry==0)
						diry++;
					Log.e("Draw","dirx: " + dirx +", diry: "+diry+ " rand: "+ rand1);
					can.drawBitmap(bubbles[i], (x -(bubbles[i].getWidth()/2))+dirx*rand1*width/80, 
							(y - (bubbles[i].getHeight()/2))+diry*rand2*height/90, color);
					Bubbles bubble = new Bubbles(bubbles[i], (x -(bubbles[i].getWidth()/2))+dirx*rand1*width/80, (y - (bubbles[i].getHeight()/2))+diry*rand2*height/90, 90); 
					if(bubblesimgs !=null && bubblesimgs.size()>=50)
						bubblesimgs.remove(1);
					bubblesimgs.add(bubble);
				}
			}
		}
		
		public void Drawbubbles(ArrayList<Bubbles> bubblesimgs, float x , float y){
			 for(int i = 0;i<bubblesimgs.size();i++){
				can.drawBitmap(bubblesimgs.get(i).getBubbleimg(), bubblesimgs.get(i).getPosx(), bubblesimgs.get(i).getPosy(), color);
				if(bubblesimgs.get(i).getTime()>0){
					bubblesimgs.get(i).setTime(bubblesimgs.get(i).getTime()-1);
				}else{
					bubblesimgs.remove(i);
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
			

			while(running){
				//limit frame rate to max 60fps
				timeNow = System.currentTimeMillis();
                timeDelta = timeNow - timePrevFrame;
                if ( timeDelta < 32) {
                    try {
                        Thread.sleep(32 - timeDelta);
                    }
                    catch(InterruptedException e) {

                    }
                }
                timePrevFrame = System.currentTimeMillis();
                if(hold.getSurface().isValid())
				{
					can = hold.lockCanvas();
				
					
						canvas.Draw(can);
					
					hold.unlockCanvasAndPost(can);
				}
			}
			
		}
		public void startCanvas()
		{
			hold = canvas.getHolder();
			running = true;
			drawthread = new Thread(this);
			drawthread.start();
		}
		public void stopCanvas()
		{
			running = false;
			Boolean retry = true;
			while(retry)
			{
				try {
					
					if(drawthread != null)
					{
						drawthread.join();
						
					}
					retry = false;
					drawthread = null;
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
}
