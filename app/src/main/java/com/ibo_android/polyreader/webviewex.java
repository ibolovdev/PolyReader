package com.ibo_android.polyreader;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.webkit.WebView;

public class webviewex extends WebView {

	public webviewex(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public webviewex(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public webviewex(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
	}

	public webviewex(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		// TODO Auto-generated constructor stub
	}
	
	
	 @Override
	    public boolean onTouchEvent(MotionEvent event){
	        requestDisallowInterceptTouchEvent(true);
	        return super.onTouchEvent(event);
	    }

	/*@override
	protected void ondestroy () {
		super.ondestroy ();
		if (webview_projectinfo!=null) {
			webview_projectinfo.removeallviews ();
			webview_projectinfo.destroy ();
			webview_projectinfo=null;
			ll_webview.removeallviews ();
			ll_webview=null;
		}
	}*/

}
