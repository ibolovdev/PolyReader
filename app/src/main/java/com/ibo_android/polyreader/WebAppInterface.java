package com.ibo_android.polyreader;

import java.lang.ref.WeakReference;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

public class WebAppInterface
{
	WeakReference<MainActivity> _act ;
    DocFile df;

    /** Instantiate the interface and set the context */
    WebAppInterface(MainActivity act, DocFile pdf)
    {
    	_act = new WeakReference<MainActivity>(act);
        df = pdf;
    }

    /** Show a toast from the web page */
    @JavascriptInterface
    public void showToast(String toast)
    {
        Toast.makeText(_act.get(), toast, Toast.LENGTH_SHORT).show();
    }
    
    @JavascriptInterface
    public void PageNumberChanged(int pagenumber, float pagescale)
    {
       df.pagenumber = pagenumber;
       df.page_scale = pagescale;
    }
    
    @JavascriptInterface
    public void MarkPage()
    {
    	 
    	SharedPreferences prefs = _act.get().getPreferences(Context.MODE_PRIVATE);
		Editor ed = prefs.edit();

		ed.putInt(df.title,  df.pagenumber);
		
		Set<String> bookmarks = new HashSet<String>();
		bookmarks.add("main_" + df.pagenumber.toString() );
	
		ed.putStringSet(df.title + "1", bookmarks);


		ed.putFloat(df.title + "_zoom",  df.page_scale);
		
		
		
		//Hashtable mov = new Hashtable();
		//mov.put("main", df.pagenumber);
		
		//Set movies=new HashSet();
		//movies.add( mov);
		//movies.addAll(collection)
		
		//ed.putStringSet(df.title + "1", movies);
		
		ed.commit(); 		
		
    }
    
    @JavascriptInterface
    public void BookMarkPage()
    {
    	
    	
       
    }    
    
}
