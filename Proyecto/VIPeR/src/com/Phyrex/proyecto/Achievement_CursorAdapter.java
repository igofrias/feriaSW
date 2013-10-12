package com.Phyrex.VIPeR;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


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
	    final TextView subtitle = (TextView)convertView.findViewById(R.id.title);	    
	    LinearLayout layout = (LinearLayout)convertView.findViewById(R.id.layout);
	    final TextView archtext = (TextView)convertView.findViewById(R.id.archname);	
	    final TextView description = (TextView)convertView.findViewById(R.id.description);	
	    
	    /*Asignacion de valores.
	     * */
	    subtitle.setText(cursor.getString(cursor.getColumnIndex(from[0])));
	    
	    //Si la condición de encontrar un 1 ("hecho") en achievement satisface, muestra todos los datos
	    //respectivos de ese achievement.
	    
	    ///*****************************************************
	    //Cada iamgen es necesario setearla de manera manual.
	    //******************************************************/
	    if(Integer.valueOf(cursor.getString(cursor.getColumnIndex(from[2]))) == 1){
	    	subtitle.setText("Ver");
	    	archtext.setText(cursor.getString(cursor.getColumnIndex(from[0])));
	    	description.setText(cursor.getString(cursor.getColumnIndex(from[1])));
	    	layout.setBackgroundResource(R.drawable.firstpetarch);
	    }
	    else{
	    	subtitle.setText("No disponible");
	    	layout.setBackgroundResource(R.drawable.logrobloq);
	    }
	    
    	return convertView;
       	}
 }