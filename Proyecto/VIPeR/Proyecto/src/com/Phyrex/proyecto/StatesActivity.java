package com.Phyrex.proyecto;

import android.os.BatteryManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import com.actionbarsherlock.app.SherlockFragment;

public class StatesActivity extends SherlockFragment {
	private static Activity thisActivity;
	private ProgressBar salud;
	private ProgressBar hambre;
	private ProgressBar energia;
	private ProgressBar felicidad;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);        
        setRetainInstance(true);
    }

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View v = inflater.inflate(R.layout.activity_states, container, false);
		return v;
	}	
	
	
	protected void onCreateView(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		thisActivity = getActivity();
		salud = (ProgressBar)thisActivity.findViewById(R.id.salud);
		energia = (ProgressBar)thisActivity.findViewById(R.id.hambre);
		hambre = (ProgressBar)thisActivity.findViewById(R.id.felicidad);
		felicidad = (ProgressBar)thisActivity.findViewById(R.id.energia);
		felicidad.setMax(100);
		hambre.setMax(100);
		salud.setMax(100);
		energia.setMax(100);
		
		energia.setProgress((int) ((MainActivity)thisActivity).getBatteryLevel());
	}

}
