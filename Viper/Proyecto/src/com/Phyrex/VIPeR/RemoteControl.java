package com.Phyrex.VIPeR;


import java.io.IOException;
import java.util.Timer;

import com.actionbarsherlock.app.SherlockFragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.BitmapFactory;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.Toast;
//Maneja el control remoto usando acelerometro
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class RemoteControl extends SherlockFragment implements SensorEventListener{
	private int MAX_RANGE;
	private double MAX_VEL;
	private Sensor accelerometer;
	SensorManager manager;
	double raw_accel_x = 0;
	double raw_accel_y = 0;
	double raw_accel_z = 0;
	double accel_x = 0;
	double accel_y = 0;
	double accel_z = 0;
	double gravity[];
    boolean running=true;
	double vel_x = 0;
	double vel_y = 0;
	double vel_z = 0;
	int vel_robot_x = 0;
	int vel_robot_y = 0;
	int vel_robot_z = 0;
	double sens;
	Runnable messegerRunnable;
	Thread thMesseger;
	//PLAYMODE
	boolean playmode;
	DrawJoystick canvas;
	Activity parent_activity;
	MainActivity thisActivity;
	
	public void setPlaymode(boolean value){
		playmode=value;
	}
	
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
	    
	    
	    manager =(SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
		accelerometer = manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		gravity = new double[3];
		MAX_RANGE = 100;
		MAX_VEL = 10;
		sens = 0.8;
		canvas.setWillNotDraw(false);
	    return canvas;
	  }	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		thisActivity = (MainActivity) getActivity();
		messegerRunnable = new Runnable() {
			public void run() {
				long wait_time = 100;
				while(running)
				{
					if(thisActivity.isConnected())
					{
						send_speeds();
					}
					else
					{
						running = false;
						thisActivity.detach_remotecontrol();
						thisActivity.launch_mainpet();
					}
					SystemClock.sleep(wait_time);

				}
				
			}
		};
		thMesseger = new Thread(messegerRunnable);

	}
	public void process_aceleration()
	{
		//Ocupa los filtros indicados en el tutorial para dejar una aceleracion
		//pura sin influencia de la gravedad.
		//alpha  = t/(dt + t) t=constante del filtro dt=event delivery rate
		double alpha = 0.8;
		for (int i = 0; i < 3; i++)
		{
			gravity[i] = 0;
		}
		  // Isolate the force of gravity with the low-pass filter.
		  gravity[0] = alpha * gravity[0] + (1 - alpha) * raw_accel_x;
		  gravity[1] = alpha * gravity[1] + (1 - alpha) * raw_accel_y;
		  gravity[2] = alpha * gravity[2] + (1 - alpha) * raw_accel_z;

		  // Remove the gravity contribution with the high-pass filter.
		  accel_x = raw_accel_x - gravity[0];
		  accel_y = raw_accel_y - gravity[1];
		  accel_z = raw_accel_z - gravity[2];
		
	}
	public int trans_vel_a_robot(double vel)
	{
		//Transforma una velocidad de celular a perro
		//Velocidad del robot proporcional a rango de movimiento 
		double k =(double)MAX_RANGE/MAX_VEL;
		int vel_robot = (int) (Math.ceil(vel*sens*k));
		if(vel_robot > MAX_RANGE)
		{
			vel_robot = MAX_RANGE;
		}
		else if (vel_robot < -MAX_RANGE)
		{
			vel_robot = -MAX_RANGE;
		}
		return vel_robot;
				
	}
	public void speeds_to_robot()
	{
		//Transforma los ejes x e y en velocidades por motor
		//Sacado del codigo de minddroids
		boolean direccion = true; //true == arriba acostado; false == derecho
		if (!direccion)
		{
			double temp_x = vel_x;
			double temp_y = vel_y;
			vel_x = temp_y;
			vel_y = temp_x;
		}
		if (vel_x > MAX_VEL){
			vel_x = (double) MAX_VEL;
		}else if (vel_x < -MAX_VEL){
			vel_x = (double) -MAX_VEL;}

		if (vel_y > MAX_VEL){
			vel_y = (float) MAX_VEL;
		}else if (vel_y < -MAX_VEL){
			vel_y = (float) -MAX_VEL;
		}
		double k = (double)MAX_RANGE/MAX_VEL;
		if (Math.abs(vel_x) > 1) {
			vel_robot_x = (int) Math.round(k * vel_x * (1.0 + vel_y / 60.0));
			vel_robot_y = (int) Math.round(k * vel_x * (1.0 - vel_y / 60.0));
		} else {
			vel_robot_x = (int) Math.round(k * vel_y - Math.signum(vel_y) * k * Math.abs(vel_x));
			vel_robot_y = -vel_robot_x;
		}
		if(vel_robot_x > MAX_RANGE)
		{
			vel_robot_x = MAX_RANGE;
		}
		else if (vel_robot_x < -MAX_RANGE)
		{
			vel_robot_x = -MAX_RANGE;
		}
		if(vel_robot_y > MAX_RANGE)
		{
			vel_robot_y = MAX_RANGE;
		}
		else if (vel_robot_y < -MAX_RANGE)
		{
			vel_robot_y = -MAX_RANGE;
		}
		
	}
	public void get_speed()
	{
		//A partir de la aceleracion obtiene la velocidad a mover
		vel_x = accel_x;
		vel_y = accel_y;
		vel_z = accel_z;
		
				//vel_robot=MAX_RANGE*vel/MAX_VEL
//		vel_robot_x = this.trans_vel_a_robot(vel_x);
//		vel_robot_y = this.trans_vel_a_robot(vel_y);
//		vel_robot_z = this.trans_vel_a_robot(vel_z);
		speeds_to_robot();
	}
	public float bar_percentage(int bar_range, double max_vel, double vel)
	{
		//Ve cual es el porcentaje de barra que debe tener un valor dado
		float percentage = (float) Math.ceil((50.0*vel)/max_vel) + 50;
		return percentage;
	}
	public float bar_percentage(int bar_range, float center, double max_vel, double vel)
	{
		//Ve el porcentaje que se tiene que mover, esta vez con un centro explicito
		float percentage = (float)Math.ceil(((bar_range-center)*vel)/max_vel) + center;
		return percentage;
	}
	public void reset_speeds()
	{
		vel_x = 0;
		vel_y = 0;
		vel_z = 0;
	}
	
	public void send_speeds()
	{
		
		//Envia los datos obtenidos al robot.
		if(thisActivity.isConnected())
		{
			
				
			//Asumiendo que motor b y c sean los motores de las ruedas.
			int motorActionb = BTCommunicator.MOTOR_A;
		    int motorActionc = BTCommunicator.MOTOR_C;
			
			thisActivity.sendBTCmessage(BTCommunicator.NO_DELAY, motorActionb,vel_robot_x , 0);
			thisActivity.sendBTCmessage(1000, motorActionb, 0, 0);
			thisActivity.sendBTCmessage(BTCommunicator.NO_DELAY, motorActionc, vel_robot_y, 0);
			thisActivity.sendBTCmessage(1000, motorActionc, 0, 0);
			
		}
		else
		{
			//Sale del control remoto. TODO
			//thisActivity.detach_remotecontrol();
			//thisActivity.launch_mainpet();
			return;
		}
		
	}
	@Override
	public final void onAccuracyChanged(Sensor sensor, int accuracy) {
	    // Do something here if sensor accuracy changes.
	}

	@Override
	public final void onSensorChanged(SensorEvent event) {
		if (event.sensor.getType() != Sensor.TYPE_ACCELEROMETER)
            return;
		//TODO
		if(!playmode  || (playmode && canvas.inplay)){
		    //Cada vez que cambia el sensor se reciben los valores y se procesan.
		    raw_accel_x = event.values[0];
		    raw_accel_y = event.values[1];
		    raw_accel_z = event.values[2];
		    
		    process_aceleration();
		    get_speed();
		    canvas.update_coordinates(this.vel_x, this.vel_y);
		}
	}
	
	@Override
	public void onResume() {
		super.onResume();
		running=true;
		manager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME);
		if(thMesseger != null)
		{
			if(!thMesseger.isAlive())
		
			{
				thMesseger.start();
			}
		}
		else
		{
			thMesseger = new Thread(messegerRunnable);
			canvas.startCanvas();
		}
		//getFragmentManager().beginTransaction().remove(getFragmentManager().findFragmentById(R.id.control_remoto_debug)).commit();
	}
	
	@Override
	public void onPause() {
		super.onPause();
		if(playmode){
			if(!canvas.release){
				if(((MainActivity)thisActivity).isConnected()){
					((MainActivity)thisActivity).startProgram("ReleaseBall.rxe");//TODO
					waitRelease();	
				}
			}
				if(((MainActivity)thisActivity).isConnected())
					((MainActivity)thisActivity).startProgram("CloseClamps.rxe");
			
		}
		manager.unregisterListener(this);
		running=false;
		try {
			if(thMesseger != null)
			{
				thMesseger.join();
			}
			canvas.stopCanvas();
			thMesseger = null;
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
        @Override
	public void onDetach(){
		super.onDetach();
		manager.unregisterListener(this);
		running = false;
		try {
			if(thMesseger != null)
			{
				thMesseger.join();
			}
			canvas.stopCanvas();
			thMesseger = null;
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
        
    public void waitRelease(){
    	long  time = System.currentTimeMillis();
    	long timePrev=0;
    	long timeDelta=0;
    	boolean flag=true;
    	while(flag){
    		timePrev = System.currentTimeMillis();
	        timeDelta = timePrev - time;
	        if ( timeDelta > 2000) {
	           flag=false;
	        }
    	}
    }   
        
	public int getMAX_RANGE() {
		return MAX_RANGE;
	}
	public void setMAX_RANGE(int mAX_RANGE) {
		MAX_RANGE = mAX_RANGE;
	}
	public double getMAX_VEL() {
		return MAX_VEL;
	}
	public void setMAX_VEL(double mAX_VEL) {
		MAX_VEL = mAX_VEL;
	}
	public void setAcelerometro(Sensor acelerometro) {
		this.accelerometer = acelerometro;
	}
	public void setManager(SensorManager manager) {
		this.manager = manager;
	}
	public void startMessenger()
	{
		this.thMesseger.start();
	}
	
	private class DrawJoystick extends SurfaceView implements SurfaceHolder.Callback, Runnable
	{
		//Clase que maneja el dibujo del joystick. Tiene un thread que llama a que
		//se dibuje en el canvas
		Double vel_x;
		Double vel_y;
		Paint color;
		Paint color_center;
		SurfaceHolder hold;
		Canvas can;
		Thread drawthread;
		Boolean running;
		Bitmap centro;
		Bitmap circulo;
		
		///////////Play Mode ///////////
		Bitmap ballb;
		Bitmap ballr;
		Bitmap ballnext;
		Bitmap buttonclose;
		Bitmap buttonopen;
		int pincersstate;
		boolean inplay;
		boolean catchball;
		boolean release;
		int score;
		int ballcolor;

		/////manejo de tiempo //////
		int count=0;
		int totalTime= 5000;
		int timeLeft= totalTime;
		float lefttimeprogress;
			
		public DrawJoystick(Context context) {
			
			super(context);
			color = new Paint();
			getHolder().addCallback(this);
			color.setColor(Color.GREEN);
			color_center = new Paint();
			color_center.setColor(Color.GRAY);
			vel_x = 0.0;
			vel_y = 0.0;
			running = false;
			centro = BitmapFactory.decodeResource(getResources(), 
					R.drawable.remotecontrolbackground);
			circulo = BitmapFactory.decodeResource(getResources(), 
					R.drawable.pointremotecontrol);
			///////////Modo play /////////////////
			ballb = BitmapFactory.decodeResource(getResources(), 
					R.drawable.ballblue);
			ballr = BitmapFactory.decodeResource(getResources(), 
					R.drawable.ballred);
			buttonclose = BitmapFactory.decodeResource(getResources(), 
					R.drawable.closepincers);
			buttonopen = BitmapFactory.decodeResource(getResources(), 
					R.drawable.openpincers);
			nextball();
			pincersstate=0;
			inplay=false;
			catchball=false;
			score=0;
			
			// TODO Auto-generated constructor stub
		}

		@Override
		public void surfaceChanged(SurfaceHolder holder, int format, int width,
				int height) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void surfaceCreated(SurfaceHolder holder) {
			// TODO Auto-generated method stub
			
			startCanvas();
			if(playmode)
				playmodedialog();
		}

		@Override
		public void surfaceDestroyed(SurfaceHolder holder) {
			// TODO Auto-generated method stub
			stopCanvas();
		}
		
		
		public void Draw(Canvas canvas) throws IOException
		{
			
			float center_x = canvas.getWidth()/2;
			float width = canvas.getWidth();
			float center_y = canvas.getHeight()/2;
			float height = canvas.getHeight();
			int size = 30;
			float x = bar_percentage(canvas.getWidth() - size, center_x, -MAX_VEL, vel_x);
			float y = bar_percentage(canvas.getHeight() - size, center_y,MAX_VEL, vel_y);
			canvas.drawARGB(255, 0, 0, 0);
			canvas.drawBitmap(centro, center_x-centro.getWidth()/2, 
					center_y - centro.getHeight()/2, color);
			canvas.drawBitmap(circulo, x-circulo.getWidth()/2,
					y-circulo.getHeight()/2, color);
			if(playmode){
				//seleccion de pelotas en funcion next ball
				//cerrar llamar a start program
				if(pincersstate==0){//cargar boton de las pinzas
					canvas.drawBitmap(buttonclose, center_x*5/16-buttonclose.getWidth()/2,
							center_y*2*13/16-buttonclose.getHeight()/2, color);
				}else{
					canvas.drawBitmap(buttonopen, center_x*5/16-buttonopen.getWidth()/2,
							center_y*2*13/16-buttonopen.getHeight()/2, color);
				}
				canvas.drawBitmap(ballnext, center_x*2*2/16-ballnext.getWidth()/2,
						center_y*2*2/16-ballnext.getHeight()/2, color);
				if(inplay){
					timecalc(height);
				}
				
				canvas.drawRect(width*13/14, 0, width , lefttimeprogress, color);
				insertTextObjetive(width, height);
				insertTextScore(width, height);
				insertTextScorevalue(width, height);
				//TODO
				if(!inplay /*&& count>0*/){
					insertTextgameover(width, height);
					//GAME OVER
				}
			}
			
		}
		
		public void insertTextObjetive(float width, float height){
			String text = "Objetivo:";
			int textColor = Color.GREEN;
			float textSize=0;
			textSize=TextSizedpi(textSize);
			can.save();
			 can.rotate(90, width*4/16, height/25);
			 Paint textPaint = new Paint();
			 textPaint.setAntiAlias(true);
			 textPaint.setColor(textColor);
			 textPaint.setTextSize(textSize);
			 Rect bounds = new Rect();
			 textPaint.getTextBounds(text, 0, text.length(), bounds);
			 can.drawText(text, width*4/16, height/25, textPaint);
			 can.restore();
			
		}
		
		public void insertTextgameover(float width, float height){
			String text = "GAME OVER";
			int textColor = Color.WHITE;
			float textSize=0;
			textSize=TextSizedpiGameover(textSize);
			can.save();
			 can.rotate(90, width/2, height/2);
			 Paint textPaint = new Paint();
			 textPaint.setAntiAlias(true);
			 textPaint.setColor(textColor);
			 textPaint.setTextSize(textSize);
			 Rect bounds = new Rect();
			 textPaint.getTextBounds(text, 0, text.length(), bounds);
			 can.drawText(text, width/2-bounds.centerX(), height/2-bounds.centerY(), textPaint);
			 can.restore();
			
		}
		
		public void insertTextScore(float width, float height){
			String text = "Puntaje:";
			int textColor = Color.GREEN;
			float textSize = 0;
			textSize=TextSizedpi(textSize);
			 can.save();
			 can.rotate(90, width*13/16, height/25);
			 Paint textPaint = new Paint();
			 textPaint.setAntiAlias(true);
			 textPaint.setColor(textColor);
			 textPaint.setTextSize(textSize);
			 Rect bounds = new Rect();
			 textPaint.getTextBounds(text, 0, text.length(), bounds);
			 can.drawText(text, width*13/16, height/25, textPaint);
			 can.restore();
			
		}
		
		public void insertTextScorevalue(float width, float height){
			String text = String.valueOf(score);
			int textColor = Color.GREEN;
			float textSize=0;
			textSize=TextSizedpiscore(textSize);
			can.save();
			 can.rotate(90, width*11/16, height/25);
			 Paint textPaint = new Paint();
			 textPaint.setAntiAlias(true);
			 textPaint.setColor(textColor);
			 textPaint.setTextSize(textSize);
			 Rect bounds = new Rect();
			 textPaint.getTextBounds(text, 0, text.length(), bounds);
			 can.drawText(text, width*11/16, height/25, textPaint);
			 can.restore();
			
		}
		
		public float TextSizedpi(float textSize){
			float dpi = getResources().getDisplayMetrics().density;
			if(dpi ==0.75){
				textSize=15;
			}else if(dpi==1){
				textSize=25;
			}
			else if(dpi==1.5){
				textSize=35;
			}
			return textSize;
		}
		public float TextSizedpiGameover(float textSize){
			float dpi = getResources().getDisplayMetrics().density;
			if(dpi ==0.75){
				textSize=50;
			}else if(dpi==1){
				textSize=70;
			}
			else if(dpi==1.5){
				textSize=90;
			}
			return textSize;
		}
		public float TextSizedpiscore(float textSize){
			float dpi = getResources().getDisplayMetrics().density;
			if(dpi ==0.75){
				textSize=25;
			}else if(dpi==1){
				textSize=35;
			}
			else if(dpi==1.5){
				textSize=45;
			}
			return textSize;
		}
		
		public void timecalc(float height){
            lefttimeprogress = (height*timeLeft)/totalTime;
		}
		
		public void score(){//manejo de puntaje
			if(catchball){
				score = (int)(300 + score + lefttimeprogress/100);
				catchball=false;
			}
		}
		
		public void validcatchball() throws IOException{
			//detenccion de sensor
			int sensorball=0;/*((MainActivity)thisActivity).recivemsg();/aqui esto debe ser igual a lo de cele :D*/
			//((MainActivity)thisActivity).sendMessageBTNumber(0, 201);
			Log.e("Mensaje Recibido", String.valueOf(sensorball));
			if (sensorball==ballcolor){
				catchball=true;
				score();
				nextball();
				totalTime=totalTime-300;
			}
		}
		
		public void exitgame(){
			running=false;
        	thisActivity.detach_remotecontrol();
			thisActivity.launch_mainpet();
		}
		
		public void playmodedialog(){
			AlertDialog.Builder dialog = new AlertDialog.Builder(thisActivity);  
	        dialog.setTitle("Tento pelota (?)");		//titulo (opcional)
	        dialog.setIcon(R.drawable.ic_launcher);		//icono  (opcional)
	        dialog.setMessage("Debes encontrar la pelota que te piden dentro del tiempo" +
	        		"utilizando el robot y sus pinzas.\n" +
	        		"¡Cuidado! debes encontrar las pelotas dentro del tiempo, entre mas" +
	        		" rapido las recojas, mas puntaje tendras!\n\n"
	        		); 
	        
	        
	        dialog.setNegativeButton("Salir", new DialogInterface.OnClickListener() {  //boton positivo (opcional)
	            public void onClick(DialogInterface dialogo1, int id) {  
	            	exitgame();
			    }
	        }); 
	        dialog.setPositiveButton("Jugar", new DialogInterface.OnClickListener() {  //boton positivo (opcional)
	            public void onClick(DialogInterface dialogo1, int id) {  
	            	//aceptar
	            	inplay=true;
	            	if(((MainActivity)thisActivity).isConnected()){
	    				((MainActivity)thisActivity).startProgram("OpenClamps.rxe");
	    				release = true;
	            	}
			    }
	        });
	        dialog.show();
	        
		}
		
		public void movepincers() throws IOException{//llama a cerrar las pinzas
			if(pincersstate==0){//cerrar
				pincersstate=1;
				if(((MainActivity)thisActivity).isConnected()){
    				((MainActivity)thisActivity).startProgram("CatchBall-NM.rxe");
    				///validcatchball();
    				release=false;
				}
			}else{//abrir TODO
				pincersstate=0;
				if(((MainActivity)thisActivity).isConnected()){
    				((MainActivity)thisActivity).startProgram("ReleaseBall.rxe");
    				release=true;
				}
			}
		}
		
		public void nextball(){//escoje una pelota al azar
			int rand = (int) (Math.random() * 2);
			if(rand==0){
				ballnext = ballr;
				ballcolor=5;
			}else{
				ballnext = ballb;
				ballcolor=2;
			}
		}
		
		public void update_coordinates(Double x, Double y)
		{
			vel_x = x;
			vel_y = y;
			
		}
		
		public boolean onTouchEvent(MotionEvent e) {
			float touchX = (int)e.getX();
			float touchY = (int)e.getY();
			float width = canvas.getWidth();
			float height= canvas.getHeight();
			
			if(inplay){
				if(touchX>width*5/32-buttonclose.getWidth()/2 && touchX<width*5/32+buttonclose.getWidth()/2 && touchY>height*13/16-buttonclose.getHeight()/2 && touchY<height*13/16+buttonclose.getHeight()/2){
					switch (e.getAction()) {
					case MotionEvent.ACTION_DOWN:
						try {
							movepincers();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
				      break;
				    }
				}
			}else{
				switch (e.getAction()) {
				case MotionEvent.ACTION_DOWN:
					exitgame();
			      break;
			    }
			}
			  return true;
		}

		@Override
		public void run() {
			
			while(running){
				if(hold.getSurface().isValid() && thisActivity.isConnected())
				{
					///////////Manejo de tiempo/////
					if (inplay){
						timeLeft = totalTime-count;  
						count++;
		                if(timeLeft == 0){
		                	inplay=false;
		                }
					}
					if(catchball){
						score();
					}
		            ////////////////////////////////
					
					
					can = hold.lockCanvas();
				
					
						try {
							canvas.Draw(can);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					
					hold.unlockCanvasAndPost(can);
				}
				else
				{
					running = false;
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

