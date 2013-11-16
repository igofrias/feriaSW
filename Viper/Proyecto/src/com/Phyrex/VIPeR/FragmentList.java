package com.Phyrex.VIPeR;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockListFragment;

public class FragmentList extends SherlockListFragment {
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.list, null);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		String[] frags = { "Estadisticas", "Logros", "Control Remoto" };
		ArrayAdapter<String> fragAdapter = new ArrayAdapter<String>(
				getActivity(), android.R.layout.simple_list_item_1,
				android.R.id.text1, frags);
		setListAdapter(fragAdapter);
	}
	
	@Override
	public void onListItemClick(ListView lv, View v, int position, long id) {
		SherlockFragment newContent = null;
		switch (position) {
		case 0:
			newContent = new Achievement_Activity();
			break;
		case 1:
			newContent = new Statistics_Activity();
			break;
		case 2:
			newContent = new RemoteControl();
			break;
		}
		if (newContent != null)
			switchFragment(newContent);

	}
	
	
	private void switchFragment(SherlockFragment newContent) {
		if (getActivity() == null)
			return;

		if (getActivity() instanceof MenuSliding) {
			MenuSliding fca = (MenuSliding) getActivity();
			fca.switchContent(newContent);
		}
	}
	
}
