package com.Phyrex.demo;

import com.Phyrex.demo.BTCommunicator;

import android.annotation.TargetApi;
import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.SeekBar;
//Maneja el control remoto usando acelerometro
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class ControlRemoto extends Fragment implements SensorEventListener{
	private int MAX_RANGE;
	private double MAX_VEL;
	private Sensor acelerometro;
	SensorManager manager;
	double raw_accel_x = 0;
	double raw_accel_y = 0;
	double raw_accel_z = 0;
	double accel_x = 0;
	double accel_y = 0;
	double accel_z = 0;
	double gravity[];
	
	double vel_x = 0;
	double vel_y = 0;
	double vel_z = 0;
	int vel_robot_x = 0;
	int vel_robot_y = 0;
	int vel_robot_z = 0;
	double sensibilidad;
	Thread thMesseger;
	
	MainActivity thisActivity;
	public void initControlRemoto(Sensor accel, SensorManager senman)
	{
		//Inicializador en caso de necesitar inicializar estas cosas desde afuera
		acelerometro = accel;
		manager = senman;
		gravity = new double[3];
		MAX_RANGE = 127;
		MAX_VEL = 1;
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
	}	
	@Override
	  public View onCreateView(LayoutInflater inflater, ViewGroup container,
	      Bundle savedInstanceState) {
		  //Crea el fragmento con el acelerometr listo
	    View view = inflater.inflate(R.layout.control_remoto_debug_layout,
	        container, false);
	    manager =(SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
		acelerometro = manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		gravity = new double[3];
		MAX_RANGE = 100;
		MAX_VEL = 10;
		sensibilidad = 0.8;
	    return view;
	  }	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		thisActivity = (MainActivity) getActivity();
		thMesseger = new Thread(new Runnable() {
	        public void run() {
	        	long wait_time = 100;
	        	while(true)
	        	{
	        		if(thisActivity.isConnected())
	        		{
	        			enviar_velocidades();
	        		}
	        		SystemClock.sleep(wait_time);
	        		
	        	}
	        }
	    });
		
	}
	public void procesar_aceleraciones()
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
		int vel_robot = (int) (Math.ceil(vel*sensibilidad*k));
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
	public void trans_vels_a_robot()
	{
		//Transforma los ejes x e y en velocidades por motor
		//Sacado del codigo de minddroids
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
			vel_robot_x = (int) Math.round(k * vel_y * (1.0 + vel_x / 60.0));
			vel_robot_y = (int) Math.round(k * vel_y * (1.0 - vel_x / 60.0));
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
		Log.d("vel",Integer.toString(vel_robot_x));
	}
	public void obtener_velocidades()
	{
		//A partir de la aceleracion obtiene la velocidad a mover
		vel_x = accel_x;
		vel_y = accel_y;
		vel_z = accel_z;
		
				//vel_robot=MAX_RANGE*vel/MAX_VEL
//		vel_robot_x = this.trans_vel_a_robot(vel_x);
//		vel_robot_y = this.trans_vel_a_robot(vel_y);
//		vel_robot_z = this.trans_vel_a_robot(vel_z);
		trans_vels_a_robot();
	}
	public int bar_percentage(int bar_range, double max_vel, double vel)
	{
		//Ve cual es el porcentaje de barra que debe tener un valor dado
		int percentage = (int)Math.ceil((50.0*vel)/max_vel) + 50;
		return percentage;
	}
	public void resetear_velocidades()
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
	public void enviar_velocidades()
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
			Log.d("Enviando","Enviando");
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
	    // Do something with this sensor value.
	    procesar_aceleraciones();
	    obtener_velocidades();
	    
	    mostrar_velocidades_debug();
	    
	    
	}
	@Override
	public void onResume() {
		super.onResume();
		manager.registerListener(this, acelerometro, SensorManager.SENSOR_DELAY_NORMAL);
		if(!thMesseger.isAlive())
		{
			thMesseger.start();
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		manager.unregisterListener(this);
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
		this.acelerometro = acelerometro;
	}
	public void setManager(SensorManager manager) {
		this.manager = manager;
	}
	public void startMessenger()
	{
		this.thMesseger.start();
	}

}
