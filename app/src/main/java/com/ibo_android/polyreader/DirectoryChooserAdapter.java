package com.ibo_android.polyreader;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;

import com.ibo_android.polyreader.R;
 

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

public class DirectoryChooserAdapter extends BaseAdapter {

	public ArrayList<DocFile> dfiles;
	private String _rootdir;
	private LayoutInflater minfl;
	//private Context _con;
	public String selectedDirectory;
	 SharedPreferences prefs ;
	public ListView _fileslist;
	//public IntentReceiverFromPlayer mMessageReceiver ;
	public WeakReference<DirectoryChooserActivity>  _act;
	 
	 		public class DirectoryViewHolder
			{
				DocFile DirectoryName;
				TextView mTitle;
				RadioButton mRadio;
			}
				
		
			public DirectoryChooserAdapter(DirectoryChooserActivity act, String rootdir )
			{
				
				minfl = LayoutInflater.from(act);
				dfiles = new ArrayList<DocFile>();
				_rootdir=rootdir;
				 _act = new WeakReference<DirectoryChooserActivity>(act);	
				//_con=con;				 
				 prefs = PreferenceManager.getDefaultSharedPreferences(act);
				getDirectories(_rootdir);
				
			         
			}//FilesAdapter	
			
			
			public void setRootDirectory(String rootDirectory) {
				_rootdir = rootDirectory;
				if(_act.get() != null)
					_act.get().SetRootDirectoryTextView(_rootdir);		
			}
			
			public void getDirectories(String rootfolder)
			{		
				
				//mfiles.add(object);
				//this is for 1.7
				/*Iterable<Path> dirs =
					    FileSystems.getDefault().getRootDirectories();
					for (Path name: dirs) {
					    System.err.println(name);
					}*/

				
				File f = new File(rootfolder);
				
			//	if (f.isDirectory())//first file is the root directory
				//{
				//	DocFile df = new  DocFile("", "UP",f.getAbsolutePath()); 
					//df.bIsRootDirectory = 1;
				//	dfiles.add(df);			
				//}
				
				 
				
				if (f.isDirectory())//first file is the root directory
				{
				//	MusicFile mf = new  MusicFile("", "UP",f.getAbsolutePath(),0); 
				//	mf.bIsRootDirectory = 1;
				//	mfiles.add(mf);	
					
					setRootDirectory(f.getAbsolutePath());
				}
				
			 
				if (f.isDirectory())
				{	
					if (f.listFiles() != null)
					{
						for (File name: f.listFiles()) {				
							
							if (name.isDirectory())
							{
								DocFile df = new  DocFile("", name.getName(),name.getAbsolutePath()); 					
								dfiles.add(df);
							}						
						}	
					}//if (f.listFiles() != null)					
					
				}
			 
				Collections.sort(dfiles);
			}//getmusicfiles	
			
				
			
				
				public int getCount() {
					// TODO Auto-generated method stub
					return dfiles.size();
				}
			
				public Object getItem(int arg0) {
					// TODO Auto-generated method stub
					//return null;
					return dfiles.get(arg0);
				}
			
				public long getItemId(int arg0) {
					// TODO Auto-generated method stub
					//return 0;
					return arg0;
				}
			
				public View getView(int pos, View v, ViewGroup vg) {
					DirectoryViewHolder holder=null;
					 
					
					if (v==null || v.getTag()== null)
					{
						
						v = minfl.inflate(R.layout.activity_directory_chooser_listitem, null);
						holder = new DirectoryViewHolder();
						holder.mTitle = (TextView)v.findViewById(R.id.txtDirectory);
						holder.mRadio = (RadioButton)v.findViewById(R.id.rbdirectory);
						holder.mRadio.setTag(holder);
						
						v.setTag(holder);
						
					}
					else
					{			
						holder = (DirectoryViewHolder) v.getTag();				
					}
					


					holder.DirectoryName = dfiles.get(pos);
					holder.mTitle.setText( holder.DirectoryName.title);				
					
					
					int  TextSize = prefs.getInt("text_size", 12);
					holder.mTitle.setTextSize(TextSize);

					holder.mTitle.setLines(3);

					try {
						MainActivity.ApplySelectorsSize(_act.get(), holder.mRadio);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					 
					v.setTag(holder);	
					
					if (selectedDirectory == holder.DirectoryName.filepath)
					{
						holder.mRadio.setChecked(true);
					}
					else
					{
						holder.mRadio.setChecked(false);
					}	
					 
					
					 					
						holder.DirectoryName = dfiles.get(pos);
						holder.mTitle.setText( holder.DirectoryName.title);				
						
						v.setTag(holder);	
						//holder.mCheck = null;		 
						//holder.mCheck.setVisibility(View.INVISIBLE);
						 v.setOnClickListener(new OnClickListener() {
					    	  public void  onClick(View v)
					    	  {
					    		  ChangeDirectory(v);					    		      		  
					    	  }
					       }
					       );					
					 
									
						 if ( dfiles.get(pos).title != "UP")
						 {
							 	holder.mRadio.setOnClickListener(new OnClickListener() {
						    	  public void  onClick(View v)
						    	  {
						    		  ChooseDirectory(v);	    		      		  
						    	  }
						       }
						       );						 
						 }	 
					 
					return v;
				}//getView		 
				
				 
				
				public void ChooseDirectory(View v)
				{				 
					DirectoryViewHolder vh = (DirectoryViewHolder)  v.getTag();
					//String dir = vh.DirectoryName.filepath;
					//RadioButton cb = vh.mRadio;	 
									 
						 if (  vh.mRadio.isChecked())						 			
							 selectedDirectory = vh.DirectoryName.filepath;
						 this.notifyDataSetChanged();
						  								
				}//ChooseDirectory				
				 
				
				public boolean ChangeDirectory(View v)
				{	 
						 
					DirectoryViewHolder vh = (DirectoryViewHolder)  v.getTag();
					DocFile df = vh.DirectoryName;
					
				
					
					
					if (df.title == "UP")
					{
						 File fc = new File(df.filepath);
						String root =   fc.getParent();
						if ( root!= null )
						{
							 //File froot = new File(root);
							 dfiles.clear();
							 getDirectories(root);
							this.notifyDataSetChanged();
							
						}
						else
						{						
							 return false;				
						}
						
						return true;
					}
					else
					{
						dfiles.clear();
						getDirectories(df.filepath);
						this.notifyDataSetChanged();	
						return true;						
					}						  
					
				}//ChangeDirectory	
				
				public boolean GoUp()
				{
					//if (mfiles.size() == 0)
					//	return false;
					
					//MusicFile mf = mfiles.get(0);
					
//					if (mf.title == "UP")
//					{
//						 File fc = new File(mf.filepath);
//						String root =   fc.getParent();
//						if (!root.equals(null))
//						{
//							 File froot = new File(root);
//							 mfiles.clear();
//							 getmusicfiles(root);
//							this.notifyDataSetChanged();
//						}
//						else
//						{
//							 return false;				
//						}
//						
//						return true;
//					}		
					
					
					if (_rootdir != "")
					{
						 File fc = new File(_rootdir);
						String root =   fc.getParent();
						if (root != null)
						{
							// File froot = new File(root);
							 dfiles.clear();
							 getDirectories(root);
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

}
