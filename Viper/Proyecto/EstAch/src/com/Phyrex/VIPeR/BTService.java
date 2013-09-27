package com.Phyrex.VIPeR;

import com.Phyrex.VIPeR.BTConnectable;
import com.Phyrex.VIPeR.BTCommunicator;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.util.Log;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;
import android.app.ProgressDialog;
import android.content.Intent;

import java.io.IOException;


public class BTService extends Service implements BTConnectable{
    
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
		return startId;
        // TODO Auto-generated method stub
    	
    }
    
    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }


	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isPairing() {
		// TODO Auto-generated method stub
		return false;
	}
    
}