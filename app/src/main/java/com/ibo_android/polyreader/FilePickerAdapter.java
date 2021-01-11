package com.ibo_android.polyreader;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
 

 
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
 
import android.widget.TextView;
 


public class FilePickerAdapter extends BaseAdapter {

	public LayoutInflater minfl;
	//private Context _con;
	 
	public ArrayList<DocFile> dfiles;
	private String _rootdir;
	public ArrayList<DocFile> selectedfiles;
	public WeakReference<FilePickerActivity>  _act;
	//private ArrayList<String> _allowableFormats;
	
	private String _RootDirectory = "";
	public ListView fileslist;
	 SharedPreferences prefs ;
	
	public String getRootDirectory() {
		return _RootDirectory;
	}
	
	public void setRootDirectory(String rootDirectory) {
		_RootDirectory = rootDirectory;
		if(_act.get() != null)
			_act.get().SetRootDirectoryTextView(_RootDirectory);		
	}

	public class PickFileViewHolder
	{
		DocFile dfile;
		CheckBox dCheck;
		TextView dTitle;				
	}	
	
	
	public FilePickerAdapter( String rootdir, FilePickerActivity act, ArrayList<Parcelable> selfiles_asparam)
	{
		
		minfl = LayoutInflater.from(act);
		dfiles = new ArrayList<DocFile>();
		selectedfiles = new ArrayList<DocFile>();	
		
		if (!(selfiles_asparam == null ))
		{				
			for (Object omf: selfiles_asparam.toArray()) 
			{
				DocFile typedmf =  (DocFile) omf;				
				selectedfiles.add(typedmf);				
			}	
		}		
		
		_rootdir=rootdir;
		 
		_act = new WeakReference<FilePickerActivity>(act);		
		
		getdocfiles(_rootdir);  
		
		//Collections.sort(dfiles);
		
		 prefs = PreferenceManager.getDefaultSharedPreferences(act);
	         
	}//FilesAdapter	
	
	public void getdocfiles(String rootfolder)
	{		
		
		//mfiles.add(object);
		//this is for 1.7
		/*Iterable<Path> dirs =
			    FileSystems.getDefault().getRootDirectories();
			for (Path name: dirs) {
			    System.err.println(name);
			}*/

		
		File f = new File(rootfolder);
		
		if (f.isDirectory())//first file is the root directory
		{
//			MusicFile mf = new  MusicFile("", "UP",f.getAbsolutePath()); 
//			mf.bIsRootDirectory = 1;
//			mfiles.add(mf);	
			
			setRootDirectory(f.getAbsolutePath());
		}
	 
		if (f.isDirectory())
		{		
			if (f.listFiles() != null)
			{
				for (File name: f.listFiles()) {				
					
					if (CheckFile(name))
					{					
						DocFile df = new DocFile("", name.getName(),name.getAbsolutePath()); 					
						dfiles.add(df);
					}		
				
				}
				
			}//if (f.listFiles() != null)
			
			
		}
		else
		{
			if (CheckFile(f))
			{ 
				DocFile mf = new  DocFile("", f.getName(),f.getAbsolutePath()); 					
				dfiles.add(mf);
			}
			
		}
		
		//Collections.sort(mfiles);
		
	}//getmusicfiles		
	
	private boolean CheckFile(File f)
	{
		
		//if (!f.isDirectory())
		//{
			FileName fn = new FileName(f.getAbsolutePath());			
			
		//	for (String format: _allowableFormats) 
		//	{
			//	if (format.toUpperCase().equals(fn.extension().toUpperCase()))
			//	{
			//		return true;	
			//	}
		//	}
			
			/*if (_allowableFormats.contains(fn.extension()))
			{
				return true;	
			}*/
				
			//return false;
		//}
		
		return true;	
		
	}//CheckFile
	
	
	public int getCount() {
		// TODO Auto-generated method stub
		return dfiles.size();
	}

	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return dfiles.get(arg0);
	}

	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	public View getView(int pos, View v, ViewGroup vg) {
		PickFileViewHolder holder=null;		 
		 
		if (v==null || v.getTag()== null)
		{
			if ( dfiles.get(pos).title == "UP")
			{
				v = minfl.inflate(R.layout.activity_file_picker_listitem, null);
				holder = new PickFileViewHolder();
				holder.dTitle = (TextView)v.findViewById(R.id.tv_file_picker_li_File);
				
				holder.dCheck = (CheckBox)v.findViewById(R.id.chk_file_picker_li_File);			
				//holder.mCheck.setTag(holder);
				v.setTag(holder);	
			//	v = minfl.inflate(R.layout.file_view, null);
			//	holder = new PickSongViewHolder();
			//	holder.mTitle = (TextView)v.findViewById(R.id.txtMusicFile);
				
				
				//holder.mCheck = (CheckBox)v.findViewById(R.id.cbCheck);			
				//holder.mCheck.setTag(holder);
			//	v.setTag(holder);	
			}
			else
			{
				v = minfl.inflate(R.layout.activity_file_picker_listitem, null);
				
				//v.setBackgroundResource(R.drawable.list_item_appearances);
				
				holder = new PickFileViewHolder();
				holder.dTitle = (TextView)v.findViewById(R.id.tv_file_picker_li_File);
				
				holder.dCheck = (CheckBox)v.findViewById(R.id.chk_file_picker_li_File);			
				//holder.mCheck.setTag(holder);
				v.setTag(holder);					
			}
			
			//v = minfl.inflate(R.layout.pick_item_view, null);
			//holder = new PickSongViewHolder();
			//holder.mTitle = (TextView)v.findViewById(R.id.txtTitle);
			
			//holder.mCheck = (CheckBox)v.findViewById(R.id.cbCheck);			
			//holder.mCheck.setTag(holder);
			//v.setTag(holder);			
		}
		else
		{			
			holder = (PickFileViewHolder) v.getTag();				
		}
		//holder.mTitle.setFocusable(true);
		//holder.mCheck.setFocusable(false);
		 holder.dTitle.setLines(3);
		
		if ( dfiles.get(pos).title == "UP")
		{		
			
			holder.dfile = dfiles.get(pos);
			holder.dTitle.setText( holder.dfile.title);				
			
			v.setTag(holder);	
			//holder.mCheck = null;		 
			//holder.mCheck.setVisibility(View.INVISIBLE);
			 v.setOnClickListener(new OnClickListener() {
		    	  public void  onClick(View v)
		    	  {
		    		  GoUp(v);		    		      		  
		    	  }
		       }
		       );			 
			 
			//return v;
			
		}
		else
		{
			
			v.setTag(holder);	
			//holder.mCheck = null;		 
			//holder.mCheck.setVisibility(View.INVISIBLE);
			 v.setOnClickListener(new OnClickListener() {
		    	  public void  onClick(View v)
		    	  {
		    		  OpenFolder(v);		    		      		  
		    	  }
		       }
		       );		 
			
		}// ( mfiles.get(pos).bIsRootDirectory == 1)
		
		holder.dfile = dfiles.get(pos);
		if ( dfiles.get(pos).title == "UP")
			holder.dTitle.setText( holder.dfile.filepath);	
		else		
			holder.dTitle.setText( holder.dfile.title);				
		
		 int  TextSize = prefs.getInt("text_size", 12);
		 holder.dTitle.setTextSize(TextSize);
		 
		v.setTag(holder);
		
	///	if (holder != null)
		//	holder.mCheck.setTag(holder);//check if this good from a performance point of view

		try {
			MainActivity.ApplySelectorsSize(_act.get(), holder.dCheck);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		if (ContainsElement(selectedfiles,holder.dfile))
		{
			if (holder != null)
				holder.dCheck.setChecked(true);
		}
		else
		{
			if (holder != null)
				holder.dCheck.setChecked(false);
		}	
		 
		if (holder != null)
			 holder.dCheck.setOnClickListener(new OnClickListener() {
		    	  public void  onClick(View v)
		    	  {	    		  
		    		  ChooseSongRecursive(v);	
		    			//Toast.makeText(_act, "ChooseSongRecursive ended",  Toast.LENGTH_LONG).show();
		    	  }
		       }
		       );	  
		  
	 
		  v.setOnLongClickListener(new OnLongClickListener() {	    	  

			public boolean onLongClick(View v) {
				FilePickerLongClick(v);				
				return false;
			}
	       }
	       );
		 
		return v;
	}//getView
	
	
	private void FilePickerLongClick(View v)
	{		
		String action = "FILE_PICKER_LONG_CLICK";		 
		Intent in = new Intent(action);
		
		
		PickFileViewHolder vh = (PickFileViewHolder)  v.getTag();		  
		File fc = new File(vh.dfile.filepath);
			 
		if (!fc.isDirectory())
		{
			in.putExtra("file_path", vh.dfile.filepath);	 
			 
			 if(_act.get() != null)
				 _act.get().sendBroadcast(in);			
		}	
		
	}
	
	
	private boolean ContainsElement(ArrayList<DocFile> al1, DocFile mf)
	{	
		for (DocFile sf: al1) 
		{			 				 
				if (sf.filepath.equals(mf.filepath))
				{
					return true;
				}		
		}
		
		return false;	
		
	}//ContainsElement
	
	public void OpenFolder(View v)
	{
		
		 //open a new pick song activity
		PickFileViewHolder vh = (PickFileViewHolder)  v.getTag();
		  //MusicFile mf = vh.mfile;
		//  CheckBox cb = vh.mCheck;
			//Toast.makeText(_con, "OpenFolder", Toast.LENGTH_LONG).show(); 
		  
		  File fc = new File(vh.dfile.filepath);
			 
			if (fc.isDirectory())
			{					 
				dfiles.clear();
				getdocfiles(fc.getAbsolutePath());
				this.notifyDataSetChanged();			
				
				
			/*	Intent i = new Intent(_con, SongsPickerActivity.class);
    		 	i.putExtra("root_dir", mf.filepath);
    			i.putExtra("SELECTED_SONGS", selectedfiles);
    		 	i.putExtra("OPEN_FOLDER", true);*/
    		 	
    		 	//_act.startActivityForResult(intent, requestCode)
    		 	// _con.startActivity(i);
				//_act.startActivityForResult(i,MainActivity.GET_SONGS_FOLDER);  
				//_con.s
					
				
				//String action = "STOP_MUSIC";
				//Toast.makeText(ctx, "AUDIO_BECOMING_NOISY", Toast.LENGTH_LONG).show();
				
				// Intent in = new Intent("PICK_SONG_INSIDE_FOLDER");
					//in.putExtra("root_dir", mf.filepath);
									
				  // You can also include some extra data.
				//  in.putExtra("message", "This is my message!");
				
				 
				//  LocalBroadcastManager.getInstance(_con).sendBroadcast(in);
				 
				// ctx.sendBroadcast(in);
			}
		
		
	}//OpenFolder
	
	private void TraverseDirectoryAdd( File fc,DocFile mf)
	{		
		if (fc.isDirectory())
		{	
			selectedfiles.add(mf);			
			for (File name: fc.listFiles()) 
			{
				DocFile mf_in = new  DocFile("",name.getName(),name.getAbsolutePath());				
				//selectedfiles.add(mf_in);
				TraverseDirectoryAdd(name,mf_in);
			}
			
		}
		else
		{						
			 selectedfiles.add(mf);
		}		
	}//TraverseDirectoryAdd
	
	
	private void TraverseDirectoryRemove( File fc,DocFile mf)
	{
		 if (fc.isDirectory())
		 {	
			selectedfiles.remove(mf);
			for (Object omf: selectedfiles.toArray()) 
			{						
				DocFile mf_in = (DocFile) omf;
				 
				if(mf_in.filepath.contains(fc.getPath()))
				{					
					 selectedfiles.remove(mf_in);
				}
				
			/*	for (File name: fc.listFiles())//takes too much time
				 {									
						if (mf_in.filepath.equals(name.getAbsolutePath()))
						{									
							 //selectedfiles.remove(mf_in);
							TraverseDirectoryRemove(name,mf_in);
							 continue;
						}								 
				}*/						  
			}	//omf								 
			 
		 }
		 else
		 {						
			 selectedfiles.remove(mf);
		 } 			 
		
		
	}
	
	public boolean GoUp()
	{
		//if (mfiles.size() == 0)
		//	return false;
		
		//MusicFile mf = mfiles.get(0);
		
//		if (mf.title == "UP")
//		{
//			 File fc = new File(mf.filepath);
//			String root =   fc.getParent();
//			if (!root.equals(null))
//			{
//				 File froot = new File(root);
//				 mfiles.clear();
//				 getmusicfiles(root);
//				this.notifyDataSetChanged();
//			}
//			else
//			{
//				 return false;				
//			}
//			
//			return true;
//		}		
		
		
		if (_RootDirectory != "")
		{
			 File fc = new File(_RootDirectory);
			String root =   fc.getParent();
			if (root != null)
			{
				// File froot = new File(root);
				 dfiles.clear();
				 getdocfiles(root);
				this.notifyDataSetChanged();
			}
			else
			{
				 return false;				
			}
			
			return true;
		}		
		
		return false;		
	}	
	
	public boolean GoUp(View v)
	{	 
			 
		PickFileViewHolder vh = (PickFileViewHolder)  v.getTag();
		DocFile mf = vh.dfile;
		//CheckBox cb = vh.mCheck;	 
		
		if (mf.title == "UP")
		{
			 File fc = new File(mf.filepath);
			String root =   fc.getParent();
			if (root != null)
			{
				 //File froot = new File(root);
				 dfiles.clear();
				 getdocfiles(root);
				this.notifyDataSetChanged();
			}
			else
			{
				 return false;				
			}
			
			return true;
		}
		 
			 return false;
		
	}//GoUp
	
	public void ChooseSongRecursive(View v)
	{	 
			 
		PickFileViewHolder vh = (PickFileViewHolder)  ((View) v.getParent()).getTag();
		//MusicFile mf = vh.mfile;
		//CheckBox cb = vh.mCheck;	 
						 
			 if ( vh.dCheck.isChecked())
			 {				
				  File fc = new File(vh.dfile.filepath);
				  TraverseDirectoryAdd(fc,vh.dfile);			 
			 }
			 else
			 {				 
				 File fu = new File(vh.dfile.filepath);
				 TraverseDirectoryRemove(fu,vh.dfile);
			 }
			 
		
	}//ChooseSongRecursive

	public void ChooseSong(View v)
	{	 
			 
		PickFileViewHolder vh = (PickFileViewHolder)  v.getTag();
			  DocFile mf = vh.dfile;
			  CheckBox cb = vh.dCheck;	 
						 
			 if ( cb.isChecked())
			 {				
				  File fc = new File(mf.filepath);
				 
					if (fc.isDirectory())
					{	
						selectedfiles.add(mf);
						for (File name: fc.listFiles()) 
						{
							DocFile mf_in = new  DocFile("",name.getName(),name.getAbsolutePath()); 					
							 selectedfiles.add(mf_in);
						}
						
					}
					else
					{						
						 selectedfiles.add(mf);
					} 
				
			 }
			 else
			 {				 
				 File fu = new File(mf.filepath);
				 if (fu.isDirectory())
				 {	
					selectedfiles.remove(mf);
					for (Object omf: selectedfiles.toArray()) 
					{						
						DocFile mf_in = (DocFile) omf;
						 
						 for (File name: fu.listFiles())
						 {									
								if (mf_in.filepath.equals(name.getAbsolutePath()))
								{									
									 selectedfiles.remove(mf_in);
									 continue;
								}								 
						}						  
					}	//omf								 
					 
				 }
				 else
				 {						
					 selectedfiles.remove(mf);
				 } 			 
				 
			 }		  
		
		
	}//ChooseSong
	
	
	public void SelectAll()
	{
		selectedfiles.clear();
		
		for (Object omf: dfiles.toArray()) 
		{						
			DocFile mf_in = (DocFile) omf;
			
			
			//int idx = mfiles.indexOf(mf_in);			  
				/* if (!(  fileslist.getChildAt(idx) == null))//for the visible ones
				 {
					 View v = (View)   fileslist.getChildAt(idx);					  
					 PickSongViewHolder vh = (PickSongViewHolder)  v.getTag();					 
					 vh.mCheck.setChecked(true);				  
				 }*/
			 
			  File fc = new File(mf_in.filepath);
			  TraverseDirectoryAdd(fc,mf_in);
			  			  
		}	//omf		 
		
		
		this.notifyDataSetChanged();////for the visible ones, for everything
		//Notifies the attached observers that the underlying data has been changed 
		//and any View reflecting the data set should refresh itself. 
		//ArrayAdapter - Control whether methods that change the list (add(T), insert(T, int), remove(T), clear()) automatically call notifyDataSetChanged().
		//this.registerDataSetObserver(observer)
		
	} //SelectAll
	
	public void UnSelectAll()
	{
		selectedfiles.clear();
		this.notifyDataSetChanged();
	}//UnSelectAll
	
}
