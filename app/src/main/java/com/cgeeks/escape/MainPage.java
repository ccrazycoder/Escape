package com.cgeeks.escape;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Typeface;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import java.util.Random;

public class MainPage extends Activity {
	public static final String PREFS_NAME = "Escape_HighScore";
	private SharedPreferences shPreferences;
	private int highScore;
	public static float roll;
	private FrameLayout fl;
	private SensorListener sl;
	private SensorManager sm;
	private Panel panel;
	private Handler handler = null;
	private int height = 0;
	private int width = 0;
	// private int myHeight = 0;
	// private int myWidth = 0;
	private Bitmap tempImage;
	private Bitmap dead;
	public static int x;
	public static int y;
	public static float x1[];
	public static float y1[];
	public static int a;
	public static int bounce;
	public static int imageCenterX;
	public static int imageCenterY;
	public static float distance[];
	public static float distance2;
	public static boolean flagBounce[];
	public static boolean flagDead = false;
	public static boolean flagBallOut[];
	private Paint paint;
	private Random random = null;
	private float randomValue[];
	private float randomFloat[];
	private int score = 0;
	int i = 0;
	private int r[];
	private boolean stopFlag = true;
	private Vibrator vibrator = null;
	private int speed = 3;
	private boolean firstTimeFlag = true;
	public MediaPlayer mp;
	private String strSound, strDiff;
	private String MY_AD_UNIT_ID = "a14d90609a806a3";

	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.main);

		// code for shared preferences
		shPreferences = getSharedPreferences(PREFS_NAME,
				Context.MODE_WORLD_WRITEABLE);
		highScore = shPreferences.getInt("highscore", 0);
		strSound = shPreferences.getString("sound", "On");
		strDiff = shPreferences.getString("difficulty", "Easy");
		Log.i("details : ", strSound + " # " + strDiff);

		/*
		 * this code will define speed of the balls according to difficulty
		 * selected
		 */
		if (strDiff.equals("Easy")) {
			speed = 3;
		} else if (strDiff.equals("Medium")) {
			speed = 4;
		} else {
			speed = 5;
		}

		handler = new Handler();
		vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		try {
			// defining panel
			panel = new Panel(this);

			try {
				fl = (FrameLayout) findViewById(R.id.FrameLayout01);
				fl.addView(panel);
				mp = new MediaPlayer();
				// if user selects sound on from the settings then it will play
				// sound
				if (strSound.equals("On")) {
					try {
						mp = new MediaPlayer();
						mp = MediaPlayer.create(getBaseContext(),
								R.raw.background);
						mp.start();

					} catch (Exception e) {
						ShowMessage("error in media player : ", e.toString());
					}
				}

				// panel.onDraw(canvas);
				// myHeight = fl.getHeight();
				// myWidth = fl.getWidth();

			} catch (Exception e) {
				ShowMessage("Error", e.toString());
			}

			// this code is for the sensors.. here we r reading roll values to
			// move the man
			sl = new SensorListener() {
				public void onSensorChanged(int sensor, float[] values) {

					if (sensor == SensorManager.SENSOR_ORIENTATION) {
						roll = (int) values[2];
					}
				}

				public void onAccuracyChanged(int sensor, int accuracy) {
				}
			};
			sm = (SensorManager) getSystemService(SENSOR_SERVICE);

			// Register your SensorListener
			sm.registerListener(sl, SensorManager.SENSOR_ORIENTATION,
					SensorManager.SENSOR_DELAY_FASTEST);
		} catch (Exception e) {
			ShowMessage("Error in onCreate", e.toString());
		}
	}

	/**
	 * this method will check if user have clicked back button then he will get
	 * one message that asks for confirmation
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			ShowConfirmation();
		}

		return false;
	}

	class Panel extends View {
		private Bitmap standing;
		private Bitmap left;
		private Bitmap right;
		private Bitmap left_face;
		private Bitmap right_face;

		public Panel(Context context) {
			super(context);

			standing = BitmapFactory.decodeResource(getResources(),
					R.drawable.standing);
			left = BitmapFactory
					.decodeResource(getResources(), R.drawable.left);
			right = BitmapFactory.decodeResource(getResources(),
					R.drawable.right);
			left_face = BitmapFactory.decodeResource(getResources(),
					R.drawable.left_face);
			right_face = BitmapFactory.decodeResource(getResources(),
					R.drawable.right_face);
			dead = BitmapFactory
					.decodeResource(getResources(), R.drawable.dead);

			paint = new Paint();
			paint.setAntiAlias(true);
			paint.setColor(Color.BLACK);
			paint.setStyle(Style.STROKE);
			paint.setTextSize(20);

			DisplayMetrics dm = new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(dm);
			height = dm.heightPixels;

			width = dm.widthPixels;

			// height = fl.getHeight();
			// width = fl.getWidth();
			x = width / 2;
			y = height - 28;
			x1 = new float[5];
			y1 = new float[5];

			distance = new float[5];
			flagBounce = new boolean[5];
			flagBounce[0] = false;
			flagBounce[1] = false;
			flagBallOut = new boolean[5];
			flagBallOut[0] = false;
			flagBallOut[1] = false;
			flagBallOut[2] = false;
			flagBallOut[3] = false;
			flagBallOut[4] = false;
			x1[0] = 0;
			y1[0] = 0;
			x1[1] = 0;
			y1[1] = 0;
			x1[2] = 0;
			y1[2] = 0;
			x1[3] = 0;
			y1[3] = 0;
			x1[4] = 0;
			y1[4] = 0;
			r = new int[5];
			randomFloat = new float[5];
			randomValue = new float[5];
			a = 30;
			bounce = 0;
		}

		@Override
		public void onDraw(Canvas canvas) {
			try {
				canvas.drawColor(Color.WHITE);
				if (x > width - 18) {
					x = width - 20;
				} else if (x < 11) {
					x = 20;
				}

				// if image is dead then media player will play scream sound
				if (tempImage == dead) {
					// mp = new MediaPlayer();
					if (strSound.equals("On")) {
						mp.stop();
						mp = MediaPlayer.create(getBaseContext(), R.raw.screem);
						mp.start();
					}
					stopFlag = false;
				} else {
					/**
					 * if image is not dead then the roll variable will move the
					 * man according to its value
					 */
					if (roll < -5) {

						if (strDiff.equals("Easy")) {
							x = x + 1;
						} else if (strDiff.equals("Medium")) {
							x = x + 2;
						} else {
							x = x + 3;
						}

						if (roll < -15) {
							x = x + 3;
						}
						if (x % 2 == 0) {
							tempImage = right;
						} else {
							tempImage = right_face;
						}
					} else if (roll > 5) {
						x = x - 1;
						if (roll > 15) {
							x = x - 3;
						}
						if (x % 2 == 0) {
							tempImage = left;
						} else {
							tempImage = left_face;
						}
					} else {
						tempImage = standing;
					}
				}

				// this code draws the man on the canvas
				canvas.drawBitmap(tempImage, x, y, paint);
				// canvas.drawText("Score : " + score, width - 100, 30, paint);
				// canvas.drawText("HighScore : " + highScore, 20, 30, paint);
				paint.setTypeface(Typeface.SANS_SERIF);
				canvas.drawText("Score : " + score, width - 100, 30, paint);
				canvas.drawText("HighScore : " + highScore, width - 150, 70,
						paint);

				if (flagBallOut[0] == false)
					r[0] = 10;
				if (flagBallOut[1] == false)
					r[1] = 15;
				if (flagBallOut[2] == false)
					r[2] = 20;
				if (flagBallOut[3] == false)
					r[3] = 18;
				if (flagBallOut[4] == false)
					r[4] = 16;

				// following code calls the method throw balls and throws the
				// balls accordingly
				if (firstTimeFlag == true) {
					throwBalls(0, 0, 0, r[0], canvas);
					throwBalls(1, 0, 0, r[1], canvas);
				} else {
					throwBalls(0, 0, 0, r[0], canvas);
					throwBalls(1, 0, 0, r[1], canvas);
					throwBalls(2, 0, 0, r[2], canvas);
					throwBalls(3, 0, 0, r[3], canvas);
					throwBalls(4, 0, 0, r[4], canvas);
				}

				// if stopflag value is true then it will refresh the panel else
				// it will stop the game
				if (stopFlag == true) {
					panel.invalidate();
				} else {
					if (score > highScore) {

						SharedPreferences.Editor editor = shPreferences.edit();
						editor.putInt("highscore", score);
						editor.commit();

						AlertDialog.Builder b = new AlertDialog.Builder(
								MainPage.this);
						AlertDialog a = b.create();
						a.setMessage("Congratulations !! You have a High Score.");

						a.setButton("OK",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface arg0,
											int arg1) {
										Intent intent = MainPage.this
												.getIntent();
										startActivity(intent);
										finish();
									}
								});
						a.show();
					}

					vibrator.vibrate(500);
					paint.setTextSize(50);
					paint.setColor(Color.RED);
					// paint.setStyle(Style.FILL);
					paint.setTextAlign(Paint.Align.CENTER);
					canvas.drawText("Game Over", width / 2, height / 2, paint);
				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
				ShowMessage("error in ondraw : ", e.toString());
			}

		}

		/**
		 * this method will take random values to throw ball at different
		 * inclinations
		 *
		 * @param id
		 * @param x_axis
		 * @param y_axis
		 * @param radius
		 * @param canvas
		 */
		public void throwBalls(int id, float x_axis, float y_axis, int radius,
				Canvas canvas) {

			random = new Random();
			if (flagBallOut[id] == true) {

			} else {
				randomFloat[0] = 1;
				randomFloat[1] = 2;
				randomFloat[2] = 3;
				randomFloat[3] = 4;
				randomFloat[4] = (float) 2.5;

				randomValue[0] = (float) 0.1;
				randomValue[1] = (float) 0.2;
				randomValue[2] = (float) 0.3;
				randomValue[3] = (float) 0.4;
				randomValue[4] = (float) 0.5;
			}

			distance[id] = (float) Math
					.sqrt((x + 10 - x1[id]) * (x + 10 - x1[id])
							+ (y + 14 - y1[id]) * (y + 14 - y1[id]));

			// code for the condition that if the ball touches the man then man
			// dies
			if (distance[id] < r[id] + 14) {
				flagDead = true;
			} else {
				flagDead = false;
			}

			// code for bouncing the ball from both the top and bottom edges
			if (y1[id] >= height - radius) {
				flagBounce[id] = true;
				if (flagDead == true) {
					tempImage = dead;

				}
			}
			if (y1[id] <= 20) {
				flagBounce[id] = false;
			}

			if (flagBounce[id] == true) {
				if (randomFloat[id] + randomValue[id] < 1) {
					x1[id] = (x1[id] + (randomFloat[id] + randomValue[id]) + 2);
					y1[id] = y1[id] - speed;
				} else {
					x1[id] = (x1[id] + (randomFloat[id] + randomValue[id]));
					y1[id] = y1[id] - speed;
				}

			} else {
				y1[id] = y1[id] + speed;
				x1[id] = (x1[id] + (randomFloat[id] + randomValue[id]));
			}

			if (x1[id] > width) {
				flagBallOut[id] = true;
				r[id] = getRadius();
				randomFloat[id] = random.nextInt(2);
				randomValue[id] = random.nextFloat();
				x1[id] = 0;
				y1[id] = 0;
				score++;
				if (strDiff.equals("Easy")) {

				} else if (strDiff.equals("Medium")) {

				} else {

				}
				if (score > 50) {
					speed = speed + 1;
				} else if (score > 100) {
					speed = speed + 1;
				} else if (score > 150) {
					speed = speed + 1;
				}
				firstTimeFlag = false;
			}
			// this code will finally draw the circle
			canvas.drawCircle(x1[id], y1[id], radius, paint);
		}

		/**
		 * this method will take random numbers for the radius
		 *
		 * @return
		 */
		private int getRadius() {
			int rad[] = { 0, 10, 15, 20, 25, 30 };
			i = random.nextInt(6);
			return rad[i];
		}

	}

	/**
	 * this method shows messages
	 *
	 * @param title
	 * @param message
	 */
	public void ShowMessage(final String title, final String message) {
		handler.post(new Runnable() {

			public void run() {
				// Looper.prepare();
				AlertDialog.Builder b = new AlertDialog.Builder(MainPage.this);
				AlertDialog a = b.create();
				a.setTitle(title);
				a.setMessage(message);
				a.setButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface arg0, int arg1) {

					}
				});

				a.show();

			}
		});
	}

	/**
	 * this method shows messages
	 *
	 * @param title
	 * @param message
	 */
	public void ShowConfirmation() {
		handler.post(new Runnable() {

			public void run() {
				// Looper.prepare();
				AlertDialog.Builder b = new AlertDialog.Builder(MainPage.this);
				AlertDialog a = b.create();
				a.setMessage("Are you sure, you want to Exit Game?");

				a.setButton("New Game", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface arg0, int arg1) {
						Intent intent = MainPage.this.getIntent();
						startActivity(intent);
						finish();
					}
				});

				a.setButton2("Exit Game",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface arg0, int arg1) {
								mp.stop();
								finish();
							}
						});

				a.setButton3("Cancel", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface arg0, int arg1) {

					}
				});

				a.show();
			}
		});
	}

	/**
	 * when the user clicks on home button or any time the on pause method of
	 * the activity is called at that time game is closed and user will be
	 * redirected to the menu page of the game
	 */
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Log.i("Called", "Home");
		mp.stop();
		finish();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		if (event.getAction() == MotionEvent.ACTION_UP) {
			Log.i("Info", "touch called");
			ShowConfirmation();
		}
		return super.onTouchEvent(event);
	}
}