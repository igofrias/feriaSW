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
	ListView list;
	Activity thisActivity;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.list, null);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		thisActivity = getActivity();
		list= (ListView) thisActivity.findViewById(R.layout.list);
		String[] frags = { "Mascota", "Conectar", "Control Remoto", "Logros", "Estadisticas", "About" };
		ArrayAdapter<String> fragAdapter = new ArrayAdapter<String>(
				getActivity(), android.R.layout.simple_list_item_1,
				android.R.id.text1, frags);
		setListAdapter(fragAdapter);
		//list.getChildAt(0).setEnabled(false);
	}

	@Override
	public void onListItemClick(ListView lv, View v, int position, long id) {
		Fragment newContent = null;
		switch (position) {
		case 0:
			//mascota, mostrar mascota, o hacer atras y siembre volver a la mascota D: 
			break;
		case 1:
			//conectar (la idea seria habilitar y deshabilitar el boton D:
			break;
		case 2:
			newContent = new RemoteControl();
			break;
		case 3:
			newContent = new Achievement_Activity();
			break;
		case 4:
			newContent = new Statistics_Activity();
			break;
		case 5:
			//about?
			break;
		
		}
		if (newContent != null)
			switchFragment(newContent);

	}

	// the meat of switching the above fragment
	private void switchFragment(Fragment fragment) {
		if (getActivity() == null)
			return;

		if (getActivity() instanceof MainActivity) {
			MainActivity fca = (MainActivity) getActivity();
			fca.switchContent(fragment);
		}
	}

}

