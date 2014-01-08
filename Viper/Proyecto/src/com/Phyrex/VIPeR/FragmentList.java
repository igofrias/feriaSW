package com.Phyrex.VIPeR;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;



import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockListFragment;

public class FragmentList extends SherlockListFragment {
	Activity thisActivity;
	String[] frags= {"Conectar", "Control Remoto", "Logros", "Estadisticas","Sobre Phyrex" };
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.list, null);
	}
	
	public void onResume(){
		super.onResume();
		Reload();
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		thisActivity= getActivity();
		if(!((MainActivity)thisActivity).isConnected())
		{
			frags[0] = "Conectar";
		}else
		{
			frags[0] = "Desconectar";
		}
		ArrayAdapter<String> fragAdapter = new ArrayAdapter<String>(
				getActivity(), android.R.layout.simple_list_item_1,
				android.R.id.text1, frags);
		
		setListAdapter(fragAdapter);
	}

	@Override
	public void onListItemClick(ListView lv, View v, int position, long id) {
		Fragment newContent = null;
		int menuitem = -1;
		switch (position) {	
		case 0:
			menuitem=0;
			break;
		case 1:
			menuitem=1;
			break;
		case 2:
			menuitem=2;
			break;
		case 3:
			menuitem = 3; //About
			break;
		case 4:
			menuitem = 4; //About
			break;
		}
		Reload();
		switchFragment(newContent, menuitem);
	}
	
	public void Reload(){
		if(!((MainActivity)thisActivity).isConnected())
		{
			frags[0] = "Conectar";
		}else
		{
			frags[0] = "Desconectar";
		}
		ArrayAdapter<String> fragAdapter = new ArrayAdapter<String>(
				getActivity(), android.R.layout.simple_list_item_1,
				android.R.id.text1, frags);
		
		setListAdapter(fragAdapter);
	}
	// the meat of switching the above fragment
	private void switchFragment(Fragment fragment, int menuitem) {
		if (getActivity() == null)
			return;
		if (getActivity() instanceof MainActivity) {
			MainActivity fca = (MainActivity) getActivity();
			fca.switchContent(fragment, menuitem);
		}
	}

}

