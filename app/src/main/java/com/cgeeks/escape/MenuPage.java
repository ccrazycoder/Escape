package com.cgeeks.escape;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TextView;

public class MenuPage extends Activity {
    /**
     * Called when the activity is first created.
     */
    public static TextView txtNewGame = null;
    public static TextView txtSettings = null;
    public static TextView txtExit = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.splash_menu);
        txtNewGame = (TextView) findViewById(R.id.txtNewGame);
        txtSettings = (TextView) findViewById(R.id.txtSettings);
        txtExit = (TextView) findViewById(R.id.txtExit);

        try {

            txtNewGame.setOnClickListener(new OnClickListener() {

                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    txtNewGame.setTextColor(Color.RED);

                    txtNewGame.postDelayed(new Runnable() {
                        public void run() {
                            txtNewGame.setTextColor(Color.WHITE);
                        }
                    }, 200);
                    Intent mainIntent = new Intent(MenuPage.this, MainPage.class);
                    startActivity(mainIntent);
                    //MenuPage.this.finish();
                }

            });

            txtSettings.setOnClickListener(new OnClickListener() {

                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    txtSettings.setTextColor(Color.RED);

                    txtSettings.postDelayed(new Runnable() {
                        public void run() {
                            txtSettings.setTextColor(Color.WHITE);
                        }
                    }, 200);
                    Intent i = new Intent(MenuPage.this, SettingsPage.class);
                    startActivity(i);
                }

            });

            txtExit.setOnClickListener(new OnClickListener() {

                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    txtExit.setTextColor(Color.RED);

                    txtExit.postDelayed(new Runnable() {
                        public void run() {
                            txtExit.setTextColor(Color.WHITE);
                        }
                    }, 200);

                    AlertDialog.Builder b = new AlertDialog.Builder(MenuPage.this);
                    AlertDialog a = b.create();
                    a.setMessage("Are you sure, you want to Exit?");

                    a.setButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {
                            System.exit(0);
                        }
                    });

                    a.setButton2("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {

                        }
                    });
                    a.show();
                }

            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}