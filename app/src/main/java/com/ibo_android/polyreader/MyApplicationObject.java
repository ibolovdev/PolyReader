package com.ibo_android.polyreader;

import java.util.ArrayList;

import android.app.Application;


public class MyApplicationObject extends Application {

	
	
	 ArrayList<DocFile> al;

	 ArrayList<DocFile> getSelectedFiles() {
	        return al;
	    }

	    void setSelectedFiles(ArrayList<DocFile> selectedfiles) {
	        this.al = selectedfiles;
	    }
	
	
	    private static MyApplicationObject singleton;
		
		public MyApplicationObject getInstance(){
			return singleton;
		}
		@Override
		public void onCreate() {
			super.onCreate();
			singleton = this;
		}
		
		
		
		
	
}
