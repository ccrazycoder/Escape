package com.cgeeks.escape;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class SettingsPage extends Activity {
	/** Called when the activity is first created. */
	private RadioButton rdbEasy = null, rdbMedium = null, rdbHard = null;
	private ToggleButton tbutton = null;
	private TextView txtExit = null, txtExit0 = null;
	private CheckBox chkScore = null;
	private String strDiff, strSound;
	private Integer highScore;
	public static final String PREFS_NAME = "Escape_HighScore";
	private SharedPreferences shPreferences;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.splash_settings);
		
		shPreferences = getSharedPreferences(PREFS_NAME,Context.MODE_WORLD_WRITEABLE);
		strDiff = shPreferences.getString("difficulty", "Easy");
		strSound = shPreferences.getString("sound", "On");
		 
		rdbEasy = (RadioButton) findViewById(R.id.rdbEasy);
		rdbMedium = (RadioButton) findViewById(R.id.rdbMedium);
		rdbHard = (RadioButton) findViewById(R.id.rdbHard);
		txtExit = (TextView) findViewById(R.id.txtExit);
		chkScore = (CheckBox) findViewById(R.id.chkScores);
		tbutton = (ToggleButton) findViewById(R.id.tbutton);
		txtExit0 = (TextView) findViewById(R.id.txtExit0);
		
		if(strDiff.equals("Easy")){
			rdbEasy.setChecked(true);
		}
		else if(strDiff.equals("Medium")){
			rdbMedium.setChecked(true);
		}
		else{
			rdbHard.setChecked(true);
		}
		
		if(strSound.equals("On")){
			tbutton.setChecked(true);
		}
		else{
			tbutton.setChecked(false);
		}
		
		txtExit.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub
				txtExit.setTextColor(Color.RED);
				txtExit.postDelayed(new Runnable() {
					public void run() {
						txtExit.setTextColor(Color.WHITE);
					}
				}, 200);
				
				SharedPreferences.Editor editor = shPreferences.edit();
				if(rdbEasy.isChecked()){
					editor.putString("difficulty", "Easy");
				}
				else if(rdbMedium.isChecked()){
					editor.putString("difficulty", "Medium");
				}
				else{
					editor.putString("difficulty", "Hard");
				}
				if(tbutton.isChecked()){
					editor.putString("sound", "On");
				}
				else{
					editor.putString("sound", "Off");
				}
				
				if(chkScore.isChecked()){
					editor.putInt("highscore", 0);
				}
				
				editor.commit();
				finish();
			}
		});
		txtExit0.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				txtExit0.setTextColor(Color.RED);
				txtExit0.postDelayed(new Runnable() {
					public void run() {
						txtExit0.setTextColor(Color.WHITE);
					}
				}, 200);
				finish();
			}
		});
	}
}