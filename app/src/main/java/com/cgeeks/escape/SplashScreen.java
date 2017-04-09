package com.cgeeks.escape;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

public class SplashScreen extends Activity {
    /** Called when the activity is first created. */
	private final int SPLASH_DISPLAY_LENGHT =2000;  
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.splash_main);
        
        LinearLayout mainFrame = ((LinearLayout)findViewById(R.id.LinearLayout01)); 
		Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(this,R.anim.hyperspace_jump);
		mainFrame.startAnimation(hyperspaceJumpAnimation);   
		try{

			new Handler().postDelayed(new Runnable() {

				public void run() {
					Intent mainIntent = new Intent(SplashScreen.this, MenuPage.class);
					SplashScreen.this.startActivity(mainIntent);
					SplashScreen.this.finish();
				}
			}, SPLASH_DISPLAY_LENGHT);

		} catch(Exception e){
			e.printStackTrace();
		}
    }
}