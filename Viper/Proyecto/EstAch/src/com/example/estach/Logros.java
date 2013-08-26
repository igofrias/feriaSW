package com.example.estach;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class Logros extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.achivements);
        

    }
    
    public void cerrar(View view) {
    	finish();
    }  
    
    
}

