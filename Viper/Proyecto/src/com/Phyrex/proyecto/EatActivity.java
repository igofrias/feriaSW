package com.Phyrex.proyecto;

import com.actionbarsherlock.app.SherlockFragment;


import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class Comer extends SherlockFragment implements SensorEventListener {

	SensorManager manager;
	Sensor accelerometer;
	double[] gravity;
	double mAccelLast;
	double mAccelCurrent;
	double mAccel;
	TextView status;
	static double ACCEL_MIN = 5;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
	}
	@Override
	public void onAttach(Activity activity)
	{
		super.onAttach(activity);
	}
	@Override
	  public View onCreateView(LayoutInflater inflater, ViewGroup container,
	      Bundle savedInstanceState) {
		  //Sets view and sensor
		
	    View view = inflater.inflate(R.layout.activity_eat,
	        container, false);
	    
	    manager =(SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
		accelerometer = manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		gravity = new double[3];
		
		status = (TextView)getView().findViewById(R.id.status_text1);
		
	    return view;
	  }	
	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSensorChanged(SensorEvent arg0) {
		// Gets information from sensors and processes it
		float x = arg0.values[0];
	      float y = arg0.values[1];
	      float z = arg0.values[2];
	      mAccelLast = mAccelCurrent;
	      mAccelCurrent = (float) Math.sqrt((double) (x*x + y*y + z*z));
	      double delta = mAccelCurrent - mAccelLast;
	      mAccel = mAccel * 0.9f + delta*0.1f; // perform low-cut filter
	      //After cleanup do something if shaking
	      if (mAccel > ACCEL_MIN)
	      {
	    	  status.setText("Dando de comer\n");
	      }
	      else 
	      {
	    	  status.setText("No hay que comer\n");
	      }
	}
	@Override
	public void onResume() {
		super.onResume();
		manager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
		
	}

	@Override
	public void onPause() {
		super.onPause();
		manager.unregisterListener(this);
		
	}
        @Override
	public void onDetach(){
		super.onDetach();
		manager.unregisterListener(this);
	}
}
