package com.Phyrex.VIPeR;


import com.actionbarsherlock.app.SherlockFragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.SeekBar;
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
	Thread thMesseger;
	
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
	    View view = inflater.inflate(R.layout.control_remoto_debug_layout,
	        container, false);
	    
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
		thMesseger = new Thread(new Runnable() {
	        public void run() {
	        	long wait_time = 100;
	        	while(running)
	        	{
	        		if(thisActivity.isConnected())
	        		{
	        			send_speeds();
	        			
	        			//SurfaceHolder hold = canvas.getHolder();
	        			//Canvas can = hold.lockCanvas();
	        			
	        			//	canvas.draw(can);
	       
	        			//hold.unlockCanvasAndPost(can);
	        			
	        		}
	        		SystemClock.sleep(wait_time);
	        		
	        	}
	        }
	    });
		
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
	public int bar_percentage(int bar_range, double max_vel, double vel)
	{
		//Ve cual es el porcentaje de barra que debe tener un valor dado
		int percentage = (int)Math.ceil((50.0*vel)/max_vel) + 50;
		return percentage;
	}
	public int bar_percentage(int bar_range, int center, double max_vel, double vel)
	{
		//Ve el porcentaje que se tiene que mover, esta vez con un centro explicito
		int percentage = (int)Math.ceil(((bar_range-center)*vel)/max_vel) + center;
		return percentage;
	}
	public void reset_speeds()
	{
		vel_x = 0;
		vel_y = 0;
		vel_z = 0;
	}
	public void mostrar_velocidades_debug()
	{
		//Muestra las velocidades actuales por debug
		SeekBar x_view = (SeekBar) getView().findViewById(R.id.seekBarX);
		SeekBar y_view = (SeekBar) getView().findViewById(R.id.seekBarY);
		
	    x_view.setProgress(bar_percentage(100,MAX_VEL,this.vel_x));
	    y_view.setProgress(bar_percentage(100,MAX_VEL,this.vel_y));
	    
	}
	public void send_speeds()
	{
		
		//Envia los datos obtenidos al robot.
		if(thisActivity.isConnected())
		{
			
				
			//Asumiendo que motor b y c sean los motores de las ruedas.
			int motorActionb = BTCommunicator.MOTOR_B;
		    int motorActionc = BTCommunicator.MOTOR_C;
			
			thisActivity.sendBTCmessage(BTCommunicator.NO_DELAY, motorActionb,vel_robot_x , 0);
			thisActivity.sendBTCmessage(1000, motorActionb, 0, 0);
			thisActivity.sendBTCmessage(BTCommunicator.NO_DELAY, motorActionc, vel_robot_y, 0);
			thisActivity.sendBTCmessage(1000, motorActionc, 0, 0);
			
		}
		else
		{
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
	    //Cada vez que cambia el sensor se reciben los valores y se procesan.
	    raw_accel_x = event.values[0];
	    raw_accel_y = event.values[1];
	    raw_accel_z = event.values[2];
	    
	    process_aceleration();
	    get_speed();
	    canvas.update_coordinates(this.vel_x, this.vel_y);
	    //mostrar_velocidades_debug();
	    
	    
	    
	}
	@Override
	public void onResume() {
		super.onResume();
		running=true;
		manager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
		if(!thMesseger.isAlive())
		{
			thMesseger.start();
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		manager.unregisterListener(this);
		running=false;
	}
        @Override
	public void onDetach(){
		super.onDetach();
		manager.unregisterListener(this);
		running = false;
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
			
			float center_x = canvas.getWidth()/2;
			float center_y = canvas.getHeight()/2;
			int size = 30;
			int x = bar_percentage(canvas.getWidth() - size, (int)center_x, -MAX_VEL, vel_x);
			int y = bar_percentage(canvas.getHeight() - size, (int)center_y,MAX_VEL, vel_y);
			canvas.drawARGB(255, 255, 255, 255);
			canvas.drawCircle(canvas.getWidth()/2,canvas.getHeight()/2,100, color_center);
			canvas.drawCircle(x, y , size, color);
			
		}
		public void update_coordinates(Double x, Double y)
		{
			vel_x = x;
			vel_y = y;
			
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			
			while(running){
				if(hold.getSurface().isValid())
				{
					can = hold.lockCanvas();
				
					
						canvas.Draw(can);
					
					hold.unlockCanvasAndPost(can);
				}
			}
		}
	}
}

