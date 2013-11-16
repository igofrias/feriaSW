package com.Phyrex.VIPeR;

import android.os.Bundle;

import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

public class MenuSliding extends SlidingFragmentActivity{

	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(null);
		setContentView(R.layout.activity_menu_sliding);

		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

		ft.replace(R.id.frame, new MainPetActivity());
		ft.commit();

		setBehindContentView(R.layout.menu_frame);

		ft = getSupportFragmentManager().beginTransaction();

		ft.replace(R.id.menu_frame, new FragmentList());
		ft.commit();

		SlidingMenu slimenu = new SlidingMenu(this);
		slimenu.setMode(SlidingMenu.LEFT);

		slimenu.setShadowWidthRes(R.dimen.shadow_width);
		slimenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		slimenu.setFadeDegree(0.35f);
		slimenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
		slimenu.setTouchModeBehind(SlidingMenu.TOUCHMODE_NONE);

		//getSupportActionBar().setHomeButtonEnabled(true);
	}

	public void switchContent(SherlockFragment fragment) {
		// TODO Auto-generated method stub
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.replace(R.id.frame, fragment);
		ft.commit();
		getSlidingMenu().showContent();

	}
	
	
	
}
