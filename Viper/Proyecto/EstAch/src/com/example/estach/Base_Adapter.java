package com.example.estach;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Base_Adapter extends BaseAdapter{

	Context context;
	String[] nombres;
	String[] descripciones;
	int[] hecho;

	public Base_Adapter(String[] nombres, String[] descripciones, int[] hecho, Context context) {
		super();
		this.nombres=nombres;
		this.descripciones=descripciones;
		this.hecho = hecho;
		this.context=context;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return nombres.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {	
	    if(convertView==null){
	    	@SuppressWarnings("static-access")
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
	    	convertView = inflater.inflate(R.layout.row, null);//identificación del xml que representa al item
	    }

	    /*declaración e identificacién de cada elemento que ira dentro del item
	     * */
	    TextView titulo = (TextView)convertView.findViewById(R.id.titulo);
	    TextView titulo1 = (TextView)convertView.findViewById(R.id.titulo1);
	    //TextView titulo2 = (TextView)convertView.findViewById(R.id.titulo2);
	    TextView titulo3 = (TextView)convertView.findViewById(R.id.titulo3);
	    LinearLayout layout = (LinearLayout)convertView.findViewById(R.id.layout);
	    
	    /*Asignacion de valores.
	     * */
	    //layout.setBackgroundColor(colores[position]);
	    titulo.setText(nombres[position]);
	    titulo1.setText(nombres[position]);
	    titulo3.setText(descripciones[position]);

	    /*El evento de click tambien se lo pueden dar al convertvews
	     * EJ: convertView.setOnClickListener(listener);
	     */
	    
    	return convertView;
       	}

}
