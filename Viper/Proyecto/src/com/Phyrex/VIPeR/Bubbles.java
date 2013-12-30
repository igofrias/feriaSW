package com.Phyrex.VIPeR;

import android.graphics.Bitmap;

public class Bubbles {

	Bitmap bubbleimg;
	float posx;
	float posy;
	int time;
	
	public Bubbles(Bitmap bubbleimg, float posx, float posy, int time) {
		super();
		this.bubbleimg = bubbleimg;
		this.posx = posx;
		this.posy = posy;
		this.time = time;
	}
	public Bitmap getBubbleimg() {
		return bubbleimg;
	}
	public void setBubbleimg(Bitmap bubbleimg) {
		this.bubbleimg = bubbleimg;
	}
	public float getPosx() {
		return posx;
	}
	public void setPosx(int posx) {
		this.posx = posx;
	}
	public float getPosy() {
		return posy;
	}
	public void setPosy(int posy) {
		this.posy = posy;
	}
	public int getTime() {
		return time;
	}
	public void setTime(int time) {
		this.time = time;
	}
	
	
	
	
}
