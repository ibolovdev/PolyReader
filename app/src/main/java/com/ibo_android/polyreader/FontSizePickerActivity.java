package com.ibo_android.polyreader;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;

public class FontSizePickerActivity extends Activity {

	SharedPreferences prefs;
	int selected_val = 12;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_number_picker);
				
		 prefs = PreferenceManager.getDefaultSharedPreferences(this);		    
		    
		 NumberPicker np = (NumberPicker)findViewById(R.id.number_time_picker_numberpicker);
		 np.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
		 
		 int  TextSize = prefs.getInt("text_size", 12);//be carefull seek_duration is different from SEEK_DURATION
			  
			    np.setMinValue(0);// restricted number to minimum value i.e 1
			    np.setMaxValue(59);// restricked number to maximum value i.e. 31
			    np.setWrapSelectorWheel(true); 
			    np.setValue(TextSize);
			    selected_val = TextSize;
			     
			    
			    np.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() 
			    {
					public void onValueChange(NumberPicker picker, int oldVal,
							int newVal) {
						
						selected_val = newVal;
						 
					}
			   
			    });
			
			    Button btOK = (Button) findViewById(R.id.bt_number_time_picker_ok);	 
			    
				 btOK.setOnClickListener(new View.OnClickListener() {
						
						public void onClick(View v) {
							 //i cannot return to mainactivity, that's why i use preferences
								Intent i = new Intent();						
								SharedPreferences.Editor editor = prefs.edit();
								editor.putInt("text_size",selected_val) ;
								editor.commit();
														
								setResult(RESULT_OK,i);
								finish();
						}
					}); 		 
				 
				 
				 Button btCancel = (Button) findViewById(R.id.bt_number_picker_time_back);	        
				 btCancel.setOnClickListener(new View.OnClickListener() {
								
								public void onClick(View v) {
									 
									setResult(RESULT_CANCELED,null);
									finish();
									
								}
							}); 		
		
	}
	
	
	@Override
	protected void onSaveInstanceState(Bundle outState) 
	{
		// useful in changing orientation
		// using the back it will not be called
		super.onSaveInstanceState(outState);

		outState.putInt("text_size", selected_val);
	
	}// onSaveInstanceState

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onRestoreInstanceState(savedInstanceState);

		if (savedInstanceState != null) {

			if (savedInstanceState.get("text_size") != null)
			{
				selected_val = savedInstanceState.getInt("text_size");
				 NumberPicker np = (NumberPicker)findViewById(R.id.number_time_picker_numberpicker);
				 np.setValue(selected_val);				 
			} 

		}

	}// onRestoreInstanceState
	
	
}
