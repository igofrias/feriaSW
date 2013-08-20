package com.Phyrex.demo;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
//Maneja el control remoto usando acelerometro
public class ControlRemoto extends Activity implements SensorEventListener{
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
	public void initControlRemoto(Sensor accel, SensorManager senman)
	{
		//Inicializador en caso de necesitar inicializar estas cosas desde afuera
		acelerometro = accel;
		manager = senman;
		gravity = new double[3];
		MAX_RANGE = 127;
		MAX_VEL = 22;
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		manager =(SensorManager) getSystemService(Context.SENSOR_SERVICE);
		acelerometro = manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		gravity = new double[3];
		MAX_RANGE = 127;
		MAX_VEL = 22;
	}	
		
	 
	public void procesar_aceleraciones()
	{
		//Ocupa los filtros indicados en el tutorial para dejar una aceleracion
		//pura sin influencia de la gravedad.
		//alpha  = t/(dt + t) t=constante del filtro dt=event delivery rate
		double alpha = 0.1;
		
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
		//vel_robot=MAX_RANGE*vel/MAX_VEL
		int k = (int)(Math.ceil((double)MAX_RANGE/MAX_VEL));
		int vel_robot = (int) (Math.ceil(vel)*k);
		return vel_robot;
				
	}
	public void obtener_velocidades()
	{
		//A partir de la aceleracion obtiene la velocidad a mover
		vel_x = vel_x + accel_x;
		vel_y = vel_y + accel_y;
		vel_z = vel_z + accel_z;
		
				//vel_robot=MAX_RANGE*vel/MAX_VEL
		vel_robot_x = this.trans_vel_a_robot(vel_x);
		vel_robot_y = this.trans_vel_a_robot(vel_y);
		vel_robot_z = this.trans_vel_a_robot(vel_z);
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
	}
	@Override
	protected void onResume() {
		super.onResume();
		manager.registerListener(this, acelerometro, SensorManager.SENSOR_DELAY_NORMAL);
	}

	@Override
	protected void onPause() {
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

}
