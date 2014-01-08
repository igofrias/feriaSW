package com.Phyrex.VIPeR;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


public class Achievement_CursorAdapter extends SimpleCursorAdapter{

	Cursor cursor;
	Context context;
	String[] from;

	@SuppressWarnings("deprecation")
	public Achievement_CursorAdapter(Context context, int layout, Cursor cursor,
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
	    LinearLayout layout = (LinearLayout)convertView.findViewById(R.id.layout);
	    final TextView archtext = (TextView)convertView.findViewById(R.id.archname);	
	    final TextView description = (TextView)convertView.findViewById(R.id.description);	
	    final TextView imgint = (TextView)convertView.findViewById(R.id.imgint);	
	    
	    /*Asignacion de valores.
	     * */
	   // subtitle.setText(cursor.getString(cursor.getColumnIndex(from[0])));
	    
	    //Si la condición de encontrar un 1 ("hecho") en achievement satisface, muestra todos los datos
	    //respectivos de ese achievement.
	    
	    ///*****************************************************
	    //Cada iamgen es necesario setearla de manera manual.
	    //******************************************************/
	    if(Integer.valueOf(cursor.getString(cursor.getColumnIndex(from[2]))) == 1){
	    	archtext.setText(cursor.getString(cursor.getColumnIndex(from[0])));
	    	description.setText(cursor.getString(cursor.getColumnIndex(from[1])));
	    	layout.setBackgroundResource(Integer.valueOf(cursor.getString(cursor.getColumnIndex(from[3]))));
	    	imgint.setText(cursor.getString(cursor.getColumnIndex(from[3])));
	    }
	    else{
	    	layout.setBackgroundResource(R.drawable.logrobloq);
	    }
	    
    	return convertView;
       	}
 }