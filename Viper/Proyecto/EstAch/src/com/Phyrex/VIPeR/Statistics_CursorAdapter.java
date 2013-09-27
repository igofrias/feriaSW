package com.Phyrex.VIPeR;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


public class Statistics_CursorAdapter extends SimpleCursorAdapter{

	Cursor cursor;
	Context context;
	String[] from;
	int[] to;

	@SuppressWarnings("deprecation")
	public Statistics_CursorAdapter(Context context, int layout, Cursor cursor,
			String[] from) {
		super(context, layout, cursor, from, null);
		this.cursor = cursor;
		this.context = context;
		this.from=from;

	}

	/*Mecánica similar al base adapter
	 * */
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView==null){
			@SuppressWarnings("static-access")
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.row, null);
		}

		cursor.moveToPosition(position);

		/*declaración e identificación de cada elemento que ira dentro del item
		 * */
		final TextView name = (TextView)convertView.findViewById(R.id.name);
		final TextView desc = (TextView)convertView.findViewById(R.id.desc);
		final TextView subtitle = (TextView)convertView.findViewById(R.id.subtitle);	    
		LinearLayout layout = (LinearLayout)convertView.findViewById(R.id.layout);

		/*Asignacion de valores.
		 * */
		name.setText(cursor.getString(cursor.getColumnIndex(from[1])));
		desc.setText(cursor.getString(cursor.getColumnIndex(from[2])));
		subtitle.setText(cursor.getString(cursor.getColumnIndex(from[0])));

		switch(Integer.valueOf(cursor.getString(cursor.getColumnIndex(from[3])))){
		case 1:
			layout.setBackgroundResource(R.drawable.est1);
			break;
		case 2:
			layout.setBackgroundResource(R.drawable.est2);
			break;
		case 3:
			layout.setBackgroundResource(R.drawable.est3);
			break;
			/*
		case 4:
			layout.setBackgroundResource(R.drawable.est4);
			break;
		case 5:
			layout.setBackgroundResource(R.drawable.est5);
			break;
			*/
		}

		/*El evento de click tambien se lo pueden dar al convertvews
		 * EJ: convertView.setOnClickListener(listener);
		 */
		return convertView;
	}
}
