package com.Phyrex.proyecto;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.Phyrex.proyecto.CreateActivity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

public class FullfragmentActivity extends SherlockFragmentActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		launch_menu(); // carga fragmento principal, el menu del juego
	
	}
	
		private void launch_menu() {//identificamos y cargamos el fragmento menu
			FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
			SherlockFragment fragmento_menu1 = ((CreateActivity)getSupportFragmentManager().findFragmentByTag("main"));
			
			if(fragmento_menu1==null){
				fragmento_menu1 = new CreateActivity();
				ft.add(R.id.linear, fragmento_menu1,"create");
			}
			else{
				if(fragmento_menu1.isDetached()){
					ft.attach(fragmento_menu1);
				}
			}
			ft.commit();
		}
		
		/*public void launch_play() {
			SherlockFragment fragment = ((CreateActivity)getSupportFragmentManager().findFragmentByTag("play"));
			FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
			
			getSupportFragmentManager().popBackStack();
			if(fragment!=null){
				if(fragment.isDetached()){
					
					ft.addToBackStack(null);
					ft.replace(R.id.frame, fragment,"play");
				}
			}
			else{
				fragment = new Fragmento_play();
					ft.addToBackStack(null);
					ft.replace(R.id.frame, fragment,"play");
			}
			ft.commit();	
		}
		
		public void launch_rankings() {
			SherlockFragment fragment = ((Fragmento_ranking)getSupportFragmentManager().findFragmentByTag("ranking"));
			FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
			
			getSupportFragmentManager().popBackStack();
			if(fragment!=null){
				if(fragment.isDetached()){
					
					ft.addToBackStack(null);
					ft.replace(R.id.frame, fragment,"ranking");
				}
			}
			else{
				fragment = new Fragmento_ranking();
					ft.addToBackStack(null);
					ft.replace(R.id.frame, fragment,"ranking");
			}
			ft.commit();	
			

		}

		public void launch_about() {
			SherlockFragment fragment = ((Fragmento_about)getSupportFragmentManager().findFragmentByTag("about"));
			FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
			
			getSupportFragmentManager().popBackStack();
			if(fragment!=null){
				if(fragment.isDetached()){
					
					ft.addToBackStack(null);
					ft.replace(R.id.frame, fragment,"about");
				}
			}
			else{
				fragment = new Fragmento_about();
					ft.addToBackStack(null);
					ft.replace(R.id.frame, fragment,"about");
			}
			ft.commit();
		}*/

		
	}
	
		
