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
			convertView = inflater.inflate(R.layout.rowlist, null);
		}

		cursor.moveToPosition(position);

		/*declaración e identificación de cada elemento que ira dentro del item
		 * */
		//final TextView name = (TextView)convertView.findViewById(R.id.desc);
		//final TextView subtitle = (TextView)convertView.findViewById(R.id.titlelista);
		final TextView title = (TextView)convertView.findViewById(R.id.titlelist);
		final TextView number = (TextView)convertView.findViewById(R.id.listnumber);
		LinearLayout layout = (LinearLayout)convertView.findViewById(R.id.layoutrowlista);

		/*Asignacion de valores.
		 * */
		//name.setText(cursor.getString(cursor.getColumnIndex(from[1])));
		//subtitle.setText(cursor.getString(cursor.getColumnIndex(from[0])));
		title.setText(cursor.getString(cursor.getColumnIndex(from[0])));
		number.setText(cursor.getString(cursor.getColumnIndex(from[2])));
		layout.setBackgroundResource(Integer.valueOf(cursor.getString(cursor.getColumnIndex(from[3]))));


		/*El evento de click tambien se lo pueden dar al convertvews
		 * EJ: convertView.setOnClickListener(listener);
		 */
		return convertView;
	}
	
	@Override
	public boolean isEnabled(int position) {
	    return false;
	}
}
