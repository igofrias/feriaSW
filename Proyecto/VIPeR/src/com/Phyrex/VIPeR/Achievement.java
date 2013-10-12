package com.Phyrex.VIPeR;

public class Achievement {
	int _id;
	String _name;
	String _desc;
	int _done;

	public Achievement() {

	}

	//constructor con id
	public Achievement(int id, String name, String desc, int done){
		this._id=id;
		this._name=name;
		this._desc=desc;
		this._done=done;
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
	public int getDone(){
		return this._done;

	}
	//set Hecho
	public void setDone(int done){
		this._done=done;
	}

	@Override
	public String toString() {
		return this._name + "\n" + this._desc + "\n" + this._done;
	}
}
