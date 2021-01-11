package com.ibo_android.polyreader;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.ibo_android.polyreader.DocsAdapter.DocFileViewHolder;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.RadioButton;


//icon from rosetta stone , like it was to be,rejected looks like a dick
 
//settings screen //problem1.when an action from an intent is the same with another action is the same 
//then the activity of a foreign app can be enabled. the solution to this is specifyng the 
//component name but i can get it to work
//2.problem2 i cannot call an activity from another package, from settings 
//number selector component

//make filepicker activity parametric , to be used as an component,cannot do that


//orientation change, ok
//keep books list when i leave, showlists, later
//
//keep page number, bookmark, implementation in preferences or sqlite,ok
//communicate the page to the app, ok
//web buttons size and icons, ok
//manually change the height of the web view,problem with the last one, ok
//test it on different screens
//change orientation in listview, web buttons
//add a button in the html page to appear/disappear the rest, ok
//ability to insert notes
//readlist, ok
//margin to the right as well, make it preference, ok
//remote debugging with chrome, ok


//25-11-16
//problem with the keyboard when manually trying to insert a web page
//manually insert page number
//make page_step a preference
//problem in number picker when you select from numpad
//bookmarks
//load a web page

//add a wait when a page still loads

//update mozilla code, ok
//examine mozilla code

//difference between webchromeclient/webviewclient
//web view investigation
//Comparison of e-book formats - wikipedia
//Comparison of e-book readers - wikipedia
//memory management - try to load a very large file
//load an html file
//create a marker
//highlight every word
//make sizable the selectboxes, ok


//17-11-18
//pattern of reading - different colours, sizes etc
//change the position of the actions, i cannot see the words, ok

//26-9-20







public class MainActivity extends Activity {
	
	private static final int SHOW_PREFERENCES = 1;	
	private DocsAdapter da;
	private ListView docslist;
	public static final int GET_FILES_FOLDER = 1;
	public static final int GET_FILES = 2;
	public static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 3;
	public static final int ADD_FILES = 4;
	ArrayList<DocFile> dfiles;
	public static final String NUMBER_OF_SCREENS = "NUMBER_OF_SCREENS_TO_SHOW"; 
	SharedPreferences prefs ;
	private static final String DOCS_CATALOG = "DOCS_CATALOG";
	private static final int DELETE_PLAYLISTS = 7;
	private static final int GET_PLAYLIST = 5;
	
	private MyDB _dba;
	private String _LatestPlaylistLoaded = "";
	
	 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		RequestPermissions();
		
		prefs = PreferenceManager.getDefaultSharedPreferences(this);		
		//int  NumberOfScreens = prefs.getInt(NUMBER_OF_SCREENS, 2);
		
		//FOR DEBUG
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
		{
		    	WebView.setWebContentsDebuggingEnabled(true);
		}
		
		dfiles = new ArrayList<DocFile>();
		_dba = new MyDB(this);
		_dba.open();
		
		/* for(int i=1; i<=NumberOfScreens; i++)
		 {
				dfiles.add(new DocFile(Integer.toString(i),"","",1,1,600));
        }*/
				
		da = new DocsAdapter(this,dfiles,_dba);
		
		docslist = (ListView) findViewById(R.id.lvItems);
		docslist.setAdapter(da);
		da._fileslist = docslist;
		
		
		docslist.setPadding(20, 0, 20, 0);
		
	}
	
	
	private void RequestPermissions()
	{		
		if (Build.VERSION.SDK_INT < 23)
		{
			return;			
		}
		
		
		 
		// Here, thisActivity is the current activity
		if (ContextCompat.checkSelfPermission(this,
		                Manifest.permission.WRITE_EXTERNAL_STORAGE)
		        != PackageManager.PERMISSION_GRANTED) {

		    // Should we show an explanation?
		    if (ActivityCompat.shouldShowRequestPermissionRationale(this,
		            Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

		        // Show an expanation to the user *asynchronously* -- don't block
		        // this thread waiting for the user's response! After the user
		        // sees the explanation, try again to request the permission.

		    } else {

		        // No explanation needed, we can request the permission.

		        ActivityCompat.requestPermissions(this,
		                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
		                MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);

		        // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
		        // app-defined int constant. The callback method gets the
		        // result of the request.
		    }
		}
			
		
	}//RequestPermissions
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onRequestPermissionsResult(int, java.lang.String[], int[])
	 */
	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		// TODO Auto-generated method stub
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		
		 switch (requestCode) {
	        case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE: {
	            // If request is cancelled, the result arrays are empty.
	            if (grantResults.length > 0
	                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

	                // permission was granted, yay! Do the
	                // contacts-related task you need to do.

	            } else {

	                // permission denied, boo! Disable the
	                // functionality that depends on this permission.
	            }
	            return;
	        }

	        // other 'case' lines to check for other
	        // permissions this app might request
	    }
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
				
		
		switch (item.getItemId()) {
		case R.id.add_files:
			AddFiles();
			return true;
		case R.id.clear_screen:
			ClearScreen();
			return true;
		case R.id.load_playlist:
			// LoadPlaylist();
			LoadPlaylistWithActivities();
			return true;
		case R.id.save_playlist:
			PromptAndSaveReadlistWithActivities();
			// PromptAndSavePlaylist();
			// PromptAndSavePlaylistWithFragments();
			return true;
		case R.id.delete_playlist:
			DeletePlaylists();
			return true;
		case R.id.action_settings:
			ShowSettings();
			return true;
	 
		default:
			return super.onOptionsItemSelected(item);
		
		}	
		
	}
		
	
	
	private void ClearScreen() 
	{		
		_LatestPlaylistLoaded = "";
		dfiles.clear();		
		da.notifyDataSetChanged();	
		
	}// ClearScreen
	
	
		private void AddFiles() 
		{
				
			 String MusicDir1 = prefs.getString("init_dir", "");//Environment.getExternalStorageDirectory().getAbsolutePath();
				// .getAbsolutePath() + MUSIC_DIR1;
				 
				 if (MusicDir1 == "") {
					 
					 MusicDir1 = Environment.getExternalStorageDirectory()
								.getAbsolutePath() ;
					}

			Intent i = new Intent(this, FilePickerActivity.class);
			i.putExtra("root_dir", MusicDir1);

			startActivityForResult(i, ADD_FILES);

		}// AddFiles
		
		
		private CharSequence[] GetAllReadlists() {
			List<String> pls = new ArrayList<String>();

			Cursor c = _dba.ShowReadlists();

			if (c.moveToFirst()) {
				do {
					String playlist = c.getString(c
							.getColumnIndex(MyDBHelper.READLIST_COL));
					pls.add(playlist);
				} while (c.moveToNext());

			}

			CharSequence[] csPlaylists = pls.toArray(new CharSequence[pls.size()]);

			return csPlaylists;
		}
		
		private void PromptAndSaveReadlistWithActivities() {
			CharSequence[] csReadlists = GetAllReadlists();

			Intent i = new Intent(MainActivity.this, SavePlaylistActivity.class);

			i.putExtra("docs_list", da.dfiles);
			i.putExtra("READLISTS", csReadlists);
			startActivity(i);
		}
		
		
		private void DeletePlaylists()
		{
			Intent i = new Intent(MainActivity.this, DeletePlaylistsActivity.class);
			i.putExtra("PLAYLISTS", GetAllReadlists());
			startActivityForResult(i, DELETE_PLAYLISTS);
		}
		
		
		private void LoadPlaylistWithActivities() {

			List<String> pls = new ArrayList<String>();

			Cursor c = _dba.ShowReadlists();

			if (c.moveToFirst()) {
				do {
					String playlist = c.getString(c
							.getColumnIndex(MyDBHelper.READLIST_COL));
					pls.add(playlist);
				} while (c.moveToNext());

			}

			CharSequence[] csPlaylists = pls.toArray(new CharSequence[pls.size()]);
			Intent i = new Intent(MainActivity.this, LoadPlaylistActivity.class);
			i.putExtra("PLAYLISTS", csPlaylists);

			startActivityForResult(i, GET_PLAYLIST);

		}
		
	
	private void ShowSettings()
	{
		Intent i = new Intent(this, SettingsActivity.class);
		startActivityForResult(i, SHOW_PREFERENCES);		
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
				
		if (requestCode == GET_FILES) {

			if (resultCode == RESULT_OK) {
											
				ArrayList<Parcelable> selectedfiles = data
						.getParcelableArrayListExtra("files_list");
				
				DocFile applied_doc_file = (DocFile) data.getParcelableExtra("_applied_doc_file");
				 	
				boolean bApplieddocassigned = false;
				for (Object odf : selectedfiles.toArray()) {
					DocFile typed_df = (DocFile) odf;
										
					File fu = new File(typed_df.filepath);
					if (!fu.isDirectory())
					{				
						
						for (DocFile df : dfiles ) 
						{
							if (df.code.equals(applied_doc_file.code) && !bApplieddocassigned )
							{
								df.filepath = typed_df.filepath;
								df.title = typed_df.title;								
								
								DocFileViewHolder dvh = da.GetViewItem(df);
								da.ShowFile(dvh.mWV,  dvh.mURL, "", df,true);
								bApplieddocassigned = true;
								break;
								 
							}
							else
							{
								
								if (df.filepath.equals(""))
								{									
									df.filepath = typed_df.filepath;
									df.title = typed_df.title;									
									
									DocFileViewHolder dvh = da.GetViewItem(df);
									if ( dvh != null)
									{
										da.ShowFile(dvh.mWV,  dvh.mURL, "", df,true);	
									}									
									
									break;
									
								}//(df.filepath.equals(df.code))
								
							}//(df.code.equals(applied_doc_file.code) && !bApplieddocassigned )				
							
							
						}//df
						
					}//(!fu.isDirectory())

				}//odf										
											
				//docslist = (ListView) findViewById(R.id.lvItems);
			//	docslist.setAdapter(da);
			//	da._fileslist = docslist;		
				 

			} else if (resultCode == RESULT_CANCELED) {

			}
			
		} // (requestCode == GET_SONGS)
			
			
		
		if (requestCode == ADD_FILES) {

				if (resultCode == RESULT_OK) {
												
					ArrayList<Parcelable> selectedfiles = data
							.getParcelableArrayListExtra("files_list");
					
				//	DocFile applied_doc_file = (DocFile) data.getParcelableExtra("_applied_doc_file");
					 	
					boolean bApplieddocassigned = false;
					for (Object odf : selectedfiles.toArray()) {
						DocFile typed_df = (DocFile) odf;
											
						File fu = new File(typed_df.filepath);
						if (!fu.isDirectory())
						{				
							
							
							
							dfiles.add(new DocFile(Integer.toString(dfiles.size() + 1 ), typed_df.title, typed_df.filepath,1,1,600));
							
							//Collections.sort(fa.mfiles);
							
														
							//docslist = (ListView) findViewById(R.id.lvItems);
						//	docslist.setAdapter(da);
							//da._fileslist = docslist;
							
						
						/*	for (DocFile df : dfiles ) 
							{
								
								
								
								if (df.code.equals(applied_doc_file.code) && !bApplieddocassigned )
								{
									df.filepath = typed_df.filepath;
									df.title = typed_df.title;								
									
									DocFileViewHolder dvh = da.GetViewItem(df);
									da.ShowFile(dvh.mWV,  dvh.mURL, "", df,true);
									bApplieddocassigned = true;
									break;
									 
								}
								else
								{
									
									if (df.filepath.equals(""))
									{									
										df.filepath = typed_df.filepath;
										df.title = typed_df.title;									
										
										DocFileViewHolder dvh = da.GetViewItem(df);
										if ( dvh != null)
										{
											da.ShowFile(dvh.mWV,  dvh.mURL, "", df,true);	
										}									
										
										break;
										
									}//(df.filepath.equals(df.code))
									
									
									
								}//(df.code.equals(applied_doc_file.code) && !bApplieddocassigned )
													
								
							}//df*/
							
							
							
							
							
						}//(!fu.isDirectory())

					}//odf										
												
					//docslist = (ListView) findViewById(R.id.lvItems);
				//	docslist.setAdapter(da);
				//	da._fileslist = docslist;		
				
					docslist = (ListView) findViewById(R.id.lvItems);
					docslist.setAdapter(da);
					da._fileslist = docslist;
					
					da.notifyDataSetChanged();

				} else if (resultCode == RESULT_CANCELED) {

				}
						

		} // (requestCode == ADD_FILES)
		
		
		if (requestCode == GET_PLAYLIST) {//2104978481

			if (resultCode == RESULT_OK) {
				ArrayList<String> selectedplaylists = data
						.getStringArrayListExtra("playlist_list");

				if (!(selectedplaylists == null))
					if (selectedplaylists.size() > 0) {
						da.getdocfilesFromReadlist(selectedplaylists.get(0));
						StoreLatestPLaylistLoaded(selectedplaylists.get(0));
					}

			} else if (resultCode == RESULT_CANCELED) {

			}

		} // (requestCode == GET_SONGS)

		 

		if (requestCode == DELETE_PLAYLISTS) {

			if (resultCode == RESULT_OK) {
				ArrayList<String> pls = data
						.getStringArrayListExtra("playlist_list");
								
				_dba.deleteReadLists(pls);
								
			} else if (resultCode == RESULT_CANCELED) {

			}

		} // (requestCode == DELETE_PLAYLISTS)
		
		
		if (requestCode == SHOW_PREFERENCES)
		{
			 if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) 
			 {
				 recreate();
			 }
			
		 }// (requestCode == SHOW_PREFERENCES) {		
		
	}//onActivityResult
	
	
	private void StoreLatestPLaylistLoaded(String playlist) {
		_LatestPlaylistLoaded = playlist;
	}
	
	
	@Override
	protected void onSaveInstanceState(Bundle outState) 
	{
		// useful in changing orientation
		// using the back it will not be called
		super.onSaveInstanceState(outState);

		if ((da != null) && !(da.dfiles == null)) {
			outState.putParcelableArrayList(DOCS_CATALOG, da.dfiles);
		}
	
	}// onSaveInstanceState

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);

		if (savedInstanceState != null)
		{	 
				if (savedInstanceState.get(DOCS_CATALOG) != null)
				{
					ArrayList<Parcelable> selecteddocs = savedInstanceState
							.getParcelableArrayList(DOCS_CATALOG);
		
					this.da.ChangeDatasource(selecteddocs);
				}	 

		}

	}// onRestoreInstanceState


	public static void ApplySelectorsSize(Context con, CompoundButton mCheck) throws Exception
	{

		String SelectorType = "";
		if (mCheck instanceof CheckBox)
		{
			SelectorType = "check";
		}
		else if (mCheck instanceof RadioButton)
		{
			SelectorType = "radio";
		}
		else
		{
			throw new Exception("invalid selector type");
		}

		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(con);


		//problem when nothing was entered
		String  SelectorSizeString = prefs.getString("settings_selectorSize", "");
		String[]  selectedThemeArr =    SelectorSizeString.split(",");
		if (selectedThemeArr != null)
			if (selectedThemeArr.length > 0)
				SelectorSizeString = selectedThemeArr[0];

		int  SelectorSize =Integer.parseInt(SelectorSizeString);


		if (MainActivity.GetThemeBehaviour(con) == "black")
		{

			switch (SelectorSize) {
				case 1:

					if  (SelectorType == "check")
					{
						mCheck.setButtonDrawable(R.drawable.checkbox_small_black);
					}
					else if  (SelectorType == "radio")
					{
						mCheck.setButtonDrawable(R.drawable.radiobutton_small_black);
					}


					break;

				case 2:

					if  (SelectorType == "check")
					{
						mCheck.setButtonDrawable(R.drawable.checkbox_medium_black);
					}
					else if  (SelectorType == "radio")
					{
						mCheck.setButtonDrawable(R.drawable.radiobutton_medium_black);
					}


					break;

				case 3:


					if  (SelectorType == "check")
					{
						mCheck.setButtonDrawable(R.drawable.checkbox_large_black);
					}
					else if  (SelectorType == "radio")
					{
						mCheck.setButtonDrawable(R.drawable.radiobutton_large_black);
					}


					break;

				case 4:

					if  (SelectorType == "check")
					{
						mCheck.setButtonDrawable(R.drawable.checkbox_extralarge_black);
					}
					else if  (SelectorType == "radio")
					{
						mCheck.setButtonDrawable(R.drawable.radiobutton_extralarge_black);
					}

					break;


				default:


					//mCheck.setButtonDrawable(R.drawable.checkbox_extralarge_black);
					break;

			}

			//holder.mCheck.setButtonDrawable(R.drawable.checkbox_extralarge_black);


		}
		else
		{

			switch (SelectorSize) {
				case 1:

					if  (SelectorType == "check")
					{
						mCheck.setButtonDrawable(R.drawable.checkbox_small);
					}
					else if  (SelectorType == "radio")
					{
						mCheck.setButtonDrawable(R.drawable.radiobutton_small);
					}


					break;

				case 2:

					if  (SelectorType == "check")
					{
						mCheck.setButtonDrawable(R.drawable.checkbox_medium);
					}
					else if  (SelectorType == "radio")
					{
						mCheck.setButtonDrawable(R.drawable.radiobutton_medium);
					}


					break;

				case 3:

					if  (SelectorType == "check")
					{
						mCheck.setButtonDrawable(R.drawable.checkbox_large);
					}
					else if  (SelectorType == "radio")
					{
						mCheck.setButtonDrawable(R.drawable.radiobutton_large);
					}


					break;

				case 4:

					if  (SelectorType == "check")
					{
						mCheck.setButtonDrawable(R.drawable.checkbox_extralarge);
					}
					else if  (SelectorType == "radio")
					{
						mCheck.setButtonDrawable(R.drawable.radiobutton_extralarge);
					}

					break;


				default:

					//holder.mCheck.setButtonDrawable(R.drawable.checkbox_extralarge);
					break;

			}
		}

		//holder.mCheck.setButtonDrawable(R.drawable.checkbox_extralarge);

		//holder.mCheck.setScaleX(2f);
		//holder.mCheck.setScaleY(2f);

		// Get the margins of Flex CheckBox
		ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) mCheck.getLayoutParams();

		// Set left, top, right and bottom margins of Flex CheckBox
		mlp.setMargins(25,15,10,10);
		mCheck.setLayoutParams(mlp);

		// Apply right padding of Flex CheckBox
		mCheck.setPadding(0,0,50,0);

	}

	public static String GetThemeBehaviour(Context con)
	{
		return "";

		/*if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.HONEYCOMB)
			return "";

		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(con);
		String  selectedTheme = prefs.getString( "settings_theme", "");

		String[]  selectedThemeArr =    selectedTheme.split(",");
		if (selectedThemeArr != null)
			if (selectedThemeArr.length > 0)
				selectedTheme = selectedThemeArr[0];

		if (selectedTheme.equals("0"))
			return "white";

		if (selectedTheme.equals("1"))
			return "black";

		return "";*/

	}

}
