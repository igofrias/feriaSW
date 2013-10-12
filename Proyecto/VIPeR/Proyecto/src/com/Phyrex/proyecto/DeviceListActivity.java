package com.Phyrex.proyecto;

/**
 *   (Changes from original are) Copyright 2010 Guenther Hoelzl, Shawn Brown
 * 
 * (original work is) Copyright (C) 2009 The Android Open Source Project
**/
import java.util.Set;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import com.actionbarsherlock.app.SherlockFragment;


/**
 * This Activity appears as a dialog. It lists any paired devices and
 * devices detected in the area after discovery. When a device is chosen
 * by the user, the MAC address of the device is sent back to the parent
 * Activity in the result Intent.
 */
public class DeviceListActivity extends SherlockFragment {
    static final String PAIRING = "pairing";

    // Return Intent extra
    public static String DEVICE_NAME_AND_ADDRESS = "device_infos";
    public static String EXTRA_DEVICE_ADDRESS = "device_address";

    // Member fields
    private BluetoothAdapter mBtAdapter;
    private ArrayAdapter<String> mPairedDevicesArrayAdapter;
    private ArrayAdapter<String> mNewDevicesArrayAdapter;

    private static Activity thisActivity;
    
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);        
        setRetainInstance(true);
    }

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View v = inflater.inflate(R.layout.device_list, container, false);
		return v;
	}	
    
    @Override
	public void onActivityCreated(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Setup the window
        //requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);//gargando en ek actionbar
        // Set result CANCELLED in case the user backs out
        //setResult(Activity.RESULT_CANCELED);

        // Initialize the button to perform device discovery
        Button scanButton = (Button) thisActivity.findViewById(R.id.button_scan);
        scanButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                doDiscovery();
                v.setVisibility(View.GONE);
            }
        });

        // Initialize array adapters. One for already paired devices and
        // one for newly discovered devices
       mPairedDevicesArrayAdapter = new ArrayAdapter<String>(thisActivity, R.layout.device_name);
        mNewDevicesArrayAdapter = new ArrayAdapter<String>(thisActivity, R.layout.device_name);

        // Find and set up the ListView for paired devices
        ListView pairedListView = (ListView) thisActivity.findViewById(R.id.paired_devices);
        pairedListView.setAdapter(mPairedDevicesArrayAdapter);
        pairedListView.setOnItemClickListener(mDeviceClickListener);

        // Find and set up the ListView for newly discovered devices
        ListView newDevicesListView = (ListView) thisActivity.findViewById(R.id.new_devices);
        newDevicesListView.setAdapter(mNewDevicesArrayAdapter);
        newDevicesListView.setOnItemClickListener(mDeviceClickListener);

        // Register for broadcasts when a device is discovered
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        thisActivity.registerReceiver(mReceiver, filter);

        // Register for broadcasts when discovery has finished
        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        thisActivity.registerReceiver(mReceiver, filter);

        // Get the local Bluetooth adapter
        mBtAdapter = BluetoothAdapter.getDefaultAdapter();

        // Get a set of currently paired devices
        Set<BluetoothDevice> pairedDevices = mBtAdapter.getBondedDevices();

        // If there are paired devices, add each one to the ArrayAdapter
        boolean legoDevicesFound = false;
        
        if (pairedDevices.size() > 0) {
        	thisActivity.findViewById(R.id.title_paired_devices).setVisibility(View.VISIBLE);
            for (BluetoothDevice device : pairedDevices) {
                // only add LEGO devices
                if (device.getAddress().startsWith(BTCommunicator.OUI_LEGO)) {
                    legoDevicesFound = true;
                    mPairedDevicesArrayAdapter.add(device.getName() + "-" + device.getAddress());
                }
            }
        }
        
        if (legoDevicesFound == false) {
            String noDevices = getResources().getText(R.string.none_paired).toString();
            mPairedDevicesArrayAdapter.add(noDevices);
        }
    }

    @Override
	public void onDestroy() {
        super.onDestroy();

        // Make sure we're not doing discovery anymore
        if (mBtAdapter != null) {
            mBtAdapter.cancelDiscovery();
        }

        // Unregister broadcast listeners
        thisActivity.unregisterReceiver(mReceiver);
    }

    /**
     * Start device discover with the BluetoothAdapter
     */
    private void doDiscovery() {

        // Indicate scanning in the title
    	thisActivity.setProgressBarIndeterminateVisibility(true);
    	thisActivity.setTitle(R.string.scanning);

        // Turn on sub-title for new devices
    	thisActivity.findViewById(R.id.title_new_devices).setVisibility(View.VISIBLE);

        // If we're already discovering, stop it
        if (mBtAdapter.isDiscovering()) {
            mBtAdapter.cancelDiscovery();
        }

        // Request discover from BluetoothAdapter
        mBtAdapter.startDiscovery();
    }

    // The on-click listener for all devices in the ListViews
    private OnItemClickListener mDeviceClickListener = new OnItemClickListener() {
        public void onItemClick(AdapterView<?> av, View v, int arg2, long arg3) {

            String info = ((TextView) v).getText().toString();
            // did we choose a correct name and address?
            if (info.lastIndexOf('-') != info.length()-18) 
                return;

            // Cancel discovery because it's costly and we're about to connect
            mBtAdapter.cancelDiscovery();
            // Get the device MAC address, this is the text after the last '-' character
            String address = info.substring(info.lastIndexOf('-')+1);
            // Create the result Intent and include the infos
            Intent intent = new Intent();
            Bundle data = new Bundle();
            data.putString(DEVICE_NAME_AND_ADDRESS, info);
            data.putString(EXTRA_DEVICE_ADDRESS, address);
            data.putBoolean(PAIRING,av.getId()==R.id.new_devices);
            intent.putExtras(data);
            // Set result and finish this Activity
            thisActivity.setResult(thisActivity.RESULT_OK, intent);
            thisActivity.finish();
        }
    };

    // The BroadcastReceiver that listens for discovered devices and
    // changes the title when discovery is finished
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            // When discovery finds a device
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Get the BluetoothDevice object from the Intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // If it's already paired, skip it, because it's been listed already
                if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                    mNewDevicesArrayAdapter.add(device.getName() + "-" + device.getAddress());
                }
            // When discovery is finished, change the Activity title
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
            	thisActivity.setProgressBarIndeterminateVisibility(false);
            	thisActivity.setTitle(R.string.select_device);
                if (mNewDevicesArrayAdapter.getCount() == 0) {
                    String noDevices = getResources().getText(R.string.none_found).toString();
                    mNewDevicesArrayAdapter.add(noDevices);
                }
            }
        }
    };

}
