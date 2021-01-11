package com.ibo_android.polyreader;


import java.util.ArrayList;

import android.content.Context; 
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.content.ContentValues;
import android.database.Cursor;

public class MyDB {
	
	//private final Context con;
	private MyDBHelper dbhelper;
	private SQLiteDatabase db;
	
	public MyDB(Context c)
	{
	//	con = c;
		dbhelper = new MyDBHelper( c, "MyBooks", null,2 );		
	}
	
	public void close()
	{	
		dbhelper.close();
		db.close();		
	}
	
	
	public void open()
	{
			try
			{
				db = dbhelper.getWritableDatabase();				
			}
			catch(SQLiteException ex)
			{
				db = dbhelper.getReadableDatabase();				
			}		
	}
	
	public Cursor ShowAll(String readlist)
	{				
		try
		{			 
			 String[] collist = new String[]{MyDBHelper.CODE_COL,MyDBHelper.TITLE_COL,
					 						MyDBHelper.FILEPATH_COL,MyDBHelper.READLIST_COL,
					 						MyDBHelper.PAGENUMBER_COL, MyDBHelper.PAGESCALE_COL, 
					 						MyDBHelper.PAGEHEIGHT_COL,  MyDBHelper.SEQ_NUM_COL };
			
			// String selection = MyDBHelper.PLAYLIST_COL + " = '" + playlist +"'";   
			//return db.query(MyDBHelper.TABLE_NAME,collist,selection, null, null, null, null);
			
			
			 String selection = MyDBHelper.READLIST_COL + " = ? ";   
				return db.query(MyDBHelper.TABLE_NAME,collist,selection, new String[] { readlist }, null, null, null);
			
			//p_query = "select * from mytable where name_field = ?";
			//mDb.rawQuery(p_query, new String[] { uvalue });
			 
		}
		catch(SQLiteException ex)
		{
			ex.printStackTrace();
			return null;
		}
	}
	
	
	public Cursor ShowReadlists( )
	{
	 	
		try
		{			 
			 String[] collist = new String[]{ MyDBHelper.READLIST_COL};		 
			 return db.query(MyDBHelper.TABLE_NAME,collist, null, null, MyDBHelper.READLIST_COL, null,  MyDBHelper.READLIST_COL);
		}
		catch(SQLiteException ex)
		{
			return null;
		}
	}	
	
	public long deletePlaylist(String playlist)
	{
		try
		{		 
			//String where = MyDBHelper.PLAYLIST_COL + " = " + "'" + playlist + "'" ;			
			//return db.delete( MyDBHelper.TABLE_NAME,  where,   null) ;
			
			String where = MyDBHelper.READLIST_COL + " =  ? ";			
			return db.delete( MyDBHelper.TABLE_NAME,  where,   new String[] { playlist }) ;
			
		}
		catch(Exception ex)
		{
			return -1;
		}	
		
	}	 
	
	public long deleteEntry(String code)
	{
		//try
		//{
			 
			String where = MyDBHelper.CODE_COL + " = ? "  ;			
			return db.delete( MyDBHelper.TABLE_NAME,  where,   new String[] { code }) ;
			
		//}
		//catch(Exception ex)
		//{
		//	return -1;
		//}		
		
	}	
	
	public long deleteAll()
	{
		try
		{		
			return db.delete( MyDBHelper.TABLE_NAME,    null ,   null) ;			
		}
		catch(Exception ex)
		{
			
			return -1;
		}	
		
	}	//deleteAll
	
	
	public long deleteReadLists(ArrayList<String> rls)
	{
		if ((rls == null) ||  rls.isEmpty() ) 
			return -1;
		
		String where ="";
		
		
	/*	for (String pl: pls) {
		    
			if (where == "" )
			{
				where = MyDBHelper.PLAYLIST_COL + " = " + "'" + pl + "'" ;	
			}
			else
			{
				where = where + " OR " + MyDBHelper.PLAYLIST_COL + " = " +  "'" + pl + "'" ;
			}
			
			return db.delete( MyDBHelper.TABLE_NAME,  where,   null) ;
		   	
		}*/	
		
		ArrayList<String>  vals = new ArrayList<String>(); 
		for (String pl: rls) 
		{		    
			if (where == "" )
			{
				where = MyDBHelper.READLIST_COL + " = ? "   ;	
				vals.add(pl);
			}
			else
			{
				where = where + " OR " + MyDBHelper.READLIST_COL + " = ? "   ;
				vals.add(pl);
			}
		   	
		}	
		
		String[] arr = vals.toArray(new String[vals.size()]);
			
		return db.delete( MyDBHelper.TABLE_NAME,  where,   arr) ;
			
	}	//deletePlayLists
	
	
	 
	
	public long insertEntry(String title, 
							String filepath,
							String readlist,
							String pagenumber,
							String pagescale,
							String pageheight,
							String seqnum )
	{	 
			ContentValues cvals = new ContentValues( );
			cvals.put(MyDBHelper.TITLE_COL,title);
			cvals.put(MyDBHelper.FILEPATH_COL,filepath);
			cvals.put(MyDBHelper.READLIST_COL,readlist);
			cvals.put(MyDBHelper.PAGENUMBER_COL,pagenumber);
			cvals.put(MyDBHelper.PAGESCALE_COL,pagescale);
			cvals.put(MyDBHelper.PAGEHEIGHT_COL,pageheight);
			cvals.put(MyDBHelper.SEQ_NUM_COL,seqnum);
		 
			 return db.insert(MyDBHelper.TABLE_NAME, null, cvals); 		
	}

}
