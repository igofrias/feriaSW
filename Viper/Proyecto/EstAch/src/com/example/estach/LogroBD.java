package com.example.estach;

public class LogroBD {
	 int _id;
	 String _name;
	 String _descripcion;
	 int _hecho;
	 
	 public LogroBD() {
		 //constructor vacio
	 }
	 
	 //constructor con id
	public LogroBD(int id, String name, String descripcion, int hecho){
		 this._id=id;
		 this._name=name;
		 this._descripcion=descripcion;
		 this._hecho = hecho;
	}
	
	//constructor sin id
	/*
	public LogroBD(String name, String descripcion, int hecho){
		 this._name=name;
		 this._descripcion=descripcion;
		 this._hecho = hecho;
	}
	*/
	
	// get ID
	public int getID(){
		return this._id;
		
	}
	//set ID
	public void setID(int id){
		this._id=id;
	}
	
	// get name
	public String getName(){
			return this._name;
	}
		
	// set name
	public void setName(String name){
		this._name = name;
	}
	 
	//get descripcion
	public String getDescripcion(){
		return this._descripcion;
	}

	//set descripcion
	public void setDescripcion(String descripcion){
		this._descripcion=descripcion;
		
	}
	
	// get Hecho
	public int getHecho(){
		return this._hecho;
		
	}
	//set Hecho
	public void setHecho(int hecho){
		this._hecho=hecho;
	}
	
	@Override
	public String toString() {
	    return this._name + "\n" + this._descripcion + "\n" + this._hecho;
	}
}
