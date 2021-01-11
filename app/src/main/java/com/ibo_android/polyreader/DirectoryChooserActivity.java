package com.ibo_android.polyreader;

import com.ibo_android.polyreader.R;

import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.View; 
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
 

public class DirectoryChooserActivity extends Activity {	
	
	SharedPreferences prefs;
	private ListView fileslist;
	private DirectoryChooserAdapter psa;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		//MainActivity.initThemeBehaviour(this);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_directory_chooser);		
		
		 String MUSIC_DIR = "" ;//= "/music/comp/";  
	          prefs = PreferenceManager.getDefaultSharedPreferences(this);
	        
	        //MUSIC_DIR = prefs.getString("init_dir", "");
	       // MUSIC_DIR = MUSIC_DIR + "/comp/";	        
	       
	        
	        MUSIC_DIR = prefs.getString("init_dir", "");
	       // MUSIC_DIR = MUSIC_DIR ;//+ "/comp/";
	        
	        if (MUSIC_DIR == "")
	        {
	        	MUSIC_DIR = "/music/";
	        	MUSIC_DIR = Environment.getExternalStorageDirectory()
				.getAbsolutePath() + MUSIC_DIR;

				//MUSIC_DIR = this.getExternalFilesDir(null).getAbsolutePath() + MUSIC_DIR;

	        }
	        
	     //   String MusicDir = Environment.getExternalStorageDirectory()				
				//	.getAbsolutePath() + MUSIC_DIR;
	        
		
		psa = new DirectoryChooserAdapter(this, MUSIC_DIR );

		fileslist = (ListView) findViewById(R.id.listViewDirectories);

		fileslist.setAdapter(psa);		 
		
		/* fileslist.setOnItemClickListener(new OnItemClickListener() {
	    	  
				public void onItemClick(AdapterView<?> parent, View v, int pos,
						long id) {
					MusicFile mf = psa.PlayStop(v);
					
					Intent i = new Intent( );
					i.putExtra("singer", mf.filepath );				  
								
				setResult(RESULT_OK,i);
				finish();
					
				}
		       }
		       );*/	

		
		 Button btOK = (Button) findViewById(R.id.btOK);	        
		 btOK.setOnClickListener(new View.OnClickListener() {
				
				public void onClick(View v) {
					 
						Intent i = new Intent(  );						
					
						if (psa.selectedDirectory != "")
						{
							SharedPreferences.Editor editor = prefs.edit();
							editor.putString("init_dir_test",psa.selectedDirectory) ;
							editor.putString("init_dir",psa.selectedDirectory) ;
							editor.commit();
						}
						
					setResult(RESULT_OK,i);
					finish();
				}
			}); 
		 
		 
		 
		 Button btCancel = (Button) findViewById(R.id.btCancel);	        
		 btCancel.setOnClickListener(new View.OnClickListener() {
						
						public void onClick(View v) {
							 
							setResult(RESULT_CANCELED,null);
							finish();
							
						}
					}); 
		 
		 
		 Button btUP = (Button) findViewById(R.id.btDirUp);
			btUP.setOnClickListener(new View.OnClickListener() {

				public void onClick(View v) {
					psa.GoUp();
				}
			});
		
	}
	
	public void SetRootDirectoryTextView(String folder)
	{		
		TextView txtRootDirectory = (TextView) findViewById(R.id.tvDirRoot);
		txtRootDirectory.setText(folder);

		int  TextSize = prefs.getInt("text_size", 12);
		txtRootDirectory.setTextSize(TextSize);
	}

	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// useful in changing orientation
		// using the back it will not be called
		super.onSaveInstanceState(outState);

		if (!(psa.selectedDirectory == null)) {
			outState.putString("SELECTED_DIRECTORY", psa.selectedDirectory);
		}

	}// onSaveInstanceState

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onRestoreInstanceState(savedInstanceState);

		if (savedInstanceState != null) {
			if (savedInstanceState.get("SELECTED_DIRECTORY") != null) {
				String selecteddirectory = savedInstanceState
						.getString("SELECTED_DIRECTORY");
				
				psa.selectedDirectory = selecteddirectory;				

			}

		}

	}// onRestoreInstanceState
	
	
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}
	
	 

}
