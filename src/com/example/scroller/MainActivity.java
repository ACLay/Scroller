package com.example.scroller;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity {

	private int scrollVelocity;
	private boolean autoScroll;
	private Scrollie scrollThread;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setupGUI();

		loadSettings();
		
	}
	
	private void loadSettings(){
		//get the stored info from the application and load into the GUI elements
		ScrollerApplication app = (ScrollerApplication) getApplication();
		TextView scrollText = (TextView) findViewById(R.id.textView);
		HorizontalScrollView scrollView = (HorizontalScrollView) findViewById(R.id.Scroll_Layout);
		
		scrollVelocity = app.getSpeed();
		autoScroll = app.getAutoScroll();
		
		scrollText.setText(app.getText());
		scrollText.setTextColor(app.getTextColor());
		
		scrollView.setBackgroundColor(app.getBgColor());
	
	}
	
	//some neat code for getting screen orientation
		//Integer rot = getResources().getConfiguration().orientation;
		//text.setText(rot.toString());
	
	@SuppressWarnings("deprecation")
	private void setupGUI(){
		setContentView(R.layout.activity_main);
		//get the size of the display, uses methods depricated in Android 3.2
		Display display = getWindowManager().getDefaultDisplay();
		Integer height = display.getHeight();
		Integer width = display.getWidth();
		
		//set the buffers sizes to fill the display
		LinearLayout leftBuffer = (LinearLayout) findViewById(R.id.leftBuffer);
		leftBuffer.setMinimumWidth(width);
		
		LinearLayout rightBuffer = (LinearLayout) findViewById(R.id.rightBuffer);
		rightBuffer.setMinimumWidth(width);
		
		//set the text size to use the whole display size
		TextView text = (TextView) findViewById(R.id.textView);
		text.setTextSize(height);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public boolean onKeyUp(int keyCode, KeyEvent event){
		//respond to menu button press
		
		if(keyCode == KeyEvent.KEYCODE_MENU){
			openMenu();
			return true;
		}
		return super.onKeyUp(keyCode, event);
	}


	protected void onResume(){
		super.onResume();
		
		loadSettings();
		//start the display scrolling
		scrollThread = new Scrollie();
		scrollThread.start();
	}
	protected void onPause(){
		super.onPause();
		//stop the scrolling thread from running
		if(scrollThread != null){
			scrollThread.terminate();
		}
	}

	private class Scrollie extends Thread{

		private boolean visible = true;
		@Override
		public void run() {
			//get the horizontalscrollview
			final HorizontalScrollView scrollView = (HorizontalScrollView) findViewById(R.id.Scroll_Layout);

			//create  a runnable that controls scrolling
			final Runnable runner = new Runnable(){
				//stores the number of pixels the display has been scrolled by
				int scroll = 0;
				
				public void run(){	
					//get the maximum number of pixels the display can scroll
					int maxScroll = getScrollWidth();
					//if it can still scroll, scroll by velocity pixels
					if(scroll < maxScroll){
						scrollView.scrollBy(scrollVelocity, 0);
						scroll += scrollVelocity;
					} else {
						//if not, scroll back to the start
						scrollView.scrollBy(- maxScroll, 0);
						scroll = 0;
					}
				}
			};
			
			//while the thread is running
			while(visible){
				//if autoscroll is enabled, send the scrollview the scroll runnable.
				if(autoScroll){
					//use this method to send runnables and prevent messing up the GUI thread
					scrollView.post(runner);
				}
				//sleep for a frame
				try {
					Thread.sleep(1000/60);
				} catch (InterruptedException e) {
					Log.e("error", "sleep messed up");
				}

			}
		}
		
		//get the size of the scrollable area
		private int getScrollWidth(){
			TextView text = (TextView) findViewById(R.id.textView);
			HorizontalScrollView scrollView = (HorizontalScrollView) findViewById(R.id.Scroll_Layout);
			//made of the size of the textview and the size of the display
			int textWidth = text.getMeasuredWidth();
			int screenWidth = scrollView.getWidth();
			
			return textWidth + screenWidth;
		}

		//toggle the boolean that controls the loop, causing the thread to close
		public void terminate(){
			visible = false;
		}

	}
	
	//respond to the on screen button being pressed
	public void menuButtonPressed(View v){
		openMenu();
	}
	
	//when the menu is set to open, stop the scrolling and start the menu activity
	public void openMenu(){
		scrollThread.terminate();
		Intent intent = new Intent(this, Menu_Activity.class);
		startActivity(intent);
	}

}
