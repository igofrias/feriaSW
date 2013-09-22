package com.Phyrex.proyecto;


import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;


public class Cursor_Adapter extends SimpleCursorAdapter{

	Cursor cursor;
	Context context;
	String[] from;
	int[] to;
	
	@SuppressWarnings("deprecation")
	public Cursor_Adapter(Context context, int layout, Cursor cursor,
			String[] from, int[] to) {
		super(context, layout, cursor, from, to);
		this.cursor = cursor;
		this.context = context;
		this.to=to;
		this.from=from;
	}
	 
	/*Mecánica similar al base adapter
	 * */
	public View getView(int position, View convertView, ViewGroup parent) {
	    if(convertView==null){
	    	@SuppressWarnings("static-access")
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
	    	convertView = inflater.inflate(android.R.layout.simple_list_item_2, null);
	    }
	   
	    cursor.moveToPosition(position);
	    
	    /*declaración e identificacién de cada elemento que ira dentro del item
	     * */
	    final TextView titulo = (TextView)convertView.findViewById(to[0]);
	    final TextView subtitulo = (TextView)convertView.findViewById(to[1]);
	    
	    
	    /*Asignacion de valores.
	     * */
	    titulo.setText(cursor.getString(cursor.getColumnIndex(from[0])));
	    subtitulo.setText(cursor.getString(cursor.getColumnIndex(from[1])));
	    
	    /*El evento de click tambien se lo pueden dar al convertvews
	     * EJ: convertView.setOnClickListener(listener);
	     */
    	return convertView;
       	}
 }