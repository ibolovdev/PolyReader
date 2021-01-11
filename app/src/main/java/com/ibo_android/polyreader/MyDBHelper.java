package com.ibo_android.polyreader;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteException;

public class MyDBHelper extends SQLiteOpenHelper {

	public static final String TABLE_NAME = "BooksList";
	public static final String CODE_COL = "code";
	public static final String TITLE_COL = "title";
	public static final String FILEPATH_COL = "filepath";
	public static final String READLIST_COL = "readlist";
	public static final String PAGENUMBER_COL = "pagenumber";
	public static final String PAGESCALE_COL = "pagescale";
	public static final String PAGEHEIGHT_COL = "pageheight";
	public static final String SEQ_NUM_COL = "seqnum";
	
	private static final String CREATE_TABLE = "" +
			" 	create table BooksList (  				" +
			"							code integer primary key autoincrement ," +
			"							title text not null,  					" +	
			"							filepath text not null,  				" +	 
			"							readlist  text not null	,			" +
			"							pagenumber text,							" + 
			"							seqnum text, 		"  +
			"							pagescale text,   " +
			"							pageheight text " + 
			"							) ";
	
	public MyDBHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
				
		try
		{
			db.execSQL(CREATE_TABLE);
		}
		catch (SQLiteException ex)
		{			
			throw ex;
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldver, int newver) {		
		
		db.execSQL("drop table if exists " + TABLE_NAME );
		onCreate(db);		

	}
	
	
	
	

}

