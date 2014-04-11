package com.example.scroller;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

public class Menu_Activity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu);
		// Show the Up button in the action bar.
		setupActionBar();
		
		populateFields();
	}

	private void populateFields(){
		ScrollerApplication app = (ScrollerApplication) getApplication();
		//get the data entry fields and populate them with their current values
		//in case their values were altered but not saved last time.
		EditText edit = (EditText) findViewById(R.id.scroll_text_box);
		edit.setText(app.getText());
		
		edit = (EditText) findViewById(R.id.text_color_box);
		edit.setText("#" + Integer.toHexString(app.getTextColor()));
		
		edit = (EditText) findViewById(R.id.bg_color_box);
		edit.setText("#" + Integer.toHexString(app.getBgColor()));
		
		edit = (EditText) findViewById(R.id.scroll_speed_box);
		edit.setText(Integer.toString(app.getSpeed()));
		
		CheckBox box = (CheckBox) findViewById(R.id.auto_checkbox);
		box.setChecked(app.getAutoScroll());
	}
	
	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	//this method called by the 'update' button
	public void scrollText(View view){
		boolean send = true;
		//temporary storage for the settings
		String text;
		int bgColor = 0, textColor = 0, speed = 0;
		boolean auto;
		
		//get the text to display
		EditText editText = (EditText) findViewById(R.id.scroll_text_box);
		text = editText.getText().toString();
		
		//get the text from the text color EditText
		editText = (EditText) findViewById(R.id.text_color_box);
		String textColorString = editText.getText().toString();
		try{
			//try parsing the text to a color's int
			textColor = Color.parseColor(textColorString);
		} catch (IllegalArgumentException e){
			//inform the user and prevent updating if it's invalid
			editText.setText("Invalid color");
			send = false;
		}
		
		//get the text from the background color EditText
		editText = (EditText) findViewById(R.id.bg_color_box);
		String bgColorString = editText.getText().toString();
		try{
			//try parsing the text to a color's int
			bgColor = Color.parseColor(bgColorString);
		} catch (IllegalArgumentException e){
			//inform the user and prevent updating if it's invalid
			editText.setText("Invalid color");
			send = false;
		}
		
		//get the state of the autoScroll checkbox
		CheckBox autoCheckBox = (CheckBox) findViewById(R.id.auto_checkbox);
		auto = autoCheckBox.isChecked();
				
		//get the text from the scroll speed EditText
		editText = (EditText) findViewById(R.id.scroll_speed_box);
		String speedString = editText.getText().toString();
		try{
			//try parsing the text to an int
			speed = Integer.parseInt(speedString);
			//ensure the speed's positive
			if(speed <= 0){
				//inform the user and prevent updating if it's invalid
				editText.setText("Speed must be positive");
				send = false;
			}
		} catch (NumberFormatException e){			
			//inform the user and prevent updating if it's invalid
			editText.setText("Invalid speed");
			send = false;
		}

		//if the inputs are valid, update the application and close this activity.
		//when the display activity resumes it will get the settings from the application
		if(send){
			ScrollerApplication app = (ScrollerApplication) getApplication();			
			app.setText(text);
			app.setTextColor(textColor);
			app.setBgColor(bgColor);
			app.setAutoScroll(auto);
			app.setSpeed(speed);
			onBackPressed();
		}
	}
	
}
