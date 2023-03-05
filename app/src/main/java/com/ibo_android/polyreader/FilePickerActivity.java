package com.ibo_android.polyreader;

import java.io.File;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class FilePickerActivity extends Activity {


private ListView fileslist;

private FilePickerAdapter psa;
private String _root_dir = "";
private DocFile _applied_doc_file = null;


private static final String FILES_CATALOG = "FILES_CATALOG";
private static final String ROOT_DIRECTORY = "ROOT_DIRECTORY";

	SharedPreferences prefs;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		//MainActivity.initThemeBehaviour(this);
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_file_picker);

		prefs = PreferenceManager.getDefaultSharedPreferences(this);

		String InitialDir = this.getIntent().getStringExtra("root_dir");
		
		DocFile applied_doc_file = (DocFile) this.getIntent().getParcelableExtra("doc_file");
		_applied_doc_file = applied_doc_file;
		 
		
		 if (savedInstanceState != null)
			 _root_dir = savedInstanceState.getString(ROOT_DIRECTORY);				
		 
		
		if (_root_dir != "" && _root_dir != null)
			InitialDir = _root_dir;
	
		ArrayList<Parcelable> selfiles_asparam = this.getIntent()
				.getParcelableArrayListExtra("SELECTED_FILES");

		// Toast.makeText(this, sel, Toast.LENGTH_LONG).show();

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
		{
			psa = new FilePickerContentProviderStrategyAdapter(InitialDir, this, selfiles_asparam);
		}
		else
		{
			psa = new FilePickerAdapter(InitialDir, this, selfiles_asparam);
		}

		if (psa.dfiles.isEmpty())
		{
			Toast.makeText(this, R.string.no_files, Toast.LENGTH_LONG).show();
		}

		fileslist = (ListView) findViewById(R.id.lv_file_picker_Files);
	//	 Collections.sort(psa.mfiles);
		fileslist.setAdapter(psa);

		psa.fileslist = fileslist;
		

		//TextView tv = (TextView) findViewById(R.id.txtDirectory);
		
		//int  TextSize = prefs.getInt("text_size", 12);
		// tv.setTextSize(TextSize);

		 
		 
		Button btOK = (Button) findViewById(R.id.bt_file_picker_OK);
		btOK.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {

				Intent i = new Intent();
				
				for (Object omf : psa.selectedfiles.toArray()) {
					DocFile typedmf = (DocFile) omf;
					File fu = new File(typedmf.filepath);
					if (fu.isDirectory()) {
						psa.selectedfiles.remove(typedmf);
					}
				}				
				
				if (psa.selectedfiles.size() < 1000)//it seems that there is a limit on the size that a intent can have
				{					
					i.putExtra("files_list", psa.selectedfiles);
					i.putExtra("_applied_doc_file", _applied_doc_file);					
					
				}
				else
				{				
				 	MyApplicationObject mApplication = (MyApplicationObject)getApplicationContext();
					mApplication.setSelectedFiles(null);
					mApplication.setSelectedFiles(psa.selectedfiles);
					
					i.putExtra("files_list", "MyApplicationObject");
					i.putExtra("_applied_doc_file", _applied_doc_file);
				}			

				setResult(RESULT_OK, i);			
				finish();
			}
		});

		Button btCancel = (Button) findViewById(R.id.bt_file_picker_Cancel);
		btCancel.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {

				setResult(RESULT_CANCELED, null);
				finish();

			}
		});

		/*Button btCheckAll = (Button) findViewById(R.id.btCheckAll);
		btCheckAll.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				psa.SelectAll();
			}
		});*/

		/*Button btUnCheckAll = (Button) findViewById(R.id.btUnCheckAll);
		btUnCheckAll.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				psa.UnSelectAll();
			}
		});*/
		
		Button btUP = (Button) findViewById(R.id.bt_file_picker_UP);
		btUP.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				psa.GoUp();
			}
		});
				
	}
	
		
	
	public void SetRootDirectoryTextView(String folder)
	{		
		TextView txtRootDirectory = (TextView) findViewById(R.id.tv_file_picker_Folder);
		txtRootDirectory.setText(folder);

		int  TextSize = prefs.getInt("text_size", 12);
		txtRootDirectory.setTextSize(TextSize);
	}
	
	
	
/*	private void WriteParcelListToFile(ArrayList<MusicFile> selectedfiles)
	{
		

		String FILENAME = "selected_files.dd";
		 
		 
		FileOutputStream fos = null;
		try {
			fos = openFileOutput(FILENAME, Context.MODE_PRIVATE);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//fos.write(string.getBytes());
		//fos.close();
		
		
		//  fout = new FileOutputStream(file);

	        Parcel parcel = Parcel.obtain();
	        ArrayList<Object> list = new ArrayList<Object>(psa.selectedfiles);
	      //  Log.d(TAG, "write items to cache: " + items.size());
	        parcel.writeList(list);
	        
	      
	        byte[] data = parcel.marshall();
	        try {
				fos.write(data);		
				
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        finally
	        {
	        	try {
					fos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        	
	        }	
		
	}*/

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == MainActivity.GET_FILES_FOLDER) {

			if (resultCode == RESULT_OK)// TODO should remove files also
			{
				ArrayList<Parcelable> selectedsongs = data
						.getParcelableArrayListExtra("files_list");

				for (Object omf : selectedsongs.toArray()) {
					psa.selectedfiles.add((DocFile) omf);
				}

				// fileslist = (ListView) findViewById(R.id.lvSongsList);
				// fileslist.setAdapter(psa);//filelist is null

				// psa._fileslist = fileslist;

			} else if (resultCode == RESULT_CANCELED) {

			}

		} // (requestCode == GET_SONGS)

	}// onActivityResult

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		
		psa.fileslist = null;
		psa._act = null;
		psa = null;
	}

/*	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_songs_picker, menu);
		return true;
	}*/

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// useful in changing orientation
		// using the back it will not be called
		super.onSaveInstanceState(outState);

		if (!(psa.selectedfiles == null)) {
			outState.putParcelableArrayList(FILES_CATALOG, psa.selectedfiles);
		}
		
		if (psa.getRootDirectory() != "") {
			outState.putString(ROOT_DIRECTORY, psa.getRootDirectory());
		}

	}// onSaveInstanceState

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onRestoreInstanceState(savedInstanceState);

		if (savedInstanceState != null) {
			if (savedInstanceState.get(FILES_CATALOG) != null) {
				ArrayList<Parcelable> selectedsongs = savedInstanceState
						.getParcelableArrayList(FILES_CATALOG);

				for (Object omf : selectedsongs.toArray()) {
					this.psa.selectedfiles.add((DocFile) omf);
				}

			}
			if (savedInstanceState.get(ROOT_DIRECTORY) != null) {
				_root_dir = savedInstanceState.getString(ROOT_DIRECTORY);				
			}		 

		}

	}// onRestoreInstanceState
	

	 
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onTrimMemory(int)
	 */
/*	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	@Override
	public void onTrimMemory(int level) {
		// TODO Auto-generated method stub
		super.onTrimMemory(level);
		
		switch (level)
		{
					case  TRIM_MEMORY_UI_HIDDEN:
						if (true)
						{}
							
					break;
						
					case TRIM_MEMORY_RUNNING_MODERATE:
						break;
						
					case TRIM_MEMORY_RUNNING_LOW:
						break;							
						
					case TRIM_MEMORY_RUNNING_CRITICAL:
						break;
						
					case TRIM_MEMORY_BACKGROUND:
						break;
						
					case TRIM_MEMORY_MODERATE:
						break;
						
					case TRIM_MEMORY_COMPLETE:
						break;		
		
		}			
		
	}*///onTrimMemory
	
	
	
	
	
	
	
	
	
}
