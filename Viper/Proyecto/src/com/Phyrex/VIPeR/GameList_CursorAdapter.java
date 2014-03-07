package com.Phyrex.VIPeR;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


public class GameList_CursorAdapter extends SimpleCursorAdapter{

	Cursor cursor;
	Context context;
	String[] from;
	boolean connected;
	int[] to;

	@SuppressWarnings("deprecation")
	public GameList_CursorAdapter(Context context, int layout, Cursor cursor,
			String[] from, boolean connected) {
		super(context, layout, cursor, from, null);
		this.cursor = cursor;
		this.context = context;
		this.from=from;
		this.connected=connected;

	}

	/*Mecánica similar al base adapter
	 * */
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView==null){
			@SuppressWarnings("static-access")
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.rowgames, null);
		}
		Log.e("pepe","D: 1");
		cursor.moveToPosition(position);

		/*declaración e identificación de cada elemento que ira dentro del item
		 * */
		final TextView gametittle = (TextView)convertView.findViewById(R.id.gametittle);
		final TextView gamedes = (TextView)convertView.findViewById(R.id.gamedes);
		final TextView gamenum = (TextView)convertView.findViewById(R.id.gamenum);
		final ImageView gameimg = (ImageView)convertView.findViewById(R.id.gameimg);
		final TextView gamescore = (TextView)convertView.findViewById(R.id.hs);
		/*Asignacion de valores.
		 * */
		gamenum.setText(cursor.getString(cursor.getColumnIndex(from[0])));
		gametittle.setText(cursor.getString(cursor.getColumnIndex(from[1])));
		gamedes.setText(cursor.getString(cursor.getColumnIndex(from[2])));
		gameimg.setImageResource(Integer.valueOf(cursor.getString(cursor.getColumnIndex(from[3]))));
		gamescore.setText(cursor.getString(cursor.getColumnIndex(from[4])));
		
		/*El evento de click tambien se lo pueden dar al convertvews
		 * EJ: convertView.setOnClickListener(listener);
		 */
		return convertView;
	}
	
	public boolean isEnabled(int position) {
	    if (!connected && position==1) {
	        return false;
	    }
	    return true;
	}
}
