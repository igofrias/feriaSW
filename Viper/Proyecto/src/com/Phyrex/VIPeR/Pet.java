package com.Phyrex.VIPeR;

public class Pet {
	int _id;
	String _name;
	String _color;
	String _raza;
	String _mac;
	String _birthdate;
	int _death;
	 
	 public Pet() {
		 //constructor vacio
	 }
	 
	 //constructor con id
	public Pet(int id, String name, String raza, String color, String birthdate, String mac, int death){
		 this._id=id;
		 this._name=name;
		 this._raza=raza;
		 this._color=color;
		 this._birthdate=birthdate;
		 this._mac=mac;
		 this._death=death;
	}
	
	public int get_id() {
		return _id;
	}

	public void set_id(int _id) {
		this._id = _id;
	}

	public String get_name() {
		return _name;
	}

	public void set_name(String _name) {
		this._name = _name;
	}

	public String get_color() {
		return _color;
	}

	public void set_color(String _color) {
		this._color = _color;
	}

	public String get_raza() {
		return _raza;
	}

	public void set_raza(String _raza) {
		this._raza = _raza;
	}

	public String get_mac() {
		return _mac;
	}

	public void set_mac(String _mac) {
		this._mac = _mac;
	}

	public String get_birthdate() {
		return _birthdate;
	}

	public void set_birthdate(String _birthdate) {
		this._birthdate = _birthdate;
	}

	public int get_death() {
		return _death;
	}

	public void set_death(int _death) {
		this._death = _death;
	}

	@Override
	public String toString() {
		return "mascota [_id=" + _id + ", _name=" + _name + ", _color="
				+ _color + ", _raza=" + _raza + ", _mac=" + _mac
				+ ", _birthdate=" + _birthdate + ", _death=" + _death + "]";
	}


}
