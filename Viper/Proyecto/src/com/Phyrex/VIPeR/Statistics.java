package com.Phyrex.VIPeR;


public class Statistics {
	int _id;
	String _name;
	String _desc;
	int _amount;

	public Statistics() {

	}

	public Statistics(int id, String name, String desc, int amount){
		this._id=id;
		this._name=name;
		this._desc=desc;
		this._amount = amount;
	}

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
	public String getDesc(){
		return this._desc;
	}

	//set descripcion
	public void setDesc(String desc){
		this._desc=desc;

	}

	// get Hecho
	public int getAmount(){
		return this._amount;

	}
	//set Hecho
	public void setAmount(int amount){
		this._amount=amount;
	}

	@Override
	public String toString() {
		return this._name + "\n" + this._desc + "\n" + this._amount;
	}
}
