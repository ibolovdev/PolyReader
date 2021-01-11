package com.ibo_android.polyreader;

import java.util.ArrayList;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.app.Activity; 
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.text.Editable;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class SavePlaylistActivity extends Activity {
		
	private ArrayList<Parcelable> selfiles_asparam;
	private MyDB _dba;
	EditText etPlaylistName;
	Editable sPlaylistName;
	
	private ListView playlists;
	private SavePlaylistAdapter spa;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	//	MainActivity.initThemeBehaviour(this);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_save_playlist);
		
	//TO DO - find a way to work WITH screen orienation
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
				
		
		  selfiles_asparam = this.getIntent()
				.getParcelableArrayListExtra("docs_list");
		  
		  
		  CharSequence[]  readlists_asparam = this.getIntent()
					.getCharSequenceArrayExtra("READLISTS");		
		
		  etPlaylistName = (EditText)  findViewById(R.id.txtEnterPlaylistName);
		  
		  _dba = new MyDB(this);
	      _dba.open();
	        
		  mProgress  =  (ProgressBar) findViewById(R.id.pbSavePlaylist);
		  
		  mProgress.setVisibility(View.INVISIBLE);
		 spa = new SavePlaylistAdapter(this, readlists_asparam);
			playlists = (ListView) findViewById(R.id.lvShowPlaylist);
			playlists.setAdapter(spa);
						
			SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);			
			 //int  TextSize = prefs.getInt("text_size", 12);
			 //etPlaylistName.setTextSize(TextSize);
			 //TextView tv = (TextView)  findViewById(R.id.txtPlaylistName);
			 //tv.setTextSize(TextSize);
		  
		  Button btSavePalylist = (Button) findViewById(R.id.btSavePalylist);
		 btSavePalylist.setOnClickListener(new OnClickListener() {
		    	  public void  onClick(View v)
		    	  {  	    		  
		    		   SavePlaylist();		    		  
		    	  }
		       }
		       );  		 
		 
		 Button btCancel = (Button) findViewById(R.id.btCancel1);
		 btCancel.setOnClickListener(new OnClickListener() {
		    	  public void  onClick(View v)
		    	  {  	    		  
		    		   
		    		 if (!(savingthread == null))
		    		 {		    			 
		    			 savingthread.interrupt();		    			
		    		 }   		  
		    		 finish();
		    	  }
		       }
		       ); 
		 
		 
		   if (this.getLastNonConfigurationInstance() != null)//the updating of the dialog does not work, the code in the thrads work but i do not see any update	  
		    {    	
		    	ArrayList<Object> al = (ArrayList<Object>) getLastNonConfigurationInstance();
		    	
		    	if ( al.get(0) instanceof Thread)
		    	{
		    		this.savingthread =(Thread) al.get(0);
		    		
		    		if (savingthread.isAlive())
		    		{   			
		    			//Dialog dlg = CreateSavePlaylistProgressBarDialog();
						//dlg.show();
						//progressBarDlg = dlg;
						// mProgress.setMax(fa.mfiles.size());
		    		}   		
		    		
		    	}
		    	
		    } 
		 		 
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#getLastNonConfigurationInstance()
	 */
	@SuppressWarnings("deprecation")
	@Override
	public Object getLastNonConfigurationInstance() {
		// TODO Auto-generated method stub
		return super.getLastNonConfigurationInstance();
			
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onRetainNonConfigurationInstance()
	 */
	@SuppressWarnings("deprecation")
	@Override
	public Object onRetainNonConfigurationInstance() {
		// TODO Auto-generated method stub
		
		
		ArrayList<Object> al = new ArrayList<Object>();
		if (savingthread != null)
			if (savingthread.isAlive())
				al.add(savingthread);		
		 	 
		
		if (al.size() > 0)
			return al;	
		
		return super.onRetainNonConfigurationInstance();
	}




	/* (non-Javadoc)
	 * @see android.app.Activity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		
		 if (!(savingthread == null))
		 {		    			 
			 savingthread.interrupt();		    			
		 }   		
		 
		 
		 _dba.close();
		 spa = null;
	}




	private ProgressBar mProgress;
	Thread savingthread;
	 Integer mProgressStatus = 0;
	 String PlaylistNameToSave = "";
	private void SavePlaylist()
	{
		mProgress.setVisibility(View.VISIBLE);	
		sPlaylistName= etPlaylistName.getText();		
	 	mProgress.setMax(selfiles_asparam.size());		
		
	 	
	 	if ( sPlaylistName.length() == 0 )
		{
	 		if ( spa.selectedplaylist != "" )
	 		{
	 			PlaylistNameToSave = spa.selectedplaylist;
	 		}
	 		else
	 		{
	 			Toast.makeText(this,"no name given", Toast.LENGTH_LONG).show();
	 			return;
	 		}			
		}
	 	else
	 	{
	 		PlaylistNameToSave = sPlaylistName.toString();
	 	}
			 
			_dba.deletePlaylist(PlaylistNameToSave);	
				    		    
				// Start lengthy operation in a background thread
		       savingthread = new Thread(new Runnable() {
		             public void run() {
		                 
		            	
		            	   mProgressStatus = 0;
		            	   
		            	for (Object odf: selfiles_asparam.toArray()) 
						{						
								DocFile df = (DocFile) odf;
								mProgressStatus = mProgressStatus + 1;
		 					_dba.insertEntry(df.title, 
		 									 df.filepath, 
		 									 PlaylistNameToSave,
		 									 df.pagenumber.toString(),
		 									 Float.toString(df.page_scale), 
		 									 df.cpage_height.toString(),
		 									 mProgressStatus.toString());
		 					
		 					
		 					 runOnUiThread (new Thread(new Runnable() { 
		 				         public void run() {
		 				           
		 				        	if (mProgress==null)
		 				        	{
		 				        		
		 				       		  mProgress.setMax(selfiles_asparam.size());
		 				       		//dlg.show(); //this works
		 				       			mProgress.setProgress(mProgressStatus);
		 				        		
		 				        	}
		 				        	else
		 				        	{
		 				        		mProgress.setProgress(mProgressStatus);
		 				        	} 	 
		 				        	 //if (mProgress != null)
		 				        	//	 mProgress.setProgress(mProgressStatus);
		 				        	
		 				         }
		 				     }));
		 				}       	     
		        	     
		        	     runOnUiThread (new Thread(new Runnable() { 
	 				         public void run() {
	 				              
	 				        	//progressBarDlg.dismiss();
	 				        	finish();
	 				        	
	 				        	
	 				         }
	 				     }));
		        	    
		            	 
		             }
		         });     
		      savingthread.start();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.save_playlist, menu);
		return true;
	}

}
