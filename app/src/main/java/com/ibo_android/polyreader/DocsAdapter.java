package com.ibo_android.polyreader;

 
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
 

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class DocsAdapter extends android.widget.BaseAdapter {

	public ArrayList<DocFile> dfiles;
	private LayoutInflater minfl;	 
	public WeakReference<MainActivity>  _act;	
	public ListView _fileslist;	
	SharedPreferences prefs;	
	
	public   MyDB _dba;
	
	
	public class DocFileViewHolder
	{
		DocFile doc;
		TextView mURL;
		WebView mWV;
		 
		Button btOpen;
		Button btReload;
		
		Button btPlus;
		Button btMinus;

		Button btClose;
		Button btInfo;

	}
	
	public DocsAdapter(MainActivity act, ArrayList<DocFile>  docfiles,MyDB dba) 
	{
		_act = new WeakReference<MainActivity>(act);		
		dfiles = docfiles;
		minfl = LayoutInflater.from(act);
		prefs = PreferenceManager.getDefaultSharedPreferences(act);
		_dba = dba;
		
	}

	@Override
	public int getCount() {
		return dfiles.size();
	}
	
	public DocFileViewHolder GetViewItem(DocFile item)
	{
		 
		if (item==null)
			return null;
		
		int idx = dfiles.indexOf(item);
		  
			 if ( !( _fileslist.getItemAtPosition(idx) == null))
			 {
				 View vprev = (View)   _fileslist.getChildAt(idx);
				 if (vprev == null)
				 {
					 return null;
				 }
				 DocFileViewHolder vh_prev = (DocFileViewHolder)  vprev.getTag();
				 return vh_prev;			 
			 }
			 return null;
			   
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return dfiles.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View v, ViewGroup parent) {
		DocFileViewHolder holder=null;		 
		
		if (v==null || v.getTag()== null)
		{
			
			v = minfl.inflate(R.layout.activity_main_listitem, null);
			holder = new DocFileViewHolder();
			holder.mURL = (TextView)v.findViewById(R.id.txtURL);
			holder.mWV = (WebView)v.findViewById(R.id.wvView);
			 
			holder.btOpen = (Button)v.findViewById(R.id.btOpen);
			holder.btOpen.setTag(holder);
			
			holder.btReload = (Button)v.findViewById(R.id.btReload);
			holder.btReload.setTag(holder);
			
			holder.btPlus = (Button)v.findViewById(R.id.btPlus);
			holder.btPlus.setTag(holder);
			
			holder.btMinus = (Button)v.findViewById(R.id.btMinus);
			holder.btMinus.setTag(holder);

			holder.btClose = (Button)v.findViewById(R.id.btClose);
			holder.btClose.setTag(holder);

			holder.btInfo = (Button)v.findViewById(R.id.btInfo);
			holder.btInfo.setTag(holder);
			
			v.setTag(holder);
			
		}
		else
		{			
			holder = (DocFileViewHolder) v.getTag();				
		}
		
		
		holder.doc = dfiles.get(position);
		holder.mURL.setText(holder.doc.filepath);				
		//int  TextSize = prefs.getInt("text_size", 12);
		//holder.mTitle.setTextSize(TextSize);
		
		ShowFile(holder.mWV,holder.mURL,"",holder.doc,false);	

		holder.mURL.setTag(holder.doc);
		
		//holder.mURL.setOnLongClickListener(new MyOnLongClickListener() {});
		
		
		
	/*	v.setOnLongClickListener(new OnLongClickListener() {
			
			
					
			  // Defines the one method for the interface, which is called when the View is long-clicked
		    @SuppressWarnings("deprecation")
			public boolean onLongClick(View v) {

		    	//TextView txt  = (TextView) v; 	
		    	DocFile doc;
		    	
		    // Create a new ClipData.
		    // This is done in two steps to provide clarity. The convenience method
		    // ClipData.newPlainText() can create a plain text ClipData in one step.

		    // Create a new ClipData.Item from the ImageView object's tag
		    	
		    	
		    	
		    	DocFileViewHolder	holder = (DocFileViewHolder) v.getTag();		
		    	//doc = (DocFile) v.getTag();
		    	doc = holder.doc;
		    	
		    
		    //	ClipData.Item item = new ClipData.Item(doc.filepath);

		    // Create a new ClipData using the tag as a label, the plain text MIME type, and
		    // the already-created item. This will create a new ClipDescription object within the
		    // ClipData, and set its MIME type entry to "text/plain"

		      //  ClipData dragData = new ClipData(v.getTag(),ClipData.MIMETYPE_TEXT_PLAIN,item);

		    	
		    	ClipData dragData2 = ClipData.newPlainText(doc.filepath, doc.filepath);
		    	
		    	
		    // Instantiates the drag shadow builder.
		    View.DragShadowBuilder myShadow = new MyDragShadowBuilder(v);

		    // Starts the drag

		           
		            
		            
		            
		            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
		            {
		                
		                v.startDragAndDrop(dragData2,  // the data to be dragged
		                        myShadow,  // the drag shadow builder
		                        null,      // no need to use local data
		                        0          // flags (not currently used, set to 0)
		                					);
		                
		            } 
		            else
		            {
		            	 v.startDrag(dragData2,  // the data to be dragged
		                         myShadow,  // the drag shadow builder
		                         null,      // no need to use local data
		                         0          // flags (not currently used, set to 0)
		             );
		            }
		            
		            
		         return true   ;

		    }
						
			
		});*/
		
		
		
		
			holder.btOpen.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					
					// Toast.makeText(_con, "open..", Toast.LENGTH_LONG).show();					
					DocFileViewHolder vh = (DocFileViewHolder)  v.getTag();
					DocFile df = vh.doc;
								
					GetFiles(df);									
				}
			});
			
			holder.btReload.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					
					// Toast.makeText(_con, "open..", Toast.LENGTH_LONG).show();
					DocFileViewHolder vh = (DocFileViewHolder)  v.getTag();					 
								
					ShowFile(vh.mWV,vh.mURL,"",vh.doc,true);	
									
				}
			});
			
			
			holder.btPlus.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					
					// Toast.makeText(_con, "open..", Toast.LENGTH_LONG).show();
					DocFileViewHolder vh = (DocFileViewHolder)  v.getTag();					 
										
					MakeBigger(vh);	
									
				}
			});
			
			
			holder.btMinus.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					
					// Toast.makeText(_con, "open..", Toast.LENGTH_LONG).show();
					DocFileViewHolder vh = (DocFileViewHolder)  v.getTag();					 
								
					MakeSmaller(vh);	
									
				}
			});


		holder.btClose.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				// Toast.makeText(_con, "open..", Toast.LENGTH_LONG).show();
				DocFileViewHolder vh = (DocFileViewHolder)  v.getTag();

				CloseItem(vh);

			}
		});

		holder.btInfo.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				// Toast.makeText(_con, "open..", Toast.LENGTH_LONG).show();
				DocFileViewHolder vh = (DocFileViewHolder)  v.getTag();

				ShowInfo(vh);

			}
		});
				 
		v.setTag(holder);	
		
		// ViewGroup.LayoutParams sparams =  holder.sv.getLayoutParams();
		// sparams.height = 500;
		//	//mlp.width = 10;
		//	 holder.sv.setLayoutParams(sparams);
			 
		holder.mWV.requestFocus(); 
		holder.mURL.setEnabled(false);
		 ViewGroup.LayoutParams params =  holder.mWV.getLayoutParams();
			 
		 params.height = holder.doc.cpage_height;//600;
		//mlp.width = 10;
		 holder.mWV.setLayoutParams(params);
		
		 
		// holder.mWV.setVerticalScrollBarEnabled(true);
		// holder.mWV.setHorizontalScrollBarEnabled(true);
		 
		 
		return v;		
		
	}
	
	
	private void GetFiles(DocFile df)
	{
		// String MUSIC_DIR1 = "/music/comp/";
		  
		 String MusicDir1 = prefs.getString("init_dir", "");//Environment.getExternalStorageDirectory().getAbsolutePath();
		// .getAbsolutePath() + MUSIC_DIR1;
		 
		 if (MusicDir1 == "") {
			 
			 MusicDir1 = Environment.getExternalStorageDirectory()
						.getAbsolutePath() ;
			}

		Intent i = new Intent(_act.get(), FilePickerActivity.class);
		i.putExtra("root_dir", MusicDir1);
		i.putExtra("doc_file", df);

		_act.get().startActivityForResult(i, MainActivity.GET_FILES);

	}// AddSongs
	
	private void   MakeBigger(DocFileViewHolder vh)
	{		
		 ViewGroup.LayoutParams params =  vh.mWV.getLayoutParams();
		 params.height += 50;
		//mlp.width = 10;
		 vh.mWV.setLayoutParams(params);
		 vh.doc.cpage_height =  params.height;
	}
	
	private void   MakeSmaller(DocFileViewHolder vh)
	{
		 ViewGroup.LayoutParams params =  vh.mWV.getLayoutParams();
		 params.height -= 50;
		//mlp.width = 10;
		 vh.mWV.setLayoutParams(params);
		 vh.doc.cpage_height =  params.height;
	}

	private void   CloseItem(DocFileViewHolder vh)
	{
		//ViewGroup.LayoutParams params =  vh.mWV.getLayoutParams();
		//params.height -= 50;
		////mlp.width = 10;
		//vh.mWV.setLayoutParams(params);
		//vh.doc.cpage_height =  params.height;

		dfiles.remove(vh.doc);
		DataSetChanged();
	}

	private void   ShowInfo(DocFileViewHolder vh)
	{

		Toast.makeText(_act.get(), vh.mURL.getText(), Toast.LENGTH_LONG).show();

	}
	
	public void   ShowFile(WebView wv,TextView tv, String url,DocFile df, boolean bFromOpen)
	{
		
		FileName fn = new FileName(df.filepath);			
		
			if ("PDF".toUpperCase().equals(fn.extension().toUpperCase()))
			{
				ShowPDF(wv,tv,"",df,bFromOpen);
				return;
			}
			 
		
			if ("HTML".toUpperCase().equals(fn.extension().toUpperCase()))
			{
				ShowWebPage(wv,tv,"",df);
				return;
			}
		
		
			if ("HTM".toUpperCase().equals(fn.extension().toUpperCase()))
			{
				ShowWebPage(wv,tv,"",df);
				return;
			}
			
			
			
			ShowOnLineWebPage(wv,tv,url,df);
		
		
	}//ShowFile
	
	
	private void   ShowOnLineWebPage(WebView wv,TextView tv, String url,DocFile df)
	{
		
		//myWebView = (WebView) findViewById(R.id.webView1);
				WebSettings websets = wv.getSettings();
			
				
				websets.setJavaScriptEnabled(true);
				 
			//	myWebView.addJavascriptInterface(new WebAppInterface(this), "Android");
				
				if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.JELLY_BEAN) //required for running javascript on android 4.1 or later
				{
					websets.setAllowFileAccessFromFileURLs(true);
					websets.setAllowUniversalAccessFromFileURLs(true);
				}
				websets.setBuiltInZoomControls(true);	 
				
				
				wv.setWebViewClient(new WebViewClient() {
				    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) 
				    {		      		    	
				    	Toast.makeText(view.getContext(), description, Toast.LENGTH_LONG).show();
				  	}		    
				    
				    @Override
					public boolean shouldOverrideUrlLoading(WebView view, String url) 
					{
						view.loadUrl(url);
						return true;
					}
				});		
				
				
				url =    tv.getText().toString();
				
				if (!url.equals(""))
				{
					if (!url.contains("http"))
					{
						url = "http://" +   url;						
					}
					
					
					wv.loadUrl(url);
					
					df.filepath = url;
					df.title = url;
				}
				 
				
				
						
		
	}//ShowWebPage
	
	private void   ShowWebPage(WebView wv,TextView tv, String url,DocFile df)
	{
		
		//myWebView = (WebView) findViewById(R.id.webView1);
				WebSettings websets = wv.getSettings();
			
				
				websets.setJavaScriptEnabled(true);
				 
			//	myWebView.addJavascriptInterface(new WebAppInterface(this), "Android");
				
				if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.JELLY_BEAN) //required for running javascript on android 4.1 or later
				{
					websets.setAllowFileAccessFromFileURLs(true);
					websets.setAllowUniversalAccessFromFileURLs(true);
				}
				websets.setBuiltInZoomControls(true);	 
				
				
				wv.setWebViewClient(new WebViewClient() {
				    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) 
				    {		      		    	
				    	Toast.makeText(view.getContext(), description, Toast.LENGTH_LONG).show();
				  	}		    
				    
				    @Override
					public boolean shouldOverrideUrlLoading(WebView view, String url) 
					{
						view.loadUrl(url);
						return true;
					}
				});		
				
				
				wv.loadUrl( "file://" +   df.filepath);		
		
	}//ShowWebPage
	
	 
	
@SuppressWarnings("deprecation")
@SuppressLint("NewApi")
private void   ShowPDF(WebView wv,TextView tv, String url,DocFile df, boolean bFromOpen)
{
	
	WebSettings websets = wv.getSettings();
	websets.setJavaScriptEnabled(true);
//	websets.enableSmoothTransition();
	//websets.setEnableSmoothTransition(true);
	 
	
	WebAppInterface wai = new WebAppInterface(_act.get(),df);
	wv.addJavascriptInterface(wai, "PolyReaderApp");
	
	if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.JELLY_BEAN) //required for running javascript on android 4.1 or later
	{
		websets.setAllowFileAccessFromFileURLs(true);
		websets.setAllowUniversalAccessFromFileURLs(true);
	}
	websets.setBuiltInZoomControls(false);
	websets.setDisplayZoomControls(false);
	
	
 
	
	//wv.setWebChromeClient(new WebChromeClient() );
	wv.setWebViewClient(new WebViewClient() {
	    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) 
	    {		      		    	
	    	Toast.makeText(view.getContext(), description, Toast.LENGTH_LONG).show();
	  	}		    
	    
	    @Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) 
		{
			view.loadUrl(url);
			return true;
		}
	    
	   /* @Override
	    public void onPageFinished(final WebView view, final String url) {
	        super.onPageFinished(view, url);
	        view.requestLayout();
	    }*/
	    
	       
	});		
 
	//url = "/storage/emulated/0/languages/sp/GarciaMarquez-ElAmorenlosTiemposdelColera.pdf";
	
	url = df.filepath;
	
	String path2 = "file://" + url;
	String testpdf_file = "testpdf_" + df.code  + ".html";
	
	Integer pageToShow = 1;
	String pageToShow2 = "";
	
	String zoomscale = "";
	
	if (bFromOpen)
	{
		SharedPreferences priv_prefs = _act.get().getPreferences(Context.MODE_PRIVATE);		
		int cur_pos = priv_prefs.getInt(df.title, 0);	
		
		float zoom =  priv_prefs.getFloat(df.title + "_zoom", 0);
		
		
		Set cur_pos2 = priv_prefs.getStringSet(df.title + "1", null);		
		HashSet<String> bookmarks = (HashSet<String>) cur_pos2;		
		 
			if ( bookmarks != null)
			{
				for (String s : bookmarks)
				{
				    int dot = s.lastIndexOf("_");
				    String key = s.substring(0, dot);
			        //s.substring(0, dot);
			        if ( "main".equals(key))
			        {
			        	pageToShow2 =  s.substring(dot+1, s.length());
			        }
				}	
				
			}	
		
		/*if (cur_pos == 0) 
		{
			pageToShow = df.pagenumber;
		}
		else
		{			
			pageToShow = cur_pos;
		}*/
						
			if (cur_pos2 == null) 
			{
				//pageToShow = df.pagenumber;
				pageToShow2 = df.pagenumber.toString();
			}
			else
			{			
				//pageToShow = cur_pos;
				//pageToShow2 = cur_pos;
			}
			
			if (zoom == 0 ) 
			{				
				zoomscale =Float.toString(df.page_scale);
			}
			else
			{	
				zoomscale =Float.toString(zoom);				
			}		
	}
	else
	{
		//pageToShow = df.pagenumber;
		pageToShow2 = df.pagenumber.toString();	
		zoomscale =Float.toString(df.page_scale);
	}
	 
	
	//GarciaMarquez-ElAmorenlosTiemposdelColera.pdf
	//file:////storage/sdcard/LoveInTheTimeOfCholera.pdf
	try
	{
		InputStream ims = _act.get().getAssets().open("pdfviewer/testpdf.html");
		
	 	String line = getStringFromInputStream(ims);
		if(line.contains("PDF_FILE")) 
		{
			line = line.replace("PDF_FILE", path2.toString());
		}
		if(line.contains("PAGE_TO_SHOW")) 
		{
			line = line.replace("PAGE_TO_SHOW", pageToShow2.toString());
		}
		
		if(line.contains("ZOOM_SCALE")) 
		{
			line = line.replace("ZOOM_SCALE", zoomscale.toString());
		}
		
		
		 FileOutputStream fos= null;
		 OutputStreamWriter osw = null;
		 fos=  _act.get().openFileOutput(testpdf_file,Context.MODE_PRIVATE);
		 osw = new OutputStreamWriter(fos);
		 osw.write(line);
		 osw.close();
		 fos.close();	
		
	} catch (IOException e) 
	{
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	 
	File f = new File( _act.get().getFilesDir() + "/" + testpdf_file);
	 
	if (f.exists())
	{
		wv.loadUrl("file://" +  _act.get().getFilesDir() + "/" + testpdf_file);
		tv.setText(url);
	}
	
}//ShowPDF


	private static String getStringFromInputStream(InputStream is)
	{

		BufferedReader br = null;
		StringBuilder sb = new StringBuilder();

		String line;
		try {

			br = new BufferedReader(new InputStreamReader(is));
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return sb.toString();

	}//getStringFromInputStream

	public void ChangeDatasource(ArrayList<Parcelable> selecteddocs) 
	{
	 
		dfiles.clear();
		//ClearScreen();
		
		for (Object omf: selecteddocs.toArray()) 
		{
			dfiles.add((DocFile) omf);
		}	
							
		//notifyDataSetChanged();
		DataSetChanged();		
		
	}
	
	public void getdocfilesFromReadlist(String playlist)
	{
		//mfiles.clear();
		
		//ClearScreen();
		
		Cursor c = _dba.ShowAll(playlist);	
		
		//startManagingCursor(c);
		
		if(c.moveToFirst())
		{
			do
			{
							
				String code = c.getString(c.getColumnIndex(MyDBHelper.CODE_COL));
				
				String title = c.getString(c.getColumnIndex(MyDBHelper.TITLE_COL));
				
				String filepath = c.getString(c.getColumnIndex(MyDBHelper.FILEPATH_COL));
										
				Integer pagenumber = c.getInt(c.getColumnIndex(MyDBHelper.PAGENUMBER_COL));
				
				float pagescale = c.getFloat(c.getColumnIndex(MyDBHelper.PAGESCALE_COL));
				
				Integer pageheight = c.getInt(c.getColumnIndex(MyDBHelper.PAGEHEIGHT_COL));						
				
				Integer seqnum = c.getInt(c.getColumnIndex(MyDBHelper.SEQ_NUM_COL));
				
				File f = new File(filepath);
				 
				if (f.exists())
				{
					DocFile df = new  DocFile(code, title, filepath,  pagenumber,  pagescale, pageheight); 					
					dfiles.add(df);								
				}
								 
				
			} while(c.moveToNext());
			
		}
		
		//this.notifyDataSetChanged();
		DataSetChanged();
		//this._nowplaying = null;
		
	}	//getmusicfilesFromPlaylist

	
	
	
	public void DataSetChanged()
	{
		this.notifyDataSetChanged();		 			
	}
	
}


