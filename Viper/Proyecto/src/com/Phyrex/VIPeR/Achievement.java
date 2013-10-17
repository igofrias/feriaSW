package com.Phyrex.VIPeR;

public class Achievement {
	int _id;
	String _name;
	String _desc;
	int _img;
	int _done;

	public Achievement() {

	}

	//constructor con id
	public Achievement(String name, String desc, int img, int done){
		this._name=name;
		this._desc=desc;
		this._img=img;
		this._done=done;
	}

	public int get_img() {
		return _img;
	}

	public void set_img(int _img) {
		this._img = _img;
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

	public String get_desc() {
		return _desc;
	}

	public void set_desc(String _desc) {
		this._desc = _desc;
	}

	public int get_done() {
		return _done;
	}

	public void set_done(int _done) {
		this._done = _done;
	}

	@Override
	public String toString() {
		return this._name + "\n" + this._desc + "\n" + this._done;
	}
}
