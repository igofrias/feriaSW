package com.Phyrex.VIPeR;


import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

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
	
	Drawpet canvas;
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
		canvas = new Drawpet(getActivity());
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
    private class PetMessage
    {
    	int brick;
    	String messageType;
    	public PetMessage(int brick, String messageType)
    	{
    		this.brick = brick;
    		this.messageType = messageType;
    	}
    }
    void sendNonOpenEyes(List<PetMessage> mensajes)
    {
    	if(eyesOpen)
    	{
    		eyesOpen = false;
    		Log.d("sendNonOpenEyes","Sending messages");
    		if(((MainActivity)thisActivity).isConnected())
			{
				for(PetMessage mensaje:mensajes)
				{
					((MainActivity)thisActivity).getBTService().sendPetMessage(mensaje.brick, mensaje.messageType);
				}
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
				sendMiscAction("EatSound",0);
				sendMiscAction("StopMoveHead",1);
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
		         			Toast.makeText(thisActivity, "Logro Desbloqueado Juguetón", Toast.LENGTH_LONG).show();
		 				((StatesActivity)fragment1).playing();
	 			    	if(((MainActivity)thisActivity).isConnected()){
	 			    		((MainActivity)thisActivity).getBTService().sendPetMessage(1, "MoveTail");
	 			    	}
		 			}else{
		 				Toast.makeText(thisActivity, "no puedes molestar a la mascota mientras duerme", Toast.LENGTH_SHORT).show();
		 			}
		 			((MainActivity)thisActivity).launch_gameselect();
     			}	
			break;
			case 6://boton accion
				fragment1 = ((StatesActivity)getFragmentManager().findFragmentByTag("state"));
				PetActionManager petman = new PetActionManager(thisActivity,(StatesActivity)fragment1);
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
	
	private class Drawpet extends SurfaceView implements SurfaceHolder.Callback, Runnable
	{
		/*****
		 * Clase que maneja el dibujo de la mascora. Tiene un thread que llama a que
		 * se dibuje en el canvas
		 */
		//COLORES////////////
		Paint color;
		Paint colorgray;
		Paint colorblack;
		
		//FUNCIONAMIENTO/////
		SurfaceHolder hold;
		Canvas can;
		Thread drawthread;
		/////////DIBUJOS DE TENTOSAURIO
		//Cuerpos
		Bitmap tento;
		Bitmap tentosleeping;
		Bitmap tentobreath []= new Bitmap[2];
		Bitmap tentoeating;
		Bitmap tentoeatingtail;
		Bitmap tentoeatingtongue []= new Bitmap[3];
		Bitmap tentonopefront;
		Bitmap tentonopebody;
		Bitmap headnopel;
		Bitmap headnoper;
		Bitmap tail;
		//Objetos
		Bitmap food;
		Bitmap bowl[] = new Bitmap[4];
		Bitmap sleepicon[] = new Bitmap[2];
		Bitmap soap;
		Bitmap poo;
		Bitmap play;
		Bitmap action;
		Bitmap bubbles[] = new Bitmap[3];
		Bitmap dirt[]=new Bitmap[9];
		//Ojos y caras
		Bitmap eyesnormal;
		Bitmap eyespooping;
		Bitmap eyesclose;
		Bitmap eyeshappy;
		Bitmap eyesangry;
		
		//no estan implementados
		Bitmap eyessad;
		Bitmap eyeshungry;
		Bitmap eyescrying;
		Bitmap eyeslazy;
		Bitmap eyessick;
		
	
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
		int timetongue=0;
		int cleantime=0;
		int timeeyes=0;
		int timesleep=0;
		int timesleepbreath=0;
		int timeangry=0;
		int timenope=0;
		int nopecount=0;
		//posiciones y estados
		//burbujas
		float posbubblesx=0;
		float posbubblesy=0;
		int tailposition=0;
		int bowlstate=0;
		int eyesposition=0;	
		/**************
		 * 0 normal  -  1 eating -   2 sleeping - 3 sad
		 * 4 nope    -  5 angry  -   6 crying   - 7 hungry
		 * 8 sick    -  9 lazy   -   10 happy 
		 * 
		 * TODO
		 *************/
		int petstate=0;
		int dirtstate=0; //de 0 a 8 el numero de mugre D:
		
		//Measure frames per second.
	    long now;
	    int framesCount=0;
	    int framesCountAvg=0;
	    long framesTimer=0;
	    Paint fpsPaint=new Paint();	
	    boolean gameselect = false;
		ArrayList<Bubbles> bubblesimgs;	
	    
		public Drawpet(Context context) {
			
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
			tentobreath[0]=BitmapFactory.decodeResource(getResources(), 
					R.drawable.tentobreath1);
			tentobreath[1]=BitmapFactory.decodeResource(getResources(), 
					R.drawable.tentobreath2);
			tentoeating=BitmapFactory.decodeResource(getResources(),
					R.drawable.tentosaurioeating);
			tentoeatingtail=BitmapFactory.decodeResource(getResources(),
					R.drawable.tentoeatingtail);
			tentoeatingtongue[0]= BitmapFactory.decodeResource(getResources(),
					R.drawable.tentoeatingtongue);
			tentoeatingtongue[1]= BitmapFactory.decodeResource(getResources(),
					R.drawable.tentoeatingtongue2);
			tentoeatingtongue[2]= BitmapFactory.decodeResource(getResources(),
					R.drawable.tentoeatingtongue3);
			tentonopefront=BitmapFactory.decodeResource(getResources(),
					R.drawable.tentonopefront);
			tentonopebody=BitmapFactory.decodeResource(getResources(),
					R.drawable.tentonopebody);
			headnopel=BitmapFactory.decodeResource(getResources(),
					R.drawable.tentonopeheadr);
			headnoper=BitmapFactory.decodeResource(getResources(),
					R.drawable.tentonopeheadl);
			food = BitmapFactory.decodeResource(getResources(), 
					R.drawable.food);
			sleepicon[0]= BitmapFactory.decodeResource(getResources(), 
					R.drawable.dayicon);
			sleepicon[1] = BitmapFactory.decodeResource(getResources(), 
					R.drawable.nighticon);
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
			eyesangry = BitmapFactory.decodeResource(getResources(), 
					R.drawable.eyesangry);
			eyessad = BitmapFactory.decodeResource(getResources(), 
					R.drawable.eyessad);
			eyescrying = BitmapFactory.decodeResource(getResources(), 
					R.drawable.eyescrying);
			eyeshungry = BitmapFactory.decodeResource(getResources(), 
					R.drawable.eyeshungry);
			eyessick = BitmapFactory.decodeResource(getResources(), 
					R.drawable.eyessick);
			eyeslazy = BitmapFactory.decodeResource(getResources(), 
					R.drawable.eyeslazy);
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
				switch (e.getAction()) {
					case MotionEvent.ACTION_DOWN:
						if(!sleeping && timeeat==0){
							if(!soapFingerMove && touchX>0 && touchX<food.getWidth() && touchY>height*5/6 && touchY<height*5/6+food.getHeight()){
								update_coordinates(touchX, touchY);//comida
								foodFingerMove = true;
							}else if(!foodFingerMove && !soapFingerMove && touchX>width/2-tento.getWidth()*1/3 && touchX<width/2+tento.getWidth()*1/3 && touchY>height/2 - tento.getHeight()*4/7 && touchY<height/2 + tento.getHeight()/7){
								update_coordinates(touchX, touchY);//tocar mascota
								petFingerMove = true;
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
				    	if(!sleeping && timeeat==0 && !soapFingerMove && !foodFingerMove && touchX>width/2-tento.getWidth()*1/3 && touchX<width/2+tento.getWidth()*1/3 && touchY>height/2 - tento.getHeight()*4/7 && touchY<height/2 + tento.getHeight()*4/7){
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
			if(timeeat==0 && !soapFingerMove && !foodFingerMove && touchX>width/4 && touchX<width/4+sleepicon[0].getWidth() && touchY>height*5/6 && touchY<height*5/6+sleepicon[0].getHeight()){
				switch (e.getAction()) {
				case MotionEvent.ACTION_DOWN:
					if(petstate==2){
						petstate=0;
						sleeping=false;
							
						Actions(2);//despertar
					}else{
						SherlockFragment fragment = ((StatesActivity)getFragmentManager().findFragmentByTag("state"));
						if(!((StatesActivity)fragment).isFullSleep()){
							petstate=2;
							sleeping=true;
							timesleep=90;
							timesleepbreath=80;
							Actions(2);//accion dormir
						}else{
							petstate=4;//enojado
							((StatesActivity)fragment).actionwhenfull();
						}
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
					SherlockFragment fragment = ((StatesActivity)getFragmentManager().findFragmentByTag("state"));
					if(((StatesActivity)fragment).getEnergylevel()>1){
						Actions(5);//accion de jugar
					}else{
						petstate=4;//enojado
						((StatesActivity)fragment).actionwhenfull();
					}
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
			if(bowlstate==0){
				can.drawBitmap(bowl[0], width*1/16, height*5/8, color);
			}else if(bowlstate==1){
				can.drawBitmap(bowl[1], width*1/16, height*5/8, color);
			}else if(bowlstate==2){
				can.drawBitmap(bowl[2], width*1/16, height*5/8, color);
			}else if(bowlstate==3){
				can.drawBitmap(bowl[3], width*1/16, height*5/8, color);
			}
			if(petstate==0 || petstate==3 || (petstate>4 && petstate<11)){//si la masta en estado normal
				
				Drawtail(center_x, center_y, width, height);
				canvas.drawBitmap(tento, center_x-tento.getWidth()*1/3, 
						center_y - tento.getHeight()*4/7, color);
				Drawtouchingpet(center_x,center_y, x , y);
				Drawdirt(center_x,center_y, canvas);
			}else if(petstate==2){
				Drawsleeping(center_x,center_y);
			}else if(petstate==1){//comiendo
				//sendNonOpenEyes("HappyEyes");
				LinkedList<PetMessage> messageList = new LinkedList<PetMessage>();
				messageList.add(new PetMessage(0,"HappyEyes"));
				messageList.add(new PetMessage(1,"StartMoveHead"));
				sendNonOpenEyes(messageList);
				//TODO
				Drawtaileating(center_x,center_y, x , y);
				can.drawBitmap(tentoeating, center_x-tentoeating.getWidth()/2, 
						center_y - tentoeating.getHeight()*4/7, color);
				Draweating(width, height, center_x, center_y);
				DrawTongue(center_x,center_y);
				Drawdirt(center_x,center_y, canvas);
			}else if(petstate==4){
				Drawnope(center_x,center_y, x , y);
				Drawdirt(center_x,center_y, canvas);
			}
			//Measure frame rate (unit: frames per second).
	         now=System.currentTimeMillis();
	         canvas.drawText(framesCountAvg+" fps", 40, 70, fpsPaint);
	         framesCount++;
	         if(now-framesTimer>1000) {
	                 framesTimer=now;
	                 framesCountAvg=framesCount;
	                 framesCount=0;
	         }
	        if(petstate==2){
	        	canvas.drawBitmap(sleepicon[0], width/4, height*5/6, color);
			}else{
				canvas.drawBitmap(sleepicon[1], width/4, height*5/6, color);
			}
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
			Changestate();
		}
		
		public void Drawsleeping(float center_x, float center_y){ //TODO
			can.drawBitmap(tentosleeping, center_x-tentosleeping.getWidth()/2, 
					center_y - tentosleeping.getHeight()*2/7, color);
			DrawBreath(center_x,center_y);
			Drawdirt(center_x,center_y, can);
			can.drawARGB(150, 0, 0, 0);
			DrawZetas(center_x*2,center_y*2);
			
		}
		
		public void Changestate(){
			SherlockFragment fragment = ((StatesActivity)getFragmentManager().findFragmentByTag("state"));
			
				if(petstate!=2 && petstate!=1 && petstate!=4){
					if (((StatesActivity)fragment).getHungrylevel()<20){
						petstate=7;//Hungry
					}else if (((StatesActivity)fragment).getHealthlevel()<20){
						petstate=8;//Sick
					}else if (((StatesActivity)fragment).getEnergylevel()<20){
						petstate=9;//Lazy
					}else if (((StatesActivity)fragment).getHapinesslevel()<15){
						petstate=6;//Crying
					}else if (((StatesActivity)fragment).getHapinesslevel()<35){
						petstate=3;//Sad
					}else if(((StatesActivity)fragment).getHapinesslevel()>90){
						petstate=10;//Happy
					}else{
						petstate=0;//Normal
					}
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
		
		public void DrawBreath(float center_x, float center_y){//TODO
			if(timesleepbreath>60){
				timesleepbreath--;
			}else if(timesleepbreath>40 || timesleepbreath<=20){
				can.drawBitmap(tentobreath[0], center_x-tentobreath[0].getWidth()*955/2048, 
						center_y - tentobreath[0].getHeight()*519/896, color);
				timesleepbreath--;
				if (timesleepbreath==0)
					timesleepbreath=80;
			}else if(timesleepbreath>20){
				can.drawBitmap(tentobreath[1], center_x-tentobreath[1].getWidth()*469/1024, 
						center_y - tentobreath[1].getHeight()*519/896, color);
				timesleepbreath--;
				
			}
		}
		
		public void Dopoop(){
			poop=true;
			Runnable shameFace = new Runnable()
			{

				@Override
				public void run() {
					
					sendNonOpenEyes("ShameEyes");
				}
				
			};
			Handler h = new Handler();
			h.postDelayed(shameFace, 10);
			sendMiscAction("FartSound",1);	
			Actions(7); //hacer caca
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
			}else if(dpi==2.4){
				if(timesleep>60){
					textSize=56;
				}else if(timesleep>30){
					textSize=112;
				}else{
					textSize=168;
				}
			}
			else if(dpi==3.6){
				if(timesleep>60){
					textSize=84;
				}else if(timesleep>30){
					textSize=168;
				}else{
					textSize=252;
				}
			}
			return textSize;
		}
		
		public void Drawdirt(float center_x, float center_y, Canvas canvas){
			int i=0;
			float desx=0;
			float desy=0;
			if(dirtstate>0){
				do{
					switch(i){
						case 0:
							if(petstate==0 || petstate==3 || (petstate>4 && petstate<11)){
								desx=(float)-3/8;//pecho
								desy=(float)5/10;
							}else if(petstate==2){
								desx=(float)12/8;//guata2
								desy=(float)5/8;
							}else if(petstate==1){
								desx=(float)8/8;//guata2
								desy=(float)5/8;
							}
						break;
						
						case 1:
							if(petstate==0 || petstate==3 || (petstate>4 && petstate<11)){
								desx=(float)-1;//cara
								desy=(float)21/10;
							}else if(petstate==2){
								desx=(float)3;//cara
								desy=(float)-23/10;
							}else if(petstate==1){
								desx=(float)17/4;//cara bajo ojo
								desy=(float)-14/10;
							}
							
						break;
						
						case 2:
							if(petstate==0 || petstate==3 || (petstate>4 && petstate<11)){
								desx=(float)1/10;//guata baja entre pierna
								desy=(float)-6/4;
							}else if(petstate==2){
								desx=(float)9/10;//
								desy=(float)-9/10;
							}else if(petstate==1){
								desx=(float)-19/10;//
								desy=(float)-14/10;
							}
							
						break;
						
						case 3:
							if(petstate==0 || petstate==3 || (petstate>4 && petstate<11)){
								desx=(float)5/8;//guata2
								desy=(float)0;
							}else if(petstate==2){
								desx=(float)1/8;//pecho
								desy=(float)-3/10;
							}else if(petstate==1){
								desx=(float)1/16;//pecho
								desy=(float)-6/10;
							}
						break;
						
						case 4:
							if(petstate==0 || petstate==3 || (petstate>4 && petstate<11)){
								desx=(float)6/8;//ojos
								desy=(float)13/8;
							}else if(petstate==2){
								desx=(float)17/8;//ojos
								desy=(float)-5/8;
							}else if(petstate==1){
								desx=(float)20/8;//ojos
								desy=(float)-1/16;
							}
						break;
						
						case 5:
							if(petstate==0 || petstate==3 || (petstate>4 && petstate<11)){
								desx=(float)-27/8;//cola
								desy=(float)13/8;
							}else if(petstate==2){
								desx=(float)-16/16;//cola
								desy=(float)-43/16;
							}else if(petstate==1){
								desx=(float)-65/16;//cola
								desy=(float)43/16;
							}
						break;
						
						case 6:
							if(petstate==0 || petstate==3 || (petstate>4 && petstate<11)){
								desx=(float)-10/16;//medi espalda guata
								desy=(float)-7/8;
							}else if(petstate==2){
								desx=(float)-15/16;//cola
								desy=(float)4/16;
							}else if(petstate==1){
								desx=(float)-38/32;//cola
								desy=(float)7/32;
							}
						break;
						
						case 7:
							if(petstate==0 || petstate==3 || (petstate>4 && petstate<11)){
								desx=(float)23/16;//pata
								desy=(float)-16/8;
							}else if(petstate==2){
								desx=(float)-5/16;//espalda media
								desy=(float)15/16;						
							}else if(petstate==1){
								desx=(float)-9/16;//espalda media
								desy=(float)8/16;
							}	
						break;
						
						case 8:
							if(petstate==0 || petstate==3 || (petstate>4 && petstate<11)){
								desx=(float)-25/16;//cola2
								desy=(float)1/8;
							}else if(petstate==2){
								desx=(float)-35/16;//cola
								desy=(float)-15/16;								
							}else if(petstate==1){
								desx=(float)-36/16;//cola
								desy=(float)17/16;	
							}
						break;
						
						default:
							desx=100;
							desy=100;
						break;

					}
					//Log.e("dirt","desy= "+desy + " desx=" + desx + " i= "+ i);
					canvas.drawBitmap(dirt[i], center_x-(dirt[i].getWidth()*desx), 
							center_y - (dirt[i].getHeight()*desy), color);
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
				can.drawBitmap(tail, center_x+tail.getWidth()*6/7, center_y - tail.getHeight()*5/7, color);
			}else{
				if(tailposition==0 || tailposition==-2 || tailposition==2){
					can.drawBitmap(tail, center_x+tail.getWidth()*6/7, center_y - tail.getHeight()*5/7, color);
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
					can.rotate(5, (center_x*2)*13/16, center_y);
					can.drawBitmap(tail, center_x+tail.getWidth()*6/7, center_y - tail.getHeight()*5/7, color);
					can.drawCircle((center_x*2)*13/16, center_y, 10, color);
					if(timetail==4){
						tailposition=2;
						timetail=0;
					}
					timetail++;
					can.restore();
				}else if(tailposition==-1){
					can.save();
					can.rotate(-5, (center_x*2)*13/16, center_y);
					can.drawBitmap(tail, center_x+tail.getWidth()*6/7, center_y - tail.getHeight()*5/7, color);
					if(timetail==4){
						tailposition=-2;
						timetail=0;
					}
					timetail++;
					can.restore();
				}
						
			}
		}
		
		//TODO 
		public void Drawnope(float center_x, float center_y, float width, float height){
			if(tailposition==0 || tailposition==-2 || tailposition==2){
				can.drawBitmap(tentonopefront, center_x-tentonopefront.getWidth()*1/3, 
							center_y - tentonopefront.getHeight()*4/7, color);
				if(timenope==6){
					if(tailposition==-2){
						tailposition=1;
						timenope =0;
						nopecount++;
					}else{
						tailposition=-1;
						timenope=0;
					}
				}
				timenope++;
				
			}else if(tailposition==1){
				can.drawBitmap(tentonopebody, center_x-tentonopebody.getWidth()*1/3, 
						center_y - tentonopebody.getHeight()*4/7, color);
				can.drawBitmap(headnoper, center_x-headnoper.getWidth()*50/104, 
						center_y - headnoper.getHeight()*452/480, color);
				if(timenope==6){
					tailposition=2;
					timenope=0;
				}
				timenope++;
				can.restore();
			}else if(tailposition==-1){
				can.drawBitmap(tentonopebody, center_x-tentonopebody.getWidth()*1/3, 
						center_y - tentonopebody.getHeight()*4/7, color);
				can.drawBitmap(headnopel, center_x-headnopel.getWidth()*26/104, 
						center_y - headnopel.getHeight()*451/480, color);				
				if(timenope==6){
					tailposition=-2;
					timenope=0;
				}
				timenope++;
			}
			if(nopecount==3){
				petstate=0;
				nopecount=0;
			}
		}
		
		public void Draweating(float width, float height, float center_x, float center_y){
			//masticando
			if(timeeat>80){
				bowlstate=3;
				timeeat--;
			}else if(timeeat>30){
				bowlstate=2;
				timeeat--;
			}else if(timeeat>0){
				bowlstate=1;
				timeeat--;
			}else if(timeeat==0){
				bowlstate=0;
				petstate=0;
				Actions(1);//uno para comer
			}
		}
		
		public void Drawtaileating(float center_x, float center_y, float width, float height){
			if(tailposition==0 || tailposition==-2 || tailposition==2){
				can.drawBitmap(tentoeatingtail, center_x+tentoeatingtail.getWidth()*8/7, center_y - tentoeatingtail.getHeight()*6/7, color);
				if(timetail==3){
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
				can.rotate(5, (center_x*2)*13/16, center_y);
				can.drawBitmap(tentoeatingtail, center_x+tentoeatingtail.getWidth()*8/7, center_y - tentoeatingtail.getHeight()*6/7, color);
				if(timetail==3){
					tailposition=2;
					timetail=0;
				}
				timetail++;
				can.restore();
			}else if(tailposition==-1){
				can.save();
				can.rotate(-5, (center_x*2)*13/16, center_y);
				can.drawBitmap(tentoeatingtail, center_x+tentoeatingtail.getWidth()*8/7, center_y - tentoeatingtail.getHeight()*6/7, color);
				if(timetail==3){
					tailposition=-2;
					timetail=0;
				}
				timetail++;
				can.restore();
			}
		}
		
		public void DrawTongue(float center_x, float center_y){//TODO
			if(timetongue>9){
				can.drawBitmap(tentoeatingtongue[2], center_x-tentoeatingtongue[2].getWidth()*24/4, 
						center_y + tentoeatingtongue[2].getHeight()*63/8, color);
				timetongue--;
			}else if(timetongue>6 || timetongue<=3){
				can.drawBitmap(tentoeatingtongue[0], center_x-tentoeatingtongue[0].getWidth()*50/8, 
						center_y + tentoeatingtongue[0].getHeight()*50/8, color);
				
				if (timetongue==0)
					timetongue=12;
				timetongue--;
			}else if(timetongue>3){
				can.drawBitmap(tentoeatingtongue[1], center_x-tentoeatingtongue[1].getWidth()*39/8, 
						center_y + tentoeatingtongue[1].getHeight()*63/8, color);
				timetongue--;
				
			}
		}
		
		public void Drawfood(float x, float y, float width, float height){
			if (foodFingerMove) {
	        	if(bowlstate<3 && timeeat==0 && x>width*2/16 && x<width*2/16+bowl[0].getWidth() &&y>height*6/11 && y<height*6/11+bowl[0].getHeight()){
					can.save();
	        		can.rotate(-40, width*3/16, height*4/7);
					can.drawBitmap(food, width*1/16, height*6/11, color);
					can.restore();
					SherlockFragment fragment = ((StatesActivity)getFragmentManager().findFragmentByTag("state"));
					if(!((StatesActivity)fragment).isFull()){
						bowlstate=3;
						petstate=1;
						timeeat=150;
						timetail=0;
						timetongue=12;
					}else{
						petstate=4;//enojado
						//timeangry=60;
						((StatesActivity)fragment).actionwhenfull();
					}
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
				can.drawBitmap(eyespooping, center_x-eyespooping.getWidth()*4/10, 
						center_y - eyespooping.getHeight()*15/7, color);
				
				Createbubbles(x,y,center_x*2, center_y*2);
				if(cleantime%10==0){
					sendNonOpenEyes("ShameEyes");
					Drawclean(center_x, center_y);
				}
				cleantime++;
			}else if(petFingerMove || petstate==10){
				can.drawBitmap(eyeshappy, center_x-eyeshappy.getWidth()*4/10, 
						center_y - eyeshappy.getHeight()*29/4, color);
			}else if(petstate==7){
				can.drawBitmap(eyeshungry, center_x-eyeshungry.getWidth()*4/10, 
						center_y - eyeshungry.getHeight()*9/8, color);
			}else if(petstate==8){
				can.drawBitmap(eyessick, center_x-eyessick.getWidth()*7/20, 
						center_y - eyessick.getHeight()*19/16, color);
			}else{
				if(eyesposition==0){
					if(petstate==0){
						can.drawBitmap(eyesnormal, center_x-eyesnormal.getWidth()*4/10, 
								center_y - eyesnormal.getHeight()*24/7, color);
					}else if(petstate==9){
						can.drawBitmap(eyeslazy, center_x-eyeslazy.getWidth()*4/10, 
								center_y - eyeslazy.getHeight()*25/8, color);
					}else if(petstate==5){
						can.drawBitmap(eyesangry, center_x-eyesangry.getWidth()*4/10, 
								center_y - eyesangry.getHeight()*21/8, color);
					}
					else if(petstate==3){
						can.drawBitmap(eyessad, center_x-eyessad.getWidth()*4/10, 
								center_y - eyessad.getHeight()*60/16, color);
					}else if(petstate==6){
						can.drawBitmap(eyescrying, center_x-eyescrying.getWidth()*4/10, 
								center_y - eyescrying.getHeight()*17/8, color);
					}//TODO
					if(timeeyes==70){
						eyesposition=1;
						timeeyes =0;
					}
					timeeyes++;
				}else if(eyesposition==1){
					can.drawBitmap(eyesclose, center_x-eyesclose.getWidth()*4/10, center_y - eyesclose.getHeight()*46/3, color);
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
                if ( timeDelta < 60) {
                    try {
                        Thread.sleep(60 - timeDelta);
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
